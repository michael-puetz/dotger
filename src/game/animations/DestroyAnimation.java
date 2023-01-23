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

package game.animations;

import java.awt.Color;

import game.GameSounds;

public class DestroyAnimation extends LineStarAnimation {

	public DestroyAnimation(double posX, double posY, double size, double speed, Color color, int gameTickDelay) {
		super(posX, posY, size, speed, color, gameTickDelay);
	}
	
	@Override
	public void animate(double speedChangeFactor) {
		if (animationTickCounter == 0) {
			GAME_SOUNDS.playSound(GameSounds.DESTROY_SOUND);
		}
		super.animate(speedChangeFactor);
	}

	@Override
	protected double[] getAngles() {
		return new double[] {
				Math.toRadians(30),
				Math.toRadians(90),
				Math.toRadians(150),
				Math.toRadians(210),
				Math.toRadians(270),
				Math.toRadians(330)
		};
	}

}
