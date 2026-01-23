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

package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides mathematical models for optimizing athlete recovery and tapering phases.
 * Based on the Banister Impulse-Response performance model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class FatigueRecoveryModel {

    private FatigueRecoveryModel() {}

    /**
     * Predicts athletic performance using Banister's Performance Model.
     * Formula: P(t) = P0 + (Fitness * e^(-t/tau1)) - (Fatigue * e^(-t/tau2))
     * 
     * @param p0       baseline performance
     * @param fitness  current fitness level
     * @param fatigue  current fatigue level
     * @param restDays days elapsed since the last training impulse
     * @param tau1     time constant for fitness decay
     * @param tau2     time constant for fatigue decay
     * @return predicted performance as a Real number
     */
    public static Real predictPerformance(double p0, double fitness, double fatigue, 
                                          int restDays, double tau1, double tau2) {
        double perf = p0 + (fitness * Math.exp(-restDays / tau1)) - 
                           (fatigue * Math.exp(-restDays / tau2));
        return Real.of(perf);
    }

    /**
     * Determines the optimal number of tapering days to achieve maximum performance.
     * 
     * @return the day count (0-30) that yields the highest predicted performance
     */
    public static int findOptimalTaper(double fitness, double fatigue, double tau1, double tau2) {
        int bestDay = 0;
        double maxPerf = -Double.MAX_VALUE;
        for (int d = 0; d < 30; d++) {
            double p = predictPerformance(0, fitness, fatigue, d, tau1, tau2).doubleValue();
            if (p > maxPerf) {
                maxPerf = p;
                bestDay = d;
            }
        }
        return bestDay;
    }
}
