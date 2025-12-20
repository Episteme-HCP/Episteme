/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.chemistry.acidbase;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.quantity.Dimensionless;

/**
 * pH and buffer calculations.
 * <p>
 * Supports both raw double and Quantity-based APIs for flexibility.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class pHCalculator {

    private pHCalculator() {
    }

    // ========== Double-based API ==========

    /**
     * Calculates pH from hydrogen ion concentration.
     * pH = -log10[H+]
     */
    public static double pHFromConcentration(double hydrogenIonMolarity) {
        if (hydrogenIonMolarity <= 0)
            throw new IllegalArgumentException("Concentration must be positive");
        return -Math.log10(hydrogenIonMolarity);
    }

    /**
     * Calculates hydrogen ion concentration from pH.
     * [H+] = 10^(-pH)
     */
    public static double concentrationFromPH(double pH) {
        return Math.pow(10, -pH);
    }

    /**
     * Calculates pOH from pH.
     * pOH = 14 - pH (at 25°C)
     */
    public static double pOHFromPH(double pH) {
        return 14.0 - pH;
    }

    /**
     * Henderson-Hasselbalch equation for buffer pH.
     * pH = pKa + log10([A-]/[HA])
     */
    public static double bufferPH(double pKa, double conjugateBaseMolarity, double acidMolarity) {
        if (acidMolarity <= 0 || conjugateBaseMolarity <= 0) {
            throw new IllegalArgumentException("Concentrations must be positive");
        }
        return pKa + Math.log10(conjugateBaseMolarity / acidMolarity);
    }

    /**
     * Calculates buffer capacity (approximate).
     * β = 2.303 * C * Ka * [H+] / (Ka + [H+])^2
     */
    public static double bufferCapacity(double totalBufferConcentration, double Ka, double pH) {
        double H = Math.pow(10, -pH);
        return 2.303 * totalBufferConcentration * Ka * H / Math.pow(Ka + H, 2);
    }

    // ========== Real-based API ==========

    /**
     * Calculates pH from hydrogen ion concentration using Real.
     */
    public static Real pHFromConcentration(Real hydrogenIonMolarity) {
        return Real.of(pHFromConcentration(hydrogenIonMolarity.doubleValue()));
    }

    /**
     * Calculates hydrogen ion concentration from pH using Real.
     */
    public static Real concentrationFromPH(Real pH) {
        return Real.of(concentrationFromPH(pH.doubleValue()));
    }

    /**
     * Henderson-Hasselbalch equation using Real.
     */
    public static Real bufferPH(Real pKa, Real conjugateBaseMolarity, Real acidMolarity) {
        return Real.of(bufferPH(pKa.doubleValue(), conjugateBaseMolarity.doubleValue(), acidMolarity.doubleValue()));
    }

    // ========== Utility Methods ==========

    /**
     * Checks if a solution is acidic (pH < 7).
     */
    public static boolean isAcidic(double pH) {
        return pH < 7.0;
    }

    /**
     * Checks if a solution is basic/alkaline (pH > 7).
     */
    public static boolean isBasic(double pH) {
        return pH > 7.0;
    }

    /**
     * Checks if a solution is neutral (pH ≈ 7).
     */
    public static boolean isNeutral(double pH, double tolerance) {
        return Math.abs(pH - 7.0) <= tolerance;
    }

    /**
     * Calculates the pKa from Ka.
     */
    public static double pKaFromKa(double Ka) {
        return -Math.log10(Ka);
    }

    /**
     * Calculates Ka from pKa.
     */
    public static double KaFromPKa(double pKa) {
        return Math.pow(10, -pKa);
    }
}
