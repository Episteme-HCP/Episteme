/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.chemistry.polymer;

/**
 * Polymerization kinetics and statistics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Polymerization {

    /**
     * Carothers Equation (Step-growth polymerization).
     * Degree of polymerization (X_n) vs extent of reaction (p).
     * X_n = 1 / (1 - p)
     * 
     * @param p Fraction of functional groups reacted (0 to 1)
     * @return Number average degree of polymerization
     */
    public static double carothersEquation(double p) {
        if (p >= 1.0)
            return Double.POSITIVE_INFINITY;
        return 1.0 / (1.0 - p);
    }

    /**
     * Carothers equation with stoichiometric imbalance r.
     * X_n = (1 + r) / (1 + r - 2rp)
     * 
     * @param p Extent of reaction
     * @param r Stoichiometric ratio (Na/Nb <= 1)
     * @return Degree of polymerization
     */
    public static double carothersEquationImbalance(double p, double r) {
        return (1.0 + r) / (1.0 + r - 2.0 * r * p);
    }
}
