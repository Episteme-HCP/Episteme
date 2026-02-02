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

package org.jscience.social.economics.trade;

import org.jscience.core.mathematics.numbers.real.Real;


import org.jscience.social.politics.Nation;

/**
 * Implements standard international trade models, such as the Gravity Model of Trade.
 * 
 * <p>The <b>Gravity Model</b> predicts bilateral trade flows based on the economic sizes
 * (often GDP) and distance between two units.</p>
 * 
 * <p>Formula: F_ij = G * (M_i * M_j) / D_ij</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TradeModel {

    private TradeModel() {
        // Utility class
    }

    /**
     * Calculates the expected trade flow between two nations using the standard Gravity Model.
     *
     * @param nationA The first nation.
     * @param gdpA The GDP of the first nation.
     * @param nationB The second nation.
     * @param gdpB The GDP of the second nation.
     * @param distance The distance between the two nations (in km or similar unit).
     * @param constantG The gravity constant (empirically determined).
     * @return The predicted trade volume.
     */
    public static Real gravity(Nation nationA, Real gdpA, Nation nationB, Real gdpB, Real distance, Real constantG) {
        if (distance.equals(Real.ZERO)) {
            // Avoid division by zero; treat as intra-national trade or extremely high
             return constantG.multiply(gdpA).multiply(gdpB); // Simplified fallback
        }
        
        // F = G * (GDP_A * GDP_B) / Distance
        return constantG.multiply(gdpA).multiply(gdpB).divide(distance);
    }
    
    /**
     * Extended Gravity Model including population.
     * F_ij = G * (GDP_i^a * GDP_j^b) / D_ij^c
     *
     * @param gdpA GDP of nation A
     * @param gdpB GDP of nation B
     * @param distance Distance
     * @param constantG Base constant
     * @param alpha Exponent for GDP A
     * @param beta Exponent for GDP B
     * @param gamma Exponent for Distance (friction)
     * @return Predicted trade volume
     */
    public static Real gravityExtended(Real gdpA, Real gdpB, Real distance, Real constantG, double alpha, double beta, double gamma) {
         if (distance.equals(Real.ZERO))  return Real.ZERO; // or simplify
         
         Real num = constantG.multiply(gdpA.pow(alpha)).multiply(gdpB.pow(beta));
         Real den = distance.pow(gamma);
         
         return num.divide(den);
    }
}
