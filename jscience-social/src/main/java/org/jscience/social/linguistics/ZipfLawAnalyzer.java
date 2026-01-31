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

package org.jscience.social.linguistics;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Validates Zipf's Law in a text corpus.
 */
public final class ZipfLawAnalyzer {

    private ZipfLawAnalyzer() {}

    /**
     * Analyzes word frequency distribution.
     * Returns a map of Rank -> Frequency.
     */
    public static Map<Integer, Integer> analyzeFrequencies(String text) {
        String[] words = text.toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+");
        Map<String, Integer> counts = new HashMap<>();
        for (String w : words) counts.put(w, counts.getOrDefault(w, 0) + 1);
        
        List<Integer> freqs = new ArrayList<>(counts.values());
        Collections.sort(freqs, Collections.reverseOrder());
        
        Map<Integer, Integer> rankToFreq = new LinkedHashMap<>();
        for (int i = 0; i < freqs.size(); i++) {
            rankToFreq.put(i + 1, freqs.get(i));
        }
        return rankToFreq;
    }

    /**
     * Checks the Zipf constant (k = rank * frequency).
     * Ideally, this should be constant across the corpus.
     */
    public static Real calculateZipfConstant(int rank, int frequency) {
        return Real.of((long) rank * frequency);
    }
}

