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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	
	private boolean isGameOver;
	private TexturePaint paint;
	private TexturePaint gameOverPaint;
	
	public BackgroundPanel() {
		setOpaque(false);
		try {
			BufferedImage image = ImageIO.read(new File("images/pattern.png"));
			paint = new TexturePaint(
					image, new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight())
			);
			BufferedImage gameOverImage = ImageIO.read(new File("images/gameoverpattern.png"));
			gameOverPaint = new TexturePaint(
					gameOverImage, new Rectangle2D.Double(0, 0, gameOverImage.getWidth(), gameOverImage.getHeight())
			);
		} catch (IOException e) {
			System.out.println("Das Hintergrundmuster konnte nicht geladen werden.");
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		));
		if (paint != null) {
			Paint oldPaint = graphics2D.getPaint();
			graphics2D.setPaint(isGameOver ? gameOverPaint : paint);
			graphics2D.fillRect(0, 0, getWidth(), getHeight());
			graphics2D.setPaint(oldPaint);
		}
		super.paintComponent(graphics2D);
	}
	
	public void showLooseColors() {
		isGameOver = true;
	}
	
	public void showNormalColors() {
		isGameOver = false;
	}

}
