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

package org.jscience.social.linguistics.quantitative;

import java.util.Map;

/**
 * Implements fundamental laws of quantitative linguistics.
 * Provides scientific metrics for statistical language analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class QuantitativeLinguistics {

    private QuantitativeLinguistics() {}

    /**
     * Zipf's Law: The frequency of any word is inversely proportional to its rank in the frequency table.
     * f(r) = C / r^s
     * 
     * @param rank The rank of the word (1-indexed).
     * @param exponent The Zipfian exponent (usually close to 1.0).
     * @param constant The normalizing constant.
     * @return The theoretical frequency.
     */
    public static double zipfLaw(int rank, double exponent, double constant) {
        return constant / Math.pow(rank, exponent);
    }

    /**
     * Heaps' Law: Describes the number of distinct words (vocabulary size) in a document as a function of its length.
     * V = K * N^beta
     * 
     * @param totalTokens (N) total number of tokens in the corpus.
     * @param K empirically determined constant (typically 10-100).
     * @param beta empirically determined exponent (typically 0.4-0.6).
     * @return theoretical vocabulary size (V).
     */
    public static double heapsLaw(long totalTokens, double K, double beta) {
        return K * Math.pow(totalTokens, beta);
    }

    /**
     * Menzerath-Altmann Law: The more components a linguistic construct has, the smaller the components are.
     * y = a * x^b * e^(cx)
     * 
     * @param x number of components (e.g., syllables in a word).
     * @param a parameter.
     * @param b parameter.
     * @param c parameter.
     * @return length of components (e.g., average phonemes in a syllable).
     */
    public static double menzerathAltmannLaw(double x, double a, double b, double c) {
        return a * Math.pow(x, b) * Math.exp(c * x);
    }

    /**
     * Calculates the Shannon Entropy of a text based on word frequencies.
     * Measures the unpredictability or information content.
     *
     * @param wordFrequencies Map of words to their occurrences.
     * @return Entropy value in bits.
     */
    public static double calculateEntropy(Map<String, Long> wordFrequencies) {
        long totalTokens = wordFrequencies.values().stream().mapToLong(Long::longValue).sum();
        if (totalTokens == 0) return 0.0;

        double entropy = 0;
        for (long count : wordFrequencies.values()) {
            double p = (double) count / totalTokens;
            if (p > 0) {
                entropy -= p * (Math.log(p) / Math.log(2));
            }
        }
        return entropy;
    }

    /**
     * Calculates the TTR (Type-Token Ratio).
     * A simple measure of lexical diversity.
     */
    public static double calculateTTR(long vocabularySize, long totalTokens) {
        if (totalTokens == 0) return 0.0;
        return (double) vocabularySize / totalTokens;
    }
}

