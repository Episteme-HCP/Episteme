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

package org.jscience.physics.classical.matter.plasma;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Plasma physics calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PlasmaPhysics {

    /** Vacuum permittivity (F/m) */
    public static final Real EPSILON_0 = Real.of(8.854187817e-12);

    /** Elementary charge (C) */
    public static final Real E = Real.of(1.602176634e-19);

    /** Electron mass (kg) */
    public static final Real M_E = Real.of(9.1093837015e-31);

    /** Proton mass (kg) */
    public static final Real M_P = Real.of(1.67262192369e-27);

    /** Boltzmann constant (J/K) */
    public static final Real K_B = Real.of(1.380649e-23);

    /** Permeability of free space */
    private static final Real MU_0 = Real.of(4 * Math.PI * 1e-7);

    /**
     * Debye length: characteristic screening distance.
     * λ_D = √(ε₀ k_B T / n e²)
     */
    public static Real debyeLength(Real temperature, Real density) {
        return EPSILON_0.multiply(K_B).multiply(temperature)
                .divide(density.multiply(E.pow(2))).sqrt();
    }

    /**
     * Plasma frequency: natural oscillation frequency.
     * ω_p = √(n e² / ε₀ m_e)
     */
    public static Real plasmaFrequency(Real density) {
        return density.multiply(E.pow(2)).divide(EPSILON_0.multiply(M_E)).sqrt();
    }

    /**
     * Plasma frequency in Hz.
     */
    public static Real plasmaFrequencyHz(Real density) {
        return plasmaFrequency(density).divide(Real.TWO_PI);
    }

    /**
     * Electron cyclotron frequency (gyrofrequency).
     * ω_ce = eB / m_e
     */
    public static Real electronCyclotronFrequency(Real magneticField) {
        return E.multiply(magneticField).divide(M_E);
    }

    /**
     * Ion cyclotron frequency.
     * ω_ci = ZeB / m_i
     */
    public static Real ionCyclotronFrequency(Real magneticField, Real ionMass, int chargeNumber) {
        return Real.of(chargeNumber).multiply(E).multiply(magneticField).divide(ionMass);
    }

    /**
     * Larmor radius (gyroradius).
     * r_L = m v_perp / (eB)
     */
    public static Real larmorRadius(Real velocity, Real magneticField, Real mass) {
        return mass.multiply(velocity).divide(E.multiply(magneticField));
    }

    /**
     * Thermal velocity.
     * v_th = √(k_B T / m)
     */
    public static Real thermalVelocity(Real temperature, Real mass) {
        return K_B.multiply(temperature).divide(mass).sqrt();
    }

    /**
     * Plasma parameter: number of particles in Debye sphere.
     * Λ = n λ_D³
     */
    public static Real plasmaParameter(Real density, Real debyeLength) {
        return density.multiply(debyeLength.pow(3));
    }

    /**
     * Coulomb logarithm (approximate).
     * ln(Λ) ≈ ln(12π n λ_D³)
     */
    public static Real coulombLogarithm(Real density, Real temperature) {
        Real lambdaD = debyeLength(temperature, density);
        return Real.of(12).multiply(Real.PI).multiply(plasmaParameter(density, lambdaD)).log();
    }

    /**
     * Alfvén velocity.
     * v_A = B / √(μ₀ ρ)
     */
    public static Real alfvenVelocity(Real magneticField, Real massDensity) {
        return magneticField.divide(MU_0.multiply(massDensity).sqrt());
    }

    /**
     * Sound speed in plasma.
     * c_s = √(γ k_B T / m_i)
     */
    public static Real soundSpeed(Real temperature, Real ionMass, Real gamma) {
        return gamma.multiply(K_B).multiply(temperature).divide(ionMass).sqrt();
    }

    /**
     * Beta: ratio of plasma pressure to magnetic pressure.
     * β = nkT / (B²/2μ₀)
     */
    public static Real beta(Real density, Real temperature, Real magneticField) {
        Real plasmaPressure = density.multiply(K_B).multiply(temperature);
        Real magneticPressure = magneticField.pow(2).divide(Real.TWO.multiply(MU_0));
        return plasmaPressure.divide(magneticPressure);
    }

    /**
     * Spitzer resistivity.
     */
    public static Real spitzerResistivity(Real temperature, Real coulombLog, int Z) {
        Real num = Real.PI.multiply(Real.of(Z)).multiply(E.pow(2)).multiply(M_E.sqrt()).multiply(coulombLog);
        Real den = Real.of(16).multiply(Real.PI.pow(2)).multiply(EPSILON_0.pow(2))
                .multiply(K_B.multiply(temperature).pow(Real.of(1.5)));
        return num.divide(den);
    }

    /**
     * Collision frequency (electron-ion).
     */
    public static Real collisionFrequency(Real density, Real temperature, Real coulombLog) {
        Real num = density.multiply(E.pow(4)).multiply(coulombLog);
        Real den = Real.of(6).multiply(Real.PI.pow(Real.of(1.5)))
                .multiply(EPSILON_0.pow(2)).multiply(M_E.pow(2))
                .multiply(K_B.multiply(temperature).divide(M_E).pow(Real.of(1.5)));
        return num.divide(den);
    }
}
