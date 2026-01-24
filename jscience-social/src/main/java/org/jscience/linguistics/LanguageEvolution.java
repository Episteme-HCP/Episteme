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

package org.jscience.linguistics;

import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Analytical engine for modeling language evolution, divergence, and 
 * glottochronology. It provides mathematical models for estimating divergence 
 * times based on cognate retention in Swadesh lists.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class LanguageEvolution {

    private LanguageEvolution() {}

    /**
     * Estimates the time since two languages diverged from a common ancestor 
     * using Swadesh list retention rates.
     * Formula: t = log(c) / (2 * log(r))
     * 
     * @param c fraction of shared cognates (0.0 to 1.0)
     * @param r retention rate per millennium (typically 0.81 to 0.86)
     * @return time since divergence in millennia
     */
    public static Real divergenceTimeMillennia(double c, double r) {
        if (c <= 0 || r <= 0 || r >= 1) return Real.ZERO;
        return Real.of(Math.log(c) / (2 * Math.log(r)));
    }

    /**
     * Calculates the fraction of shared cognates between two lists of words 
     * representing the same concepts (Swadesh lists).
     * 
     * @param list1 first word list
     * @param list2 second word list
     * @return fraction (0.0 to 1.0)
     */
    public static double calculateCognateFraction(List<String> list1, List<String> list2) {
        int shared = 0;
        int minSize = Math.min(list1.size(), list2.size());
        if (minSize == 0) return 0;

        for (int i = 0; i < minSize; i++) {
            if (isCognate(list1.get(i), list2.get(i))) {
                shared++;
            }
        }
        return (double) shared / minSize;
    }

    /**
     * Determines if two words are likely cognates using normalized 
     * Levenshtein distance.
     * 
     * @param word1 first word
     * @param word2 second word
     * @return true if similarity exceeds the cognate threshold (default 0.6)
     */
    public static boolean isCognate(String word1, String word2) {
        if (word1 == null || word2 == null) return false;
        int dist = editDistance(word1.toLowerCase(), word2.toLowerCase());
        int maxLen = Math.max(word1.length(), word2.length());
        if (maxLen == 0) return true;
        
        double similarity = 1.0 - (double) dist / maxLen;
        return similarity > 0.6; // Empirical threshold for cognates
    }

    private static int editDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }

    /**
     * Projects the probability of semantic retention over a given time period.
     * Formula: S(t) = exp(-kt)
     * 
     * @param kDrift semantic drift coefficient
     * @param years time span in years
     * @return retention probability (0.0 to 1.0)
     */
    public static Real semanticRetention(double kDrift, int years) {
        return Real.of(Math.exp(-kDrift * years / 1000.0));
    }
}
