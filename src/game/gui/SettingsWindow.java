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

import game.GameLanguage;
import game.GameSounds;
import game.LanguageSwitcher;

import static game.GameConstants.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class SettingsWindow extends JFrame implements LanguageSwitcher {

    private static final GameLanguage gameLanguage = GameLanguage.getInstance();
    private static final SettingsWindow INSTANCE = new SettingsWindow();
    private static final String DESCRIPTION_PRE = "<html><center>Dotger V " + VERSION_NUMBER + "<br>";
    private static final String DESCRIPTION_POST = "</center></html>";

    public static SettingsWindow getInstance() {
        return INSTANCE;
    }

    private final BackgroundPanel contentPane;
    private final StatusbarLabel lblDescription;
    private final JLabel lblSounds;
    private final JLabel lblGameMusic;
    private final JLabel lblLanguage;
    private final LanguageButton btnGerman;
    private final LanguageButton btnEnglish;
    private final LanguageButton btnFrench;
    private final GameButton btnOk;
    private final GameSlider gameMusicSlider;
    private final GameSlider soundsSlider;

    private SettingsWindow() {
        ImageIcon icon = new ImageIcon("icons/Settings.png");
        setIconImage(icon.getImage());

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle(gameLanguage.getString("settings_text"));
        setMinimumSize(new Dimension(270, 0));

        contentPane = new BackgroundPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setOpaque(false);
        settingsPanel.setLayout(new GridBagLayout());
        contentPane.add(settingsPanel, BorderLayout.CENTER);

        GridBagConstraints settingsPanelConstraints = new GridBagConstraints();
        settingsPanelConstraints.ipadx = 5;
        settingsPanelConstraints.ipady = 5;
        settingsPanelConstraints.insets = new Insets(4, 4, 4, 4);
        settingsPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

        int yPositionCounter = 0;

        lblDescription = new StatusbarLabel(
                DESCRIPTION_PRE + gameLanguage.getString("creator_text") + DESCRIPTION_POST
        );
        settingsPanelConstraints.gridy = yPositionCounter++;
        settingsPanelConstraints.weighty = 1;
        settingsPanelConstraints.gridx = 0;
        settingsPanelConstraints.weightx = 1;
        settingsPanel.add(lblDescription, settingsPanelConstraints);

        lblSounds = new JLabel(gameLanguage.getString("sounds_volume_text"));
        lblSounds.setOpaque(false);
        lblSounds.setHorizontalAlignment(SwingConstants.CENTER);
        settingsPanelConstraints.gridy = yPositionCounter++;
        settingsPanel.add(lblSounds, settingsPanelConstraints);

        soundsSlider = new GameSlider(0, 200, VOLUME_INIT_VALUE);
        soundsSlider.setOpaque(false);
        soundsSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            GameSounds sounds = GameSounds.getInstance();
            if (!source.getValueIsAdjusting()) {
                sounds.setSoundsVolume(source.getValue());
                sounds.playSound(GameSounds.START_SOUND);
            }
        });
        settingsPanelConstraints.gridy = yPositionCounter++;
        settingsPanel.add(soundsSlider, settingsPanelConstraints);

        lblGameMusic = new JLabel(gameLanguage.getString("music_volume_text"));
        lblGameMusic.setOpaque(false);
        lblGameMusic.setHorizontalAlignment(SwingConstants.CENTER);
        settingsPanelConstraints.gridy = yPositionCounter++;
        settingsPanel.add(lblGameMusic, settingsPanelConstraints);

        gameMusicSlider = new GameSlider(0, 200, VOLUME_INIT_VALUE);
        gameMusicSlider.setOpaque(false);
        gameMusicSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            GameSounds sounds = GameSounds.getInstance();
            if (!source.getValueIsAdjusting()) {
                sounds.setGameMusicVolume(source.getValue());
                sounds.playSound(GameSounds.GAME_MUSIC);
            }
        });
        settingsPanelConstraints.gridy = yPositionCounter++;
        settingsPanel.add(gameMusicSlider, settingsPanelConstraints);
        
        JPanel languagePanel = new JPanel();
        languagePanel.setOpaque(false);
        languagePanel.setLayout(new GridBagLayout());
        settingsPanelConstraints.gridy = yPositionCounter;
        settingsPanel.add(languagePanel, settingsPanelConstraints);
        
        GridBagConstraints languagePanelConstraints = new GridBagConstraints();
        languagePanelConstraints.ipadx = 5;
        languagePanelConstraints.ipady = 5;
        languagePanelConstraints.insets = new Insets(2, 2, 2, 2);
        
        int xPositionCounter = 0;
        
        lblLanguage = new JLabel(gameLanguage.getString("language_text"));
        languagePanelConstraints.gridy = 0;
        languagePanelConstraints.weighty = 1;
        languagePanelConstraints.gridx = xPositionCounter++;
        languagePanelConstraints.weightx = 1;
        languagePanel.add(lblLanguage, languagePanelConstraints);
        
        btnGerman = new LanguageButton(new ImageIcon("images/german.png"));
        btnGerman.addActionListener(e -> gameLanguage.switchToLanguage(new Locale("de")));
        btnGerman.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	btnGerman.doClick();
                }
            }
        });
        languagePanelConstraints.gridx = xPositionCounter++;
        languagePanel.add(btnGerman, languagePanelConstraints);
        
        btnEnglish = new LanguageButton(new ImageIcon("images/english.png"));
        btnEnglish.addActionListener(e -> gameLanguage.switchToLanguage(new Locale("en")));
        btnEnglish.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	btnEnglish.doClick();
                }
            }
        });
        languagePanelConstraints.gridx = xPositionCounter++;
        languagePanel.add(btnEnglish, languagePanelConstraints);
        
        btnFrench = new LanguageButton(new ImageIcon("images/french.png"));
        btnFrench.addActionListener(e -> gameLanguage.switchToLanguage(new Locale("fr")));
        btnFrench.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	btnFrench.doClick();
                }
            }
        });
        languagePanelConstraints.gridx = xPositionCounter;
        languagePanel.add(btnFrench, languagePanelConstraints);

        btnOk = new GameButton("OK");
        btnOk.addActionListener(e -> SettingsWindow.this.setVisible(false));
        btnOk.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnOk.doClick();
                }
            }
        });
        contentPane.add(btnOk, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(false);
        setResizable(false);
        
    	gameLanguage.register(this);
    }

    public void focusOkButton() {
        btnOk.requestFocus();
    }

    public void showNormalColors() {
        contentPane.showNormalColors();
        soundsSlider.showNormalColors();
        gameMusicSlider.showNormalColors();
        btnGerman.showNormalColors();
        btnEnglish.showNormalColors();
        btnFrench.showNormalColors();
        btnOk.showNormalColors();
        revalidate();
        repaint();
    }

    public void showLooseColors() {
        contentPane.showLooseColors();
        soundsSlider.showLooseColors();
        gameMusicSlider.showLooseColors();
        btnGerman.showLooseColors();
        btnEnglish.showLooseColors();
        btnFrench.showLooseColors();
        btnOk.showLooseColors();
        revalidate();
        repaint();
    }
    
    public void updateLanguage() {
    	setTitle(gameLanguage.getString("settings_text"));
    	lblDescription.setText(
    			DESCRIPTION_PRE + gameLanguage.getString("creator_text") + DESCRIPTION_POST
    	);
    	lblSounds.setText(gameLanguage.getString("sounds_volume_text"));
    	lblGameMusic.setText(gameLanguage.getString("music_volume_text"));
    	lblLanguage.setText(gameLanguage.getString("language_text"));
		EventQueue.invokeLater(() -> {revalidate(); repaint();}); 
	}

}
