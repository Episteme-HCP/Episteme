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

package org.jscience.physics.classical.waves;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.complex.Complex;

/**
 * Represents a general wave: $\psi(x,t) = A \cos(kx - \omega t + \phi)$.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Wave {

    private Real amplitude; // A
    private Real waveNumber; // k = 2Ãâ‚¬/ÃŽÂ»
    private Real angularFrequency; // Ãâ€° = 2Ãâ‚¬f
    private Real phase; // Ãâ€ 

    public Wave(Real amplitude, Real waveNumber, Real angularFrequency, Real phase) {
        this.amplitude = amplitude;
        this.waveNumber = waveNumber;
        this.angularFrequency = angularFrequency;
        this.phase = phase;
    }

    /**
     * Wavelength: $\lambda = 2\pi / k$
     */
    public Real getWavelength() {
        return Real.of(2.0 * Math.PI).divide(waveNumber);
    }

    /**
     * Frequency: $f = \omega / 2\pi$
     */
    public Real getFrequency() {
        return angularFrequency.divide(Real.of(2.0 * Math.PI));
    }

    /**
     * Period: $T = 1/f$
     */
    public Real getPeriod() {
        return Real.ONE.divide(getFrequency());
    }

    /**
     * Phase velocity: $v_p = \omega / k$
     */
    public Real getPhaseVelocity() {
        return angularFrequency.divide(waveNumber);
    }

    /**
     * Wave value at position x and time t.
     * $\psi(x,t) = A \cos(kx - \omega t + \phi)$
     */
    public Real evaluate(Real x, Real t) {
        Real arg = waveNumber.multiply(x)
                .subtract(angularFrequency.multiply(t))
                .add(phase);
        return amplitude.multiply(arg.cos());
    }

    /**
     * Complex phasor representation: $A e^{i(kx - \omega t + \phi)}$
     */
    public Complex phasor(Real x, Real t) {
        Real arg = waveNumber.multiply(x)
                .subtract(angularFrequency.multiply(t))
                .add(phase);
        return Complex.ofPolar(amplitude.doubleValue(), arg.doubleValue());
    }

    // --- Superposition ---

    /**
     * Superposition of two waves (same k, Ãâ€° assumed for simplicity).
     */
    public static Wave superpose(Wave w1, Wave w2) {
        Real newAmplitude = w1.amplitude.add(w2.amplitude);
        return new Wave(newAmplitude, w1.waveNumber, w1.angularFrequency, w1.phase);
    }

    // --- Accessors ---
    public Real getAmplitude() {
        return amplitude;
    }

    public Real getWaveNumber() {
        return waveNumber;
    }

    public Real getAngularFrequency() {
        return angularFrequency;
    }

    public Real getPhase() {
        return phase;
    }
}


