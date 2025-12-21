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
package org.jscience.physics.relativity;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import org.jscience.physics.PhysicalConstants;

import java.util.Arrays;

/**
 * Lorentz Transformation utilities for Special Relativity.
 * <p>
 * Handles boosting of spacetime events between inertial reference frames.
 * </p>
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LorentzTransformation {

    private LorentzTransformation() {
        // Utility class
    }

    /**
     * Calculates the Lorentz factor (gamma).
     * 
     * @param v velocity magnitude (m/s)
     * @return gamma = 1 / sqrt(1 - v^2/c^2)
     */
    public static Real gamma(Real v) {
        Real c = PhysicalConstants.c;
        Real beta = v.divide(c);
        Real betaSq = beta.multiply(beta);

        if (betaSq.compareTo(Real.ONE) >= 0) {
            throw new IllegalArgumentException("Velocity must be less than speed of light");
        }

        return Real.ONE.divide(Real.ONE.subtract(betaSq).sqrt());
    }

    /**
     * Transforms an event (t, x, y, z) from frame S to S' moving with velocity v
     * along x-axis.
     * 
     * @param t time in S
     * @param x position x in S
     * @param v relative velocity of S' wrt S (along x)
     * @return new coordinates [t', x'] (y and z are unchanged)
     */
    public static Vector<Real> boostX(Real t, Real x, Real v) {
        Real gamma = gamma(v.abs());
        Real c = PhysicalConstants.c;
        Real cSq = c.multiply(c);

        // t' = gamma * (t - vx/c^2)
        // x' = gamma * (x - vt)

        Real term1 = v.multiply(x).divide(cSq);
        Real tPrime = gamma.multiply(t.subtract(term1));

        Real xPrime = gamma.multiply(x.subtract(v.multiply(t)));

        return DenseVector.of(Arrays.asList(tPrime, xPrime), Reals.getInstance());
    }

    /**
     * General Lorentz boost for arbitrary velocity vector.
     * 
     * @param t time in S
     * @param r position vector in S
     * @param v relative velocity vector of S' wrt S
     * @return new coordinates [t', r']
     */
    public static Object[] boost(Real t, Vector<Real> r, Vector<Real> v) {
        Real vMag = v.norm();
        if (vMag.equals(Real.ZERO)) {
            return new Object[] { t, r };
        }

        Real c = PhysicalConstants.c;
        Real gamma = gamma(vMag);

        // r' = r + (gamma - 1)(r . n)n - gamma * v * t
        // t' = gamma * (t - (r . v) / c^2)

        Vector<Real> n = v.multiply(Real.ONE.divide(vMag)); // direction

        // Vector.times(Vector) returns Vector (element-wise) or scalar?
        // JScience Vector usually implies inner product via explicit methods or if it's
        // a specific Hilbert space.
        // Let's assume standard dot product is available or we implement it.
        // Vector interface in JScience math might vary.
        // Matrix.times(Vector) is typical.
        // For Vector<Real>, we need to know the implementation.
        // Since we are generic, we can assume a standard dot product helper exists or
        // loop.

        // Assuming we rely on Matrix algebra or direct implementation.
        // Let's perform dot product manually if needed or use a scalar product method
        // if available in interface.
        // Vector interface: T get(int i), int getDimension().

        Real dotProduct = Real.ZERO;
        for (int i = 0; i < r.dimension(); i++) {
            dotProduct = dotProduct.add(r.get(i).multiply(v.get(i)));
        }

        Real cSq = c.multiply(c);
        Real tPrime = gamma.multiply(t.subtract(dotProduct.divide(cSq)));

        Real rDotN = Real.ZERO;
        for (int i = 0; i < r.dimension(); i++) {
            rDotN = rDotN.add(r.get(i).multiply(n.get(i)));
        }

        Vector<Real> term2 = n.multiply(rDotN.multiply(gamma.subtract(Real.ONE)));
        Vector<Real> term3 = v.multiply(t.multiply(gamma));

        Vector<Real> rPrime = r.add(term2).subtract(term3);

        return new Object[] { tPrime, rPrime };
    }
}
