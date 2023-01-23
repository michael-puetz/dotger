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
import game.animations.Animation;

import java.awt.Color;

public class PartyBall extends GameBall {
	
	public PartyBall() {
		this(0, 0, new CircleAngle());
	}
	
	public PartyBall(double posX, double posY, CircleAngle direction) {
		super(posX, posY, PARTYBALL_RADIUS, direction, PARTYBALL_SPEED);
	}

	@Override
	public GameFieldObject getInstance(double posX, double posY, CircleAngle direction) {
		return new PartyBall(posX, posY, direction);
	}
	
	@Override
	public void animate(double speedChangeFactor) {
		if (partyChangeCounter >= partyChangeAfterGameTicks) {
			partyColor = getNextPartyColor();
			partyChangeCounter = 0;
		}
		partyChangeCounter += speedChangeFactor;
	}
	
	@Override
	public Animation[] getDestroyAnimations() {
		return new Animation[0];
	}
	
	@Override
	public Color getColor() {
		return partyColor;
	}
	
}
