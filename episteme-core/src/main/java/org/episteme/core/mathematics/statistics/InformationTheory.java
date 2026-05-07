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

package org.episteme.core.mathematics.statistics;

import org.episteme.core.mathematics.numbers.real.Real;
import java.util.Map;
import java.util.HashMap;

/**
 * Information theory primitives.
 */
public final class InformationTheory {

    private InformationTheory() {}

    /**
     * Calculates the Shannon entropy of a discrete probability distribution.
     * H(X) = -sum(p(x) * log2(p(x)))
     * 
     * @param probabilities Map of outcomes to their probabilities.
     * @return Entropy in bits.
     */
    public static Real shannonEntropy(Map<?, Real> probabilities) {
        Real entropy = Real.of(0.0);
        Real log2 = Real.of(2.0).log();
        for (Real p : probabilities.values()) {
            if (p.compareTo(Real.of(0.0)) > 0) {
                entropy = entropy.subtract(p.multiply(p.log().divide(log2)));
            }
        }
        return entropy;
    }

    /**
     * Calculates the entropy of a string based on character frequency.
     * 
     * @param s The input string.
     * @return Entropy in bits per character.
     */
    public static Real stringEntropy(String s) {
        if (s == null || s.isEmpty()) return Real.ZERO;
        
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        
        Real len = Real.of(s.length());
        Map<Character, Real> probs = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            probs.put(entry.getKey(), Real.of(entry.getValue()).divide(len));
        }
        
        return shannonEntropy(probs);
    }
}

