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

import game.CircleAngle;
import game.GameColors;

public class SuperBall extends GameBall {

	public SuperBall() {
		this(0, 0, new CircleAngle());
	}
	
	public SuperBall(double posX, double posY, CircleAngle direction) {
		super(posX, posY, SUPERBALL_RADIUS, direction, SUPERBALL_START_SPEED);
		color = GameColors.SUPERBALL_START;
	}

	@Override
	public GameFieldObject getInstance(double posX, double posY, CircleAngle direction) {
		return new SuperBall(posX, posY, direction);
	}
	
	@Override
	protected void setSpeed(double speed) {
		this.speed = Math.max(Math.min(speed, SUPERBALL_START_SPEED), GAMEBALL_MIN_SPEED);
		double fadePercent = 1 - (this.speed - GAMEBALL_MIN_SPEED) / (SUPERBALL_START_SPEED - GAMEBALL_MIN_SPEED);
		color = GameColors.getSuperBallFadeColor(fadePercent);
	}
}
