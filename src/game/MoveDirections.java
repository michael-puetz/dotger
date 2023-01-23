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

public enum MoveDirections {

    RIGHT(CircleAngle.DEGREE_0),
    DOWN_RIGHT(CircleAngle.DEGREE_45),
    DOWN(CircleAngle.DEGREE_90),
    DOWN_LEFT(CircleAngle.DEGREE_135),
    LEFT(CircleAngle.DEGREE_180),
    UP_LEFT(CircleAngle.DEGREE_225),
    UP(CircleAngle.DEGREE_270),
    UP_RIGHT(CircleAngle.DEGREE_315);

    final CircleAngle angle;

    MoveDirections(CircleAngle angle) {
        this.angle = angle;
    }

    public CircleAngle getAngle() {
        return angle;
    }

}
