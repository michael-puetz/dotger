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

public class FixedBlock extends Block implements PlayerCollidable, Spawnable {

    public FixedBlock() {
        this(50, 50);
    }

    public FixedBlock(double posX, double posY) {
        super(posX, posY, FIXEDBLOCK_SIDE_LENGTH, GameColors.FIXED_BLOCK);
    }

    public FixedBlock(double posX, double posY, double sideLength, Color color) {
        super(posX, posY, sideLength, color);
    }

    @Override
    public FixedBlock getInstance(double posX, double posY, CircleAngle direction) {
        return new FixedBlock(posX, posY);
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
        g.fillRect(
                toPixelInt(position.getX() - getSideLength() / 2.0),
                toPixelInt(position.getY() - getSideLength() / 2.0),
                toPixelInt(getSideLength()),
                toPixelInt(getSideLength())
        );
        g.setColor(getColor().darker());
        g.fillRect(
                toPixelInt(position.getX() - getSideLength() / 2.0 * 0.8),
                toPixelInt(position.getY() - getSideLength() / 2.0 * 0.8),
                toPixelInt(getSideLength() * 0.8),
                toPixelInt(getSideLength() * 0.8)
        );
    }

}
