package org.jscience.arts.music;

import java.util.*;

/**
 * Suggestions for orchestral distribution.
 */
public final class OrchestrationSolver {

    private OrchestrationSolver() {}

    public record OrchestralBalance(
        double woodwindIntensity,
        double brassIntensity,
        double stringsIntensity,
        double percussionIntensity
    ) {}

    /**
     * Evaluates the balance of an orchestration.
     */
    public static OrchestralBalance evaluateBalance(int numStrings, int numBrass, int numWoodwinds) {
        // Simple heuristic: Horns are loud, strings need numbers.
        double s = numStrings * 1.0;
        double b = numBrass * 4.0;
        double w = numWoodwinds * 2.0;
        double total = s + b + w;
        
        return new OrchestralBalance(w/total, b/total, s/total, 0.0);
    }

    /**
     * Checks if a chord is in a "balanced" distribution (wide at bottom, tight at top).
     */
    public static boolean isOvertoneCompliant(List<Integer> midiNotes) {
        if (midiNotes.size() < 2) return true;
        List<Integer> sorted = new ArrayList<>(midiNotes);
        Collections.sort(sorted);
        
        for (int i = 1; i < sorted.size() - 1; i++) {
            int gapLower = sorted.get(i) - sorted.get(i-1);
            int gapUpper = sorted.get(i+1) - sorted.get(i);
            if (gapLower < gapUpper - 2) return false; // Heuristic: gaps should generally narrow
        }
        return true;
    }
}
