package org.jscience.arts.music;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Advanced musical harmony and scale engine.
 */
public final class HarmonyEngine {

    private HarmonyEngine() {}

    public enum ScaleType {
        MAJOR(0, 2, 4, 5, 7, 9, 11),
        NATURAL_MINOR(0, 2, 3, 5, 7, 8, 10),
        HARMONIC_MINOR(0, 2, 3, 5, 7, 8, 11),
        MELODIC_MINOR(0, 2, 3, 5, 7, 9, 11),
        DORIAN(0, 2, 3, 5, 7, 9, 10),
        PHRYGIAN(0, 1, 3, 5, 7, 8, 10),
        LYDIAN(0, 2, 4, 6, 7, 9, 11),
        MIXOLYDIAN(0, 2, 4, 5, 7, 9, 10),
        PENTATONIC_MAJOR(0, 2, 4, 7, 9),
        PENTATONIC_MINOR(0, 3, 5, 7, 10);

        private final int[] intervals;
        ScaleType(int... intervals) { this.intervals = intervals; }
        public int[] getIntervals() { return intervals; }
    }

    /**
     * Generates a scale starting from a root note.
     */
    public static List<Integer> generateScale(int rootSemitone, ScaleType type) {
        List<Integer> scale = new ArrayList<>();
        for (int interval : type.getIntervals()) {
            scale.add(rootSemitone + interval);
        }
        return scale;
    }

    public static Quantity<Frequency> calculateFrequency(int semitonesFromA4) {
        double freq = 440.0 * Math.pow(2, semitonesFromA4 / 12.0);
        return Quantities.create(freq, Units.HERTZ);
    }

    public static String identifyChordType(List<Integer> semitones) {
        if (semitones == null || semitones.isEmpty()) return "Unknown";
        
        List<Integer> sorted = new ArrayList<>(semitones);
        Collections.sort(sorted);
        
        int root = sorted.get(0);
        List<Integer> normalized = new ArrayList<>();
        for (int s : sorted) {
            int interval = (s - root) % 12;
            if (!normalized.contains(interval)) normalized.add(interval);
        }
        Collections.sort(normalized);

        if (normalized.equals(List.of(0, 4, 7))) return "Major Triad";
        if (normalized.equals(List.of(0, 3, 7))) return "Minor Triad";
        if (normalized.equals(List.of(0, 3, 6))) return "Diminished Triad";
        if (normalized.equals(List.of(0, 4, 8))) return "Augmented Triad";
        if (normalized.equals(List.of(0, 5, 7))) return "Sus4";
        if (normalized.equals(List.of(0, 2, 7))) return "Sus2";
        if (normalized.equals(List.of(0, 4, 7, 10))) return "Dominant 7th";
        if (normalized.equals(List.of(0, 4, 7, 11))) return "Major 7th";
        if (normalized.equals(List.of(0, 3, 7, 10))) return "Minor 7th";
        if (normalized.equals(List.of(0, 3, 6, 9))) return "Diminished 7th";
        if (normalized.equals(List.of(0, 3, 6, 10))) return "Half-Diminished 7th";
        
        return "Unknown";
    }

    /**
     * Transposes a list of notes by a given number of semitones.
     */
    public static List<Integer> transpose(List<Integer> semitones, int steps) {
        List<Integer> transposed = new ArrayList<>();
        for (int s : semitones) {
            transposed.add(s + steps);
        }
        return transposed;
    }

    /**
     * Calculates the interval in semitones between two notes.
     */
    public static int interval(int note1, int note2) {
        return Math.abs(note2 - note1) % 12;
    }

    /**
     * Names an interval (0-12 semitones).
     */
    public static String intervalName(int semitones) {
        return switch (semitones % 12) {
            case 0 -> "Unison";
            case 1 -> "Minor 2nd";
            case 2 -> "Major 2nd";
            case 3 -> "Minor 3rd";
            case 4 -> "Major 3rd";
            case 5 -> "Perfect 4th";
            case 6 -> "Tritone";
            case 7 -> "Perfect 5th";
            case 8 -> "Minor 6th";
            case 9 -> "Major 6th";
            case 10 -> "Minor 7th";
            case 11 -> "Major 7th";
            default -> "Octave";
        };
    }
}
