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

package org.jscience.social.arts.music;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Frequency;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a musical note with microtonal and dynamic properties.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

    @Attribute
    private final Pitch pitch;
    @Attribute
    private final int octave;
    @Attribute
    private final Duration duration;
    @Attribute
    private final int cents; // Microtonal adjustment in cents
    @Attribute
    private double velocity; // 0.0 to 1.0 (dynamic)
    
    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

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

    public Note(Pitch pitch, Duration duration) {
        this(pitch, 4, duration);
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
    
    public static Note rest(Duration duration) {
        // A rest is represented by a null pitch for now, or we could handle it differently
        return new Note(null, 0, duration, 0, 0.0);
    }

    public void setTrait(String key, Object value) {
        traits.put(key, value);
    }

    public Object getTrait(String key) {
        return traits.get(key);
    }

    public void setDynamics(Dynamics dynamics) {
        // Approximate mapping from Dynamics enum to velocity
        // Assuming Dynamics is available or we accept Object. 
        // If Dynamics is an enum, we can switch on it.
        // For now, if Dynamics is not standard, we might need to rely on trait.
        setTrait("dynamics", dynamics);
        // Simple mapping if possible (pseudo-code)
        // if (dynamics.name().equals("FF")) this.velocity = 0.9;
        // else ...
    }

    public static final Note MIDDLE_C = new Note(Pitch.C, 4);
    public static final Note A440 = new Note(Pitch.A, 4);
}



