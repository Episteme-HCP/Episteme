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

package org.jscience.chemistry.acidbase;

import org.jscience.mathematics.numbers.real.Real;

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
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AcidBase {

    /** Standard temperature for Kw (25°C) */
    public static final Real STANDARD_TEMP_K = Real.of(298.15);

    /** Water ionization constant at 25°C */
    public static final Real KW_25C = Real.of(1.0e-14);

    private static final Real FOURTEEN = Real.of(14.0);
    private static final Real R_GAS = Real.of(8.314);
    private static final Real DELTA_H = Real.of(55800);

    private AcidBase() {
    }

    /**
     * Calculates pH from hydrogen ion concentration.
     */
    public static Real pH(Real hConcentration) {
        if (hConcentration.compareTo(Real.ZERO) <= 0)
            throw new IllegalArgumentException("[H+] must be positive");
        return hConcentration.log10().negate();
    }

    /**
     * Calculates pOH from hydroxide ion concentration.
     */
    public static Real pOH(Real ohConcentration) {
        if (ohConcentration.compareTo(Real.ZERO) <= 0)
            throw new IllegalArgumentException("[OH-] must be positive");
        return ohConcentration.log10().negate();
    }

    /**
     * Calculates [H+] from pH.
     */
    public static Real hConcentration(Real pH) {
        return Real.of(10).pow(pH.negate());
    }

    /**
     * Calculates [OH-] from pOH.
     */
    public static Real ohConcentration(Real pOH) {
        return Real.of(10).pow(pOH.negate());
    }

    /**
     * Converts pH to pOH at 25°C.
     * (pH + pOH = 14 at 25°C)
     */
    public static Real pHtopOH(Real pH) {
        return FOURTEEN.subtract(pH);
    }

    /**
     * Henderson-Hasselbalch equation for buffer pH.
     * pH = pKa + log([A-]/[HA])
     */
    public static Real hendersonHasselbalch(Real pKa, Real conjugateBaseConc, Real acidConc) {
        if (conjugateBaseConc.compareTo(Real.ZERO) <= 0 || acidConc.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Concentrations must be positive");
        }
        return pKa.add(conjugateBaseConc.divide(acidConc).log10());
    }

    /**
     * Calculates pH of a weak acid solution.
     * Uses simplified Ka expression: Ka = x²/(Ca - x)
     */
    public static Real weakAcidpH(Real Ka, Real acidConcentration) {
        // Solve x² + Ka*x - Ka*Ca = 0
        Real a = Real.ONE;
        Real b = Ka;
        Real c = Ka.negate().multiply(acidConcentration);
        Real discriminant = b.multiply(b).subtract(Real.of(4).multiply(a).multiply(c));
        Real x = b.negate().add(discriminant.sqrt()).divide(Real.TWO.multiply(a));
        return pH(x);
    }

    /**
     * Calculates pH of a weak base solution.
     */
    public static Real weakBasepH(Real Kb, Real baseConcentration) {
        Real a = Real.ONE;
        Real b = Kb;
        Real c = Kb.negate().multiply(baseConcentration);
        Real discriminant = b.multiply(b).subtract(Real.of(4).multiply(a).multiply(c));
        Real x = b.negate().add(discriminant.sqrt()).divide(Real.TWO.multiply(a));
        Real pOHVal = pOH(x);
        return FOURTEEN.subtract(pOHVal);
    }

    /**
     * Buffer capacity (Van Slyke equation approximation).
     * β = 2.303 * C * Ka * [H+] / (Ka + [H+])²
     */
    public static Real bufferCapacity(Real totalBufferConc, Real Ka, Real pH) {
        Real h = hConcentration(pH);
        Real term = Ka.multiply(h).divide(Ka.add(h).pow(2));
        return Real.of(2.303).multiply(totalBufferConc).multiply(term);
    }

    /**
     * Temperature-corrected Kw.
     */
    public static Real kwAtTemperature(Real temperatureK) {
        Real lnKw25 = KW_25C.log();
        Real invDeltaT = Real.ONE.divide(temperatureK).subtract(Real.ONE.divide(STANDARD_TEMP_K));
        Real lnKw = lnKw25.subtract(DELTA_H.divide(R_GAS).multiply(invDeltaT));
        return lnKw.exp();
    }

    /**
     * Classifies solution by pH.
     */
    public static String classify(Real pH) {
        double pHVal = pH.doubleValue();
        if (pHVal < 3)
            return "Strongly Acidic";
        if (pHVal < 6)
            return "Weakly Acidic";
        if (pHVal < 8)
            return "Neutral";
        if (pHVal < 11)
            return "Weakly Basic";
        return "Strongly Basic";
    }
}
