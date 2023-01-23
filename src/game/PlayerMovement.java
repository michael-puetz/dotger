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

public class PlayerMovement {
	
	private boolean leftValue;
	private boolean topValue;
	private boolean rightValue;
	private boolean bottomValue;
	private boolean aValue;
	private boolean wValue;
	private boolean dValue;
	private boolean sValue;
	
	public void setLeftValue(boolean isMoving) {
		leftValue = isMoving && !rightValue && !dValue;
	}
	
	public void setTopValue(boolean isMoving) {
		topValue = isMoving && !bottomValue && !sValue;
	}
	
	public void setRightValue(boolean isMoving) {
		rightValue = isMoving && !leftValue && !aValue;
	}
	
	public void setBottomValue(boolean isMoving) {
		bottomValue = isMoving && !topValue && !wValue;
	}
	
	public void setAValue(boolean isMoving) {
		aValue = isMoving && !rightValue && !dValue;
	}
	
	public void setWValue(boolean isMoving) {
		wValue = isMoving && !bottomValue && !sValue;
	}
	
	public void setDValue(boolean isMoving) {
		dValue = isMoving && !leftValue && !aValue;
	}
	
	public void setSValue(boolean isMoving) {
		sValue = isMoving && !topValue && !wValue;
	}

	public void resetAll() {
		leftValue = topValue = rightValue = bottomValue = false;
		aValue = wValue = dValue = sValue = false;
	}

	public CircleAngle getMoveAngle() {
		if ((leftValue || aValue) && (topValue || wValue)) {
			return MoveDirections.UP_LEFT.getAngle();
		} else if ((topValue || wValue) && (rightValue || dValue)) {
			return MoveDirections.UP_RIGHT.getAngle();
		} else if ((rightValue || dValue) && (bottomValue || sValue)) {
			return MoveDirections.DOWN_RIGHT.getAngle();
		} else if ((bottomValue || sValue) && (leftValue || aValue)) {
			return MoveDirections.DOWN_LEFT.getAngle();
		} else if (leftValue || aValue) {
			return MoveDirections.LEFT.getAngle();
		} else if (topValue || wValue) {
			return MoveDirections.UP.getAngle();
		} else if (rightValue || dValue) {
			return MoveDirections.RIGHT.getAngle();
		} else if (bottomValue || sValue) {
			return MoveDirections.DOWN.getAngle();
		}
		return null;
	}
	
}
