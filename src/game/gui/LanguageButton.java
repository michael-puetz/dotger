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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JButton;

import game.GameColors;

public class LanguageButton extends JButton {
	
	private static final int CORNER_SIZE = 5;
	
    private boolean isGameOver = false;
	
	public LanguageButton(Icon icon) {
        super(icon);
        setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);
    }
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g.create();
		graphics2D.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		));
		super.paintComponent(graphics2D);
		if (hasFocus()) {
			graphics2D.setColor(isGameOver ? GameColors.BUTTON_LOOSE_FOCUSED : GameColors.BUTTON_FOCUSED);
			graphics2D.fillPolygon(
					new int[] {0, CORNER_SIZE, 0}, 
					new int[] {0, 0, CORNER_SIZE}, 
					3
			);
			graphics2D.fillPolygon(
					new int[] {getWidth() - CORNER_SIZE, getWidth(), getWidth()}, 
					new int[] {0, 0, CORNER_SIZE}, 
					3
			);
			graphics2D.fillPolygon(
					new int[] {getWidth(), getWidth(), getWidth() - CORNER_SIZE}, 
					new int[] {getHeight() - CORNER_SIZE, getHeight(), getHeight()}, 
					3
			);
			graphics2D.fillPolygon(
					new int[] {0, CORNER_SIZE, 0}, 
					new int[] {getHeight() - CORNER_SIZE, getHeight(), getHeight()}, 
					3
			);
		}
		graphics2D.dispose();
	}
	
	public void showNormalColors() {
        isGameOver = false;
    }

    public void showLooseColors() {
        isGameOver = true;
    }
	
}
