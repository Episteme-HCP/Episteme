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

package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Bloch Sphere representation for single-qubit quantum states.
 * <p>
 * A pure qubit state $|\psi\rangle = \cos(\theta/2)|0\rangle +
 * e^{i\phi}\sin(\theta/2)|1\rangle$
 * is represented as a point on the unit sphere.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BlochSphere {

    /**
     * Converts qubit amplitudes to Bloch sphere coordinates.
     * 
     * @param alpha Amplitude of |0⟩ state
     * @param beta  Amplitude of |1⟩ state
     * @return Real[3] = {x, y, z} on unit sphere
     */
    public static Real[] toBlochCoordinates(Complex alpha, Complex beta) {
        // Ensure normalization
        Real absAlpha = alpha.abs();
        Real absBeta = beta.abs();
        Real normSq = absAlpha.multiply(absAlpha).add(absBeta.multiply(absBeta));
        
        if (normSq.subtract(Real.ONE).abs().compareTo(Real.of(1e-9)) > 0) {
            Real norm = normSq.sqrt();
            alpha = Complex.of(alpha.getReal().divide(norm), alpha.getImaginary().divide(norm));
            beta = Complex.of(beta.getReal().divide(norm), beta.getImaginary().divide(norm));
            absAlpha = alpha.abs();
            absBeta = beta.abs();
        }

        // theta: angle from +z axis
        // cos(theta/2) = abs(alpha)
        // sin(theta/2) = abs(beta) (if pure state)
        // But better: theta = 2 * atan2(|beta|, |alpha|)
        Real theta = Real.of(2).multiply(absBeta.atan2(absAlpha));

        // phi: azimuthal angle = arg(beta) - arg(alpha)
        Real argAlpha = alpha.arg();
        Real argBeta = beta.arg();
        Real phi = argBeta.subtract(argAlpha);

        // Bloch coordinates
        Real x = theta.sin().multiply(phi.cos());
        Real y = theta.sin().multiply(phi.sin());
        Real z = theta.cos();

        return new Real[] { x, y, z };
    }

    /**
     * Converts Bloch sphere coordinates back to qubit state.
     * 
     * @param theta Polar angle (0 to π)
     * @param phi   Azimuthal angle (0 to 2π)
     * @return Complex[2] = {alpha, beta}
     */
    public static Complex[] fromBlochAngles(Real theta, Real phi) {
        Real halfTheta = theta.divide(Real.of(2));
        Complex alpha = Complex.of(halfTheta.cos(), Real.ZERO);
        // e^(i*phi) * sin(theta/2)
        // = (cos(phi) + i sin(phi)) * sin(theta/2)
        Real s = halfTheta.sin();
        Complex phase = Complex.of(phi.cos(), phi.sin());
        Complex beta = phase.multiply(Complex.of(s));
        
        return new Complex[] { alpha, beta };
    }

    /**
     * Special states on the Bloch sphere (Coordinates).
     */
    public static final Real[] STATE_ZERO = { Real.ZERO, Real.ZERO, Real.ONE }; // |0⟩ = +z
    public static final Real[] STATE_ONE = { Real.ZERO, Real.ZERO, Real.ONE.negate() }; // |1⟩ = -z
    public static final Real[] STATE_PLUS = { Real.ONE, Real.ZERO, Real.ZERO }; // |+⟩ = +x
    public static final Real[] STATE_MINUS = { Real.ONE.negate(), Real.ZERO, Real.ZERO }; // |-⟩ = -x
    public static final Real[] STATE_PLUS_I = { Real.ZERO, Real.ONE, Real.ZERO }; // |+i⟩ = +y
    public static final Real[] STATE_MINUS_I = { Real.ZERO, Real.ONE.negate(), Real.ZERO }; // |-i⟩ = -y
}
