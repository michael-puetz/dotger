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

package game.gameFieldObjects;

import static game.GameConstants.*;
import static game.GeometryLogic.*;
import static game.Helpers.*;

import game.*;
import game.animations.Animation;
import game.animations.DestroyAnimation;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

public class GameBall extends Ball implements ObjectCollidable, Movable, Spawnable, Animatable, Destructible {

	public static boolean isParty;

	protected double speed;
	protected int partyColorIndex;
	protected double partyChangeCounter;
	protected final int partyChangeAfterGameTicks;
	protected CircleAngle direction;
	protected Color partyColor;
	
	public GameBall() {
		this(50, 50, 10, new CircleAngle(0), 1);
	}
	
	public GameBall(double posX, double posY, double radius, CircleAngle direction, double speed) {
		this(posX, posY, radius, direction, speed, GameColors.getRandomGameBallColor());
	}
	
	public GameBall(double posX, double posY, double radius, CircleAngle direction, double speed, Color color) {
		super(posX, posY, radius, color);
		this.direction = direction;
		this.speed = speed;
		partyColor = GameColors.PARTY_COLORS[0];
		partyColorIndex = 0;
		partyChangeCounter = 0;
		partyChangeAfterGameTicks = RANDOM.nextInt(25, 50);
	}

	@Override
	public GameFieldObject getInstance(double posX, double posY, CircleAngle direction) {
		double newRadius = RANDOM.nextInt(GAMEBALL_MIN_RADIUS, GAMEBALL_MAX_RADIUS);
		double newSpeed = RANDOM.nextDouble(GAMEBALL_MIN_SPEED, GAMEBALL_MAX_SPEED);
		return new GameBall(posX, posY, newRadius, direction, newSpeed);
	}

	@Override
	public Point2D.Double getRandomSpawnPosition(Rectangle2D.Double bounds) {
		double x = RANDOM.nextDouble(
				bounds.getX() + getRadius(),
				bounds.getX() + bounds.getWidth() - getRadius()
		);
		double y = RANDOM.nextDouble(
				bounds.getY() + getRadius(),
				bounds.getY() + bounds.getHeight() - getRadius()
		);
		return new Point2D.Double(x, y);
	}

	@Override
	public void draw(Graphics2D g) {
		Graphics2D graphics2D = (Graphics2D) g.create();
		graphics2D.setPaint(new RadialGradientPaint(
				(float) getPosX(), (float) getPosY(),
				(float) getRadius(),
				new float[] {0f, 1f},
				new Color[] {getColor(), GameColors.getDarkerByValue(getColor(), 20)}
		));
		graphics2D.fillOval(
				toPixelInt(position.getX() - getRadius()),
				toPixelInt(position.getY() - getRadius()),
				toPixelInt(getRadius() * 2),
				toPixelInt(getRadius() * 2)
		);
		graphics2D.setColor(Color.white);
		graphics2D.fillOval(
				toPixelInt(position.getX() - getRadius() + getRadius() / 1.5),
				toPixelInt(position.getY() - getRadius() + getRadius() / 3),
				toPixelInt(getRadius() / 4),
				toPixelInt(getRadius() / 8)
		);
		graphics2D.dispose();
	}

	@Override
	public void move(double speedChangeFactor) {
		position.setLocation(
				position.getX() + Math.cos(direction.get()) * speed * speedChangeFactor,
				position.getY() + Math.sin(direction.get()) * speed * speedChangeFactor
		);
	}

	@Override
	public boolean isOnCollisionCourse(Collidable otherObject) {
		CircleAngle collisionAngle = getCollisionAngle(otherObject);
		return CircleAngle.smallerAngleBetween(collisionAngle, direction).get() < CircleAngle.DEGREE_90.get();
	}

	@Override
	public void reactToCollision(Collidable otherObject) {
		CircleAngle collisionAngle = getCollisionAngle(otherObject);
		setSpeed(getSpeedAfterCollision(collisionAngle, direction, speed));
		direction = getDirectionAfterCollision(collisionAngle, direction);
	}

	@Override
	public Animation[] getDestroyAnimations() {
		return new Animation[] {
				new DestroyAnimation(
						getPosX(),
						getPosY(),
						getSize(),
						DESTROYANIMATION_SPEED,
						getColor(),
						0
				)
		};
	}
	
	@Override
	public void animate(double speedChangeFactor) {
		if (isParty) {
			partyChangeCounter += speedChangeFactor;
			if (partyChangeCounter >= partyChangeAfterGameTicks) {
				partyColor = getNextPartyColor();
				partyChangeCounter = 0;
			}
		}
	}

	@Override
	public Color getColor() {
		if (isParty) {
			return partyColor;
		} else {
			return color;
		}
	}
	
	protected Color getNextPartyColor() {
		partyColorIndex++;
		if (partyColorIndex >= GameColors.PARTY_COLORS.length) {
			partyColorIndex = 0;
		}
		return GameColors.PARTY_COLORS[partyColorIndex];
	}

	protected void setSpeed(double speed) {
		this.speed = Math.min(Math.max(speed, GAMEBALL_MIN_SPEED), GAMEBALL_MAX_SPEED);
	}
}
