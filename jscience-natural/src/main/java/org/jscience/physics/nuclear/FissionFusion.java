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

package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Fission and fusion calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FissionFusion {

    /** MeV to Joules conversion */
    private static final Real MEV_TO_JOULES = Real.of(1.602176634e-13);

    /** Speed of light (m/s) */
    private static final Real C = Real.of(299792458);

    /** Speed of light squared (mÃ‚Â²/sÃ‚Â²) */
    @SuppressWarnings("unused")
    private static final Real C_SQUARED = C.pow(2);

    // ==================== FISSION ====================

    /**
     * Energy released per U-235 fission event.
     * Average: ~200 MeV total, ~180 MeV recoverable
     */
    public static final Real U235_ENERGY_MEV = Real.of(200.0);

    /**
     * Calculates energy released from fission events.
     * 
     * @param numFissions      Number of fission events
     * @param energyPerFission Energy per fission in MeV
     * @return Total energy in Joules
     */
    public static Real fissionEnergy(Real numFissions, Real energyPerFission) {
        return numFissions.multiply(energyPerFission).multiply(MEV_TO_JOULES);
    }

    /**
     * Estimates critical mass for a spherical bare core.
     * M_c Ã¢â€°Ë† (Ãâ‚¬ / 3) * (ÃÂ_c / (ÃŽÂ½ - 1)) * (1/ÃŽÂ£_f)Ã‚Â³ * ÃŽÂ»_tr
     * 
     * This is a highly simplified model. Real critical mass depends on:
     * - Geometry, reflectors, enrichment, density
     * 
     * @param density            Material density (kg/mÃ‚Â³)
     * @param macroFissionXS     Macroscopic fission cross-section (1/m)
     * @param neutronsPerFission Average neutrons per fission (ÃŽÂ½)
     * @return Approximate critical mass in kg
     */
    public static Real criticalMassEstimate(Real density, Real macroFissionXS,
            Real neutronsPerFission) {
        // Simplified: use critical radius formula
        // For U-235: bare sphere critical mass ~52 kg
        // Formula: M = (4/3) Ãâ‚¬ r_cÃ‚Â³ ÃÂ

        Real nuMinusOne = neutronsPerFission.subtract(Real.ONE);
        Real diffusionLength = Real.of(0.03); // ~3 cm typical

        // r_c Ã¢â€°Ë† Ãâ‚¬ * L / sqrt(k - 1) where k = ÃŽÂ½*ÃÆ’_f/(ÃÆ’_a)
        Real kEffMinusOne = nuMinusOne.multiply(Real.of(0.9)); // Simplified
        Real criticalRadius = Real.PI.multiply(diffusionLength)
                .divide(kEffMinusOne.sqrt());

        /** 4/3 as Real */
        Real fourThirds = Real.of(4).divide(Real.of(3));

        Real volume = fourThirds.multiply(Real.PI).multiply(criticalRadius.pow(3));
        return density.multiply(volume);
    }

    /**
     * Number of fissions per second in a reactor (power level).
     * P = E_per_fission * N_fissions/s
     * 
     * @param powerWatts Thermal power output
     * @return Fission rate (fissions/second)
     */
    public static Real fissionRate(Real powerWatts) {
        Real energyPerFission = U235_ENERGY_MEV.multiply(MEV_TO_JOULES);
        return powerWatts.divide(energyPerFission);
    }

    // ==================== FUSION ====================

    /** D-T fusion: D + T Ã¢â€ â€™ He-4 + n + 17.6 MeV */
    public static final Real DT_ENERGY_MEV = Real.of(17.6);

    /** D-D fusion: D + D Ã¢â€ â€™ He-3 + n + 3.27 MeV (50%) or T + p + 4.03 MeV (50%) */
    public static final Real DD_ENERGY_MEV_AVG = Real.of(3.65);

    /** p-p chain total energy: ~26.73 MeV (stellar fusion) */
    public static final Real PP_CHAIN_ENERGY_MEV = Real.of(26.73);

    /**
     * Calculates fusion energy yield.
     * 
     * @param numReactions      Number of fusion events
     * @param energyPerReaction Energy per reaction in MeV
     * @return Total energy in Joules
     */
    public static Real fusionEnergy(Real numReactions, Real energyPerReaction) {
        return numReactions.multiply(energyPerReaction).multiply(MEV_TO_JOULES);
    }

    /**
     * Lawson criterion for fusion ignition.
     * n * Ãâ€ž > L where n is density (mÃ¢ÂÂ»Ã‚Â³), Ãâ€ž is confinement time (s)
     * For D-T: n*Ãâ€ž > 1.5 Ãƒâ€” 10Ã‚Â²Ã¢ÂÂ° mÃ¢ÂÂ»Ã‚Â³Ã‚Â·s at 10 keV
     * 
     * @param density         Plasma density (particles/mÃ‚Â³)
     * @param confinementTime Energy confinement time (s)
     * @return n*Ãâ€ž product
     */
    public static Real lawsonProduct(Real density, Real confinementTime) {
        return density.multiply(confinementTime);
    }

    /** Lawson criterion for D-T at 10 keV */
    public static final Real DT_LAWSON_CRITERION = Real.of(1.5e20);

    /**
     * Triple product criterion (improved Lawson).
     * n * T * Ãâ€ž > threshold
     * For D-T ignition: n*T*Ãâ€ž > 3 Ãƒâ€” 10Ã‚Â²Ã‚Â¹ keVÃ‚Â·mÃ¢ÂÂ»Ã‚Â³Ã‚Â·s
     * 
     * @param density         Plasma density (mÃ¢ÂÂ»Ã‚Â³)
     * @param temperature     Ion temperature (keV)
     * @param confinementTime Energy confinement time (s)
     * @return Triple product
     */
    public static Real tripleProduct(Real density, Real temperature, Real confinementTime) {
        return density.multiply(temperature).multiply(confinementTime);
    }

    /** Triple product threshold for D-T ignition */
    public static final Real DT_TRIPLE_PRODUCT_THRESHOLD = Real.of(3e21);

    /**
     * Estimates energy gain Q = fusion power / input power.
     * Q = 1 Ã¢â€ â€™ breakeven, Q > 1 Ã¢â€ â€™ net energy
     * 
     * @param fusionPower Power from fusion reactions
     * @param inputPower  Power used to heat/confine plasma
     * @return Q factor
     */
    public static Real energyGainQ(Real fusionPower, Real inputPower) {
        return fusionPower.divide(inputPower);
    }
}


