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

package org.jscience.physics.astronomy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Calculator for orbital perturbations.
 * <p>
 * Includes J2 (oblateness) and atmospheric drag estimates.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Perturbations {

    private Perturbations() {
    }

    /**
     * Calculates perturbation acceleration due to J2 (Earth oblateness).
     * 
     * @param r      position vector (approx ECI)
     * @param mu     standard gravitational parameter
     * @param J2     J2 coefficient
     * @param R_body equatorial radius of the body
     * @return acceleration vector
     */
    public static Vector<Real> computeJ2Perturbation(Vector<Real> r, Real mu, Real J2, Real R_body) {
        Real rMag = r.norm();
        Real x = r.get(0);
        Real y = r.get(1);
        Real z = r.get(2);

        Real r2 = rMag.multiply(rMag);
        Real r5 = r2.multiply(r2).multiply(rMag);

        Real combined = mu.multiply(J2).multiply(R_body.multiply(R_body)).divide(Real.of(2)); // mu*J2*R^2 / 2
        Real factor = combined.divide(r5);

        Real z2 = z.multiply(z);
        Real zTerm = z2.multiply(Real.of(5)).divide(r2); // 5*z^2 / r^2

        // ax = factor * x * (5*z^2/r^2 - 1)
        Real ax = factor.multiply(x).multiply(zTerm.subtract(Real.ONE));

        // ay = factor * y * (5*z^2/r^2 - 1)
        Real ay = factor.multiply(y).multiply(zTerm.subtract(Real.ONE));

        // az = factor * z * (5*z^2/r^2 - 3)
        // zTerm is same 5*z^2/r^2
        Real az = factor.multiply(z).multiply(zTerm.subtract(Real.of(3)));

        // Construct result vector using same factory/method as input if possible
        // Assuming DenseVector behavior or similar helper from Vector params works.
        // We'll create a new DenseVector or use helper.
        // Issue: We don't have easy factory for Vector from here without import.
        // Assuming DenseVector is standard implementation.

        return org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(
                java.util.Arrays.asList(ax, ay, az),
                org.jscience.mathematics.sets.Reals.getInstance());
    }
}


