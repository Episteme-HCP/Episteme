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

/**
 * General wave mechanics - traveling waves, standing waves, interference.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Waves {

    /** Wave equation: y(x,t) = A sin(kx - ωt + φ) */
    public static Real waveAmplitude(Real amplitude, Real wavenumber, Real angularFreq,
            Real position, Real time, Real phase) {
        Real arg = wavenumber.multiply(position).subtract(angularFreq.multiply(time)).add(phase);
        return amplitude.multiply(arg.sin());
    }

    /** Wave speed: v = λf */
    public static Real waveSpeed(Real wavelength, Real frequency) {
        return wavelength.multiply(frequency);
    }

    /** Angular frequency: ω = 2πf */
    public static Real angularFrequency(Real frequency) {
        return Real.TWO_PI.multiply(frequency);
    }

    /** Wavenumber: k = 2π/λ */
    public static Real wavenumber(Real wavelength) {
        return Real.TWO_PI.divide(wavelength);
    }

    /** Standing wave: y(x,t) = 2A sin(kx) cos(ωt) */
    public static Real standingWave(Real amplitude, Real wavenumber, Real angularFreq,
            Real position, Real time) {
        Real spatial = wavenumber.multiply(position).sin();
        Real temporal = angularFreq.multiply(time).cos();
        return amplitude.multiply(Real.TWO).multiply(spatial).multiply(temporal);
    }

    /** Group velocity: v_g = dω/dk */
    public static Real groupVelocity(Real omega1, Real omega2, Real k1, Real k2) {
        return omega2.subtract(omega1).divide(k2.subtract(k1));
    }

    /** Phase velocity: v_p = ω/k */
    public static Real phaseVelocity(Real angularFreq, Real wavenumber) {
        return angularFreq.divide(wavenumber);
    }

    /** Interference amplitude: A = √(A₁² + A₂² + 2A₁A₂cos(Δφ)) */
    public static Real interferenceAmplitude(Real amp1, Real amp2, Real phaseDiff) {
        Real a1sq = amp1.pow(2);
        Real a2sq = amp2.pow(2);
        Real crossTerm = Real.TWO.multiply(amp1).multiply(amp2).multiply(phaseDiff.cos());
        return a1sq.add(a2sq).add(crossTerm).sqrt();
    }

    /** Wave energy density: u = ½ρω²A² */
    public static Real waveEnergyDensity(Real density, Real angularFreq, Real amplitude) {
        return Real.of(0.5).multiply(density).multiply(angularFreq.pow(2)).multiply(amplitude.pow(2));
    }

    /** Wave intensity: I = ½ρvω²A² */
    public static Real waveIntensity(Real density, Real velocity, Real angularFreq, Real amplitude) {
        return Real.of(0.5).multiply(density).multiply(velocity)
                .multiply(angularFreq.pow(2)).multiply(amplitude.pow(2));
    }
}
