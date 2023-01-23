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

import java.awt.*;

public class MulticolorBall extends GameBall {

	protected Color secondColor;

	public MulticolorBall(double posX, double posY, double radius, CircleAngle direction, double speed, Color color, Color secondColor) {
		super(posX, posY, radius, direction, speed, color);
		this.secondColor = secondColor;
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
		g.setColor(getSecondColor());
		g.fillOval(
				toPixelInt(position.getX() - getSecondRadius()),
				toPixelInt(position.getY() - getSecondRadius()),
				toPixelInt(getSecondRadius() * 2),
				toPixelInt(getSecondRadius() * 2)
		);
	}

	public Color getSecondColor() {
		return secondColor;
	}
	
	public double getSecondRadius() {
		return getRadius() * 0.7;
	}

}
