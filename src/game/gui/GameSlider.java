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

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class GameSlider extends JSlider {

    private boolean isGameOver;

    private class GameSliderUI extends BasicSliderUI {

        public GameSliderUI(GameSlider slider) {
            super(slider);
        }

        private void paintCustomThumb(Graphics g, Color thumbColor) {
            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.addRenderingHints(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            ));
            graphics2D.setPaint(new RadialGradientPaint(
                    (float) (thumbRect.x + thumbRect.width / 2.0),
                    (float) (thumbRect.y + thumbRect.height / 2.0),
                    (float) (thumbRect.height / 2.0),
                    new float[] {0f, 1f},
                    new Color[] {thumbColor, GameColors.getDarkerByValue(thumbColor, 20)}
            ));
            graphics2D.fillRoundRect(
                    thumbRect.x,
                    thumbRect.y,
                    thumbRect.width,
                    thumbRect.height,
                    thumbRect.width / 2,
                    thumbRect.width / 2
            );
            graphics2D.dispose();
        }

        @Override
        public void paintFocus(Graphics g) {
            paintCustomThumb(g, isGameOver ? GameColors.BUTTON_LOOSE_FOCUSED : GameColors.BUTTON_FOCUSED);
        }

        @Override
        public void paintThumb(Graphics g) {
            if (!isFocusOwner()) {
                paintCustomThumb(g, isGameOver ? GameColors.BUTTON_LOOSE : GameColors.BUTTON);
            }
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.addRenderingHints(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            ));
            Color color;
            if (isGameOver) {
                color = GameColors.getDarkerByValue(GameColors.LOOSE, 25);
            } else {
                color = GameColors.getDarkerByValue(GameColors.MAIN, 25);
            }
            graphics2D.setColor(color);
            graphics2D.fillRoundRect(
                    trackRect.x,
                    trackRect.y + trackRect.height / 3,
                    trackRect.width,
                    trackRect.height / 3,
                    LABEL_CORNER_RADIUS,
                    LABEL_CORNER_RADIUS
            );
            int shadowOffset = 2;
            if (isGameOver) {
                color = GameColors.getDarkerByValue(GameColors.LOOSE, 10);
            } else {
                color = GameColors.getDarkerByValue(GameColors.MAIN, 10);
            }
            graphics2D.setColor(color);
            graphics2D.fillRoundRect(
                    trackRect.x + shadowOffset,
                    trackRect.y + trackRect.height / 3 + shadowOffset,
                    trackRect.width - shadowOffset,
                    trackRect.height / 3 - shadowOffset,
                    LABEL_CORNER_RADIUS,
                    LABEL_CORNER_RADIUS
            );
            graphics2D.dispose();
        }

    }

    public GameSlider(int min, int max, int value) {
        super(min, max, value);
        setBackground(GameColors.MAIN);
        setUI(new GameSliderUI(this));
    }

    public void showNormalColors() {
        isGameOver = false;
    }

    public void showLooseColors() {
        isGameOver = true;
    }

}


