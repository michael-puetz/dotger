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

public class ParticleAnimation extends Animation {

    private static final double START_RADIUS = 0;
    private double radius;
    private final double maxRadius;
    private boolean reverseAnimation = false;

    public ParticleAnimation(double posX, double posY, double radius, double speed, Color color, int gameTickDelay) {
        super(posX, posY, speed, color, gameTickDelay);
        this.radius = START_RADIUS;
        this.maxRadius = radius;
    }

    @Override
    public void animate(final double speedChangeFactor) {
        tickAndRunIfAlive(() -> {
            if (reverseAnimation) {
                radius -= speed * speedChangeFactor;
                if (radius <= START_RADIUS) {
                	radius = START_RADIUS;
                    isFinished = true;
                }
            } else {
                radius += speed * speedChangeFactor;
                if (radius >= maxRadius) {
                	radius = maxRadius;
                    reverseAnimation = true;
                }
            }
        });
    }

    @Override
    public void draw(Graphics2D g) {
        if (isFinished || animationTickCounter < 0) {
            return;
        }
        g.setColor(color);
        g.fillOval(
                toPixelInt(position.getX() - radius),
                toPixelInt(position.getY() - radius),
                toPixelInt(radius * 2),
                toPixelInt(radius * 2)
        );
    }

}
