/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.arts.music;

/**
 * Represents a musical note.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Note {

    public enum Pitch {
        C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B
    }

    public enum Duration {
        WHOLE(1.0), HALF(0.5), QUARTER(0.25), EIGHTH(0.125), SIXTEENTH(0.0625);

        private final double value;

        Duration(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }

    private final Pitch pitch;
    private final int octave; // 0-8 (middle C is C4)
    private final Duration duration;
    private boolean sharp;
    private boolean flat;

    public Note(Pitch pitch, int octave, Duration duration) {
        this.pitch = pitch;
        this.octave = Math.max(0, Math.min(8, octave));
        this.duration = duration;
    }

    public Note(Pitch pitch, int octave) {
        this(pitch, octave, Duration.QUARTER);
    }

    // Getters
    public Pitch getPitch() {
        return pitch;
    }

    public int getOctave() {
        return octave;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean isSharp() {
        return sharp;
    }

    public boolean isFlat() {
        return flat;
    }

    public void setSharp(boolean sharp) {
        this.sharp = sharp;
        this.flat = false;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
        this.sharp = false;
    }

    /**
     * Returns frequency in Hz (A4 = 440 Hz).
     */
    public double getFrequency() {
        // Calculate semitones from A4
        int pitchIndex = pitch.ordinal();
        int a4Index = Pitch.A.ordinal();
        int semitones = pitchIndex - a4Index + (octave - 4) * 12;
        if (sharp)
            semitones++;
        if (flat)
            semitones--;
        return 440.0 * Math.pow(2, semitones / 12.0);
    }

    /**
     * Returns MIDI note number (middle C = 60).
     */
    public int getMidiNote() {
        int note = (octave + 1) * 12 + pitch.ordinal();
        if (sharp)
            note++;
        if (flat)
            note--;
        return note;
    }

    /**
     * Returns scientific pitch notation (e.g., "C4", "A#5").
     */
    public String getNotation() {
        String name = pitch.name().replace("_SHARP", "#");
        if (sharp && !name.contains("#"))
            name += "#";
        if (flat)
            name += "b";
        return name + octave;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %.2f Hz)", getNotation(), duration, getFrequency());
    }

    // Common notes
    public static final Note MIDDLE_C = new Note(Pitch.C, 4);
    public static final Note A440 = new Note(Pitch.A, 4);
}


