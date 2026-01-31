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

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Computational musicology tools for pattern discovery.
 */
public final class MusicalPatternMatcher {

    private MusicalPatternMatcher() {}

    /**
     * Finds repeated melodic intervals in a note sequence.
     */
    public static List<List<Integer>> findRepeatedIntervals(List<Note> melody, int length) {
        List<Integer> intervals = getIntervals(melody);
        Map<List<Integer>, Integer> patterns = new HashMap<>();
        
        for (int i = 0; i <= intervals.size() - length; i++) {
            List<Integer> sub = intervals.subList(i, i + length);
            patterns.put(sub, patterns.getOrDefault(sub, 0) + 1);
        }
        
        return patterns.entrySet().stream()
            .filter(e -> e.getValue() > 1)
            .map(Map.Entry::getKey)
            .toList();
    }

    private static List<Integer> getIntervals(List<Note> melody) {
        List<Integer> intervals = new ArrayList<>();
        for (int i = 1; i < melody.size(); i++) {
            intervals.add(melody.get(i).getMidiNote() - melody.get(i - 1).getMidiNote());
        }
        return intervals;
    }

    /**
     * Calculates rhythmic entropy of a sequence.
     */
    public static Real rhythmicEntropy(List<Note> score) {
        Map<Double, Integer> durations = new HashMap<>();
        for (Note n : score) {
            durations.put(n.getDuration().getValue(), durations.getOrDefault(n.getDuration().getValue(), 0) + 1);
        }
        
        double entropy = 0;
        int total = score.size();
        for (int count : durations.values()) {
            double p = (double) count / total;
            entropy -= p * (Math.log(p) / Math.log(2));
        }
        return Real.of(entropy);
    }
}

