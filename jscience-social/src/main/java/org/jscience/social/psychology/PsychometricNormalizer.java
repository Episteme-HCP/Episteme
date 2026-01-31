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

package org.jscience.social.psychology;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Utility class for normalizing and transforming psychometric test scores.
 * Supports standard coversions between Raw, Z, T, and IQ scores.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class PsychometricNormalizer {

    private PsychometricNormalizer() {}

    /**
     * Converts a raw score to a Standard Score (Z-score).
     * Z = (X - mean) / stdDev
     *
     * @param rawScore The raw observation
     * @param mean     The population mean
     * @param stdDev   The population standard deviation
     * @return The Z-score (number of standard deviations from mean)
     */
    public static Real toZScore(Real rawScore, Real mean, Real stdDev) {
        if (stdDev.compareTo(Real.ZERO) == 0) return Real.ZERO;
        return rawScore.subtract(mean).divide(stdDev);
    }

    /**
     * Converts a Z-score to a T-score.
     * T-scores have a fixed Mean of 50 and Standard Deviation of 10.
     * T = Z * 10 + 50
     *
     * @param zScore The standardized Z-score
     * @return The T-score
     */
    public static Real toTScore(Real zScore) {
        return zScore.multiply(Real.of(10.0)).add(Real.of(50.0));
    }

    /**
     * Converts a Z-score to a standard deviation IQ score.
     * Modern deviation IQ has a Mean of 100 and Standard Deviation of 15 (typically, e.g., Wechsler).
     * IQ = Z * 15 + 100
     *
     * @param zScore The standardized Z-score
     * @return The IQ score
     */
    public static Real toIQScore(Real zScore) {
        return zScore.multiply(Real.of(15.0)).add(Real.of(100.0));
    }
}

