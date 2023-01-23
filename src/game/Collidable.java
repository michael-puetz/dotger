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

import static game.GeometryLogic.*;

import game.shapes.Circle;
import game.shapes.Line;
import game.shapes.GameShape;
import game.shapes.Square;

import java.awt.geom.Point2D;

public interface Collidable {

    default boolean isCollidingWith(Collidable otherObject) {
        Collidable objectOne = this;
        Collidable objectTwo = otherObject;
        GameShape shapeOne = objectOne.getShape();
        GameShape shapeTwo = objectTwo.getShape();
        double xDistance = xDistanceToPoint(objectOne.getPosX(), objectTwo.getPosX());
        double yDistance = yDistanceToPoint(objectOne.getPosY(), objectTwo.getPosY());

        if (shapeOne instanceof Circle circleOne && shapeTwo instanceof Circle circleTwo) {
            double collisionDistance = circleOne.getRadius() + circleTwo.getRadius();
            double actualDistance = objectOne.getPosition().distance(objectTwo.getPosition());
            return actualDistance <= collisionDistance;
        }
        if (shapeOne instanceof Square squareOne && shapeTwo instanceof Square squareTwo) {
            double xyMinDistance = (squareOne.getSideLength() + squareTwo.getSideLength()) / 2.0;
            return xDistance <= xyMinDistance && yDistance <= xyMinDistance;
        }
        if ((shapeOne instanceof Square && shapeTwo instanceof Circle)
        		|| (shapeOne instanceof Circle && shapeTwo instanceof Line)
        			|| (shapeOne instanceof Square && shapeTwo instanceof Line)) {
            objectOne = objectTwo;
            objectTwo = this;
            shapeOne = objectOne.getShape();
            shapeTwo = objectTwo.getShape();
        }
        if (shapeOne instanceof Circle circle && shapeTwo instanceof Square square) {
            double xyMinDistance = circle.getRadius() + square.getSideLength() / 2.0;
            double squareHalfWidth = square.getSideLength() / 2.0;
            double squareXLeft = objectTwo.getPosX() - squareHalfWidth;
            double squareXRight = objectTwo.getPosX() + squareHalfWidth;
            double squareYTop = objectTwo.getPosY() - squareHalfWidth;
            double squareYBottom = objectTwo.getPosY() + squareHalfWidth;
            if (objectOne.getPosX() >= squareXLeft && objectOne.getPosX() <= squareXRight
                    && yDistance <= xyMinDistance) {
                return true;
            }
            if (objectOne.getPosY() >= squareYTop && objectOne.getPosY() <= squareYBottom
                    && xDistance <= xyMinDistance) {
                return true;
            }
            Point2D.Double topLeft = new Point2D.Double(squareXLeft, squareYTop);
            Point2D.Double topRight = new Point2D.Double(squareXRight, squareYTop);
            Point2D.Double bottomLeft = new Point2D.Double(squareXLeft, squareYBottom);
            Point2D.Double bottomRight = new Point2D.Double(squareXRight, squareYBottom);
            double topLeftDistance = topLeft.distance(objectOne.getPosition());
            double topRightDistance = topRight.distance(objectOne.getPosition());
            double bottomLeftDistance = bottomLeft.distance(objectOne.getPosition());
            double bottomRightDistance = bottomRight.distance(objectOne.getPosition());
            return topLeftDistance <= circle.getRadius() || topRightDistance <= circle.getRadius()
                    || bottomLeftDistance <= circle.getRadius() || bottomRightDistance <= circle.getRadius();
        }
        if (shapeOne instanceof Line line && shapeTwo instanceof Circle circle) {
        	double firstPointDistance = objectOne.getPosition().distance(objectTwo.getPosition());
            Point2D.Double secondPoint = Line.getSecondPoint(line, getPosX(), getPosY());
        	double secondPointDistance = secondPoint.distance(objectTwo.getPosition());
        	if (firstPointDistance < circle.getRadius() || secondPointDistance < circle.getRadius()) {
        		return true;
        	}
        	CircleAngle angleToCircle = angleToPoint(objectOne.getPosition(), objectTwo.getPosition());
        	double angleBetween = CircleAngle.smallerAngleBetween(angleToCircle, line.getAngle()).get();
        	if (angleBetween > CircleAngle.DEGREE_90.get()) {
        		return false;
        	}
        	double distance = objectOne.getPosition().distance(objectTwo.getPosition());
        	if (distance > line.getLength() / Math.cos(angleBetween)) {
        		return false;
        	}
        	double centerToLineDistance = Math.sin(angleBetween) * distance;
        	return centerToLineDistance < circle.getRadius();
        }
        if (shapeOne instanceof Line line && shapeTwo instanceof Square square) {
        	double xyMinDistance = square.getSideLength() / 2.0;
        	double xDistanceFirstPoint = xDistanceToPoint(objectOne.getPosX(), objectTwo.getPosX());
        	double yDistanceFirstPoint = yDistanceToPoint(objectOne.getPosY(), objectTwo.getPosY());
        	double secondPointX = objectOne.getPosX() + Math.cos(line.getAngle().get()) * line.getLength();
        	double secondPointY = objectOne.getPosY() + Math.sin(line.getAngle().get()) * line.getLength();
        	double xDistanceSecondPoint = xDistanceToPoint(secondPointX, objectTwo.getPosX());
        	double yDistanceSecondPoint = yDistanceToPoint(secondPointY, objectTwo.getPosY());
        	if (xDistanceFirstPoint < xyMinDistance && yDistanceFirstPoint < xyMinDistance
        			|| xDistanceSecondPoint < xyMinDistance && yDistanceSecondPoint < xyMinDistance) {
        		return true;
        	}
        	CircleAngle angleToSquare = angleToPoint(objectOne.getPosition(), objectTwo.getPosition());
        	double angleBetween = CircleAngle.smallerAngleBetween(angleToSquare, line.getAngle()).get();
        	if (angleBetween > CircleAngle.DEGREE_90.get()) {
        		return false;
        	}
        	double distance = objectOne.getPosition().distance(objectTwo.getPosition());
        	if (distance > line.getLength() / Math.cos(angleBetween)) {
        		return false;
        	}
        	double centerToLineDistance = Math.sin(angleBetween) * distance;
        	double angle45Offset = line.getAngle().get() % CircleAngle.DEGREE_45.get();
        	if (((int) (line.getAngle().get() / CircleAngle.DEGREE_45.get())) % 2 == 0) {
        		angle45Offset = CircleAngle.DEGREE_45.get() - angle45Offset;
        	}
        	double minCenterToLineDistance = Math.cos(angle45Offset) * Math.sqrt(2) * xyMinDistance;
        	return centerToLineDistance < minCenterToLineDistance;
        }
        return false;
    }

    default CircleAngle getCollisionAngle(Collidable otherObject) {
        Collidable objectOne = this;
        Collidable objectTwo = otherObject;
        GameShape shapeOne = objectOne.getShape();
        GameShape shapeTwo = objectTwo.getShape();
        CircleAngle angleToObjectTwo = angleToPoint(objectOne.getPosition(), objectTwo.getPosition());
        boolean swapped = false;

        if (shapeOne instanceof Circle && shapeTwo instanceof Circle) {
            return angleToObjectTwo;
        }
        if (shapeOne instanceof Square && shapeTwo instanceof Square) {
            if (angleToObjectTwo.isBetweenOrEqual(CircleAngle.DEGREE_45, CircleAngle.DEGREE_135)) {
                return CircleAngle.DEGREE_90;
            } else if (angleToObjectTwo.isBetween(CircleAngle.DEGREE_135, CircleAngle.DEGREE_225)) {
                return CircleAngle.DEGREE_180;
            } else if (angleToObjectTwo.isBetweenOrEqual(CircleAngle.DEGREE_225, CircleAngle.DEGREE_315)) {
                return CircleAngle.DEGREE_270;
            } else {
                return CircleAngle.DEGREE_0;
            }
        }
        if ((shapeOne instanceof Square && shapeTwo instanceof Circle)
        		|| (shapeOne instanceof Circle && shapeTwo instanceof Line)
        			|| (shapeOne instanceof Square && shapeTwo instanceof Line)) {
            objectOne = objectTwo;
            objectTwo = this;
            shapeOne = objectOne.getShape();
            shapeTwo = objectTwo.getShape();
            angleToObjectTwo = angleToPoint(objectOne.getPosition(), objectTwo.getPosition());
            swapped = true;
        }
        if (shapeOne instanceof Circle && shapeTwo instanceof Square square) {
            CircleAngle angle;
            double squareHalfWidth = square.getSideLength() / 2.0;
            double squareXLeft = objectTwo.getPosX() - squareHalfWidth;
            double squareXRight = objectTwo.getPosX() + squareHalfWidth;
            double squareYTop = objectTwo.getPosY() - squareHalfWidth;
            double squareYBottom = objectTwo.getPosY() + squareHalfWidth;

            Point2D.Double topLeft = new Point2D.Double(squareXLeft, squareYTop);
            Point2D.Double topRight = new Point2D.Double(squareXRight, squareYTop);
            Point2D.Double bottomLeft = new Point2D.Double(squareXLeft, squareYBottom);
            Point2D.Double bottomRight = new Point2D.Double(squareXRight, squareYBottom);

            if (angleToObjectTwo.isBetweenOrEqual(CircleAngle.DEGREE_45, CircleAngle.DEGREE_135)) {
                if (objectOne.getPosX() < squareXLeft) {
                    angle = angleToPoint(objectOne.getPosition(), topLeft);
                } else if (objectOne.getPosX() > squareXRight) {
                    angle = angleToPoint(objectOne.getPosition(), topRight);
                } else {
                    angle = CircleAngle.DEGREE_90;
                }
            } else if (angleToObjectTwo.isBetween(CircleAngle.DEGREE_135, CircleAngle.DEGREE_225)) {
                if (objectOne.getPosY() < squareYTop) {
                    angle = angleToPoint(objectOne.getPosition(), topRight);
                } else if (objectOne.getPosY() > squareYBottom) {
                    angle = angleToPoint(objectOne.getPosition(), bottomRight);
                } else {
                    angle = CircleAngle.DEGREE_180;
                }
            } else if (angleToObjectTwo.isBetweenOrEqual(CircleAngle.DEGREE_225, CircleAngle.DEGREE_315)) {
                if (objectOne.getPosX() < squareXLeft) {
                    angle = angleToPoint(objectOne.getPosition(), bottomLeft);
                } else if (objectOne.getPosX() > squareXRight) {
                    angle = angleToPoint(objectOne.getPosition(), bottomRight);
                } else {
                    angle = CircleAngle.DEGREE_270;
                }
            } else {
                if (objectOne.getPosY() < squareYTop) {
                    angle = angleToPoint(objectOne.getPosition(), topLeft);
                } else if (objectOne.getPosY() > squareYBottom) {
                    angle = angleToPoint(objectOne.getPosition(), bottomLeft);
                } else {
                    angle = CircleAngle.DEGREE_0;
                }
            }
            if (swapped) {
                return CircleAngle.oppositeAngle(angle);
            } else {
                return angle;
            }
        }
        if (shapeOne instanceof Line line && (shapeTwo instanceof Circle || shapeTwo instanceof Square)) {
        	CircleAngle angle;
        	CircleAngle angleToCircle = angleToPoint(objectOne.getPosition(), objectTwo.getPosition());
        	if (angleToCircle.isBetweenOrEqual(line.getAngle(), CircleAngle.oppositeAngle(line.getAngle()))) {
        		angle = line.getAngle().subtract(CircleAngle.DEGREE_90);
        	} else {
        		angle = line.getAngle().add(CircleAngle.DEGREE_90);
        	}
        	if (swapped) {
                return angle;
            } else {
                return CircleAngle.oppositeAngle(angle);
            }
        }
        return new CircleAngle();
    }

    GameShape getShape();

    double getPosX();

    double getPosY();

    Point2D.Double getPosition();

}
