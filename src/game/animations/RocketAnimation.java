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

package game.animations;

import static game.GameConstants.*;
import static game.Helpers.*;

import game.GameColors;
import game.GameSounds;

import java.awt.*;

public class RocketAnimation extends Animation {

    private final double startYPosition;
    private final double xOffset;
    private double currentYPosition;
    private double currentXPosition;
    private final double functionOneDistance;
    private boolean exploded = false;
    private final FireworkAnimation firework;

    private enum driftDirection {LEFT, RIGHT}
    private final driftDirection drift;

    public RocketAnimation(double posX, double posY, double speed, Color color, Dimension gameSize, int gameTickDelay) {
        super(posX, posY, speed, GameColors.ROCKET, gameTickDelay);
        functionOneDistance = RANDOM.nextDouble(gameSize.height * 2.0, gameSize.height * 30.0);
        startYPosition = gameSize.height + (int) Math.ceil(ROCKET_RADIUS);
        xOffset = Math.pow((startYPosition - posY) / functionOneDistance, 2.0) * functionOneDistance;
        currentYPosition = startYPosition;
        currentXPosition = getCurrentXPosition();
        firework = new FireworkAnimation(
                posX,
                posY,
                RANDOM.nextDouble(MIN_FIREWORK_SIZE, MAX_FIREWORK_SIZE),
                speed,
                color,
                0
        );
        drift = driftDirection.values()[RANDOM.nextInt(driftDirection.values().length)];
    }

    private double getCurrentXPosition() {
        double functionValue = (startYPosition - currentYPosition) / functionOneDistance;
        if (drift == driftDirection.LEFT) {
            return position.getX() + xOffset - Math.pow(functionValue , 2.0) * functionOneDistance;
        } else if (drift == driftDirection.RIGHT) {
            return position.getX() - xOffset + Math.pow(functionValue , 2.0) * functionOneDistance;
        }
        return position.getX();
    }

    @Override
    public void animate(final double speedChangeFactor) {
    	if (animationTickCounter == 0) {
    		GAME_SOUNDS.playSound(GameSounds.ROCKET_SOUND);
    	}
        tickAndRunIfAlive(() -> {
            if (!exploded) {
                currentYPosition -= (speed * speedChangeFactor);
                currentXPosition = getCurrentXPosition();
                if (currentYPosition <= position.getY()) {
                    currentYPosition = position.getY();
                    exploded = true;
                }
            } else if (!firework.isFinished()) {
                firework.animate(speedChangeFactor);
            } else {
                isFinished = true;
            }
        });
    }

    @Override
    public void draw(Graphics2D g) {
        if (isFinished || animationTickCounter < 0) {
            return;
        }
        if (exploded) {
            firework.draw(g);
            return;
        }
        g.setColor(GameColors.ROCKET);
        g.fillOval(
                toPixelInt(currentXPosition - ROCKET_RADIUS),
                toPixelInt(currentYPosition - ROCKET_RADIUS),
                toPixelInt(ROCKET_RADIUS * 2.0),
                toPixelInt(ROCKET_RADIUS * 2.0)
        );

    }

}
