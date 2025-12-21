/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * Equilibrium Constant calculations.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EquilibriumConstant {

    /**
     * Calculates K_eq from Gibbs Free Energy change.
     * K = exp(-ΔG° / RT)
     * 
     * @param deltaG      Standard Gibbs free energy change (J/mol)
     * @param temperature Temperature (Kelvin)
     * @return Equilibrium constant (dimensionless)
     */
    public static double calculateK(double deltaG, double temperature) {
        double R = 8.314462618; // Gas constant J/(mol·K)
        return Math.exp(-deltaG / (R * temperature));
    }

    /**
     * Calculates Gibbs Free Energy from K_eq.
     * ΔG° = -RT ln K
     */
    public static double calculateDeltaG(double K, double temperature) {
        double R = 8.314462618;
        return -R * temperature * Math.log(K);
    }

    /**
     * Reaction Quotient Q.
     * Q = product concentrations / reactant concentrations
     * 
     * @param productConcs  Array of product concentrations
     * @param reactantConcs Array of reactant concentrations
     * @return Q
     */
    public static double calculateQ(double[] productConcs, double[] reactantConcs) {
        double num = 1.0;
        for (double c : productConcs)
            num *= c;

        double den = 1.0;
        for (double c : reactantConcs)
            den *= c;

        return den == 0 ? Double.POSITIVE_INFINITY : num / den;
    }
}
