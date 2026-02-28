/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.arts.music;

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

