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

package org.jscience.physics.classical.matter.solidstate;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Solid state physics calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SolidStatePhysics {

    /** Planck constant (JÃ‚Â·s) */
    public static final Real H = Real.of(6.62607015e-34);

    /** Reduced Planck constant */
    public static final Real HBAR = Real.of(1.054571817e-34);

    /** Boltzmann constant (J/K) */
    public static final Real K_B = Real.of(1.380649e-23);

    /** Electron mass (kg) */
    public static final Real M_E = Real.of(9.1093837015e-31);

    /** Elementary charge (C) */
    public static final Real E = Real.of(1.602176634e-19);

    /** One third exponent */
    private static final Real ONE_THIRD = Real.ONE.divide(Real.of(3));
    /** Exponent threshold for numerical stability */
    private static final Real EXP_THRESHOLD = Real.of(50);

    /**
     * Fermi energy for free electron gas.
     * E_F = (Ã¢â€žÂÃ‚Â²/2m) * (3Ãâ‚¬Ã‚Â²n)^(2/3)
     */
    public static Real fermiEnergy(Real electronDensity) {
        Real kF = Real.of(3).multiply(Real.PI.pow(2)).multiply(electronDensity).pow(ONE_THIRD);
        return HBAR.pow(2).multiply(kF.pow(2)).divide(Real.TWO.multiply(M_E));
    }

    /**
     * Fermi velocity.
     * v_F = Ã¢â€žÂk_F / m
     */
    public static Real fermiVelocity(Real electronDensity) {
        Real kF = Real.of(3).multiply(Real.PI.pow(2)).multiply(electronDensity).pow(ONE_THIRD);
        return HBAR.multiply(kF).divide(M_E);
    }

    /**
     * Density of states at Fermi level (3D free electrons).
     */
    public static Real densityOfStates(Real electronDensity, Real fermiEnergy) {
        return Real.of(1.5).multiply(electronDensity).divide(fermiEnergy);
    }

    /**
     * Fermi-Dirac distribution.
     */
    public static Real fermiDirac(Real energy, Real mu, Real temperature) {
        Real x = energy.subtract(mu).divide(K_B.multiply(temperature));
        if (x.compareTo(EXP_THRESHOLD) > 0)
            return Real.ZERO;
        if (x.compareTo(EXP_THRESHOLD.negate()) < 0)
            return Real.ONE;
        return Real.ONE.divide(x.exp().add(Real.ONE));
    }

    /**
     * Debye frequency.
     */
    public static Real debyeFrequency(Real soundVelocity, Real atomDensity) {
        return soundVelocity.multiply(Real.of(6).multiply(Real.PI.pow(2)).multiply(atomDensity).pow(ONE_THIRD));
    }

    /**
     * Debye temperature.
     */
    public static Real debyeTemperature(Real debyeFrequency) {
        return HBAR.multiply(debyeFrequency).divide(K_B);
    }

    /**
     * Phonon energy.
     */
    public static Real phononEnergy(Real frequency) {
        return HBAR.multiply(frequency);
    }

    /**
     * Bose-Einstein distribution for phonons.
     */
    public static Real boseEinstein(Real frequency, Real temperature) {
        Real x = HBAR.multiply(frequency).divide(K_B.multiply(temperature));
        if (x.compareTo(EXP_THRESHOLD) > 0)
            return Real.ZERO;
        return Real.ONE.divide(x.exp().subtract(Real.ONE));
    }

    /**
     * Band gap from semiconductor conductivity temperature dependence.
     */
    public static Real conductivityFromBandGap(Real sigma0, Real bandGap, Real temperature) {
        return sigma0.multiply(bandGap.negate().divide(Real.TWO.multiply(K_B).multiply(temperature)).exp());
    }

    /**
     * Intrinsic carrier concentration.
     */
    public static Real intrinsicCarrierConcentration(Real Nc, Real Nv, Real bandGap, Real temperature) {
        return Nc.multiply(Nv).sqrt().multiply(
                bandGap.negate().divide(Real.TWO.multiply(K_B).multiply(temperature)).exp());
    }

    /**
     * Effective mass from band curvature.
     */
    public static Real effectiveMass(Real bandCurvature) {
        return HBAR.pow(2).divide(bandCurvature);
    }

    /**
     * Hall coefficient.
     */
    public static Real hallCoefficient(Real carrierDensity, boolean isElectron) {
        Real sign = isElectron ? Real.ONE.negate() : Real.ONE;
        return sign.divide(carrierDensity.multiply(E));
    }

    /**
     * Mobility from conductivity and carrier density.
     */
    public static Real mobility(Real conductivity, Real carrierDensity) {
        return conductivity.divide(carrierDensity.multiply(E));
    }

    /**
     * Drude conductivity.
     */
    public static Real drudeConductivity(Real carrierDensity, Real relaxationTime, Real effectiveMass) {
        return carrierDensity.multiply(E.pow(2)).multiply(relaxationTime).divide(effectiveMass);
    }

    // --- Common semiconductor band gaps (eV) ---
    public static final Real BANDGAP_SI = Real.of(1.12);
    public static final Real BANDGAP_GE = Real.of(0.67);
    public static final Real BANDGAP_GAAS = Real.of(1.42);
    public static final Real BANDGAP_GAN = Real.of(3.4);
    public static final Real BANDGAP_INP = Real.of(1.35);
}


