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
package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.complex.Complex;

/**
 * Bloch Sphere representation for single-qubit quantum states.
 * <p>
 * A pure qubit state $|\psi\rangle = \cos(\theta/2)|0\rangle +
 * e^{i\phi}\sin(\theta/2)|1\rangle$
 * is represented as a point on the unit sphere.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BlochSphere {

    /**
     * Converts qubit amplitudes to Bloch sphere coordinates.
     * 
     * @param alpha Amplitude of |0⟩ state
     * @param beta  Amplitude of |1⟩ state
     * @return double[3] = {x, y, z} on unit sphere
     */
    public static double[] toBlochCoordinates(Complex alpha, Complex beta) {
        // Ensure normalization - convert Real to double
        double absAlpha = alpha.abs().doubleValue();
        double absBeta = beta.abs().doubleValue();
        double normSq = absAlpha * absAlpha + absBeta * absBeta;
        if (Math.abs(normSq - 1.0) > 1e-9) {
            double norm = Math.sqrt(normSq);
            alpha = Complex.of(alpha.real() / norm, alpha.imaginary() / norm);
            beta = Complex.of(beta.real() / norm, beta.imaginary() / norm);
            absAlpha = alpha.abs().doubleValue();
            absBeta = beta.abs().doubleValue();
        }

        // theta: angle from +z axis
        double cosHalfTheta = absAlpha;
        double sinHalfTheta = absBeta;
        double theta = 2.0 * Math.atan2(sinHalfTheta, cosHalfTheta);

        // phi: azimuthal angle = arg(beta) - arg(alpha)
        // beta / alpha = e^{i*phi} * tan(theta/2)
        // arg(beta) - arg(alpha) = phi
        double argAlpha = Math.atan2(alpha.imaginary(), alpha.real());
        double argBeta = Math.atan2(beta.imaginary(), beta.real());
        double phi = argBeta - argAlpha;

        // Bloch coordinates
        double x = Math.sin(theta) * Math.cos(phi);
        double y = Math.sin(theta) * Math.sin(phi);
        double z = Math.cos(theta);

        return new double[] { x, y, z };
    }

    /**
     * Converts Bloch sphere coordinates back to qubit state.
     * 
     * @param theta Polar angle (0 to π)
     * @param phi   Azimuthal angle (0 to 2π)
     * @return Complex[2] = {alpha, beta}
     */
    public static Complex[] fromBlochAngles(double theta, double phi) {
        Complex alpha = Complex.of(Math.cos(theta / 2.0), 0.0);
        Complex beta = Complex.ofPolar(Math.sin(theta / 2.0), phi);
        return new Complex[] { alpha, beta };
    }

    /**
     * Special states on the Bloch sphere.
     */
    public static final double[] STATE_ZERO = { 0.0, 0.0, 1.0 }; // |0⟩ = north pole
    public static final double[] STATE_ONE = { 0.0, 0.0, -1.0 }; // |1⟩ = south pole
    public static final double[] STATE_PLUS = { 1.0, 0.0, 0.0 }; // |+⟩ = +x
    public static final double[] STATE_MINUS = { -1.0, 0.0, 0.0 }; // |-⟩ = -x
    public static final double[] STATE_PLUS_I = { 0.0, 1.0, 0.0 }; // |+i⟩ = +y
    public static final double[] STATE_MINUS_I = { 0.0, -1.0, 0.0 }; // |-i⟩ = -y
}
