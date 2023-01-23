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

package game.gui;

import static game.GameConstants.*;

import game.GameColors;

import java.awt.*;

import javax.swing.*;

public class StatusbarLabel extends JLabel implements Runnable {

	protected static Color background = GameColors.STATUSBAR;
	protected static Color darkerColor = GameColors.STATUSBAR_DARKER;

	protected boolean isHighlighted;
	protected boolean highlightPositive;
	protected int highlightCounter;

	public StatusbarLabel(String text) {
		super(text);
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public void run() {
		if (isHighlighted) {
			highlightCounter++;
			if (highlightCounter == HIGHLIGHT_GAME_TICKS) {
				isHighlighted = false;
			}
			revalidate();
			repaint();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		));
		Paint oldPaint = graphics2D.getPaint();
		Color color;
		double fadePercent = (double) highlightCounter / HIGHLIGHT_GAME_TICKS;
		if (isHighlighted && highlightPositive) {
			color = GameColors.getHighlightFadeColor(fadePercent, GameColors.HIGHLIGHT_POSITIVE, getBackgroundColor());
		} else if (isHighlighted) {
			color = GameColors.getHighlightFadeColor(fadePercent, GameColors.HIGHLIGHT_NEGATIVE, getBackgroundColor());
		} else {
			color = getBackgroundColor();
		}
		graphics2D.setPaint(new LinearGradientPaint(
				0, 0, 
				0, getHeight(), 
				new float[] {0f, 1f}, 
				new Color[] {color, GameColors.getDarkerByValue(color, 15)}
		));
		graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), LABEL_CORNER_RADIUS, LABEL_CORNER_RADIUS);
		graphics2D.setPaint(oldPaint);
		super.paintComponent(graphics2D);
	}
	
	public static void showLooseColors() {
		setBackgroundColor(GameColors.STATUSBAR_LOOSE);
		setDarkerColor(GameColors.STATUSBAR_LOOSE_DARKER);
	}
	
	public static void showNormalColors() {
		setBackgroundColor(GameColors.STATUSBAR);
		setDarkerColor(GameColors.STATUSBAR_DARKER);
	}

	public void highlight(boolean positive) {
		isHighlighted = true;
		highlightPositive = positive;
		highlightCounter = 0;
	}
	
	protected static void setBackgroundColor(Color color) {
		background = color;
	}
	
	protected static Color getBackgroundColor() {
		return background;
	}
	
	protected static void setDarkerColor(Color color) {
		darkerColor = color;
	}
	
	protected static Color getDarkerColor() {
		return darkerColor;
	}

}
