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
 * Black hole physics calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BlackHole {

    /** Gravitational constant (mÃ‚Â³/(kgÃ‚Â·sÃ‚Â²)) */
    public static final Real G = Real.of(6.67430e-11);

    /** Speed of light (m/s) */
    public static final Real C = Real.of(2.998e8);

    /** Solar mass (kg) */
    public static final Real M_SUN = Real.of(1.989e30);

    /** Reduced Planck constant */
    private static final Real HBAR = Real.of(1.054571817e-34);

    /** Boltzmann constant */
    private static final Real KB = Real.of(1.380649e-23);

    /**
     * Schwarzschild radius (event horizon for non-rotating black hole).
     * r_s = 2GM/cÃ‚Â²
     */
    public static Real schwarzschildRadius(Real mass) {
        return Real.TWO.multiply(G).multiply(mass).divide(C.multiply(C));
    }

    /**
     * Schwarzschild radius in solar masses.
     * r_s Ã¢â€°Ë† 2.95 km per solar mass
     */
    public static Real schwarzschildRadiusSolarMass(Real solarMasses) {
        return Real.of(2953).multiply(solarMasses);
    }

    /**
     * Gravitational time dilation near Schwarzschild black hole.
     */
    public static Real timeDilation(Real r, Real rs) {
        if (r.compareTo(rs) <= 0)
            return Real.POSITIVE_INFINITY;
        return Real.ONE.divide(Real.ONE.subtract(rs.divide(r)).sqrt());
    }

    /**
     * Gravitational redshift.
     */
    public static Real gravitationalRedshift(Real r, Real rs) {
        if (r.compareTo(rs) <= 0)
            return Real.POSITIVE_INFINITY;
        return Real.ONE.divide(Real.ONE.subtract(rs.divide(r)).sqrt()).subtract(Real.ONE);
    }

    /**
     * Innermost stable circular orbit (ISCO) for Schwarzschild.
     */
    public static Real iscoRadius(Real mass) {
        return Real.of(3).multiply(schwarzschildRadius(mass));
    }

    /**
     * Photon sphere radius (light can orbit).
     */
    public static Real photonSphereRadius(Real mass) {
        return Real.of(1.5).multiply(schwarzschildRadius(mass));
    }

    /**
     * Hawking temperature.
     * T = Ã¢â€žÂcÃ‚Â³ / (8Ãâ‚¬GMk_B)
     */
    public static Real hawkingTemperature(Real mass) {
        Real numerator = HBAR.multiply(C.pow(3));
        Real denominator = Real.of(8 * Math.PI).multiply(G).multiply(mass).multiply(KB);
        return numerator.divide(denominator);
    }

    /**
     * Bekenstein-Hawking entropy.
     */
    public static Real entropy(Real mass) {
        Real rs = schwarzschildRadius(mass);
        Real A = Real.of(4 * Math.PI).multiply(rs.pow(2));
        return KB.multiply(C.pow(3)).multiply(A).divide(Real.of(4).multiply(G).multiply(HBAR));
    }

    /**
     * Kerr black hole outer horizon radius.
     */
    public static Real kerrOuterHorizon(Real mass, Real J) {
        Real rs = schwarzschildRadius(mass);
        Real a = J.divide(mass.multiply(C));
        Real rg = rs.divide(Real.TWO);
        return rg.add(rg.pow(2).subtract(a.pow(2)).sqrt());
    }

    /**
     * Kerr black hole ergosphere radius at equator.
     */
    public static Real kerrErgosphereEquator(Real mass) {
        return schwarzschildRadius(mass);
    }

    /**
     * Black hole luminosity from Hawking radiation.
     */
    public static Real hawkingLuminosity(Real mass) {
        return HBAR.multiply(C.pow(6)).divide(
                Real.of(15360 * Math.PI).multiply(G.pow(2)).multiply(mass.pow(2)));
    }

    /**
     * Black hole evaporation time.
     */
    public static Real evaporationTime(Real mass) {
        return Real.of(5120 * Math.PI).multiply(G.pow(2)).multiply(mass.pow(3))
                .divide(HBAR.multiply(C.pow(4)));
    }
}


