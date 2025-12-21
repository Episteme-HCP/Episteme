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
package org.jscience.physics.classical.spectroscopy;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Blackbody radiation and spectral analysis.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpectralAnalysis {

    public static final Real H = Real.of(6.62607015e-34); // Planck constant
    public static final Real C = Real.of(299792458.0); // Speed of light
    public static final Real K = Real.of(1.380649e-23); // Boltzmann constant
    public static final Real WIEN_B = Real.of(2.897771955e-3); // Wien constant
    public static final Real SIGMA = Real.of(5.670374419e-8); // Stefan-Boltzmann

    private SpectralAnalysis() {
    }

    /** Wien's displacement law: λ_max = b / T */
    public static Real wienPeakWavelength(Real temperatureKelvin) {
        return WIEN_B.divide(temperatureKelvin);
    }

    /** Stefan-Boltzmann: P = σ * A * T⁴ */
    public static Real stefanBoltzmannPower(Real areaM2, Real temperatureKelvin) {
        return SIGMA.multiply(areaM2).multiply(temperatureKelvin.pow(4));
    }

    /** Planck radiance: B(λ,T) = (2hc²/λ⁵) / (exp(hc/λkT) - 1) */
    public static Real planckRadiance(Real wavelengthMeters, Real temperatureKelvin) {
        Real numerator = Real.TWO.multiply(H).multiply(C.pow(2)).divide(wavelengthMeters.pow(5));
        Real exponent = H.multiply(C).divide(wavelengthMeters.multiply(K).multiply(temperatureKelvin));
        Real denominator = exponent.exp().subtract(Real.ONE);
        return numerator.divide(denominator);
    }

    /** Photon energy: E = hc/λ */
    public static Real photonEnergy(Real wavelengthMeters) {
        return H.multiply(C).divide(wavelengthMeters);
    }

    /** Wavelength from energy: λ = hc/E */
    public static Real wavelengthFromEnergy(Real energyJoules) {
        return H.multiply(C).divide(energyJoules);
    }

    /** Color temperature from peak wavelength */
    public static Real colorTemperature(Real peakWavelengthMeters) {
        return WIEN_B.divide(peakWavelengthMeters);
    }
}
