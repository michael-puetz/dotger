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

import java.awt.geom.Point2D;

import static game.GameConstants.*;

public class GeometryLogic {

    public static double xDifferenceToPoint(double pointX, double otherPointX) {
        return otherPointX - pointX;
    }

    public static double yDifferenceToPoint(double pointY, double otherPointY) {
        return otherPointY - pointY;
    }

    public static double xDistanceToPoint(double pointX, double otherPointX) {
        return Math.abs(pointX - otherPointX);
    }

    public static double yDistanceToPoint(double pointY, double otherPointY) {
        return Math.abs(pointY - otherPointY);
    }

    public static CircleAngle angleToPoint(double pointX, double pointY, double otherPointX, double otherPointY) {
        double xDifference = xDifferenceToPoint(pointX, otherPointX);
        double yDifference = yDifferenceToPoint(pointY, otherPointY);
        double angleFromZeroOrOneEighty = Math.atan(Math.abs(yDifference) / Math.abs(xDifference));
        double angleFromNinetyOrTwoSeventy = Math.atan(Math.abs(xDifference) / Math.abs(yDifference));
        if (xDifference >= 0 && yDifference >= 0) {
            return new CircleAngle(angleFromZeroOrOneEighty);
        } else if (xDifference < 0 && yDifference >= 0) {
            return new CircleAngle(CircleAngle.DEGREE_90.get() + angleFromNinetyOrTwoSeventy);
        } else if (xDifference < 0 && yDifference < 0) {
            return new CircleAngle(CircleAngle.DEGREE_180.get() + angleFromZeroOrOneEighty);
        } else {
            return new CircleAngle(CircleAngle.DEGREE_270.get() + angleFromNinetyOrTwoSeventy);
        }
    }

    public static CircleAngle angleToPoint(Point2D.Double point, Point2D.Double otherPoint) {
        return angleToPoint(point.getX(), point.getY(), otherPoint.getX(), otherPoint.getY());
    }

    public static CircleAngle getDirectionAfterCollision(CircleAngle collisionAngle, CircleAngle direction) {
        CircleAngle angleBetween = CircleAngle.smallerAngleBetween(collisionAngle, direction);
        CircleAngle change;
        if (angleBetween.get() <= CircleAngle.DEGREE_90.get()) {
            change = CircleAngle.DEGREE_90.subtract(angleBetween).multiply(2);
        } else {
            change = CircleAngle.DEGREE_180.subtract(angleBetween).divide(2);
        }
        CircleAngle newAngleOne = direction.subtract(change);
        CircleAngle newAngleTwo = direction.add(change);
        CircleAngle angleBetweenOne = CircleAngle.smallerAngleBetween(newAngleOne, collisionAngle);
        CircleAngle angleBetweenTwo = CircleAngle.smallerAngleBetween(newAngleTwo, collisionAngle);
        if (angleBetweenOne.get() > angleBetweenTwo.get()) {
            return newAngleOne;
        } else {
            return newAngleTwo;
        }
    }

    public static double getSpeedAfterCollision(CircleAngle collisionAngle, CircleAngle direction, double currentSpeed) {
        CircleAngle angleBetween = CircleAngle.smallerAngleBetween(collisionAngle, direction);
        if (angleBetween.get() <= CircleAngle.DEGREE_90.get()) {
            return currentSpeed - COLLISION_SPEED_CHANGE;
        } else {
            return currentSpeed + COLLISION_SPEED_CHANGE;
        }
    }

}
