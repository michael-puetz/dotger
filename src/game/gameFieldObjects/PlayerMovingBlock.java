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
import static game.GeometryLogic.*;
import static game.Helpers.*;

import game.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PlayerMovingBlock extends Block implements PlayerMovingCollidable, Movable, Spawnable {

    private final double speed;
    private CircleAngle direction;

    public PlayerMovingBlock() {
        this(0, 0, 0, new CircleAngle(), 0);
    }

    public PlayerMovingBlock(double posX, double posY, double sideLength, CircleAngle direction, double speed) {
        super(posX, posY, sideLength, GameColors.PLAYER_MOVING_BLOCK);
        this.direction = direction;
        this.speed = speed;
    }

    @Override
    public GameFieldObject getInstance(double posX, double posY, CircleAngle direction) {
        return new PlayerMovingBlock(posX, posY, PLAYER_MOVING_BLOCK_SIDE_LENGTH, direction, PLAYER_MOVING_BLOCK_SPEED);
    }

    @Override
    public Point2D.Double getRandomSpawnPosition(Rectangle2D.Double bounds) {
        double x = RANDOM.nextDouble(
                bounds.getX() + getSideLength() / 2.0,
                bounds.getX() + bounds.getWidth() - getSideLength() / 2.0
        );
        double y = RANDOM.nextDouble(
                bounds.getY() + getSideLength() / 2.0,
                bounds.getY() + bounds.getHeight() - getSideLength() / 2.0
        );
        return new Point2D.Double(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        double halfSideLength = getSideLength() / 2.0;
        int middleX = toPixelInt(position.getX());
        int middleY = toPixelInt(position.getY());
        int topLeftX = toPixelInt(position.getX() - halfSideLength);
        int topLeftY = toPixelInt(position.getY() - halfSideLength);
        int topRightX = toPixelInt(position.getX() + halfSideLength);
        int topRightY = toPixelInt(position.getY() - halfSideLength);
        int bottomLeftX = toPixelInt(position.getX() - halfSideLength);
        int bottomLeftY = toPixelInt(position.getY() + halfSideLength);
        int bottomRightX = toPixelInt(position.getX() + halfSideLength);
        int bottomRightY = toPixelInt(position.getY() + halfSideLength);
        g.setColor(getColor());
        g.fillPolygon(new int[] {topLeftX, topRightX, middleX}, new int[] {topLeftY, topRightY, middleY}, 3);
        g.setColor(getColor().darker().darker());
        g.fillPolygon(new int[] {topRightX, bottomRightX, middleX}, new int[] {topRightY, bottomRightY, middleY}, 3);
        g.setColor(getColor().darker().darker().darker());
        g.fillPolygon(new int[] {bottomRightX, bottomLeftX, middleX}, new int[] {bottomRightY, bottomLeftY, middleY}, 3);
        g.setColor(getColor().darker());
        g.fillPolygon(new int[] {bottomLeftX, topLeftX, middleX}, new int[] {bottomLeftY, topLeftY, middleY}, 3);
    }

    @Override
    public void move(double speedChangeFactor) {
        position.setLocation(
                position.getX() + Math.cos(direction.get()) * speed * speedChangeFactor,
                position.getY() + Math.sin(direction.get()) * speed * speedChangeFactor
        );
    }

    @Override
    public boolean isOnCollisionCourse(Collidable otherObject) {
        CircleAngle collisionAngle = getCollisionAngle(otherObject);
        return CircleAngle.smallerAngleBetween(collisionAngle, direction).get() < CircleAngle.DEGREE_90.get();
    }

    @Override
    public double getPlayerPushSpeed(PlayerBall player) {
        CircleAngle angleBetween = CircleAngle.smallerAngleBetween(getPlayerPushDirection(player), direction);
        double pushSpeed = Math.cos(angleBetween.get()) * speed;
        return pushSpeed > 0 ? pushSpeed : 0;
    }

    @Override
    public void reactToCollision(Collidable otherObject) {
        CircleAngle collisionAngle = getCollisionAngle(otherObject);
        direction = getDirectionAfterCollision(collisionAngle, direction);
    }

}
