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

package org.jscience.physics.classical.thermodynamics;

/**
 * Phase Equilibria calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PhaseEquilibria {

    /**
     * Clausius-Clapeyron equation.
     * Relates vapor pressure and temperature.
     * ln(P2/P1) = (-ΔH_vap / R) * (1/T2 - 1/T1)
     * 
     * @param p1        Pressure at T1 (Pa)
     * @param t1        Temperature 1 (K)
     * @param t2        Temperature 2 (K)
     * @param deltaHvap Enthalpy of vaporization (J/mol)
     * @return Pressure at T2 (Pa)
     */
    public static double clausiusClapeyron(double p1, double t1, double t2, double deltaHvap) {
        double R = 8.314462618;
        double exponent = (-deltaHvap / R) * (1.0 / t2 - 1.0 / t1);
        return p1 * Math.exp(exponent);
    }

    /**
     * Gibbs Phase Rule.
     * F = C - P + 2
     * 
     * @param components Number of chemically independent components
     * @param phases     Number of phases
     * @return Degrees of freedom
     */
    public static int gibbsPhaseRule(int components, int phases) {
        return components - phases + 2;
    }
}
