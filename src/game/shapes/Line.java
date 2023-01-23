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

package game.shapes;

import game.CircleAngle;

import java.awt.geom.Point2D;

public class Line extends GameShape {

    private final double length;
    private final CircleAngle angle;

    public Line(double length, CircleAngle angle) {
        this.length = length;
        this.angle = angle;
    }

    public static Point2D.Double getSecondPoint(Line line, double posX, double posY) {
        return new Point2D.Double(
                posX + Math.cos(line.getAngle().get()) * line.getLength(),
                posY + Math.sin(line.getAngle().get()) * line.getLength()
        );
    }

    @Override
    public Line getCopy() {
        return new Line(length, getAngle());
    }

    @Override
    public double getSize() {
        return length / 2.0;
    }

    public double getLength() {
    	return length;
    }
    
    public CircleAngle getAngle() {
    	return new CircleAngle(angle.get());
    }
}
