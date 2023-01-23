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

import game.GameColors;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static game.GameConstants.LABEL_CORNER_RADIUS;

public class GameButton extends JButton {

    private boolean isGameOver = false;

    private final FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            setBackground(isGameOver ? GameColors.BUTTON_LOOSE_FOCUSED : GameColors.BUTTON_FOCUSED);
        }

        @Override
        public void focusLost(FocusEvent e) {
            setBackground(isGameOver ? GameColors.BUTTON_LOOSE : GameColors.BUTTON);
        }
    };

    {
        addFocusListener(focusListener);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);
        setBackground(GameColors.BUTTON);
    }

    public GameButton(String text) {
        super(text);
    }

    public GameButton(Icon icon) {
        super(icon);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Color background = getModel().isPressed() ? getButtonPressedColor() : getBackground();
    	Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		));
    	Paint oldPaint = graphics2D.getPaint();
		graphics2D.setPaint(new LinearGradientPaint(
				0, 0, 
				0, getHeight(), 
				new float[] {0f, 1f}, 
				new Color[] {background, GameColors.getDarkerByValue(background, 15)}
		));
		graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), LABEL_CORNER_RADIUS, LABEL_CORNER_RADIUS);
		graphics2D.setPaint(oldPaint);
		super.paintComponent(graphics2D);
    }

    private Color getButtonPressedColor() {
        return isGameOver ? GameColors.BUTTON_LOOSE.darker() : GameColors.BUTTON.darker();
    }

    public void showNormalColors() {
        setBackground(hasFocus() ? GameColors.BUTTON_FOCUSED : GameColors.BUTTON);
        isGameOver = false;
    }

    public void showLooseColors() {
        setBackground(hasFocus() ? GameColors.BUTTON_LOOSE_FOCUSED : GameColors.BUTTON_LOOSE);
        isGameOver = true;
    }

}
