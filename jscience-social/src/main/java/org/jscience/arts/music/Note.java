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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Represents a musical note with microtonal and dynamic properties.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Note {

    public enum Pitch {
        C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B
    }

    public enum Duration {
        WHOLE(1.0), HALF(0.5), QUARTER(0.25), EIGHTH(0.125), SIXTEENTH(0.0625), THIRTY_SECOND(0.03125);

        private final double value;

        Duration(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }

    private final Pitch pitch;
    private final int octave;
    private final Duration duration;
    private final int cents; // Microtonal adjustment in cents
    private final double velocity; // 0.0 to 1.0 (dynamic)

    public Note(Pitch pitch, int octave, Duration duration, int cents, double velocity) {
        this.pitch = pitch;
        this.octave = octave;
        this.duration = duration;
        this.cents = cents;
        this.velocity = Math.max(0.0, Math.min(1.0, velocity));
    }

    public Note(Pitch pitch, int octave, Duration duration) {
        this(pitch, octave, duration, 0, 0.8);
    }

    public Note(Pitch pitch, int octave) {
        this(pitch, octave, Duration.QUARTER);
    }

    public Pitch getPitch() { return pitch; }
    public int getOctave() { return octave; }
    public Duration getDuration() { return duration; }
    public int getCents() { return cents; }
    public double getVelocity() { return velocity; }

    /**
     * Returns frequency as a Real-based Quantity.
     * f = 440 * 2^((n - 69 + cents/100) / 12)
     */
    public Quantity<Frequency> getFrequency() {
        int n = getMidiNote();
        double semitones = (n - 69) + (cents / 100.0);
        Real freq = Real.of(440.0).multiply(Real.of(2.0).pow(semitones / 12.0));
        return Quantities.create(freq.doubleValue(), Units.HERTZ);
    }

    public int getMidiNote() {
        return (octave + 1) * 12 + pitch.ordinal();
    }

    public String getNotation() {
        String name = pitch.name().replace("_SHARP", "#");
        String micro = cents != 0 ? (cents > 0 ? "+" + cents : cents) + "c" : "";
        return name + octave + micro;
    }

    @Override
    public String toString() {
        return String.format("%s (vel=%.2f, f=%.2f Hz)", getNotation(), velocity, getFrequency().getValue().doubleValue());
    }

    public static Note fromMidi(int midiNote, double durationValue) {
        int octave = (midiNote / 12) - 1;
        Pitch pitch = Pitch.values()[midiNote % 12];
        // Closest duration
        Duration duration = Duration.QUARTER;
        double minDiff = Double.MAX_VALUE;
        for (Duration d : Duration.values()) {
            double diff = Math.abs(d.getValue() - durationValue);
            if (diff < minDiff) {
                minDiff = diff;
                duration = d;
            }
        }
        return new Note(pitch, octave, duration);
    }

    public static final Note MIDDLE_C = new Note(Pitch.C, 4);
    public static final Note A440 = new Note(Pitch.A, 4);
}


