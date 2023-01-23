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

import static game.Helpers.*;

import game.CircleAngle;
import game.PlayerCollidable;
import game.shapes.Line;

import java.awt.*;

public class Wall extends GameFieldObject implements PlayerCollidable {

    public Wall(double posX, double posY, double length, CircleAngle angle, Color color) {
        super(posX, posY, new Line(length, angle), color);
    }
    
    @Override
    public void draw(Graphics2D g) {
    	g.setColor(color);
        g.setStroke(new BasicStroke(2));
    	g.drawLine(
    			toPixelInt(position.getX()),
    			toPixelInt(position.getY()),
    			toPixelInt(position.getX() + Math.cos(((Line) shape).getAngle().get()) * ((Line) shape).getLength()),
    			toPixelInt(position.getY() + Math.sin(((Line) shape).getAngle().get()) * ((Line) shape).getLength())
    	);
        g.setStroke(new BasicStroke(1));
    }

}
