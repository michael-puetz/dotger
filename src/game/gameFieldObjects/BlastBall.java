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
import game.animations.Animation;
import game.animations.DestroyAnimation;
import game.animations.ShockWave;

public class BlastBall extends MulticolorBall {

	private boolean growing;
	private final double secondRadiusStart;
	private double secondRadius;
	
	public BlastBall() {
		this(0, 0, new CircleAngle());
	}
	
	public BlastBall(double posX, double posY, CircleAngle direction) {
		super(posX, posY, BLASTBALL_RADIUS, direction, BLASTBALL_SPEED, GameColors.BLASTBALL_MAIN, GameColors.BLASTBALL_SECOND);
		secondRadiusStart = getRadius() * 0.5;
		secondRadius = secondRadiusStart;
		growing = true;
	}

	@Override
	public GameFieldObject getInstance(double posX, double posY, CircleAngle direction) {
		return new BlastBall(posX, posY, direction);
	}

	@Override
	public void animate(double speedChangeFactor) {
		super.animate(speedChangeFactor);
		if (growing) {
			secondRadius += BLASTBALL_PULSATING_SPEED;
		} else {
			secondRadius -= BLASTBALL_PULSATING_SPEED;
		}
		if (secondRadius > secondRadiusStart + BLASTBALL_PULSATING_WIDTH) {
			secondRadius = secondRadiusStart + BLASTBALL_PULSATING_WIDTH;
			growing = false;
		} else if (secondRadius < secondRadiusStart) {
			secondRadius = secondRadiusStart;
			growing = true;
		}

	}
	
	@Override
	public Animation[] getDestroyAnimations() {
		return new Animation[] {
				new DestroyAnimation(
						getPosX(), 
						getPosY(), 
						getSize(), 
						DESTROYANIMATION_SPEED, 
						GameColors.SHOCKWAVE, 
						0
				), new ShockWave(
						getPosX(), 
						getPosY(), 
						getRadius(), 
						SHOCKWAVE_SPEED, 
						0
				)
		};
	}

	@Override
	public double getSecondRadius() {
		return secondRadius;
	}

}
