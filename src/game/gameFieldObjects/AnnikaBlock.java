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
import game.GameColors;
import game.shapes.Square;

import java.awt.*;

import static game.Helpers.toPixelInt;

public class AnnikaBlock extends FixedBlock {

    public AnnikaBlock() {
        this(50, 50);
    }

    public AnnikaBlock(double posX, double posY) {
        super(posX, posY, 40, Color.pink);
    }

    @Override
    public AnnikaBlock getInstance(double posX, double posY, CircleAngle direction) {
        return new AnnikaBlock(posX, posY);
    }

    @Override
    public void draw(Graphics2D g) {
        Paint oldPaint = g.getPaint();
        g.setPaint(new LinearGradientPaint(
                (float) getPosX(), (float) getPosY(),
                (float) getPosX() + (float) ((Square) shape).getSideLength(),
                (float) getPosY() + (float) ((Square) shape).getSideLength(),
                new float[] {0f, 1f},
                new Color[] {Color.red, GameColors.getDarkerByValue(Color.red, 75)}
        ));
        g.fillRect(
                toPixelInt(position.getX() - getSideLength() / 2.0),
                toPixelInt(position.getY() - getSideLength() / 2.0),
                toPixelInt(getSideLength()),
                toPixelInt(getSideLength())
        );
        g.setPaint(new LinearGradientPaint(
                (float) getPosX(), (float) getPosY(),
                (float) getPosX() + (float) ((Square) shape).getSideLength(),
                (float) getPosY() + (float) ((Square) shape).getSideLength(),
                new float[] {0f, 1f},
                new Color[] {getColor(), GameColors.getDarkerByValue(getColor(), 75)}
        ));
        g.fillRect(
                toPixelInt(position.getX() - getSideLength() / 2.0 + 2),
                toPixelInt(position.getY() - getSideLength() / 2.0 + 2),
                toPixelInt(getSideLength() - 4),
                toPixelInt(getSideLength() - 4)
        );
        g.setPaint(oldPaint);
        g.setColor(Color.red);
        double halfHeartSize = getSideLength() / 2.0 * 0.8;
        g.fillOval(
                toPixelInt(position.getX() - halfHeartSize + 1),
                toPixelInt(position.getY() - halfHeartSize + 2),
                toPixelInt(halfHeartSize),
                toPixelInt(halfHeartSize)
        );
        g.fillOval(
                toPixelInt(position.getX() - 1),
                toPixelInt(position.getY() - halfHeartSize + 2),
                toPixelInt(halfHeartSize),
                toPixelInt(halfHeartSize)
        );
        int[] xPoints = {
                toPixelInt(position.getX() - halfHeartSize + 1),
                toPixelInt(position.getX() + halfHeartSize - 1),
                toPixelInt(position.getX())
        };
        int[] yPoints = {
                toPixelInt(position.getY() - halfHeartSize * 0.3 + 2),
                toPixelInt(position.getY() - halfHeartSize * 0.3 + 2),
                toPixelInt(position.getY() + halfHeartSize * 0.7 + 2)
        };
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.white);
        g.fillOval(
                toPixelInt(position.getX() - 10),
                toPixelInt(position.getY() - 11),
                toPixelInt(5),
                toPixelInt(2)
        );
    }

}
