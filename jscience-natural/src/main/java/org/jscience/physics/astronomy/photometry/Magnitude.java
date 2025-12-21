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
package org.jscience.physics.astronomy.photometry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Stellar magnitude calculations and conversions.
 * 
 * Apparent magnitude (m): Brightness as seen from Earth.
 * Absolute magnitude (M): Brightness at 10 parsecs distance.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Magnitude {

    private Magnitude() {
    } // Utility class

    /**
     * Converts apparent magnitude and distance to absolute magnitude.
     * M = m - 5 * log10(d) + 5
     */
    public static Real toAbsolute(Real apparentMag, Real distancePc) {
        return apparentMag.subtract(Real.of(5).multiply(distancePc.log10())).add(Real.of(5));
    }

    /**
     * Converts absolute magnitude and distance to apparent magnitude.
     * m = M + 5 * log10(d) - 5
     */
    public static Real toApparent(Real absoluteMag, Real distancePc) {
        return absoluteMag.add(Real.of(5).multiply(distancePc.log10())).subtract(Real.of(5));
    }

    /**
     * Calculates distance modulus.
     * μ = m - M = 5 * log10(d) - 5
     */
    public static Real distanceModulus(Real apparentMag, Real absoluteMag) {
        return apparentMag.subtract(absoluteMag);
    }

    /**
     * Calculates distance from distance modulus.
     * d = 10^((μ + 5) / 5)
     */
    public static Real distanceFromModulus(Real distanceModulus) {
        return Real.of(10).pow(distanceModulus.add(Real.of(5)).divide(Real.of(5)));
    }

    /**
     * Combines magnitudes of multiple sources.
     * m_total = -2.5 * log10(Σ 10^(-m_i/2.5))
     */
    public static Real combine(Real... magnitudes) {
        Real totalFlux = Real.ZERO;
        for (Real m : magnitudes) {
            totalFlux = totalFlux.add(Real.of(10).pow(m.negate().divide(Real.of(2.5))));
        }
        return Real.of(-2.5).multiply(totalFlux.log10());
    }

    /**
     * Calculates flux ratio from magnitude difference.
     * F1/F2 = 10^((m2 - m1) / 2.5)
     */
    public static Real fluxRatio(Real mag1, Real mag2) {
        return Real.of(10).pow(mag2.subtract(mag1).divide(Real.of(2.5)));
    }

    /**
     * Converts magnitude difference to flux ratio.
     */
    public static Real deltaMagToFluxRatio(Real deltaMag) {
        return Real.of(10).pow(deltaMag.negate().divide(Real.of(2.5)));
    }

    /**
     * Converts B-V color index to effective temperature (approximate).
     * Using Ballesteros formula.
     */
    public static Real colorToTemperature(Real bMinusV) {
        // T = 4600 * (1/(0.92*BV + 1.7) + 1/(0.92*BV + 0.62))
        Real term1 = Real.ONE.divide(Real.of(0.92).multiply(bMinusV).add(Real.of(1.7)));
        Real term2 = Real.ONE.divide(Real.of(0.92).multiply(bMinusV).add(Real.of(0.62)));
        return Real.of(4600).multiply(term1.add(term2));
    }

    /**
     * Converts absolute magnitude to luminosity (solar units).
     * L/L☉ = 10^((M☉ - M) / 2.5)
     */
    public static Real toLuminosity(Real absoluteMag) {
        return Real.of(10).pow(SUN_ABSOLUTE.subtract(absoluteMag).divide(Real.of(2.5)));
    }

    /**
     * Converts luminosity to absolute magnitude.
     * M = M☉ - 2.5 * log10(L/L☉)
     */
    public static Real fromLuminosity(Real luminositySolar) {
        return SUN_ABSOLUTE.subtract(Real.of(2.5).multiply(luminositySolar.log10()));
    }

    // --- Reference magnitudes ---

    /** Sun's apparent visual magnitude */
    public static final Real SUN_APPARENT = Real.of(-26.74);

    /** Sun's absolute visual magnitude */
    public static final Real SUN_ABSOLUTE = Real.of(4.83);

    /** Full Moon apparent magnitude */
    public static final Real FULL_MOON = Real.of(-12.74);

    /** Sirius apparent magnitude */
    public static final Real SIRIUS = Real.of(-1.46);

    /** Vega apparent magnitude (defines magnitude zero point) */
    public static final Real VEGA = Real.of(0.03);
}
