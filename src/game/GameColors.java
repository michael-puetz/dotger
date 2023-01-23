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

import static game.Helpers.*;

import java.awt.Color;

public class GameColors {

	public static final Color MAIN = new Color(110, 130, 162);
	public static final Color LOOSE = new Color(170, 0, 0);
	public static final Color GAME_FIELD = new Color(35, 35, 40);
	public static final Color GAME_OVER_BACKGROUND = new Color(50, 45, 45, 200);
	public static final Color GAME_OVER_TEXT = new Color(140, 140, 140);
	public static final Color STATUSBAR = getBrighterByValue(MAIN, 40);
	public static final Color STATUSBAR_DARKER = getDarkerByValue(STATUSBAR, 12);
	public static final Color STATUSBAR_LOOSE = getBrighterByValue(LOOSE, 40);
	public static final Color STATUSBAR_LOOSE_DARKER = getDarkerByValue(STATUSBAR_LOOSE, 12);
	public static final Color BUTTON = getBrighterByValue(STATUSBAR, 25);
	public static final Color BUTTON_FOCUSED = getBrighterByValue(BUTTON, 25);
	public static final Color BUTTON_LOOSE = getBrighterByValue(STATUSBAR_LOOSE, 25);
	public static final Color BUTTON_LOOSE_FOCUSED = getBrighterByValue(BUTTON_LOOSE, 25);
	public static final Color HIGHLIGHT_POSITIVE = new Color(250, 220, 0);
	public static final Color HIGHLIGHT_NEGATIVE = new Color(170, 0, 0);
	
	public static final Color PLAYER = new Color(255, 255, 255);
	public static final Color PLAYER_INVINCIBLE = new Color(0, 200, 0);
	public static final Color PLAYER_HURT = new Color(255, 0, 0);
	public static final Color LIVEBALL = new Color(252, 186, 3);
	public static final Color SLOWMOTIONBALL = new Color(0, 250, 255);
	public static final Color GODMODEBALL_MAIN = new Color(0, 0, 0);
	public static final Color GODMODEBALL_SECOND = new Color(0, 255, 0);
	public static final Color BLASTBALL_MAIN = new Color(0, 0, 0);
	public static final Color BLASTBALL_SECOND = new Color(200, 100, 10);
	public static final Color SUPERBALL_START = new Color(150, 10, 10);

	public static final Color SHOCKWAVE = new Color(100, 100, 100);
	public static final Color LIGHTNINGANIMATION = new Color(100, 170, 205);
	public static final Color SPAWN_ANIMATION = new Color(180, 180, 180);
	public static final Color ROCKET = new Color(200, 200, 200);
	public static final Color FIXED_BALL = new Color(70, 69, 73);
	public static final Color FIXED_BLOCK = FIXED_BALL;
	public static final Color PLAYER_MOVING_BLOCK = new Color(100, 100, 255);
	
	public static final Color[] PARTY_COLORS = {
			new Color(250, 10, 10),
			new Color(10, 250, 10),
			new Color(20, 20, 250),
			new Color(252, 186, 3),
			new Color(215, 0, 215),
			new Color(40, 160, 142)
	};

	private static final int SUPERBALL_COLOR_CHANGE = 70;

	public static Color getRandomPartyColor() {
		return PARTY_COLORS[RANDOM.nextInt(PARTY_COLORS.length)];
	}
	
	public static Color getSuperBallFadeColor(double percent) {
		double change = SUPERBALL_COLOR_CHANGE * percent;
		int newRed = (int) (SUPERBALL_START.getRed() - change);
		int newGreen = (int) (SUPERBALL_START.getGreen() + change);
		int newBlue = (int) (SUPERBALL_START.getBlue() + change);
		return new Color(newRed, newGreen, newBlue);
	}

	public static Color getHighlightFadeColor(double percent, Color highlight, Color background) {
		int newRed = (int) (highlight.getRed() + (background.getRed() - highlight.getRed()) * percent);
		int newGreen = (int) (highlight.getGreen() + (background.getGreen() - highlight.getGreen()) * percent);
		int newBlue = (int) (highlight.getBlue() + (background.getBlue() - highlight.getBlue()) * percent);
		return new Color(newRed, newGreen, newBlue);
	}
	
	public static Color getRandomGameBallColor() {
		int base = RANDOM.nextInt(10, 31);
		return new Color(base, base + RANDOM.nextInt(50, 106), base + RANDOM.nextInt(50, 106));
	}

	public static Color getRandomTitleBallColor() {
		int base = RANDOM.nextInt(10, 31);
		return new Color(base, base + RANDOM.nextInt(50, 106), base + RANDOM.nextInt(50, 106));
	}

	public static Color getBrighterByValue(Color color, int value) {
		return new Color(
				Math.min(color.getRed() + value, 255),
				Math.min(color.getGreen() + value, 255),
				Math.min(color.getBlue() + value, 255)
		);
	}

	public static Color getDarkerByValue(Color color, int value) {
		return new Color(
				Math.max(color.getRed() - value, 0),
				Math.max(color.getGreen() - value, 0),
				Math.max(color.getBlue() - value, 0)
		);
	}

	public static Color getRandomDarkerOrBrighter(Color color) {
		int red = RANDOM.nextInt(Math.max(color.getRed() - 20, 0), Math.min(color.getRed() + 20, 255));
		int green = RANDOM.nextInt(Math.max(color.getGreen() - 20, 0), Math.min(color.getGreen() + 20, 255));
		int blue = RANDOM.nextInt(Math.max(color.getBlue() - 20, 0), Math.min(color.getBlue() + 20, 255));
		return new Color(red, green, blue);
	}

}
