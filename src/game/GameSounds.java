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

import static game.GameConstants.*;

import java.io.File;

import javax.sound.sampled.*;

public class GameSounds {

	private static final GameSounds INSTANCE = new GameSounds();

	public static final int START_SOUND = 1;
	public static final int SPAWN_SOUND = 2;
	public static final int LIFE_SOUND = 3;
	public static final int EXPLODE_SOUND = 4;
	public static final int DESTROY_SOUND = 5;
	public static final int PARTY_SOUND = 6;
	public static final int GODMODE_SOUND = 7;
	public static final int END_SOUND = 8;
	public static final int LIFE_LOST_SOUND = 9;
	public static final int FIREWORK_SOUND = 10;
	public static final int ROCKET_SOUND = 11;
	public static final int SLOW_MOTION_START = 12;
	public static final int SLOW_MOTION_END = 13;
	public static final int LIGHTNING = 14;
	public static final int GAME_MUSIC = 15;

	public static GameSounds getInstance() {
		return INSTANCE;
	}

	private float soundsVolume = VOLUME_INIT_VALUE / 100.0f;
	
	private final AudioArray startSound;
	private final AudioArray spawnSound;
	private final AudioArray lifeSound;
	private final AudioArray explodeSound;
	private final AudioArray destroySound;
	private final AudioArray partySound;
	private final AudioArray godModeSound;
	private final AudioArray endSound;
	private final AudioArray lifeLostSound;
	private final AudioArray fireworkSound;
	private final AudioArray rocketSound;
	private final AudioArray slowMotionStart;
	private final AudioArray slowMotionEnd;
	private final AudioArray lightning;
	
	private final Clip gameMusic;
	
	private GameSounds() {
		startSound = getAudioArray("./sounds/start.wav");
		spawnSound = getAudioArray("./sounds/spawn.wav");
		lifeSound = getAudioArray("./sounds/life.wav");
		explodeSound = getAudioArray("./sounds/explode.wav");
		destroySound = getAudioArray("./sounds/destroy.wav");
		partySound = getAudioArray("./sounds/party.wav");
		godModeSound = getAudioArray("./sounds/godmode.wav");
		endSound = getAudioArray("./sounds/end.wav");
		lifeLostSound = getAudioArray("./sounds/lifelost.wav");
		fireworkSound = getAudioArray("./sounds/firework.wav");
		rocketSound = getAudioArray("./sounds/rocket.wav");
		slowMotionStart = getAudioArray("./sounds/slowmotionstart.wav");
		slowMotionEnd = getAudioArray("./sounds/slowmotionend.wav");
		lightning = getAudioArray("./sounds/lightning.wav");
		
		gameMusic = getAudioClip("./sounds/gamemusic.wav");
		setClipVolume(gameMusic, VOLUME_INIT_VALUE / 100.0f);
	}
	
	public void playSound(int soundType) {
		try {
			switch (soundType) {
				case START_SOUND:
					playAudioFromArray(startSound);
					break;
				case SPAWN_SOUND:
					playAudioFromArray(spawnSound);
					break;
				case LIFE_SOUND:
					playAudioFromArray(lifeSound);
					break;
				case EXPLODE_SOUND:
					playAudioFromArray(explodeSound);
					break;
				case DESTROY_SOUND:
					playAudioFromArray(destroySound);
					break;
				case PARTY_SOUND:
					playAudioFromArray(partySound);
					break;
				case GODMODE_SOUND:
					playAudioFromArray(godModeSound);
					break;
				case END_SOUND:
					playAudioFromArray(endSound);
					break;
				case LIFE_LOST_SOUND:
					playAudioFromArray(lifeLostSound);
					break;
				case FIREWORK_SOUND:
					playAudioFromArray(fireworkSound);
					break;
				case ROCKET_SOUND:
					playAudioFromArray(rocketSound);
					break;
				case SLOW_MOTION_START:
					playAudioFromArray(slowMotionStart);
					break;
				case SLOW_MOTION_END:
					playAudioFromArray(slowMotionEnd);
					break;
				case LIGHTNING:
					playAudioFromArray(lightning);
					break;
				case GAME_MUSIC:
					if (!gameMusic.isActive()) {
						gameMusic.stop();
						gameMusic.setFramePosition(0);
						gameMusic.start();
					}
					break;
			}
		} catch (NullPointerException e) {
			System.out.println("Die Sounddatei konnte nicht abgespielt werden.");
		}
	}
	
	public void startGameMusic() {
		if (gameMusic == null) {
			return;
		}
		gameMusic.stop();
		gameMusic.setFramePosition(0);
		gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stopGameMusic() {
		if (gameMusic == null) {
			return;
		}
		gameMusic.stop();
	}

	private void playAudioFromArray(AudioArray audioArray) {
		try {
			Clip audioClip = AudioSystem.getClip();
			audioClip.open(
					audioArray.getAudioFormat(),
					audioArray.getAudioData(),
					0,
					audioArray.getBufferSize()
			);
			setClipVolume(audioClip, soundsVolume);
			audioClip.start();
		} catch (Exception e) {
			System.out.println("Der Sound konnte nicht abgespielt werden.");
			System.out.println(e.getMessage());
		}
	}
	
	private AudioArray getAudioArray(String path) {
		byte[] audioData;
		AudioFormat audioFormat;
		File audioFile = new File(path);
		audioData = new byte[(int) audioFile.length()];
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile)) {
			ais.read(audioData);
			audioFormat = ais.getFormat();
			return new AudioArray(audioData, audioFormat);
		} catch (Exception e) {
			System.out.println("Die Sounddatei konnte nicht geladen werden.");
			return null;
		}
	}

	private Clip getAudioClip(String path) {
		Clip audioClip;
		try {
			audioClip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
			audioClip.open(ais);
		} catch (Exception e) {
			System.out.println("Die Sounddatei konnte nicht geladen werden.");
			audioClip = null;
		}
		return audioClip;
	}

	private void setClipVolume(Clip clip, float volume) {
		if (clip == null) {
			return;
		}
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(volume));
		
	}

	public void setSoundsVolume(int volumePercent) {
		soundsVolume = volumePercent / 100.0f;
	}

	public void setGameMusicVolume(int volumePercent) {
		setClipVolume(gameMusic, volumePercent / 100.0f);
	}

}
