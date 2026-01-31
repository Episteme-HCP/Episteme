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

package org.jscience.social.architecture.urbanism;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Analytical tool for modeling urban sprawl and land-use expansion dynamics. 
 * It employs cellular automata-inspired logic to estimate growth probabilities 
 * and structural metrics for assessing urban fragmentation and horizontal 
 * expansion.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class UrbanSprawlModel {

    private UrbanSprawlModel() {}

    /**
     * Calculates the probability of a specific geographical cell transitioning 
     * from undeveloped to urbanized.
     * 
     * @param distToRoad normalized distance to the nearest transportation infrastructure
     * @param distToCenter normalized distance to the primary urban core
     * @param currentDensity existing urbanization level (0.0 to 1.0)
     * @return calculated growth probability (0.0 to 1.0)
     */
    public static double growthProbability(double distToRoad, double distToCenter, double currentDensity) {
        // Inverse relationship with distance: proximity increases probability.
        // Proximity to roads (accessibility) and city center (agglomeration) are drivers.
        // Current density acts as a saturation constraint.
        double prob = (1.0 / (1.0 + Math.max(0, distToRoad))) * 
                      (1.0 / (1.0 + Math.max(0, distToCenter))) * 
                      (1.0 - Math.max(0, Math.min(1.0, currentDensity)));
        return Math.max(0, Math.min(1.0, prob));
    }

    /**
     * Calculates the Shannon Entropy index to measure the dispersion of 
     * urban growth. Higher values indicate higher sprawl.
     * 
     * @param zoneDensities array of density values for different urban zones
     * @return Shannon Entropy value as a Real
     */
    public static Real calculateShannonEntropy(double[] zoneDensities) {
        if (zoneDensities == null || zoneDensities.length == 0) return Real.ZERO;
        
        double sum = 0;
        for (double d : zoneDensities) sum += d;
        if (sum == 0) return Real.ZERO;
        
        double entropy = 0;
        for (double d : zoneDensities) {
            if (d > 0) {
                double p = d / sum;
                entropy -= p * Math.log(p);
            }
        }
        
        // Normalize by log(n)
        double normalizedEntropy = entropy / Math.log(zoneDensities.length);
        return Real.of(normalizedEntropy);
    }

    /**
     * Estimates the Urban Sprawl Index based on the ratio of land consumption 
     * rate to population growth rate.
     * 
     * @param landExpansionRate annual percentage increase in urban land area
     * @param populationGrowthRate annual percentage increase in population
     * @return ratio value; values > 1.0 indicate sprawling growth
     */
    public static Real sprawlRatio(double landExpansionRate, double populationGrowthRate) {
        if (populationGrowthRate <= 0) return Real.of(Double.POSITIVE_INFINITY);
        return Real.of(landExpansionRate / populationGrowthRate);
    }
}

