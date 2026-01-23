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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
     * Categorizes adopters based on when they adopt an innovation.
     */
    public enum AdoptionStatus { INNOVATOR, EARLY_ADOPTER, EARLY_MAJORITY, LATE_MAJORITY, LAGGARD }

    /**
     * Represents a cultural identifier or innovation with specific properties.
     * @param name       the name of the trait
     * @param complexity difficulty of adoption (0-1, higher is harder)
     * @param utility    attractiveness of the trait (0-1, higher is better)
     */
    public record CultureTrait(
        String name,
        double complexity,
        double utility
    ) {}

    /**
     * Simulates the Bass Diffusion Model for innovation adoption.
     * <p>
     * Equation: dN(t)/dt = (p + q * N(t)/M) * (M - N(t))
     * </p>
     * 
     * @param p       Coefficient of innovation (external influence)
     * @param q       Coefficient of imitation (internal influence)
     * @param m       Potential market/population size
     * @param periods Number of time periods to simulate
     * @return a list of cumulative adopter counts for each period
     */
    public static List<Double> simulateBassDiffusion(double p, double q, double m, int periods) {
        List<Double> adopters = new ArrayList<>();
        double n = 0; // Cumulative adopters

        for (int t = 0; t < periods; t++) {
            double dn = (p + q * (n / m)) * (m - n);
            n += dn;
            adopters.add(n);
        }
        return adopters;
    }

    /**
     * Calculates the probability of cultural transmission between two agents.
     * Based on Axelrod's Model, where probability is proportional to cultural similarity.
     *
     * @param traits1 feature vector of first agent
     * @param traits2 feature vector of second agent
     * @return probability of interaction/transmission (0.0 to 1.0)
     */
    public static Real transmissionProbability(int[] traits1, int[] traits2) {
        if (traits1.length != traits2.length || traits1.length == 0) {
            return Real.ZERO;
        }
        int shared = 0;
        for (int i = 0; i < traits1.length; i++) {
            if (traits1[i] == traits2[i]) shared++;
        }
        return Real.of((double) shared / traits1.length);
    }

    /**
     * Categorizes an adopter based on their position in the adoption timeline.
     * Uses standard diffusion of innovations percentages (e.g., Rogers).
     *
     * @param cumulativePercent the percentile of adoption (0-100)
     * @return the corresponding AdoptionStatus
     */
    public static AdoptionStatus getAdopterType(double cumulativePercent) {
        if (cumulativePercent <= 2.5) return AdoptionStatus.INNOVATOR;
        if (cumulativePercent <= 16.0) return AdoptionStatus.EARLY_ADOPTER;
        if (cumulativePercent <= 50.0) return AdoptionStatus.EARLY_MAJORITY;
        if (cumulativePercent <= 84.0) return AdoptionStatus.LATE_MAJORITY;
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
    public static Real culturalDistance(Map<String, Double> values1, Map<String, Double> values2) {
        double sumSq = 0;
        Set<String> allKeys = new HashSet<>(values1.keySet());
        allKeys.addAll(values2.keySet());

        for (String key : allKeys) {
            double v1 = values1.getOrDefault(key, 0.5);
            double v2 = values2.getOrDefault(key, 0.5);
            sumSq += Math.pow(v1 - v2, 2);
        }
        return Real.of(Math.sqrt(sumSq));
    }
}
