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

package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Quantum Harmonic Oscillator calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantumHarmonicOscillator {

    public static final Real HBAR = Real.of(1.054571817e-34); // Reduced Planck constant (JÃ‚Â·s)

    private QuantumHarmonicOscillator() {
    }

    /** Energy eigenvalues: E_n = Ã¢â€žÂÃâ€°(n + 1/2) */
    public static Real energyLevel(int n, Real omega) {
        return HBAR.multiply(omega).multiply(Real.of(n + 0.5));
    }

    /** Angular frequency: Ãâ€° = sqrt(k/m) */
    public static Real angularFrequency(Real springConstant, Real mass) {
        return springConstant.divide(mass).sqrt();
    }

    /** Ground state energy: E_0 = Ã¢â€žÂÃâ€°/2 */
    public static Real groundStateEnergy(Real omega) {
        return HBAR.multiply(omega).divide(Real.TWO);
    }

    /** Transition energy between levels */
    public static Real transitionEnergy(int n1, int n2, Real omega) {
        return energyLevel(n2, omega).subtract(energyLevel(n1, omega)).abs();
    }

    /** Classical amplitude: A = sqrt(2E/(mÃâ€°Ã‚Â²)) */
    public static Real classicalAmplitude(Real energy, Real mass, Real omega) {
        return Real.TWO.multiply(energy).divide(mass.multiply(omega.pow(2))).sqrt();
    }

    /** Probability density at x=0 for ground state: |ÃË†_0(0)|Ã‚Â² = sqrt(mÃâ€°/(Ãâ‚¬Ã¢â€žÂ)) */
    public static Real groundStateProbabilityAt0(Real mass, Real omega) {
        return mass.multiply(omega).divide(Real.PI.multiply(HBAR)).sqrt();
    }

    /** Zero-point motion: ÃŽâ€x_0 = sqrt(Ã¢â€žÂ/(2mÃâ€°)) */
    public static Real zeroPointMotion(Real mass, Real omega) {
        return HBAR.divide(Real.TWO.multiply(mass).multiply(omega)).sqrt();
    }
}


