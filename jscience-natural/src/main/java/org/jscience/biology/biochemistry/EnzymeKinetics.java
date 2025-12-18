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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.biology.biochemistry;

/**
 * Michaelis-Menten enzyme kinetics model.
 * <p>
 * Models enzyme-catalyzed reactions using:
 * <ul>
 * <li>Michaelis-Menten equation: v = Vmax * [S] / (Km + [S])</li>
 * <li>Lineweaver-Burk linearization</li>
 * <li>Inhibition models (competitive, non-competitive)</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class EnzymeKinetics {

    private final double vmax; // Maximum velocity
    private final double km; // Michaelis constant

    /**
     * Creates Michaelis-Menten kinetics model.
     * 
     * @param vmax maximum velocity (product/time)
     * @param km   Michaelis constant (same units as substrate concentration)
     */
    public EnzymeKinetics(double vmax, double km) {
        if (vmax <= 0 || km <= 0) {
            throw new IllegalArgumentException("Vmax and Km must be positive");
        }
        this.vmax = vmax;
        this.km = km;
    }

    /**
     * Calculates reaction velocity at given substrate concentration.
     * v = Vmax * [S] / (Km + [S])
     * 
     * @param substrateConc substrate concentration
     * @return reaction velocity
     */
    public double velocity(double substrateConc) {
        if (substrateConc < 0)
            throw new IllegalArgumentException("Substrate concentration must be non-negative");
        return vmax * substrateConc / (km + substrateConc);
    }

    /**
     * Calculates velocity with competitive inhibition.
     * Apparent Km = Km * (1 + [I]/Ki)
     * 
     * @param substrateConc substrate concentration
     * @param inhibitorConc inhibitor concentration
     * @param ki            inhibitor dissociation constant
     * @return reaction velocity
     */
    public double velocityCompetitiveInhibition(double substrateConc, double inhibitorConc, double ki) {
        double kmApp = km * (1 + inhibitorConc / ki);
        return vmax * substrateConc / (kmApp + substrateConc);
    }

    /**
     * Calculates velocity with non-competitive inhibition.
     * Apparent Vmax = Vmax / (1 + [I]/Ki)
     * 
     * @param substrateConc substrate concentration
     * @param inhibitorConc inhibitor concentration
     * @param ki            inhibitor dissociation constant
     * @return reaction velocity
     */
    public double velocityNonCompetitiveInhibition(double substrateConc, double inhibitorConc, double ki) {
        double vmaxApp = vmax / (1 + inhibitorConc / ki);
        return vmaxApp * substrateConc / (km + substrateConc);
    }

    /**
     * Substrate concentration needed for a given fraction of Vmax.
     * [S] = Km * (v/Vmax) / (1 - v/Vmax)
     * 
     * @param fraction fraction of Vmax (0 to 1)
     * @return required substrate concentration
     */
    public double substrateForFraction(double fraction) {
        if (fraction <= 0 || fraction >= 1) {
            throw new IllegalArgumentException("Fraction must be between 0 and 1 (exclusive)");
        }
        return km * fraction / (1 - fraction);
    }

    /**
     * Calculates catalytic efficiency (kcat/Km).
     * 
     * @param enzymeConc enzyme concentration
     * @return catalytic efficiency
     */
    public double catalyticEfficiency(double enzymeConc) {
        double kcat = vmax / enzymeConc;
        return kcat / km;
    }

    /**
     * Turnover number (kcat).
     * 
     * @param enzymeConc enzyme concentration
     * @return kcat (substrate molecules converted per enzyme per unit time)
     */
    public double turnoverNumber(double enzymeConc) {
        return vmax / enzymeConc;
    }

    /**
     * Generates Lineweaver-Burk plot data.
     * 1/v vs 1/[S]
     * 
     * @param substrateConcs array of substrate concentrations
     * @return array of [1/S, 1/v] pairs
     */
    public double[][] lineweaverBurkData(double[] substrateConcs) {
        double[][] data = new double[substrateConcs.length][2];
        for (int i = 0; i < substrateConcs.length; i++) {
            double s = substrateConcs[i];
            double v = velocity(s);
            data[i][0] = 1.0 / s;
            data[i][1] = 1.0 / v;
        }
        return data;
    }

    /**
     * Hill equation for cooperative binding.
     * v = Vmax * [S]^n / (K0.5^n + [S]^n)
     * 
     * @param substrateConc   substrate concentration
     * @param hillCoefficient n (>1 positive cooperativity, <1 negative)
     * @param k05             half-saturation constant
     * @return reaction velocity
     */
    public static double hillEquation(double vmax, double substrateConc, double hillCoefficient, double k05) {
        double sn = Math.pow(substrateConc, hillCoefficient);
        double kn = Math.pow(k05, hillCoefficient);
        return vmax * sn / (kn + sn);
    }

    public double getVmax() {
        return vmax;
    }

    public double getKm() {
        return km;
    }

    @Override
    public String toString() {
        return String.format("EnzymeKinetics{Vmax=%.3f, Km=%.3f}", vmax, km);
    }
}
