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

import static game.GameConstants.*;
import static game.Helpers.*;

import java.awt.*;

import game.GameColors;
import game.GameSounds;
import game.shapes.Circle;
import game.shapes.GameShape;
import game.DestructiveCollidable;


public class ShockWave extends Animation implements DestructiveCollidable {

	private final double maxRadius;
    private final Circle shape;
	
	public ShockWave(double posX, double posY, double radius, double speed, int gameTickDelay) {
		super(posX, posY, speed, GameColors.SHOCKWAVE, gameTickDelay);
        this.maxRadius = SHOCKWAVE_MAX_RADIUS;
        shape = new Circle(radius);
	}

	@Override
	public void animate(final double speedChangeFactor) {
		if (animationTickCounter == 0) {
			GAME_SOUNDS.playSound(GameSounds.EXPLODE_SOUND);
		}
		tickAndRunIfAlive(() -> {
			setRadius(getRadius() + speed * speedChangeFactor);
			if (getRadius() > maxRadius) {
				setRadius(maxRadius);
				isFinished = true;
			}
		});
	}

	@Override
	public void draw(Graphics2D g) {
		if (isFinished || animationTickCounter < 0) {
			return;
		}
		g.setColor(color);
		g.drawOval(
				toPixelInt(position.getX() - getRadius()),
				toPixelInt(position.getY() - getRadius()),
				toPixelInt(getRadius() * 2),
				toPixelInt(getRadius() * 2)
		);
	}
	
	public double getRadius() {
        return shape.getRadius();
    }

    public void setRadius(double radius) {
        shape.setRadius(radius);
    }

    public GameShape getShape() {
        return shape.getCopy();
    }
	
}
