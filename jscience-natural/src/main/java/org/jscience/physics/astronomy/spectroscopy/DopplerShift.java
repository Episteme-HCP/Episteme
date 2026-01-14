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

package org.jscience.physics.astronomy.spectroscopy;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Doppler shift and redshift calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DopplerShift {

    /** Speed of light in km/s */
    public static final Real C_KM_S = Real.of(299792.458);
    /** Hubble constant (Planck 2018) */
    public static final Real H0_PLANCK = Real.of(67.4);

    // --- Spectral lines (Angstroms) ---
    public static final Real H_ALPHA = Real.of(6562.8);
    public static final Real H_BETA = Real.of(4861.3);
    public static final Real CA_K = Real.of(3933.7);
    public static final Real CA_H = Real.of(3968.5);
    public static final Real NA_D1 = Real.of(5895.9);
    public static final Real NA_D2 = Real.of(5889.9);

    private DopplerShift() {
    }

    /** Radial velocity: v = c * (ÃŽÂ»_obs - ÃŽÂ»_rest) / ÃŽÂ»_rest */
    public static Real radialVelocity(Real observedWavelength, Real restWavelength) {
        return C_KM_S.multiply(observedWavelength.subtract(restWavelength)).divide(restWavelength);
    }

    /** Redshift: z = (ÃŽÂ»_obs - ÃŽÂ»_rest) / ÃŽÂ»_rest */
    public static Real redshift(Real observedWavelength, Real restWavelength) {
        return observedWavelength.subtract(restWavelength).divide(restWavelength);
    }

    /** Redshift to velocity */
    public static Real redshiftToVelocity(Real z, boolean relativistic) {
        if (relativistic) {
            Real zp1 = Real.ONE.add(z);
            Real zp1Sq = zp1.pow(2);
            return C_KM_S.multiply(zp1Sq.subtract(Real.ONE)).divide(zp1Sq.add(Real.ONE));
        }
        return C_KM_S.multiply(z);
    }

    /** Velocity to redshift */
    public static Real velocityToRedshift(Real velocityKmS, boolean relativistic) {
        Real beta = velocityKmS.divide(C_KM_S);
        if (relativistic) {
            return Real.ONE.add(beta).divide(Real.ONE.subtract(beta)).sqrt().subtract(Real.ONE);
        }
        return beta;
    }

    /** Observed wavelength from rest wavelength and velocity */
    public static Real observedWavelength(Real restWavelength, Real velocityKmS) {
        return restWavelength.multiply(Real.ONE.add(velocityKmS.divide(C_KM_S)));
    }

    /** Hubble distance: d = c * z / H0 (Mpc) */
    public static Real hubbleDistance(Real z, Real H0) {
        return C_KM_S.multiply(z).divide(H0);
    }
}


