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

import game.CircleAngle;
import game.shapes.Line;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Rod extends GameFieldObject {

    protected Point2D.Double secondPoint;

    public Rod(double posX, double posY, double length, CircleAngle angle, Color color) {
        super(posX, posY, new Line(length, angle), color);
        secondPoint = Line.getSecondPoint((Line) shape, posX, posY);
    }

    public double getLength() {
        return ((Line) shape).getLength();
    }

    public CircleAngle getAngle() {
        return ((Line) shape).getAngle();
    }

}
