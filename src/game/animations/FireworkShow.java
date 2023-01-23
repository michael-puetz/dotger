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

import java.awt.*;
import java.awt.geom.Point2D;

public class FireworkShow extends Animation {

    private final Animation[] particlesAndRockets;

    public FireworkShow(int points, Dimension gameSize) {
        int numberOfRockets = points * points / 200;
        int numberOfParticles = numberOfRockets * 10;
        particlesAndRockets = new Animation[numberOfRockets + numberOfParticles];
        for (int i = 0; i < numberOfParticles; i++) {
            particlesAndRockets[i] = getRandomParticle(i, gameSize);
        }
        for (int i = 0; i < numberOfRockets; i++) {
            particlesAndRockets[numberOfParticles + i] = getRandomRocket(i, gameSize);
        }
    }

    private ParticleAnimation getRandomParticle(int numberOfParticlesExisting, Dimension gameSize) {
        Point2D.Double position = getRandomPosition(gameSize);
        int delay = numberOfParticlesExisting * FIREWORKSHOW_PARTICLE_DELAY;
        return new ParticleAnimation(
                position.getX(),
                position.getY(),
                FIREWORKSHOW_PARTICLE_RADIUS,
                FIREWORKSHOW_PARTICLE_SPEED,
                GameColors.getRandomPartyColor(),
                delay
        );
    }

    private RocketAnimation getRandomRocket(int numberOfRocketsExisting, Dimension gameSize) {
        Point2D.Double position = getRandomPosition(gameSize);
        double rocketSpeed = RANDOM.nextDouble(2.0, 3.0);
        int delay = numberOfRocketsExisting * FIREWORKSHOW_ROCKET_DELAY;
        delay += RANDOM.nextInt(FIREWORKSHOW_ROCKET_DELAY / 2);
        return new RocketAnimation(
                position.getX(),
                position.getY(),
                rocketSpeed,
                GameColors.getRandomPartyColor(),
                gameSize,
                delay
        );
    }

    private Point2D.Double getRandomPosition(Dimension gameSize) {
        double xPosition = RANDOM.nextDouble(
                FIREWORK_MIN_BORDER_DISTANCE, gameSize.width - FIREWORK_MIN_BORDER_DISTANCE
        );
        double yPosition = RANDOM.nextDouble(
                FIREWORK_MIN_BORDER_DISTANCE, gameSize.height - FIREWORK_MIN_BORDER_DISTANCE
        );
        return new Point2D.Double(xPosition, yPosition);
    }

    @Override
    public void animate(final double speedChangeFactor) {
        tickAndRunIfAlive(() -> {
            boolean finished = true;
            for (Animation animation : particlesAndRockets) {
                if (!animation.isFinished()) {
                    animation.animate(speedChangeFactor);
                    finished = false;
                }
            }
            isFinished = finished;
        });
    }

    @Override
    public void draw(Graphics2D g) {
        if (isFinished || animationTickCounter < 0) {
            return;
        }
        for (Animation animation : particlesAndRockets) {
            if (!animation.isFinished()) {
                animation.draw(g);
            }
        }
    }

}
