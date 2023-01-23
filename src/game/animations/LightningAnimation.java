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

import game.CircleAngle;
import game.DestructiveCollidable;
import game.GameColors;
import game.GameSounds;
import game.shapes.*;

import java.awt.*;
import java.awt.geom.Point2D;

public class LightningAnimation extends Animation implements DestructiveCollidable {

    private final Line shape;
    private final Point2D.Double secondPoint;
    private int currentThickness;

    public LightningAnimation(
            double posX, double posY,
            double length, CircleAngle angle,
            double speed, Color color,
            int gameTickDelay) {
        super(posX, posY, speed, color, gameTickDelay);
        shape = new Line(length, angle);
        secondPoint = Line.getSecondPoint(shape, posX, posY);
        currentThickness = 1;
    }

    @Override
    public void animate(double speedChangeFactor) {
        if (animationTickCounter == 0) {
            GAME_SOUNDS.playSound(GameSounds.LIGHTNING);
        }
        tickAndRunIfAlive(() -> {
            currentThickness = (int) (animationTickCounter * speed * speedChangeFactor) + 1;
            if (currentThickness > LIGHTNINGANIMATION_MAX_THICKNESS) {
                isFinished = true;
            }
        });
    }

    @Override
    public void draw(Graphics2D g) {
        if (isFinished || animationTickCounter < 0) {
            return;
        }
        Color drawColor = getColor();
        for (int i = currentThickness; i > 0; i--) {
            g.setColor(drawColor);
            g.setStroke(new BasicStroke(i, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(
                    toPixelInt(position.getX()),
                    toPixelInt(position.getY()),
                    toPixelInt(secondPoint.getX()),
                    toPixelInt(secondPoint.getY())
            );
            drawColor = GameColors.getBrighterByValue(drawColor, 15);
        }
        g.setStroke(new BasicStroke(1));
    }

    @Override
    public GameShape getShape() {
        return shape.getCopy();
    }

}
