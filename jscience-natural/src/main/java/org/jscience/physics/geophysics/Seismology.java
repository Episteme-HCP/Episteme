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

package org.jscience.physics.geophysics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Seismology calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Seismology {

    /**
     * Richter magnitude from seismogram amplitude.
     * M_L = log10(A) - log10(A0(δ))
     */
    public static Real richterMagnitude(Real amplitude, Real distanceKm) {
        Real logA0 = Real.ONE.add(Real.of(0.00189).multiply(distanceKm)).subtract(Real.TWO);
        return amplitude.log10().subtract(logA0);
    }

    /**
     * Moment magnitude from seismic moment.
     * M_w = (2/3) * log10(M0) - 10.7
     */
    public static Real momentMagnitude(Real seismicMoment) {
        return Real.of(2.0 / 3.0).multiply(seismicMoment.log10()).subtract(Real.of(10.7));
    }

    /**
     * Energy released from magnitude.
     * log10(E) = 1.5*M + 4.8 (E in joules)
     */
    public static Real energyFromMagnitude(Real magnitude) {
        return Real.of(10).pow(Real.of(1.5).multiply(magnitude).add(Real.of(4.8)));
    }

    /** P-wave velocity in Earth's crust. Vp ≈ 6 km/s */
    public static final Real VP_CRUST = Real.of(6.0);

    /** S-wave velocity (typically Vp/1.73). */
    public static final Real VS_CRUST = Real.of(3.5);

    /**
     * Estimates distance from P-S time difference.
     * d = Δt / (1/Vs - 1/Vp)
     */
    public static Real distanceFromPSTime(Real deltaT_seconds) {
        return deltaT_seconds.divide(VS_CRUST.inverse().subtract(VP_CRUST.inverse()));
    }

    /** Travel time for P-wave. */
    public static Real pWaveTravelTime(Real distanceKm) {
        return distanceKm.divide(VP_CRUST);
    }

    /** Travel time for S-wave. */
    public static Real sWaveTravelTime(Real distanceKm) {
        return distanceKm.divide(VS_CRUST);
    }

    /**
     * Gutenberg-Richter relation: frequency-magnitude.
     * log10(N) = a - b*M
     */
    public static Real gutenbergRichter(Real magnitude, Real a, Real b) {
        return Real.of(10).pow(a.subtract(b.multiply(magnitude)));
    }

    /**
     * Modified Mercalli Intensity from magnitude and distance.
     */
    public static int mercalliIntensity(Real magnitude, Real distanceKm) {
        Real intensity = Real.of(1.5).multiply(magnitude)
                .subtract(Real.of(2.5).multiply(distanceKm.add(Real.ONE).log10()))
                .add(Real.TWO);
        return (int) Math.round(Math.max(1, Math.min(12, intensity.doubleValue())));
    }
}
