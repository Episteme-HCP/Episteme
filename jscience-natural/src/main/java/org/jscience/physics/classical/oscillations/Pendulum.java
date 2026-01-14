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

package org.jscience.physics.classical.oscillations;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Pendulum motion calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Pendulum {

    private Pendulum() {
    }

    /** Simple pendulum period: T = 2Ãâ‚¬ * sqrt(L/g) */
    public static Real simplePendulumPeriod(Real lengthMeters) {
        return Real.TWO_PI.multiply(lengthMeters.divide(PhysicalConstants.g_n).sqrt());
    }

    /** Angular frequency: Ãâ€° = sqrt(g/L) */
    public static Real angularFrequency(Real lengthMeters) {
        return PhysicalConstants.g_n.divide(lengthMeters).sqrt();
    }

    /** Position at time t: ÃŽÂ¸(t) = ÃŽÂ¸Ã¢â€šâ‚¬ * cos(Ãâ€°t + Ãâ€ ) */
    public static Real position(Real amplitude, Real omega, Real time, Real phase) {
        return amplitude.multiply(omega.multiply(time).add(phase).cos());
    }

    /** Damped amplitude: A(t) = AÃ¢â€šâ‚¬ * exp(-ÃŽÂ³t) */
    public static Real dampedAmplitude(Real initialAmplitude, Real dampingCoefficient, Real time) {
        return initialAmplitude.multiply(dampingCoefficient.negate().multiply(time).exp());
    }

    /** Large angle period (elliptic integral approximation) */
    public static Real largeAnglePeriod(Real lengthMeters, Real amplitudeRadians) {
        Real T0 = simplePendulumPeriod(lengthMeters);
        Real theta2 = amplitudeRadians.pow(2);
        Real theta4 = amplitudeRadians.pow(4);
        Real correction = Real.ONE.add(theta2.divide(Real.of(16)))
                .add(Real.of(11).multiply(theta4).divide(Real.of(3072)));
        return T0.multiply(correction);
    }

    /** Potential energy: U = mgL(1 - cos(ÃŽÂ¸)) */
    public static Real potentialEnergy(Real mass, Real lengthMeters, Real angleRadians) {
        return mass.multiply(PhysicalConstants.g_n).multiply(lengthMeters)
                .multiply(Real.ONE.subtract(angleRadians.cos()));
    }

    /** Kinetic energy: KE = 0.5 * m * LÃ‚Â² * Ãâ€°Ã‚Â² */
    public static Real kineticEnergy(Real mass, Real lengthMeters, Real angularVelocity) {
        return Real.of(0.5).multiply(mass).multiply(lengthMeters.pow(2)).multiply(angularVelocity.pow(2));
    }
}


