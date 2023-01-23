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

import java.awt.Font;

public class GameConstants {

    // Version
    public static final String VERSION_NUMBER = "1.0.18";

    // Fonts
    public static final Font POINTS_FONT = new Font("SansSerif", Font.PLAIN, 34);

    // General
    public static final int CORNER_SIZE = 40;
    public static final int LABEL_CORNER_RADIUS = 4;
    public static final double MIN_SPAWN_DISTANCE = 200;
    public static final double FIXED_OBJECT_MIN_DISTANCE = 70;
    public static final double PLAYER_SPEED = 4.2;
    public static final double SLOW_MOTION_FACTOR = 0.5;
    public static final int OBJECTS_UNTIL_SPECIAL_OBJECT = 2;
    public static final int STARTUP_PREVIEW_DELAY = 200;
    public static final int STARTUP_SPEEDUP_TICKS = 200;
    public static final double TITLEBALL_DISTANCE = 50;
    public static final double COLLISION_SPEED_CHANGE = 0.4;
    public static final String[] STARTUP_TEXTS = {
            "LET'S GO!",
            "GET READY!",
            "LET'S PLAY!",
            "READY?",
            "DODGE IT!"
    };

    public static final int GAME_TICK_LENGTH_MS = 10;
    public static final int GAME_TICKS_UNTIL_OBJECT = 200;
    public static final int GAME_TICKS_UNTIL_LIGHTNINGROD = 1200;
    public static final int PARTY_GAME_TICKS = 500;
    public static final int INVINCIBLE_GAME_TICKS = 300;
    public static final int SPEED_CHANGE_GAME_TICKS = 400;
    public static final int PLAYER_HURT_GAME_TICKS = 50;
    public static final int HIGHLIGHT_GAME_TICKS = 70;

    // Sound
    public static final int VOLUME_INIT_VALUE = 100;

    // Cheats
    public static final boolean INVINCIBLE_FOR_TEST = false;
    public static final boolean FOR_ANNIKA = false;

    // Animations
    public static final double SHOCKWAVE_SPEED = 6.0;
    public static final double SHOCKWAVE_MAX_RADIUS = 100.0;
    public static final double DESTROYANIMATION_SPEED = 0.8;
    public static final double SPAWNANIMATION_SPEED = 0.1;
    public static final double MAX_PARTICLE_SIZE_PERCENT = 0.16;
    public static final int MIN_NUMBER_OF_PARTICLES = 10;
    public static final int MAX_NUMBER_OF_PARTICLES = 14;
    public static final int MAX_PARTICLE_DELAY_GT = 3;
    public static final double ROCKET_RADIUS = 3.5;
    public static final double MIN_FIREWORK_SIZE = 150;
    public static final double MAX_FIREWORK_SIZE = 250;
    public static final double FIREWORK_MIN_BORDER_DISTANCE = 50;
    public static final int FIREWORKSHOW_ROCKET_DELAY = 60;
    public static final int FIREWORKSHOW_PARTICLE_DELAY = 6;
    public static final double FIREWORKSHOW_PARTICLE_RADIUS = 4.0;
    public static final double FIREWORKSHOW_PARTICLE_SPEED = 0.12;
    public static final double LIGHTNINGANIMATION_SPEED = 0.7;
    public static final int LIGHTNINGANIMATION_MAX_THICKNESS = 7;

    // Game Objects
    public static final int GAMEBALL_MIN_RADIUS = 5;
    public static final int GAMEBALL_MAX_RADIUS = 30;
    public static final int SUPERBALL_RADIUS = 10;
    public static final int LIVEBALL_RADIUS = 5;
    public static final int PARTYBALL_RADIUS = 5;
    public static final int BLASTBALL_RADIUS = 15;
    public static final int GODMODEBALL_RADIUS = 10;
    public static final int TITLEBALL_RADIUS = 22;
    public static final int SLOWMOTIONBALL_RADIUS = 8;

    public static final int FIXEDBALL_RADIUS = 15;
    public static final int FIXEDBLOCK_SIDE_LENGTH = 30;
    public static final int PLAYER_MOVING_BLOCK_SIDE_LENGTH = 30;

    public static final double GAMEBALL_MIN_SPEED = 2.4;
    public static final double GAMEBALL_MAX_SPEED = 3.8;
    public static final double SUPERBALL_START_SPEED = 6.0;
    public static final double LIVEBALL_SPEED = 2.2;
    public static final double PARTYBALL_SPEED = 1.8;
    public static final double BLASTBALL_SPEED = 2.2;
    public static final double GODMODEBALL_SPEED = 2.8;
    public static final double TITLEBALL_SPEED = 2.0;
    public static final double SLOWMOTIONBALL_SPEED = 2.8;
    public static final double PLAYER_MOVING_BLOCK_SPEED = 0.8;

    public static final double BLASTBALL_PULSATING_WIDTH = 2;
    public static final double BLASTBALL_PULSATING_SPEED = 0.05;

    public static final int PRELIGHTNINGROD_GAME_TICKS = 70;

}
