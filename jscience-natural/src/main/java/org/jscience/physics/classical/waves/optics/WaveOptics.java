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
package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Utilities for wave optics (diffraction, interference, polarization).
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WaveOptics {

    private WaveOptics() {
    } // Utility class

    /**
     * Calculates intensity pattern for single-slit diffraction.
     * I = I₀ [sin(β)/β]² where β = (π a sin θ) / λ
     * 
     * @param theta      Diffraction angle (radians)
     * @param slitWidth  Width of the slit (a)
     * @param wavelength Wavelength of light (λ)
     * @param i0         Central intensity (I₀)
     * @return Intensity at angle θ
     */
    public static Real singleSlitIntensity(Real theta, Real slitWidth, Real wavelength, Real i0) {
        if (theta.compareTo(Real.ZERO) == 0)
            return i0;

        Real beta = Real.PI.multiply(slitWidth).multiply(theta.sin()).divide(wavelength);

        if (beta.compareTo(Real.ZERO) == 0)
            return i0;

        Real sinc = beta.sin().divide(beta);
        return i0.multiply(sinc.pow(2));
    }

    /**
     * Calculates intensity pattern for double-slit interference (Young's
     * experiment).
     * I = I₀ cos²(δ/2) [diffraction factor neglected for pure interference]
     * δ = (2π d sin θ) / λ
     * 
     * @param theta          Viewing angle (radians)
     * @param slitSeparation Distance between slits (d)
     * @param wavelength     Wavelength of light (λ)
     * @param i0             Maximum intensity (I₀)
     * @return Intensity at angle θ
     */
    public static Real doubleSlitIntensity(Real theta, Real slitSeparation, Real wavelength, Real i0) {
        Real delta = Real.TWO.multiply(Real.PI).multiply(slitSeparation).multiply(theta.sin()).divide(wavelength);
        Real term = delta.divide(Real.TWO).cos();
        return i0.multiply(term.pow(2));
    }

    /**
     * Malus's Law for polarization.
     * I = I₀ cos²(θ)
     * 
     * @param i0    Initial intensity
     * @param angle Angle between polarizer axis and plane of polarization
     * @return Transmitted intensity
     */
    public static Real polarizationMalusLaw(Real i0, Real angle) {
        return i0.multiply(angle.cos().pow(2));
    }

    /**
     * Calculates intensity for diffraction grating.
     * I = I₀ [sin(Nβ/2) / sin(β/2)]² * [sin(α)/α]²
     * where α = πa·sinθ/λ, β = πd·sinθ/λ
     * 
     * @param theta          Diffraction angle
     * @param slitWidth      Width of each slit (a)
     * @param slitSeparation Distance between slits (d)
     * @param wavelength     Wavelength of light
     * @param numSlits       Number of slits (N)
     * @param i0             Central intensity
     * @return Intensity at angle theta
     */
    public static Real diffractionGratingIntensity(Real theta, Real slitWidth, Real slitSeparation,
            Real wavelength, int numSlits, Real i0) {
        Real sinTheta = theta.sin();

        // Single slit envelope
        Real alpha = Real.PI.multiply(slitWidth).multiply(sinTheta).divide(wavelength);
        Real singleSlitFactor = alpha.isZero() ? Real.ONE : alpha.sin().divide(alpha);

        // Grating interference
        Real beta = Real.PI.multiply(slitSeparation).multiply(sinTheta).divide(wavelength);
        Real betaHalf = beta.divide(Real.TWO);
        Real betaHalfSin = betaHalf.sin();
        Real nBetaHalf = beta.multiply(Real.of(numSlits)).divide(Real.TWO);

        Real gratingFactor = betaHalfSin.isZero() ? Real.of(numSlits) : nBetaHalf.sin().divide(betaHalfSin);

        return i0.multiply(singleSlitFactor.pow(2)).multiply(gratingFactor.pow(2));
    }

    /**
     * Airy disk pattern for circular aperture diffraction.
     * I = I₀ [2J₁(x)/x]² where x = πD·sinθ/λ
     * Uses approximation for J₁.
     * 
     * @param theta      Angle from optical axis
     * @param diameter   Aperture diameter (D)
     * @param wavelength Wavelength of light
     * @param i0         Central intensity
     * @return Intensity at angle theta
     */
    /** Threshold for near-zero detection */
    private static final Real EPSILON = Real.of(1e-10);

    public static Real airyDiskIntensity(Real theta, Real diameter, Real wavelength, Real i0) {
        Real x = Real.PI.multiply(diameter).multiply(theta.sin()).divide(wavelength);

        if (x.abs().compareTo(EPSILON) < 0) {
            return i0; // Center of pattern
        }

        // Bessel J1 approximation
        Real j1Approx = besselJ1Approx(x);
        Real airyFactor = j1Approx.multiply(Real.TWO).divide(x);

        return i0.multiply(airyFactor.pow(2));
    }

    /**
     * Approximate Bessel function J₁(x) using polynomial series.
     */
    private static Real besselJ1Approx(Real x) {
        double xd = x.doubleValue();
        double ax = Math.abs(xd);

        if (ax < 8.0) {
            double y = xd * xd;
            double ans1 = xd * (72362614232.0 + y * (-7895059235.0 + y * (242396853.1
                    + y * (-2972611.439 + y * (15704.48260 + y * (-30.16036606))))));
            double ans2 = 144725228442.0 + y * (2300535178.0 + y * (18583304.74
                    + y * (99447.43394 + y * (376.9991397 + y * 1.0))));
            return Real.of(ans1 / ans2);
        } else {
            double z = 8.0 / ax;
            double y = z * z;
            double xx = ax - 2.356194491;
            double ans1 = 1.0 + y * (0.183105e-2 + y * (-0.3516396496e-4
                    + y * (0.2457520174e-5 + y * (-0.240337019e-6))));
            double ans2 = 0.04687499995 + y * (-0.2002690873e-3
                    + y * (0.8449199096e-5 + y * (-0.88228987e-6
                            + y * 0.105787412e-6)));
            double ans = Math.sqrt(0.636619772 / ax) * (Math.cos(xx) * ans1 - z * Math.sin(xx) * ans2);
            return Real.of(xd < 0.0 ? -ans : ans);
        }
    }

    /**
     * Calculates the resolving power of a grating.
     * R = λ/Δλ = mN where m is order and N is number of slits.
     */
    public static Real gratingResolvingPower(int diffractionOrder, int numSlits) {
        return Real.of(diffractionOrder * numSlits);
    }
}
