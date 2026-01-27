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

package org.jscience.law;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Analyzes formal justice and equity in resource distribution based on 
 * Aristotelian principles of distributive and corrective justice.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 */
public final class FormalJusticeAnalyzer {

    private FormalJusticeAnalyzer() {
        // Utility class
    }

    /**
     * Distributive Justice (Proportional): 
     * Calculates an equity index where shares are proportional to merit or contribution.
     * Ratio = Share / Contribution.
     * 
     * @param contributions the merit/contribution values
     * @param shares the distributed shares
     * @return a index where 1.0 represents perfect proportional justice
     */
    public static Real distributiveEquityIndex(double[] contributions, double[] shares) {
        if (contributions == null || shares == null || contributions.length != shares.length || contributions.length == 0) {
            return Real.ZERO;
        }
        
        double[] ratios = new double[contributions.length];
        double sumRatios = 0;
        for (int i = 0; i < contributions.length; i++) {
            if (contributions[i] == 0) {
                ratios[i] = 0;
            } else {
                ratios[i] = shares[i] / contributions[i];
            }
            sumRatios += ratios[i];
        }
        
        double avg = sumRatios / contributions.length;
        double variance = 0;
        for (double r : ratios) {
            variance += Math.pow(r - avg, 2);
        }
        
        // Return 1.0 - normalized variance (1.0 = perfect justice)
        return Real.of(1.0 / (1.0 + Math.sqrt(variance)));
    }

    /**
     * Corrective Justice: Compares the value of harm inflicted with the compensation provided.
     * 
     * @param harmValue the estimated magnitude of harm
     * @param compensation the amount of restitution or compensation given
     * @return the ratio of compensation to harm (1.0 = perfect restitution)
     */
    public static Real compensationAdequacy(double harmValue, double compensation) {
        if (harmValue == 0) {
            return Real.ONE;
        }
        return Real.of(compensation / harmValue);
    }
}
