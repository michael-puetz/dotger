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

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;

public final class AudioArray {

    private final byte[] audioData;
    private final AudioFormat audioFormat;

    public AudioArray(byte[] audioData, AudioFormat audioFormat) {
        this.audioData = audioData;
        this.audioFormat = audioFormat;
    }

    public int getBufferSize() {
        return audioData.length - audioData.length % 4;
    }

    public byte[] getAudioData() {
        return Arrays.copyOf(audioData, audioData.length);
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

}