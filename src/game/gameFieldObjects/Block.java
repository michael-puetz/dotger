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

import game.Destructible;
import game.animations.Animation;
import game.animations.DestroyAnimation;
import game.shapes.Square;

import static game.GameConstants.DESTROYANIMATION_SPEED;

import java.awt.*;

public abstract class Block extends GameFieldObject implements Destructible {

    public Block(double posX, double posY, double sideLength, Color color) {
        super(posX, posY, new Square(sideLength), color);
    }
    
    @Override
	public Animation[] getDestroyAnimations() {
    	return new Animation[] {
    			new DestroyAnimation(
    					getPosX(),
    					getPosY(),
    					getSize(),
    					DESTROYANIMATION_SPEED,
    					getColor(),
    					0
    			)
    	};
	}
	
    public double getSideLength() {
        return ((Square) shape).getSideLength();
    }

}
