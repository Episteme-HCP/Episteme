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

package org.jscience.psychology;

import java.io.Serializable;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the Big Five personality traits (OCEAN model).
 * Provides utilities for scoring and compatibility analysis based on personality dimensions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class BigFiveProfile {

    private BigFiveProfile() {}

    /**
     * Represents a personality profile using the Five Factor Model.
     * Dimensions are generally normalized (e.g., 0.0 to 1.0 or T-scores).
     *
     * @param openness          Openness to experience
     * @param conscientiousness Conscientiousness
     * @param extraversion      Extraversion
     * @param agreeableness     Agreeableness
     * @param neuroticism       Neuroticism (Emotional Stability usually inverse)
     */
    public record OCEAN(
        double openness,
        double conscientiousness,
        double extraversion,
        double agreeableness,
        double neuroticism
    ) implements Serializable {}

    /**
     * Standardizes a raw score into a T-score.
     * T-scores have a mean of 50 and a standard deviation of 10.
     *
     * @param raw  the raw score
     * @param mean the population mean
     * @param sd   the population standard deviation
     * @return the standardized T-score
     */
    public static Real toTScore(double raw, double mean, double sd) {
        if (sd == 0) return Real.of(50);
        return Real.of(50 + 10 * (raw - mean) / sd);
    }

    /**
     * Calculates the compatibility index between two personality profiles.
     * Usage inverse Euclidean distance to determine similarity.
     * <p>
     * Note: In some contexts, complementarity (opposites attract) might be preferred 
     * for certain dimensions, but this method assumes similarity indicates compatibility.
     * </p>
     *
     * @param p1 first profile
     * @param p2 second profile
     * @return compatibility score (0.0 to 1.0), where 1.0 is identical
     */
    public static Real calculateCompatibility(OCEAN p1, OCEAN p2) {
        double dist = Math.sqrt(
            Math.pow(p1.openness() - p2.openness(), 2) +
            Math.pow(p1.conscientiousness() - p2.conscientiousness(), 2) +
            Math.pow(p1.extraversion() - p2.extraversion(), 2) +
            Math.pow(p1.agreeableness() - p2.agreeableness(), 2) +
            Math.pow(p1.neuroticism() - p2.neuroticism(), 2)
        );
        // Inverse distance function bounded between 0 and 1 (assuming normalized inputs ~0-1)
        // If inputs are T-scores (0-100), this will be very small.
        // Assuming inputs are normalized 0-1 range for a 0-1 result context.
        return Real.of(1.0 / (1.0 + dist));
    }
}
