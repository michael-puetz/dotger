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

import static game.Helpers.*;

import java.awt.*;

public abstract class LineStarAnimation extends Animation {

    protected final double radius;
    protected double currentRadius;

    public LineStarAnimation(double posX, double posY, double size, double speed, Color color, int gameTickDelay) {
        super(posX, posY, speed, color, gameTickDelay);
        this.radius = size / 2.0;
        currentRadius = radius / 2.0;
    }

    @Override
    public void animate(final double speedChangeFactor) {
        tickAndRunIfAlive(() -> {
            currentRadius += (speed * speedChangeFactor);
            if (currentRadius >= radius) {
                currentRadius = radius;
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
        for (double angle : getAngles()) {
            g.drawLine(
                    toPixelInt(position.getX() + Math.cos(angle) * (currentRadius / 2)),
                    toPixelInt(position.getY() + Math.sin(angle) * (currentRadius / 2)),
                    toPixelInt(position.getX() + Math.cos(angle) * currentRadius),
                    toPixelInt(position.getY() + Math.sin(angle) * currentRadius)
            );
        }
    }

    protected abstract double[] getAngles();

}
