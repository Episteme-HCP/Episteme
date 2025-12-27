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
package org.jscience.chemistry.acidbase;

import org.jscience.mathematics.numbers.real.Real;

/**
 * pH and buffer calculations.
 * All methods use Real for type-safe computations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class pHCalculator {

    private static final Real SEVEN = Real.of(7.0);
    private static final Real FOURTEEN = Real.of(14.0);

    private pHCalculator() {
    }

    /**
     * Calculates pH from hydrogen ion concentration.
     * pH = -log10[H+]
     */
    public static Real pHFromConcentration(Real hydrogenIonMolarity) {
        if (hydrogenIonMolarity.compareTo(Real.ZERO) <= 0)
            throw new IllegalArgumentException("Concentration must be positive");
        return hydrogenIonMolarity.log10().negate();
    }

    /**
     * Calculates hydrogen ion concentration from pH.
     * [H+] = 10^(-pH)
     */
    public static Real concentrationFromPH(Real pH) {
        return Real.of(10).pow(pH.negate());
    }

    /**
     * Calculates pOH from pH.
     * pOH = 14 - pH (at 25°C)
     */
    public static Real pOHFromPH(Real pH) {
        return FOURTEEN.subtract(pH);
    }

    /**
     * Henderson-Hasselbalch equation for buffer pH.
     * pH = pKa + log10([A-]/[HA])
     */
    public static Real bufferPH(Real pKa, Real conjugateBaseMolarity, Real acidMolarity) {
        if (acidMolarity.compareTo(Real.ZERO) <= 0 || conjugateBaseMolarity.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Concentrations must be positive");
        }
        return pKa.add(conjugateBaseMolarity.divide(acidMolarity).log10());
    }

    /**
     * Calculates buffer capacity (approximate).
     * β = 2.303 * C * Ka * [H+] / (Ka + [H+])^2
     */
    public static Real bufferCapacity(Real totalBufferConcentration, Real Ka, Real pH) {
        Real H = Real.of(10).pow(pH.negate());
        Real numerator = Real.of(2.303).multiply(totalBufferConcentration).multiply(Ka).multiply(H);
        Real denominator = Ka.add(H).pow(2);
        return numerator.divide(denominator);
    }

    /**
     * Checks if a solution is acidic (pH < 7).
     */
    public static boolean isAcidic(Real pH) {
        return pH.compareTo(SEVEN) < 0;
    }

    /**
     * Checks if a solution is basic/alkaline (pH > 7).
     */
    public static boolean isBasic(Real pH) {
        return pH.compareTo(SEVEN) > 0;
    }

    /**
     * Checks if a solution is neutral (pH ≈ 7).
     */
    public static boolean isNeutral(Real pH, Real tolerance) {
        return pH.subtract(SEVEN).abs().compareTo(tolerance) <= 0;
    }

    /**
     * Calculates the pKa from Ka.
     */
    public static Real pKaFromKa(Real Ka) {
        return Ka.log10().negate();
    }

    /**
     * Calculates Ka from pKa.
     */
    public static Real KaFromPKa(Real pKa) {
        return Real.of(10).pow(pKa.negate());
    }
}
