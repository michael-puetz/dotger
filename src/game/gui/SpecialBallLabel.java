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

import game.gameFieldObjects.GameBall;
import game.gameFieldObjects.MulticolorBall;

import java.awt.*;

import javax.swing.SwingConstants;

public class SpecialBallLabel extends StatusbarLabel implements Runnable {

	private static final Dimension SIZE = new Dimension(100, 35);
	private final GameBall ball;
	// The color shouldn't change if the SpecialBallLabel isn't updated and a repaint happens during a party.
	private Color ballColor;

	public SpecialBallLabel(GameBall ball, String text) {
		super(text);
		this.ball = ball;
		ballColor = this.ball.getColor();
		setVerticalAlignment(SwingConstants.BOTTOM);
	}
	
	@Override
	public void run() {
		ball.animate(1);
		ballColor = ball.getColor();
		revalidate();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return SIZE;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		));
		super.paintComponent(graphics2D);
		Dimension size = getSize();
		graphics2D.setColor(getDarkerColor());
		graphics2D.fillRect(5, 6, size.width - 10, 12);
		graphics2D.setColor(getDarkerColor());
		graphics2D.fillOval(size.width / 2 - 10, 2, 20, 20);
		graphics2D.setColor(ballColor);
		graphics2D.fillOval(size.width / 2 - 6, 6, 12, 12);
		if (ball instanceof MulticolorBall) {
			graphics2D.setColor(((MulticolorBall)ball).getSecondColor());
			graphics2D.fillOval(size.width / 2 - 4, 8, 8, 8);
		}
	}

}
