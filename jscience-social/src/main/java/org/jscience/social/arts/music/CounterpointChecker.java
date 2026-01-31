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

