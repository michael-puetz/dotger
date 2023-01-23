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
import static game.Helpers.*;

import game.*;

import java.awt.*;

public class PlayerBall extends Ball implements Collidable, Animatable {
	
	private boolean isInvincible = false;

	private boolean getsHurt = false;
	private double hurtCounter;
	private final double speed;
	
	public PlayerBall(double posX, double posY, double radius, double speed) {
		super(posX, posY, radius, GameColors.PLAYER);
		hurtCounter = 0;
		this.speed = speed;
	}

	public PlayerBall getFuturePlayer(CircleAngle direction, double speedChangeFactor) {
		PlayerBall futurePlayer = new PlayerBall(position.getX(), position.getY(), getRadius(), speed);
		if (direction != null) {
			futurePlayer.moveInDirection(direction, speedChangeFactor);
		}
		return futurePlayer;
	}
	
	public void moveInDirection(CircleAngle direction, double speedChangeFactor) {
		position.setLocation(
				position.getX() + Math.cos(direction.get()) * speed * speedChangeFactor,
				position.getY() + Math.sin(direction.get()) * speed * speedChangeFactor
		);
	}

	public void moveInDirection(CircleAngle direction, double speed, double speedChangeFactor) {
		position.setLocation(
				position.getX() + Math.cos(direction.get()) * speed * speedChangeFactor,
				position.getY() + Math.sin(direction.get()) * speed * speedChangeFactor
		);
	}

	public void hurtPlayer() {
		getsHurt = true;
	}

	public void animate(double speedChangeFactor) {
		hurtCounter += speedChangeFactor;
		if (hurtCounter >= PLAYER_HURT_GAME_TICKS) {
			getsHurt = false;
			hurtCounter = 0;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(getColor());
		g.fillOval(
				toPixelInt(position.getX() - getRadius()),
				toPixelInt(position.getY() - getRadius()),
				toPixelInt(getRadius() * 2),
				toPixelInt(getRadius() * 2)
		);
	}

	public Color getColor() {
		if (isInvincible) {
			return GameColors.PLAYER_INVINCIBLE;
		} else if (getsHurt) {
			return GameColors.PLAYER_HURT;
		} else {
			return color;
		}
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}

	public boolean isInvincible() {
		return isInvincible;
	}

}
