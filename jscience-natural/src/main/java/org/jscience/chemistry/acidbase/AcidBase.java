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
package org.jscience.chemistry.acidbase;

/**
 * pH calculations for aqueous solutions.
 * <p>
 * Provides tools for:
 * <ul>
 * <li>pH and pOH calculations</li>
 * <li>H+ and OH- concentration conversions</li>
 * <li>Buffer capacity analysis</li>
 * <li>Henderson-Hasselbalch equation</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class AcidBase {

    /** Standard temperature for Kw (25°C) */
    public static final double STANDARD_TEMP_K = 298.15;

    /** Water ionization constant at 25°C */
    public static final double KW_25C = 1.0e-14;

    private AcidBase() {
    }

    /**
     * Calculates pH from hydrogen ion concentration.
     * 
     * @param hConcentration [H+] in mol/L
     * @return pH value
     */
    public static double pH(double hConcentration) {
        if (hConcentration <= 0)
            throw new IllegalArgumentException("[H+] must be positive");
        return -Math.log10(hConcentration);
    }

    /**
     * Calculates pOH from hydroxide ion concentration.
     * 
     * @param ohConcentration [OH-] in mol/L
     * @return pOH value
     */
    public static double pOH(double ohConcentration) {
        if (ohConcentration <= 0)
            throw new IllegalArgumentException("[OH-] must be positive");
        return -Math.log10(ohConcentration);
    }

    /**
     * Calculates [H+] from pH.
     * 
     * @param pH pH value
     * @return hydrogen ion concentration (mol/L)
     */
    public static double hConcentration(double pH) {
        return Math.pow(10, -pH);
    }

    /**
     * Calculates [OH-] from pOH.
     * 
     * @param pOH pOH value
     * @return hydroxide ion concentration (mol/L)
     */
    public static double ohConcentration(double pOH) {
        return Math.pow(10, -pOH);
    }

    /**
     * Converts pH to pOH at 25°C.
     * (pH + pOH = 14 at 25°C)
     */
    public static double pHtopOH(double pH) {
        return 14.0 - pH;
    }

    /**
     * Henderson-Hasselbalch equation for buffer pH.
     * pH = pKa + log([A-]/[HA])
     * 
     * @param pKa               acid dissociation constant
     * @param conjugateBaseConc [A-] concentration
     * @param acidConc          [HA] concentration
     * @return buffer pH
     */
    public static double hendersonHasselbalch(double pKa, double conjugateBaseConc, double acidConc) {
        if (conjugateBaseConc <= 0 || acidConc <= 0) {
            throw new IllegalArgumentException("Concentrations must be positive");
        }
        return pKa + Math.log10(conjugateBaseConc / acidConc);
    }

    /**
     * Calculates pH of a weak acid solution.
     * Uses simplified Ka expression: Ka = x²/(Ca - x)
     * 
     * @param Ka                acid dissociation constant
     * @param acidConcentration initial acid concentration (mol/L)
     * @return pH
     */
    public static double weakAcidpH(double Ka, double acidConcentration) {
        // Solve x² + Ka*x - Ka*Ca = 0
        double a = 1;
        double b = Ka;
        double c = -Ka * acidConcentration;
        double x = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        return pH(x);
    }

    /**
     * Calculates pH of a weak base solution.
     * 
     * @param Kb                base dissociation constant
     * @param baseConcentration initial base concentration (mol/L)
     * @return pH
     */
    public static double weakBasepH(double Kb, double baseConcentration) {
        // Calculate pOH first
        double a = 1;
        double b = Kb;
        double c = -Kb * baseConcentration;
        double x = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        double pOH = pOH(x);
        return 14.0 - pOH;
    }

    /**
     * Buffer capacity (Van Slyke equation approximation).
     * β = 2.303 * C * Ka * [H+] / (Ka + [H+])²
     * 
     * @param totalBufferConc total buffer concentration
     * @param Ka              acid dissociation constant
     * @param pH              current pH
     * @return buffer capacity (mol/L per pH unit)
     */
    public static double bufferCapacity(double totalBufferConc, double Ka, double pH) {
        double h = hConcentration(pH);
        double term = Ka * h / Math.pow(Ka + h, 2);
        return 2.303 * totalBufferConc * term;
    }

    /**
     * Temperature-corrected Kw.
     * Kw increases with temperature.
     * 
     * @param temperatureK temperature in Kelvin
     * @return Kw at given temperature (approximate)
     */
    public static double kwAtTemperature(double temperatureK) {
        // Approximate using Van 't Hoff: dln(Kw)/d(1/T) ≈ -ΔH/R
        // ΔH ≈ 55.8 kJ/mol for water ionization
        double deltaH = 55800; // J/mol
        double R = 8.314; // J/(mol*K)
        double lnKw25 = Math.log(KW_25C);
        double lnKw = lnKw25 - (deltaH / R) * (1 / temperatureK - 1 / STANDARD_TEMP_K);
        return Math.exp(lnKw);
    }

    /**
     * Classifies solution by pH.
     */
    public static String classify(double pH) {
        if (pH < 3)
            return "Strongly Acidic";
        if (pH < 6)
            return "Weakly Acidic";
        if (pH < 8)
            return "Neutral";
        if (pH < 11)
            return "Weakly Basic";
        return "Strongly Basic";
    }
}
