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

import game.GameObject;
import game.GameSounds;
import game.Animatable;

import java.awt.*;

public abstract class Animation extends GameObject implements Animatable {

	protected static final GameSounds GAME_SOUNDS = GameSounds.getInstance();
	
	protected boolean isFinished = false;
	protected double speed;
	protected int animationTickCounter;

	public Animation() {
		this(0, 0, 0, Color.black, 0);
	}

	public Animation(double posX, double posY, double speed, Color color, int gameTickDelay) {
		super(posX, posY, color);
		this.speed = speed;
		this.animationTickCounter = -gameTickDelay;
	}
	
	public abstract void animate(double speedChangeFactor);

	protected void tickAndRunIfAlive(Runnable animateCode) {
		if (isFinished || animationTickCounter++ < 0) {
			return;
		}
		animateCode.run();
	}

	public boolean isFinished() {
		return isFinished;
	}

}
