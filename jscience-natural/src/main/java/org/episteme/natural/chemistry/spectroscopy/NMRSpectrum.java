/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.chemistry.spectroscopy;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Nuclear Magnetic Resonance (NMR) spectroscopy models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NMRSpectrum {

    // Common Gyromagnetic Ratios (rad/s/T)
    public static final Real GAMMA_1H = Real.of("267.513e6");
    public static final Real GAMMA_13C = Real.of("67.262e6");
    public static final Real GAMMA_15N = Real.of("-27.116e6");
    public static final Real GAMMA_31P = Real.of("108.291e6");

    private NMRSpectrum() {
    }

    /**
     * Calculates Larmor frequency.
     * v = (gamma * B0) / (2 * pi)
     * 
     * @param gyromagneticRatio gamma (rad s^-1 T^-1)
     * @param magneticField     B0 (Tesla)
     * @return Frequency (Hz)
     */
    public static Real calculateLarmorFrequency(Real gyromagneticRatio, Real magneticField) {
        return gyromagneticRatio.multiply(magneticField).divide(Real.PI.multiply(Real.TWO));
    }

    /**
     * Calculates chemical shift (delta).
     * delta = (v - v_ref) / v_spectrometer * 10^6
     * 
     * @param frequencyObserved     v (Hz)
     * @param frequencyReference    v_ref (Hz, e.g., TMS)
     * @param spectrometerFrequency v_spectrometer (Hz)
     * @return Chemical shift (ppm)
     */
    public static Real calculateChemicalShift(Real frequencyObserved, Real frequencyReference,
            Real spectrometerFrequency) {
        return frequencyObserved.subtract(frequencyReference)
                .divide(spectrometerFrequency)
                .multiply(Real.of(1e6));
    }
}

