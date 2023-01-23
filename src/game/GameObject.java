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

package game;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class GameObject {

    protected Point2D.Double position;
    protected Color color;
    protected boolean markedForRemoval = false;

    protected GameObject(double posX, double posY, Color color) {
        position = new Point2D.Double(posX, posY);
        this.color = color;
    }

    public abstract void draw(Graphics2D g);
    
    public void markForRemoval() {
		markedForRemoval = true;
	}
	
	public boolean isMarkedForRemoval() {
		return markedForRemoval;
	}

    public Color getColor() {
        return color;
    }

    public double getPosX() {
        return position.getX();
    }

    public double getPosY() {
        return position.getY();
    }

    public Point2D.Double getPosition() {
        return new Point2D.Double(position.getX(), position.getY());
    }

}
