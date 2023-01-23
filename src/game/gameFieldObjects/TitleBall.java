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

import static game.GeometryLogic.getDirectionAfterCollision;
import static game.Helpers.*;
import static game.GameConstants.*;

import game.CircleAngle;
import game.Collidable;
import game.GameColors;

import java.awt.*;

public class TitleBall extends GameBall {

    private static final Font FONT = new Font("SansSerif", Font.PLAIN, 20);
    private final char letter;

    public TitleBall(double posX, double posY, char letter) {
        super(posX, posY, TITLEBALL_RADIUS, CircleAngle.randomAngle(), TITLEBALL_SPEED, GameColors.getRandomTitleBallColor());
        this.letter = letter;
    }

    @Override
    public GameFieldObject getInstance(double posX, double posY, CircleAngle direction) {
        return new TitleBall(posX, posY, ' ');
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        g.setFont(FONT);
        g.setColor(Color.white);
        g.drawString(
                String.valueOf(letter),
                toPixelInt(position.getX() - g.getFontMetrics().stringWidth(String.valueOf(letter)) / 2.0),
                toPixelInt(position.getY() + g.getFontMetrics().getAscent() / 2.0)
        );
    }

    @Override
    public void reactToCollision(Collidable otherObject) {
        CircleAngle collisionAngle = getCollisionAngle(otherObject);
        direction = getDirectionAfterCollision(collisionAngle, direction);
    }

}
