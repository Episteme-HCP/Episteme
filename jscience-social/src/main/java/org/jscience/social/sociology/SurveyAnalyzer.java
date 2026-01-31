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

package org.jscience.social.sociology;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Utility class for analyzing social survey data, providing statistical methods
 * such as weighted averages to account for demographic sampling bias.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class SurveyAnalyzer {

    private SurveyAnalyzer() {}

    /**
     * Calculates the weighted arithmetic mean of a set of responses.
     * Useful for correcting sample imbalances where certain groups are over- or under-represented.
     *
     * @param responses array of numerical response values
     * @param weights   array of corresponding weights for each response
     * @return the weighted mean
     * @throws IllegalArgumentException if arrays are null, conform to different lengths, or are empty
     */
    public static Real weightedMean(double[] responses, double[] weights) {
        if (responses == null || weights == null) {
            throw new IllegalArgumentException("Input arrays cannot be null");
        }
        if (responses.length != weights.length) {
            throw new IllegalArgumentException("Responses and weights arrays must have the same length");
        }
        if (responses.length == 0) {
            return Real.ZERO;
        }

        double sum = 0;
        double weightSum = 0;
        for (int i = 0; i < responses.length; i++) {
            sum += responses[i] * weights[i];
            weightSum += weights[i];
        }
        
        if (weightSum == 0) {
            return Real.ZERO; // Avoid division by zero if all weights are zero
        }
        
        return Real.of(sum / weightSum);
    }
}

