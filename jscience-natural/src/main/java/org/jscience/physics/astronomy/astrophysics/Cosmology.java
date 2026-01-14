/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.astronomy.astrophysics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Cosmology calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Cosmology {

    /** Speed of light (km/s) */
    public static final Real C_KM_S = Real.of(299792.458);

    /** Hubble constant (km/s/Mpc) - Planck 2018 */
    public static final Real H0_PLANCK = Real.of(67.4);

    /** Critical density (kg/mÃ‚Â³) for H0 = 67.4 */
    public static final Real RHO_CRIT = Real.of(8.5e-27);

    /** Age of universe (seconds) */
    public static final Real AGE_UNIVERSE = Real.of(4.35e17);

    /** Present density parameters (Planck 2018) */
    public static final Real OMEGA_M = Real.of(0.315);
    public static final Real OMEGA_LAMBDA = Real.of(0.685);
    public static final Real OMEGA_R = Real.of(9.2e-5);

    /**
     * Hubble's law: recession velocity.
     * v = H0 * d
     */
    public static Real recessionVelocity(Real distanceMpc, Real H0) {
        return H0.multiply(distanceMpc);
    }

    /**
     * Hubble distance from redshift (low-z approximation).
     * d = cz / H0
     */
    public static Real hubbleDistance(Real z, Real H0) {
        return C_KM_S.multiply(z).divide(H0);
    }

    /**
     * Hubble time (age of universe if expansion were constant).
     * 
     * @return Hubble time in Gyr
     */
    public static Real hubbleTime(Real H0) {
        return Real.of(977.8).divide(H0);
    }

    /**
     * Cosmological redshift to scale factor.
     * a = 1/(1+z)
     */
    public static Real scaleFactor(Real z) {
        return Real.ONE.divide(Real.ONE.add(z));
    }

    /**
     * Scale factor to redshift.
     * z = 1/a - 1
     */
    public static Real redshiftFromScaleFactor(Real a) {
        return Real.ONE.divide(a).subtract(Real.ONE);
    }

    /**
     * Lookback time (simplified, matter-dominated).
     */
    public static Real lookbackTime(Real z, Real H0) {
        Real tH = hubbleTime(H0);
        return tH.multiply(Real.ONE.subtract(Real.ONE.add(z).pow(Real.of(-1.5))));
    }

    /**
     * Comoving distance (simplified integral for ÃŽâ€ºCDM).
     */
    public static Real comovingDistance(Real z, Real H0, Real omegaM, Real omegaLambda) {
        int steps = 1000;
        Real dz = z.divide(Real.of(steps));
        Real dc = Real.ZERO;

        for (int i = 0; i < steps; i++) {
            Real zi = Real.of(i + 0.5).multiply(dz);
            Real onePlusZ = Real.ONE.add(zi);
            Real Ez = omegaM.multiply(onePlusZ.pow(3)).add(omegaLambda).sqrt();
            dc = dc.add(dz.divide(Ez));
        }

        return C_KM_S.multiply(dc).divide(H0);
    }

    /**
     * Luminosity distance.
     * d_L = (1+z) * d_c
     */
    public static Real luminosityDistance(Real z, Real comovingDistance) {
        return Real.ONE.add(z).multiply(comovingDistance);
    }

    /**
     * Angular diameter distance.
     * d_A = d_c / (1+z)
     */
    public static Real angularDiameterDistance(Real z, Real comovingDistance) {
        return comovingDistance.divide(Real.ONE.add(z));
    }

    /**
     * Cosmic microwave background temperature at redshift z.
     * T(z) = T0 * (1+z)
     */
    public static Real cmbTemperature(Real z) {
        Real T0 = Real.of(2.725);
        return T0.multiply(Real.ONE.add(z));
    }

    /**
     * Dark energy equation of state parameter.
     */
    public static Real darkEnergyDensity(Real z, Real w) {
        return Real.ONE.add(z).pow(Real.of(3).multiply(Real.ONE.add(w)));
    }

    /**
     * Friedmann equation: HÃ‚Â²/H0Ã‚Â² = EÃ‚Â²(z)
     */
    public static Real friedmannE(Real z, Real omegaM, Real omegaLambda, Real omegaR) {
        Real onePlusZ = Real.ONE.add(z);
        return omegaM.multiply(onePlusZ.pow(3))
                .add(omegaLambda)
                .add(omegaR.multiply(onePlusZ.pow(4)))
                .sqrt();
    }

    /**
     * Deceleration parameter at present.
     * q0 = ÃŽÂ©m/2 - ÃŽÂ©ÃŽâ€º
     */
    public static Real decelerationParameter(Real omegaM, Real omegaLambda) {
        return omegaM.divide(Real.TWO).subtract(omegaLambda);
    }
}


