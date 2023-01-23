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

import game.CircleAngle;
import game.GameColors;
import game.PlayerCollidable;
import game.Spawnable;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class FixedBall extends Ball implements PlayerCollidable, Spawnable {

    public FixedBall() {
        this(50, 50);
    }

    public FixedBall(double posX, double posY) {
        super(posX, posY, FIXEDBALL_RADIUS, GameColors.FIXED_BALL);
    }

    @Override
    public FixedBall getInstance(double posX, double posY, CircleAngle direction) {
        return new FixedBall(posX, posY);
    }

    @Override
    public Point2D.Double getRandomSpawnPosition(Rectangle2D.Double bounds) {
        double borderDistance = FIXED_OBJECT_MIN_DISTANCE / 2.0;
        double x = RANDOM.nextDouble(
                bounds.getX() + borderDistance,
                bounds.getX() + bounds.getWidth() - borderDistance
        );
        double y = RANDOM.nextDouble(
                bounds.getY() + borderDistance,
                bounds.getY() + bounds.getHeight() - borderDistance
        );
        return new Point2D.Double(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(getColor());
        g.fillOval(
                toPixelInt(position.getX() - getRadius()),
                toPixelInt(position.getY() - getRadius()),
                toPixelInt(getRadius() * 2),
                toPixelInt(getRadius() * 2)
        );
        g.setColor(getColor().darker());
        g.fillOval(
                toPixelInt(position.getX() - getRadius() * 0.8),
                toPixelInt(position.getY() - getRadius() * 0.8),
                toPixelInt(getRadius() * 0.8 * 2),
                toPixelInt(getRadius() * 0.8 * 2)
        );
    }

}
