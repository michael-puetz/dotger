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
import game.animations.Animation;
import game.animations.LightningAnimation;
import game.shapes.Line;

import java.awt.*;

public class PreLightningRod extends Rod implements Destructible, Animatable {

    private int gameTickCounter = 0;
    private Color drawColor;

    public PreLightningRod(double posX, double posY, double length, CircleAngle angle) {
        super(posX, posY, length, angle, GameColors.GAME_FIELD);
        drawColor = getColor();
    }

    @Override
    public void animate(double speedChangeFactor) {
        drawColor = GameColors.getBrighterByValue(drawColor, 1);
        if (gameTickCounter > PRELIGHTNINGROD_GAME_TICKS) {
            markForRemoval();
        }
        gameTickCounter++;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(drawColor);
        g.drawLine(
                toPixelInt(position.getX()),
                toPixelInt(position.getY()),
                toPixelInt(secondPoint.getX()),
                toPixelInt(secondPoint.getY())
        );
    }

    @Override
    public Animation[] getDestroyAnimations() {
        return new Animation[] {
        		new LightningAnimation(
        				position.getX(),
        				position.getY(),
        				((Line) shape).getLength(),
        				((Line) shape).getAngle(),
        				LIGHTNINGANIMATION_SPEED,
        				GameColors.LIGHTNINGANIMATION,
        				0
        		)
        };
    }

}
