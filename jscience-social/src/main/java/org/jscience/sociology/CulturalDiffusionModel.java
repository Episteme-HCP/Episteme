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

package org.jscience.sociology;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the diffusion of culture, innovations, or rumors through a population.
 * Provides implementations of standard diffusion models like the Bass Diffusion Model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class CulturalDiffusionModel {

    private CulturalDiffusionModel() {}

    /**
     * Categorizes an adopter based on their position in the adoption timeline.
     * Uses standard diffusion of innovations percentages (e.g., Rogers).
     *
     * @param cumulativePercent the percentile of adoption (0-100)
     * @return the corresponding AdoptionStatus
     */
    public static AdoptionStatus getAdopterType(Real cumulativePercent) {
        if (cumulativePercent.compareTo(Real.of(2.5)) <= 0) return AdoptionStatus.INNOVATOR;
        if (cumulativePercent.compareTo(Real.of(16.0)) <= 0) return AdoptionStatus.EARLY_ADOPTER;
        if (cumulativePercent.compareTo(Real.of(50.0)) <= 0) return AdoptionStatus.EARLY_MAJORITY;
        if (cumulativePercent.compareTo(Real.of(84.0)) <= 0) return AdoptionStatus.LATE_MAJORITY;
        return AdoptionStatus.LAGGARD;
    }

    /**
     * Estimates the "Cultural Distance" between two societies or groups.
     * Calculates the Euclidean distance between value maps.
     *
     * @param values1 map of cultural dimensions/values for group 1
     * @param values2 map of cultural dimensions/values for group 2
     * @return the calculated distance
     */
    public static Real culturalDistance(Map<String, Real> values1, Map<String, Real> values2) {
        Real sumSq = Real.ZERO;
        Set<String> allKeys = new HashSet<>(values1.keySet());
        allKeys.addAll(values2.keySet());

        Real defaultVal = Real.of(0.5);
        for (String key : allKeys) {
            Real v1 = values1.getOrDefault(key, defaultVal);
            Real v2 = values2.getOrDefault(key, defaultVal);
            Real diff = v1.subtract(v2);
            sumSq = sumSq.add(diff.multiply(diff));
        }
        return sumSq.sqrt();
    }
}
