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

package org.jscience.physics.classical.waves.acoustics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an acoustic wave.
 * <p>
 * Models properties like frequency, amplitude, wavelength, and propagation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AcousticWave {

    private Real frequency; // Hz
    private Real amplitude; // Pa (pressure amplitude)
    private Real speedOfSound; // m/s (medium dependent)

    public AcousticWave(Real frequency, Real amplitude, Real speedOfSound) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.speedOfSound = speedOfSound;
    }

    /**
     * Wavelength: $\lambda = c / f$
     */
    public Real getWavelength() {
        return speedOfSound.divide(frequency);
    }

    /**
     * Angular frequency: $\omega = 2\pi f$
     */
    public Real getAngularFrequency() {
        return frequency.multiply(Real.of(2.0 * Math.PI));
    }

    /**
     * Wave number: $k = 2\pi / \lambda = \omega / c$
     */
    public Real getWaveNumber() {
        return getAngularFrequency().divide(speedOfSound);
    }

    /**
     * Intensity (power per unit area): $I = p^2 / (2 \rho c)$
     * Simplified for air at STP: $\rho \approx 1.2 kg/m^3$
     */
    public Real getIntensity() {
        Real rho = Real.of(1.2); // kg/m^3
        return amplitude.pow(2).divide(rho.multiply(speedOfSound).multiply(Real.of(2.0)));
    }

    /**
     * Sound Pressure Level in dB: $SPL = 20 \log_{10}(p / p_0)$
     * where $p_0 = 20 \mu Pa$ (threshold of hearing)
     */
    public Real getSPL() {
        Real p0 = Real.of(20e-6); // Pa
        return amplitude.divide(p0).log().divide(Real.LN10).multiply(Real.of(20.0));
    }

    // --- Accessors ---
    public Real getFrequency() {
        return frequency;
    }

    public Real getAmplitude() {
        return amplitude;
    }

    public Real getSpeedOfSound() {
        return speedOfSound;
    }
}
