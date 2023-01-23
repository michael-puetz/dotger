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

import game.CircleAngle;
import game.GameColors;

import java.awt.*;

public class SpawnAnimation extends Animation {

    private final double size;
    private final ParticleAnimation[] particles;

    public SpawnAnimation(double posX, double posY, double size, double speed, Color color, int gameTickDelay) {
        super(posX, posY, speed, color, gameTickDelay);
        this.size = size;
        particles = new ParticleAnimation[RANDOM.nextInt(MIN_NUMBER_OF_PARTICLES, MAX_NUMBER_OF_PARTICLES)];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = getRandomParticle();
        }
    }

    private ParticleAnimation getRandomParticle() {
        CircleAngle angle = CircleAngle.randomAngle();
        double particleSize = RANDOM.nextDouble(size * MAX_PARTICLE_SIZE_PERCENT);
        double distance = RANDOM.nextDouble(size / 2.0 - particleSize / 2.0);
        double xPosition = position.getX() + Math.cos(angle.get()) * distance;
        double yPosition = position.getY() + Math.sin(angle.get()) * distance;
        return new ParticleAnimation(
                xPosition,
                yPosition,
                particleSize / 2.0,
                speed,
                GameColors.getRandomDarkerOrBrighter(color),
                RANDOM.nextInt(MAX_PARTICLE_DELAY_GT)
        );
    }

    @Override
    public void animate(final double speedChangeFactor) {
        tickAndRunIfAlive(() -> {
            boolean finished = true;
            for (ParticleAnimation animation : particles) {
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
        for (ParticleAnimation animation : particles) {
            if (!animation.isFinished()) {
                animation.draw(g);
            }
        }
    }

}
