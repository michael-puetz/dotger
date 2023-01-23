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

import static game.GeometryLogic.angleToPoint;
import static game.GeometryLogic.xDistanceToPoint;
import static game.GeometryLogic.yDistanceToPoint;

import java.awt.geom.Point2D;

import game.gameFieldObjects.PlayerBall;
import game.shapes.Circle;
import game.shapes.Line;
import game.shapes.GameShape;
import game.shapes.Square;

public interface PlayerCollidable extends Collidable {

    default CircleAngle getPossibleMoveAngle(PlayerBall player, CircleAngle direction) {
        if (direction == null) {
            return null;
        }
        GameShape objectShape = getShape();
        CircleAngle collisionAngle = player.getCollisionAngle(this);
        CircleAngle newAngleOne = collisionAngle.subtract(CircleAngle.DEGREE_90);
        CircleAngle newAngleTwo = collisionAngle.add(CircleAngle.DEGREE_90);
        CircleAngle angleBetweenOne = CircleAngle.smallerAngleBetween(newAngleOne, direction);
        CircleAngle angleBetweenTwo = CircleAngle.smallerAngleBetween(newAngleTwo, direction);
        if (objectShape instanceof Circle) {
            if (CircleAngle.smallerAngleBetween(collisionAngle, direction).get() >= CircleAngle.DEGREE_90.get()) {
                return direction;
            }
            if (angleBetweenOne.get() < angleBetweenTwo.get()) {
                return newAngleOne;
            }
            return newAngleTwo;
        } else if (objectShape instanceof Square) {
            if (collisionAngle.get() == CircleAngle.DEGREE_0.get()) {
                if (direction == MoveDirections.UP_RIGHT.getAngle()) {
                    return MoveDirections.UP.getAngle();
                } else if (direction == MoveDirections.DOWN_RIGHT.getAngle()){
                    return MoveDirections.DOWN.getAngle();
                } else if (direction == MoveDirections.RIGHT.getAngle()) {
                    return null;
                }
            } else if (collisionAngle.get() == CircleAngle.DEGREE_90.get()) {
                if (direction == MoveDirections.DOWN_LEFT.getAngle()) {
                    return MoveDirections.LEFT.getAngle();
                } else if (direction == MoveDirections.DOWN_RIGHT.getAngle()){
                    return MoveDirections.RIGHT.getAngle();
                } else if (direction == MoveDirections.DOWN.getAngle()) {
                    return null;
                }
            } else if (collisionAngle.get() == CircleAngle.DEGREE_180.get()) {
                if (direction == MoveDirections.UP_LEFT.getAngle()) {
                    return MoveDirections.UP.getAngle();
                } else if (direction == MoveDirections.DOWN_LEFT.getAngle()){
                    return MoveDirections.DOWN.getAngle();
                } else if (direction == MoveDirections.LEFT.getAngle()) {
                    return null;
                }
            } else if (collisionAngle.get() == CircleAngle.DEGREE_270.get()) {
                if (direction == MoveDirections.UP_LEFT.getAngle()) {
                    return MoveDirections.LEFT.getAngle();
                } else if (direction == MoveDirections.UP_RIGHT.getAngle()){
                    return MoveDirections.RIGHT.getAngle();
                } else if (direction == MoveDirections.UP.getAngle()) {
                    return null;
                }
            } else {
                if (CircleAngle.smallerAngleBetween(collisionAngle, direction).get() >= CircleAngle.DEGREE_90.get()) {
                    return direction;
                }
                if (angleBetweenOne.get() < angleBetweenTwo.get()) {
                    return newAngleOne;
                }
                return newAngleTwo;
            }
        } else if (objectShape instanceof Line) {
        	if (direction.equals(collisionAngle)) {
        		return null;
        	}
        	if (CircleAngle.smallerAngleBetween(collisionAngle, direction).get() >= CircleAngle.DEGREE_90.get()) {
                return direction;
            }
        	if (angleBetweenOne.get() < angleBetweenTwo.get()) {
                return newAngleOne;
            }
            return newAngleTwo;
        }
        return direction;
    }
    
    default double getPossibleMoveDistance(PlayerBall player, CircleAngle direction) {
    	if (direction == null) {
    		return 0.0;
    	}
    	GameShape shape = getShape();
    	CircleAngle angleToThis = angleToPoint(player.getPosition(), this.getPosition());
    	double actualDistance = this.getPosition().distance(player.getPosition());
    	double possibleMovement = 0.0;
    	if (shape instanceof Circle circle) {
    		double collisionDistance = circle.getRadius() + player.getRadius();
    		// In a triangle with sides a, b, c and angles A, B, C where collisionDistance is 'a'
            // and actualDistance is 'b'.
    		double angleA = CircleAngle.smallerAngleBetween(angleToThis, direction).get();
    		if (angleA >= CircleAngle.DEGREE_90.get()) {
    			return player.getSpeed();
    		}
    		if (actualDistance <= collisionDistance) {
    			return 0.0;
    		}
    		double angleB = Math.asin(actualDistance / collisionDistance * Math.sin(angleA));
    		double angleC = CircleAngle.DEGREE_180.get() - angleA - angleB;
    		possibleMovement = collisionDistance * Math.sin(angleC) / Math.sin(angleA);
    	} else if (shape instanceof Square square) {
    		double xyMinDistance = player.getRadius() + square.getSideLength() / 2.0;
            double xDistance = xDistanceToPoint(this.getPosX(), player.getPosX());
            double yDistance = yDistanceToPoint(this.getPosY(), player.getPosY());
            double movementToSquareSide;
            CircleAngle angleToSquareSide;
    		if (angleToThis.isBetweenOrEqual(CircleAngle.DEGREE_45, CircleAngle.DEGREE_135)) {
    			movementToSquareSide = yDistance - xyMinDistance;
    			angleToSquareSide = CircleAngle.DEGREE_90;
            } else if (angleToThis.isBetween(CircleAngle.DEGREE_135, CircleAngle.DEGREE_225)) {
            	movementToSquareSide = xDistance - xyMinDistance;
    			angleToSquareSide = CircleAngle.DEGREE_180;
            } else if (angleToThis.isBetweenOrEqual(CircleAngle.DEGREE_225, CircleAngle.DEGREE_315)) {
            	movementToSquareSide = yDistance - xyMinDistance;
    			angleToSquareSide = CircleAngle.DEGREE_270;
            } else {
            	movementToSquareSide = xDistance - xyMinDistance;
    			angleToSquareSide = CircleAngle.DEGREE_0;
            }
    		double angleBetween = CircleAngle.smallerAngleBetween(angleToSquareSide, direction).get();
    		if (angleBetween >= CircleAngle.DEGREE_90.get()) {
    			return player.getSpeed();
    		}
    		if (xDistance < xyMinDistance && yDistance < xyMinDistance) {
    			return 0.0;
    		}
    		possibleMovement = movementToSquareSide / Math.cos(angleBetween);
    	} else if (shape instanceof Line line) {
    		double angleOffset = CircleAngle.smallerAngleBetween(
                    CircleAngle.oppositeAngle(angleToThis), line.getAngle()
            ).get();
        	double distance = this.getPosition().distance(player.getPosition());
            Point2D.Double secondPoint = Line.getSecondPoint(line, getPosX(), getPosY());
        	if (angleOffset > CircleAngle.DEGREE_90.get() || distance > line.getLength() / Math.cos(angleOffset)) {
        		possibleMovement = Math.min(
        				actualDistance, 
        				secondPoint.distance(player.getPosition())
        		) - player.getRadius();
        	} else {
        		double movementToLine = Math.sin(angleOffset) * actualDistance - player.getRadius();
        		if (movementToLine < 0.0) {
        			return 0.0;
        		}
                CircleAngle collisionAngle = player.getCollisionAngle(this);
                double angleBetween = CircleAngle.smallerAngleBetween(collisionAngle, direction).get();
                if (angleBetween >= CircleAngle.DEGREE_90.get()) {
                    return player.getSpeed();
                }
                possibleMovement = movementToLine / Math.cos(angleBetween);
        	}
    	}
    	return Math.min(possibleMovement, player.getSpeed());
    }

}
