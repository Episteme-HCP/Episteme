package org.jscience.arts.music;

import java.util.*;

/**
 * Checks for classical counterpoint rule violations (Fux style).
 */
public final class CounterpointChecker {

    private CounterpointChecker() {}

    public record Violation(int index, String type, String description) {}

    /**
     * Detects parallel fifths and octaves between two voices.
     */
    public static List<Violation> checkParallelism(List<Note> cantusFirmus, List<Note> counterpoint) {
        List<Violation> violations = new ArrayList<>();
        int size = Math.min(cantusFirmus.size(), counterpoint.size());
        
        for (int i = 1; i < size; i++) {
            int intervalPrev = Math.abs(counterpoint.get(i-1).getMidiNote() - cantusFirmus.get(i-1).getMidiNote()) % 12;
            int intervalCurr = Math.abs(counterpoint.get(i).getMidiNote() - cantusFirmus.get(i).getMidiNote()) % 12;
            
            if (intervalPrev == 7 && intervalCurr == 7 && 
                counterpoint.get(i-1).getMidiNote() != counterpoint.get(i).getMidiNote()) {
                violations.add(new Violation(i, "PARALLEL_FIFTHS", "Consecutive fifths detected."));
            }
            if (intervalPrev == 0 && intervalCurr == 0 && 
                counterpoint.get(i-1).getMidiNote() != counterpoint.get(i).getMidiNote()) {
                violations.add(new Violation(i, "PARALLEL_OCTAVES", "Consecutive octaves detected."));
            }
        }
        return violations;
    }

    /**
     * Checks if the range of the voice is within a reasonable limit (e.g., 10th).
     */
    public static boolean checkVoiceRange(List<Note> voice, int maxInterval) {
        if (voice.isEmpty()) return true;
        int min = voice.stream().mapToInt(Note::getMidiNote).min().getAsInt();
        int max = voice.stream().mapToInt(Note::getMidiNote).max().getAsInt();
        return (max - min) <= maxInterval;
    }
}
