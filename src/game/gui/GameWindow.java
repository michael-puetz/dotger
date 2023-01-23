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

import game.*;
import game.gameFieldObjects.*;

import static game.GameConstants.GAME_TICK_LENGTH_MS;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameWindow extends JFrame implements LanguageSwitcher {

	private static final GameLanguage gameLanguage = GameLanguage.getInstance();
	private final BackgroundPanel contentPane;
	private final JPanel statusBar;
	private final StatusbarLabel lblTitleText;
	private final StatusbarLabel lblPointsText;
	private final StatusbarLabel lblLivesText;
	private final SpecialBallLabel lifeBallLabel;
	private final SpecialBallLabel blastBallLabel;
	private final SpecialBallLabel partyBallLabel;
	private final SpecialBallLabel godModeBallLabel;
	private final SpecialBallLabel slowMotionBallLabel;
	private final GameButton btnSettings;
	private final GameButton btnStart;
	private final GamePanel gamePanel;
	private int lives = 0;

	public GameWindow() {
		ImageIcon icon = new ImageIcon("icons/GameIcon.png");
		setIconImage(icon.getImage());

		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		KeyListener gameKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					gamePanel.setPlayerLeftMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					gamePanel.setPlayerTopMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					gamePanel.setPlayerRightMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					gamePanel.setPlayerBottomMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_A) {
					gamePanel.setPlayerAMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_W) {
					gamePanel.setPlayerWMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					gamePanel.setPlayerDMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_S) {
					gamePanel.setPlayerSMovement(true);
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (btnStart.hasFocus()) {
						btnStart.doClick();
					} else if (btnSettings.hasFocus()) {
						btnSettings.doClick();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					gamePanel.setPlayerLeftMovement(false);
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					gamePanel.setPlayerTopMovement(false);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					gamePanel.setPlayerRightMovement(false);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					gamePanel.setPlayerBottomMovement(false);
				} else if (e.getKeyCode() == KeyEvent.VK_A) {
					gamePanel.setPlayerAMovement(false);
				} else if (e.getKeyCode() == KeyEvent.VK_W) {
					gamePanel.setPlayerWMovement(false);
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					gamePanel.setPlayerDMovement(false);
				} else if (e.getKeyCode() == KeyEvent.VK_S) {
					gamePanel.setPlayerSMovement(false);
				}
			}
		};
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dotger");
		contentPane = new BackgroundPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		statusBar = new JPanel();
		statusBar.setLayout(new GridBagLayout());
		statusBar.setOpaque(false);
		contentPane.add(statusBar, BorderLayout.NORTH);
		
		GridBagConstraints statusBarConstraints = new GridBagConstraints();
		statusBarConstraints.ipadx = 5;
		statusBarConstraints.ipady = 5;
		statusBarConstraints.insets = new Insets(2, 2, 2, 2);
		statusBarConstraints.fill = GridBagConstraints.HORIZONTAL;

		lblTitleText = new StatusbarLabel(gameLanguage.getString("statusbar_titletext"));
		statusBarConstraints.gridy = 0;
		statusBarConstraints.weighty = 1;
		statusBarConstraints.gridx = 0;
		statusBarConstraints.weightx = 0;
		statusBarConstraints.gridwidth = 3;
		statusBar.add(lblTitleText, statusBarConstraints);

		statusBarConstraints.gridwidth = 1;
		statusBarConstraints.weightx = 1;

		StatusbarLabel lblMovementText = new StatusbarLabel("W ↑ | A ← | S ↓ | D →");
		statusBarConstraints.gridx = 3;
		statusBar.add(lblMovementText, statusBarConstraints);
		
		lblPointsText = new StatusbarLabel(gameLanguage.getString("number_of_points") + 0);
		statusBarConstraints.gridx = 4;
		statusBar.add(lblPointsText, statusBarConstraints);

		lblLivesText = new StatusbarLabel(gameLanguage.getString("number_of_lives") + 0);
		statusBarConstraints.gridx = 5;
		statusBar.add(lblLivesText, statusBarConstraints);

		statusBarConstraints.gridy = 1;
		statusBarConstraints.weightx = 0;

		ImageIcon settingsIcon = new ImageIcon("icons/Settings.png");
		btnSettings = new GameButton(settingsIcon);
		btnSettings.addActionListener(e -> {
			SettingsWindow settingsWindow = SettingsWindow.getInstance();
			if (!settingsWindow.isVisible()) {
				settingsWindow.setLocationRelativeTo(GameWindow.this);
				settingsWindow.setVisible(true);
			} else {
				settingsWindow.toFront();
			}
			settingsWindow.focusOkButton();
		});
		btnSettings.addKeyListener(gameKeyListener);
		statusBarConstraints.gridx = 0;
		btnSettings.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSettings.setPreferredSize(new Dimension(35, 35));
		statusBar.add(btnSettings, statusBarConstraints);

		statusBarConstraints.weightx = 1;

		lifeBallLabel = new SpecialBallLabel(
				new LiveBall(), 
				gameLanguage.getString("lifeball_description")
		);
		statusBarConstraints.gridx = 1;
		statusBar.add(lifeBallLabel, statusBarConstraints);
		
		blastBallLabel = new SpecialBallLabel(
				new BlastBall(), 
				gameLanguage.getString("blastball_description")
		);
		statusBarConstraints.gridx = 2;
		statusBar.add(blastBallLabel, statusBarConstraints);
		
		partyBallLabel = new SpecialBallLabel(
				new PartyBall(), 
				gameLanguage.getString("partyball_description")
		);
		statusBarConstraints.gridx = 3;
		statusBar.add(partyBallLabel, statusBarConstraints);
		
		godModeBallLabel = new SpecialBallLabel(
				new GodModeBall(), 
				gameLanguage.getString("godmodeball_description")
		);
		statusBarConstraints.gridx = 4;
		statusBar.add(godModeBallLabel, statusBarConstraints);

		slowMotionBallLabel = new SpecialBallLabel(
				new SlowMotionBall(), 
				gameLanguage.getString("slowmotionball_description")
		);
		statusBarConstraints.gridx = 5;
		statusBar.add(slowMotionBallLabel, statusBarConstraints);

		Runnable finishedRunnable = new Runnable() {
			public void run() {
				showLooseColors();
				SettingsWindow.getInstance().showLooseColors();
				btnStart.requestFocus();
			}
		};
		Runnable updateRunnable = new Runnable() {
			public void run() {
				lblPointsText.setText(gameLanguage.getString("number_of_points") + gamePanel.getPoints());
				int newLives = gamePanel.getLives();
				if (lives < newLives) {
					lblLivesText.highlight(true);
				} else if (lives > newLives) {
					lblLivesText.highlight(false);
				}
				lblLivesText.setText(gameLanguage.getString("number_of_lives") + (lives = newLives));
			}
		};
		gamePanel = new GamePanel(800, 400, finishedRunnable, updateRunnable);
		contentPane.add(gamePanel, BorderLayout.CENTER);
		
		btnStart = new GameButton(gameLanguage.getString("startbutton_text"));
		btnStart.addActionListener(e -> {
			gamePanel.setupNewGame();
			showNormalColors();
			SettingsWindow.getInstance().showNormalColors();
		});
		btnStart.addKeyListener(gameKeyListener);
		contentPane.add(btnStart, BorderLayout.SOUTH);

		Runnable gameTickRunnable = () -> {
			EventQueue.invokeLater(gamePanel);
			EventQueue.invokeLater(lblLivesText);
			EventQueue.invokeLater(partyBallLabel);
		};

		scheduler.scheduleAtFixedRate(gameTickRunnable, 0, GAME_TICK_LENGTH_MS, TimeUnit.MILLISECONDS);
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		toFront();
		btnStart.requestFocus();

    	gameLanguage.register(this);
	}
	
	private void showLooseColors() {
		contentPane.showLooseColors();
		statusBar.setBackground(GameColors.LOOSE);
		btnStart.showLooseColors();
		btnSettings.showLooseColors();
		StatusbarLabel.showLooseColors();
		SpecialBallLabel.showLooseColors();
		revalidate();
		repaint();
	}
	
	private void showNormalColors() {
		contentPane.showNormalColors();
		statusBar.setBackground(GameColors.MAIN);
		btnStart.showNormalColors();
		btnSettings.showNormalColors();
		StatusbarLabel.showNormalColors();
		SpecialBallLabel.showNormalColors();
		revalidate();
		repaint();
	}
	
	public void updateLanguage() {
		lblTitleText.setText(gameLanguage.getString("statusbar_titletext"));
		lblPointsText.setText(gameLanguage.getString("number_of_points"));
		lblLivesText.setText(gameLanguage.getString("number_of_lives"));
		
		lifeBallLabel.setText(gameLanguage.getString("lifeball_description"));
		blastBallLabel.setText(gameLanguage.getString("blastball_description"));
		partyBallLabel.setText(gameLanguage.getString("partyball_description"));
		godModeBallLabel.setText(gameLanguage.getString("godmodeball_description"));
		slowMotionBallLabel.setText(gameLanguage.getString("slowmotionball_description"));
		
		btnStart.setText(gameLanguage.getString("startbutton_text"));
		
		EventQueue.invokeLater(() -> {revalidate(); repaint();}); 
	}

}
