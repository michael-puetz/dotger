/*
 * Dotger - A game where you have to dodge objects.
 * Copyright (C) 2023  Michael Pütz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package game.gui;

import static game.GameConstants.*;
import static game.Helpers.*;
import static game.GeometryLogic.*;

import game.*;
import game.animations.*;
import game.gameFieldObjects.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private final Dimension size;
	private PlayerBall player;
	private final PlayerMovement movement;
	private final GameSounds gameSounds;
	private GameState gameState;
	private final Rectangle2D.Double spawnBounds;
	private final Polygon gameFieldArea;
	private long gameTickCounter;
	private int lives;
	private int points;
	private int partyGameTicks;
	private int invincibleGameTicks;
	private int speedChangeGameTicks;
	private double speedChangeFactor;
	
	private final List<GameObject> gameObjects = new ArrayList<>(100);
	
	private enum GameState {NOT_STARTED, RUNNING, GAME_OVER}
	private final Spawnable[] gameObjectOccurrence = {
			new SuperBall(),
			new LiveBall(),
			new FixedBall(),
			new BlastBall(),
			new PartyBall(),
			new FixedBlock(),
			new GodModeBall(),
			new PlayerMovingBlock(),
			new SlowMotionBall()
	};
	private int specialObjectIndex;

	private final Runnable finishedRunnable, updateRunnable;
	
	public GamePanel(int width, int height, Runnable finishedRunnable, Runnable updateRunnable) {
		setOpaque(false);
		size = new Dimension(width, height);
		player = new PlayerBall(size.width / 2.0, size.height / 2.0, 5, PLAYER_SPEED);
		movement = new PlayerMovement();
		gameSounds = GameSounds.getInstance();
		spawnBounds = new Rectangle2D.Double(
				CORNER_SIZE / 2.0,
				CORNER_SIZE / 2.0,
				size.width - CORNER_SIZE,
				size.height - CORNER_SIZE
		);
		gameFieldArea = new Polygon(
				new int[] {
						CORNER_SIZE, size.width - CORNER_SIZE, size.width, size.width,
						size.width - CORNER_SIZE, CORNER_SIZE, 0, 0
				},
				new int[] {
						0, 0, CORNER_SIZE, size.height - CORNER_SIZE,
						size.height, size.height, size.height - CORNER_SIZE, CORNER_SIZE
				},
				8
		);
		this.finishedRunnable = finishedRunnable;
		this.updateRunnable = updateRunnable;

		gameTickCounter = 0;
		lives = 0;
		points = 0;
		gameState = GameState.NOT_STARTED;
		partyGameTicks = 0;
		speedChangeGameTicks = 0;
		speedChangeFactor = 1;
		specialObjectIndex = 0;
		invincibleGameTicks = 0;
		if (FOR_ANNIKA) {
			gameObjectOccurrence[0] = new AnnikaBlock();
		}

		addWalls();
		showStartupText("DOTGER", size.height / 2.0 - TITLEBALL_DISTANCE);
		String randomText = STARTUP_TEXTS[RANDOM.nextInt(0, STARTUP_TEXTS.length)];
		showStartupText(randomText, size.height / 2.0 + TITLEBALL_DISTANCE);
	}
	
	@Override
	public void run() {
		try {
			gameTickCounter++;
			if (gameState == GameState.NOT_STARTED && gameTickCounter < STARTUP_PREVIEW_DELAY + STARTUP_SPEEDUP_TICKS) {
				if (gameTickCounter < STARTUP_PREVIEW_DELAY) {
					speedChangeFactor = 0;
				} else {
					speedChangeFactor = (double) (gameTickCounter - STARTUP_PREVIEW_DELAY) / STARTUP_SPEEDUP_TICKS;
				}
			}

			if (gameState == GameState.RUNNING) {
				addNewGameObjectOnLoopCount();
				addNewLightningRodOnLoopCount();
				movePlayer();
				checkForPlayerCollision();
			}

			moveAndAnimateGameObjects();
			checkGameObjectCollision();

			removeObjectsIfMarked();

			if (gameState == GameState.RUNNING) {
				if (GameBall.isParty) {
					partyGameTicks++;
					if (partyGameTicks > PARTY_GAME_TICKS) {
						GameBall.isParty = false;
					}
				}
				if (player.isInvincible()) {
					invincibleGameTicks++;
					if (invincibleGameTicks > INVINCIBLE_GAME_TICKS) {
						player.setInvincible(false);
					}
				}
				if (speedChangeFactor != 1) {
					speedChangeGameTicks++;
					if (speedChangeGameTicks > SPEED_CHANGE_GAME_TICKS) {
						speedChangeFactor = 1;
						gameSounds.playSound(GameSounds.SLOW_MOTION_END);
					}
				}
			}

			EventQueue.invokeLater(updateRunnable);
			revalidate();
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void addWalls() {
		int left = 0;
		int leftWithCorner = CORNER_SIZE;
		int right = size.width - 1;
		int rightWithCorner = size.width - 1 - CORNER_SIZE;
		int top = 0;
		int topWithCorner = CORNER_SIZE;
		int bottom = size.height - 1;
		int bottomWithCorner = size.height - 1 - CORNER_SIZE;
		int xLengthWithCorners = size.width - CORNER_SIZE * 2;
		int yLengthWithCorners = size.height - CORNER_SIZE * 2;
		double cornerDiagonal = Math.sqrt(2) * CORNER_SIZE;
		gameObjects.add(new Wall(leftWithCorner, top, xLengthWithCorners, CircleAngle.DEGREE_0, Color.darkGray));
		gameObjects.add(new Wall(rightWithCorner, top, cornerDiagonal, CircleAngle.DEGREE_45, Color.darkGray));
		gameObjects.add(new Wall(right, topWithCorner, yLengthWithCorners, CircleAngle.DEGREE_90, Color.darkGray));
		gameObjects.add(new Wall(right, bottomWithCorner, cornerDiagonal, CircleAngle.DEGREE_135, Color.darkGray));
		gameObjects.add(new Wall(rightWithCorner, bottom, xLengthWithCorners, CircleAngle.DEGREE_180, Color.darkGray));
		gameObjects.add(new Wall(leftWithCorner, bottom, cornerDiagonal, CircleAngle.DEGREE_225, Color.darkGray));
		gameObjects.add(new Wall(left, bottomWithCorner, yLengthWithCorners, CircleAngle.DEGREE_270, Color.darkGray));
		gameObjects.add(new Wall(left, topWithCorner, cornerDiagonal, CircleAngle.DEGREE_315, Color.darkGray));
	}
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		));
		super.paintComponent(graphics2D);
		graphics2D.setColor(GameColors.GAME_FIELD);
		graphics2D.fillPolygon(gameFieldArea);
		drawObjectsAndAnimations(graphics2D);
		if (gameState == GameState.RUNNING) {
			drawPlayer(graphics2D);
		} else if (gameState == GameState.GAME_OVER) {
			drawGameOverScreen(graphics2D);
		}
	}

	private void drawObjectsAndAnimations(Graphics2D g) {
		for (GameObject object : gameObjects) {
			if (!(object instanceof FireworkShow)) {
				object.draw(g);
			}
		}
	}

	private void drawPlayer(Graphics2D g) {
		player.draw(g);
	}

	private void drawGameOverScreen(Graphics2D g) {
		g.setColor(GameColors.GAME_OVER_BACKGROUND);
		g.fillPolygon(gameFieldArea);
		for (GameObject object : gameObjects) {
			if (object instanceof FireworkShow) {
				object.draw(g);
			}
		}
		g.setColor(GameColors.GAME_OVER_TEXT);
		g.setFont(POINTS_FONT);
		GameLanguage gameLanguage = GameLanguage.getInstance();
		String pointsSingularText = " " + gameLanguage.getString("points_singular");
		String pointsPluralText = " " + gameLanguage.getString("points_plural");
		String text = points + (points == 1 ? pointsSingularText : pointsPluralText);
		g.drawString(text, size.width / 2 - g.getFontMetrics().stringWidth(text) / 2, size.height / 2);
	}
	
	private void showStartupText(String text, double positionY) {
		assert ((text.length() + 1) * TITLEBALL_DISTANCE <= size.width) : "Startup text is too long";
		assert (positionY >= TITLEBALL_DISTANCE / 2.0) : "Y position is invalid";
		assert (positionY <= size.height - TITLEBALL_DISTANCE / 2.0) : "Y position is invalid";
		
		double leftMostBallX = size.width / 2.0 - (text.length() - 1) * TITLEBALL_DISTANCE / 2.0;
		char[] textChars = text.toCharArray();
		for (int i = 0; i < textChars.length; i++) {
			if (textChars[i] == ' ') {
				continue;
			}
			double positionX = leftMostBallX + i * TITLEBALL_DISTANCE;
			gameObjects.add(new TitleBall(positionX, positionY, textChars[i]));
		}
	}

	public void setupNewGame() {
		gameTickCounter = 1;
		lives = 0;
		points = 0;
		gameState = GameState.RUNNING;
		gameObjects.clear();
		GameBall.isParty = false;
		partyGameTicks = 0;
		speedChangeGameTicks = 0;
		speedChangeFactor = 1;
		specialObjectIndex = 0;
		player = new PlayerBall(size.width / 2.0, size.height / 2.0, 5, PLAYER_SPEED);
		if (INVINCIBLE_FOR_TEST) {
			player.setInvincible(true);
			invincibleGameTicks = Integer.MIN_VALUE;
		} else {
			player.setInvincible(false);
			invincibleGameTicks = 0;
		}
		addWalls();
		gameSounds.playSound(GameSounds.START_SOUND);
		gameSounds.startGameMusic();
		movement.resetAll();
	}
	
	private void addNewGameObjectOnLoopCount() {
		if (gameTickCounter % GAME_TICKS_UNTIL_OBJECT == 0) {
			if (gameTickCounter != 0 && gameTickCounter % (GAME_TICKS_UNTIL_OBJECT * OBJECTS_UNTIL_SPECIAL_OBJECT) == 0) {
				addNewGameObject(gameObjectOccurrence[specialObjectIndex]);
				switchSpecialObject();
			} else {
				addNewGameObject(new GameBall());
			}
			points++;
		}
	}

	private void addNewLightningRodOnLoopCount() {
		if (gameTickCounter % GAME_TICKS_UNTIL_LIGHTNINGROD != 0 || gameTickCounter == 0) {
			return;
		}
		Point2D.Double startPoint = getRandomPointOnSide();
		Point2D.Double endPoint;
		do {
			endPoint = getRandomPointOnSide();
		} while (endPoint.getX() == startPoint.getX() || endPoint.getY() == startPoint.getY());
		double length = startPoint.distance(endPoint);
		CircleAngle angle = angleToPoint(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
		gameObjects.add(new PreLightningRod(startPoint.getX(), startPoint.getY(), length, angle));
	}

	private Point2D.Double getRandomPointOnSide() {
		double posX = 0;
		double posY = 0;
		int randomSide = RANDOM.nextInt(4);
		// The corner walls are included in the top and bottom wall calculation to shorten the code.
		switch (randomSide) {
			case 0:
				// Top wall.
				posX = RANDOM.nextDouble(size.width);
				posY = getTopSideYPosition(posX);
				break;
			case 1:
				// Right wall.
				posX = size.width;
				posY = RANDOM.nextDouble(CORNER_SIZE, size.height - CORNER_SIZE);
				break;
			case 2:
				// Bottom wall.
				posX = RANDOM.nextDouble(size.width);
				posY = size.height - getTopSideYPosition(posX);
				break;
			case 3:
				// Left wall.
				posX = 0;
				posY = RANDOM.nextDouble(CORNER_SIZE, size.height - CORNER_SIZE);
				break;
		}
		return new Point2D.Double(posX, posY);
	}

	private double getTopSideYPosition(double posX) {
		return Math.max(Math.max(0, CORNER_SIZE - (size.width - posX)), Math.max(0, CORNER_SIZE - posX));
	}
	
	private void moveAndAnimateGameObjects() {
		player.animate(speedChangeFactor);
		for (GameObject object : gameObjects) {
			if (object instanceof Movable movableObject) {
				movableObject.move(speedChangeFactor);
			}
			if (object instanceof Animation animation && animation.isFinished()) {
				animation.markForRemoval();
			} else if (object instanceof Animatable animatableObject) {
				animatableObject.animate(speedChangeFactor);
			}
		}
	}

	private void movePlayer() {
		CircleAngle moveAngle = movement.getMoveAngle();
		double moveSpeed = player.getSpeed();
		boolean normalCollisionHappened = false;
		PlayerBall futurePlayer;
		futurePlayer = player.getFuturePlayer(moveAngle, speedChangeFactor);
		// Every object of PlayerCollidable that collides with the player should modify the
		// moveAngle and / or moveSpeed once, even if it collides only after a modification
		// happened. If one stops the player by setting moveAngle to null moveAngle will
		// stay null.
		List<GameObject> playerCollideObjects = gameObjects.stream()
				.filter(object -> object instanceof PlayerCollidable && !(object instanceof AnnikaBlock))
				.collect(Collectors.toList());
		
		GameObject lastCollisionObject = null;
		boolean loopedWithCollision;
		do {
			loopedWithCollision = false;
			for (GameObject object : playerCollideObjects) {
				if (((PlayerCollidable) object).isCollidingWith(player)) {
					moveAngle = ((PlayerCollidable) object).getPossibleMoveAngle(player, moveAngle);
				} else if (((PlayerCollidable) object).isCollidingWith(futurePlayer)) {
					moveSpeed = ((PlayerCollidable) object).getPossibleMoveDistance(player, moveAngle);
					if (moveSpeed <= 0.1) {
						moveAngle = ((PlayerCollidable) object).getPossibleMoveAngle(player, moveAngle);
						moveSpeed = player.getSpeed();
					}
				} else {
					continue;
				}
				lastCollisionObject = object;
				loopedWithCollision = true;
				futurePlayer = player.getFuturePlayer(moveAngle, speedChangeFactor);
				if (!(object instanceof PlayerMovingCollidable)) {
					normalCollisionHappened = true;
				}
			}
			if (loopedWithCollision) {
				playerCollideObjects.remove(lastCollisionObject);
			}
		} while (loopedWithCollision);
		
		if (moveAngle != null) {
			player.moveInDirection(moveAngle, moveSpeed, speedChangeFactor);
		}
		
		CircleAngle pushAngle = null;
		double pushSpeed = 0;
		GameObject pushingObject = null;
		int playerMovingCollisionCounter = 0;
		for (GameObject object : gameObjects) {
			if (object instanceof PlayerMovingCollidable playerMovingObject) {
				if (playerMovingObject.isCollidingWith(player)) {
					pushAngle = playerMovingObject.getPlayerPushDirection(player);
					pushSpeed = playerMovingObject.getPlayerPushSpeed(player);
					pushingObject = object;
					playerMovingCollisionCounter++;
				}
			}
		}
		
		if (pushAngle != null) {
			player.moveInDirection(pushAngle, pushSpeed, speedChangeFactor);
		}
		
		if (playerMovingCollisionCounter > 1 || (playerMovingCollisionCounter > 0 && normalCollisionHappened)) {
			if (player.isInvincible()) {
				pushingObject.markForRemoval();
			} else if (lives > 0) {
				pushingObject.markForRemoval();
				hurtPlayer();
			} else {
				killPlayer();
			}
		}
	}
	
	private void checkGameObjectCollision() {
		collisionCheck: for (GameObject object : gameObjects) {
			if (object instanceof Wall) {
				continue;
			}
			int collisionCounter = 0;
			int squeezeCollisionCounter = 0;
			for (GameObject otherObject : gameObjects) {
				if (object == otherObject) {
					continue;
				}
				if (object instanceof Collidable collidable 
						&& otherObject instanceof DestructiveCollidable destructObject) {
					if (collidable.isCollidingWith(destructObject)) {
						object.markForRemoval();
						continue collisionCheck;
					}
				}
				if (object instanceof ObjectCollidable reactionObject 
						&& otherObject instanceof Collidable collidableOtherObject) {
					if (reactionObject.isCollidingWith(collidableOtherObject)
							&& reactionObject.isOnCollisionCourse(collidableOtherObject)) {
						reactionObject.reactToCollision(collidableOtherObject);
						collisionCounter++;
						if (otherObject instanceof PlayerCollidable) {
							squeezeCollisionCounter++;
						}
					}
				}
				if (object instanceof PlayerMovingCollidable reactionObject 
						&& otherObject instanceof PlayerCollidable collidableOtherObject) {
					if (reactionObject.isCollidingWith(collidableOtherObject)
							&& reactionObject.isOnCollisionCourse(collidableOtherObject)) {
						reactionObject.reactToCollision(collidableOtherObject);
						collisionCounter++;
						squeezeCollisionCounter++;
					}
				}
			}
			if (collisionCounter > 2 || squeezeCollisionCounter > 1) {
				object.markForRemoval();
			}
		}
	}
	
	private void checkForPlayerCollision() {
		for (GameObject object : gameObjects) {
			if (!(object instanceof Collidable)
					|| object instanceof PlayerCollidable || object instanceof ShockWave) {
				continue;
			}
			if (!player.isCollidingWith((Collidable) object)) {
				continue;
			}
			if (object instanceof LiveBall) {
				object.markForRemoval();
				lives++;
				gameSounds.playSound(GameSounds.LIFE_SOUND);
			} else if (object instanceof BlastBall) {
				object.markForRemoval();
			} else if (object instanceof PartyBall) {
				object.markForRemoval();
				partyGameTicks = 0;
				GameBall.isParty = true;
				gameSounds.playSound(GameSounds.PARTY_SOUND);
			} else if (object instanceof GodModeBall) {
				object.markForRemoval();
				if (!INVINCIBLE_FOR_TEST) {
					invincibleGameTicks = 0;
					player.setInvincible(true);
				}
				gameSounds.playSound(GameSounds.GODMODE_SOUND);
			} else if (object instanceof SlowMotionBall) {
				object.markForRemoval();
				speedChangeGameTicks = 0;
				if (speedChangeFactor == 1) {
					gameSounds.playSound(GameSounds.SLOW_MOTION_START);
				}
				speedChangeFactor = SLOW_MOTION_FACTOR;
			} else if (player.isInvincible()) {
				object.markForRemoval();
			} else {
				if (lives > 0) {
					object.markForRemoval();
					hurtPlayer();
				} else {
					killPlayer();
					break;
				}
			}
		}
	}
	
	private void removeObjectsIfMarked() {
		List<Animation> addList = gameObjects.stream()
				.filter(object -> object.isMarkedForRemoval() && object instanceof Destructible)
				.map(destructible -> ((Destructible) destructible).getDestroyAnimations())
				.flatMap(Arrays::stream)
				.collect(Collectors.toCollection(ArrayList<Animation>::new));
		gameObjects.removeIf(GameObject::isMarkedForRemoval);
		gameObjects.addAll(addList);
	}

	private void hurtPlayer() {
		player.hurtPlayer();
		lives--;
		gameSounds.playSound(GameSounds.LIFE_LOST_SOUND);
	}

	private void killPlayer() {
		gameState = GameState.GAME_OVER;
		gameObjects.add(new DestroyAnimation(
				player.getPosX(),
				player.getPosY(),
				140,
				1.5,
				player.getColor(),
				0
		));
		speedChangeFactor = 1;
		GameBall.isParty = false;
		gameObjects.add(new FireworkShow(points, size));
		gameSounds.playSound(GameSounds.END_SOUND);
		gameSounds.stopGameMusic();
		EventQueue.invokeLater(finishedRunnable);
	}
	
	private void addNewGameObject(Spawnable objectToSpawn) {
		GameFieldObject newObject;
		boolean canPlace;
		int tryCounter = 0;
		do {
			Point2D.Double pos = objectToSpawn.getRandomSpawnPosition(spawnBounds);
			CircleAngle direction = CircleAngle.randomAngle();
			newObject = objectToSpawn.getInstance(pos.getX(), pos.getY(), direction);
			canPlace = canBePlaced(newObject);
			tryCounter++;
			if (tryCounter > 2000) {
				return;
			}
		} while (!canPlace);
		gameObjects.add(newObject);
		gameObjects.add(new SpawnAnimation(
				newObject.getPosX(),
				newObject.getPosY(),
				newObject.getSize(),
				SPAWNANIMATION_SPEED,
				GameColors.SPAWN_ANIMATION,
				0
		));
		gameSounds.playSound(GameSounds.SPAWN_SOUND);
	}

	private boolean canBePlaced(GameObject newObject) {
		if (!(newObject instanceof Collidable newCollideObject)) {
			return true;
		}
		for (GameObject object : gameObjects) {
			if (object instanceof Collidable) {
				if (((Collidable) object).isCollidingWith(newCollideObject)) {
					return false;
				}
				if (object instanceof PlayerCollidable) {
					double distance = newObject.getPosition().distance(object.getPosition());
					if (distance < FIXED_OBJECT_MIN_DISTANCE) {
						return false;
					}
				}
			}
		}
		double spawnDistance = newCollideObject.getPosition().distance(player.getPosition());
		return spawnDistance > MIN_SPAWN_DISTANCE;
	}

	private void switchSpecialObject() {
		specialObjectIndex = (specialObjectIndex + 1) % gameObjectOccurrence.length;
	}
	
	public void setPlayerLeftMovement(boolean isMoving) {
		movement.setLeftValue(isMoving);
	}
	
	public void setPlayerTopMovement(boolean isMoving) {
		movement.setTopValue(isMoving);
	}
	
	public void setPlayerRightMovement(boolean isMoving) {
		movement.setRightValue(isMoving);
	}
	
	public void setPlayerBottomMovement(boolean isMoving) {
		movement.setBottomValue(isMoving);
	}
	
	public void setPlayerAMovement(boolean isMoving) {
		movement.setAValue(isMoving);
	}
	
	public void setPlayerWMovement(boolean isMoving) {
		movement.setWValue(isMoving);
	}
	
	public void setPlayerDMovement(boolean isMoving) {
		movement.setDValue(isMoving);
	}
	
	public void setPlayerSMovement(boolean isMoving) {
		movement.setSValue(isMoving);
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getLives() {
		return lives;
	}
	
}
