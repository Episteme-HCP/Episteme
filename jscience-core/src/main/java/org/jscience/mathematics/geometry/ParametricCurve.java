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
package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.mathematics.analysis.DifferentiableFunction;

/**
 * Represents a parametric curve in N-dimensional space.
 * <p>
 * A parametric curve is defined by: C(t) = (x₁(t), x₂(t), ..., xₙ(t))
 * where t is the parameter, typically in some interval [a, b].
 * </p>
 * <p>
 * Examples:
 * - Line: C(t) = P₀ + t*d
 * - Circle: C(t) = (r*cos(t), r*sin(t))
 * - Helix: C(t) = (r*cos(t), r*sin(t), h*t)
 * - Bézier curve: C(t) = Σ Bᵢ(t)*Pᵢ
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ParametricCurve extends DifferentiableFunction<Real, Vector<Real>> {

    @Override
    default org.jscience.mathematics.analysis.Function<Real, Vector<Real>> differentiate() {
        return new org.jscience.mathematics.analysis.Function<Real, Vector<Real>>() {
            @Override
            public Vector<Real> evaluate(Real t) {
                // Return tangent approximation with small h
                // Ideally this would be an exact derivative if known
                return tangent(t, org.jscience.mathematics.numbers.real.Real.of(1.0e-5));
            }
        };
    }

    @Override
    default Vector<Real> evaluate(Real t) {
        return at(t).toVector();
    }

    // @Override removed as ParametricCurve does not extend VectorFunction (which
    // enforces Vector input)
    default int getOutputDimension() {
        return dimension();
    }

    /**
     * Evaluates the curve at parameter t.
     * 
     * @param t the parameter value
     * @return the point on the curve
     */
    PointND at(Real t);

    /**
     * Returns the dimension of the ambient space.
     * 
     * @return the dimension
     */
    int dimension();

    /**
     * Returns the tangent vector at parameter t.
     * <p>
     * The tangent vector is the derivative: C'(t) = dC/dt
     * </p>
     * 
     * @param t the parameter value
     * @param h the step size for numerical differentiation
     * @return the tangent vector
     */
    default Vector<Real> tangent(Real t, Real h) {
        PointND p1 = at(t.subtract(h));
        PointND p2 = at(t.add(h));

        Vector<Real> diff = p2.toVector().subtract(p1.toVector());
        return diff.multiply(Real.of(0.5).divide(h));
    }

    /**
     * Returns the unit tangent vector at parameter t.
     * 
     * @param t the parameter value
     * @param h the step size
     * @return the normalized tangent vector
     */
    default Vector<Real> unitTangent(Real t, Real h) {
        return tangent(t, h).normalize();
    }

    /**
     * Computes the arc length from t0 to t1.
     * <p>
     * Arc length: L = ∫[t0,t1] ||C'(t)|| dt
     * </p>
     * 
     * @param t0       the start parameter
     * @param t1       the end parameter
     * @param numSteps the number of integration steps
     * @return the arc length
     */
    default Real arcLength(Real t0, Real t1, int numSteps) {
        Real dt = t1.subtract(t0).divide(Real.of(numSteps));
        Real h = dt.multiply(Real.of(0.01)); // Small h for derivative
        Real length = Real.ZERO;

        for (int i = 0; i < numSteps; i++) {
            Real t = t0.add(dt.multiply(Real.of(i)));
            Real speed = tangent(t, h).norm();
            length = length.add(speed.multiply(dt));
        }

        return length;
    }

    /**
     * Returns the curvature at parameter t.
     * <p>
     * Curvature: κ = ||C'(t) × C''(t)|| / ||C'(t)||³
     * </p>
     * <p>
     * For 2D curves: κ = |x'y'' - y'x''| / (x'² + y'²)^(3/2)
     * </p>
     * 
     * @param t the parameter value
     * @param h the step size for numerical differentiation
     * @return the curvature
     */
    default Real curvature(Real t, Real h) {
        // First derivative
        Vector<Real> firstDeriv = tangent(t, h);

        // Second derivative (derivative of tangent)
        Vector<Real> tangentPlus = tangent(t.add(h), h);
        Vector<Real> tangentMinus = tangent(t.subtract(h), h);
        Vector<Real> secondDeriv = tangentPlus.subtract(tangentMinus)
                .multiply(Real.of(0.5).divide(h));

        // Curvature = ||C' × C''|| / ||C'||³
        Vector<Real> cross = firstDeriv.cross(secondDeriv);
        Real numerator = cross.norm();
        Real denominator = firstDeriv.norm().pow(3);

        return numerator.divide(denominator);
    }

    /**
     * Returns the normal vector at parameter t (for 2D/3D curves).
     * <p>
     * The normal vector is perpendicular to the tangent.
     * </p>
     * 
     * @param t the parameter value
     * @param h the step size
     * @return the unit normal vector
     */
    default Vector<Real> normal(Real t, Real h) {
        Vector<Real> tangent = tangent(t, h);

        // Second derivative
        Vector<Real> tangentPlus = tangent(t.add(h), h);
        Vector<Real> tangentMinus = tangent(t.subtract(h), h);
        Vector<Real> secondDeriv = tangentPlus.subtract(tangentMinus)
                .multiply(Real.of(0.5).divide(h));

        // Normal is perpendicular to tangent, in direction of curvature
        // N = (T' - (T'·T)T) / ||T' - (T'·T)T||
        Vector<Real> unitTangent = tangent.normalize();
        Real projection = secondDeriv.dot(unitTangent);
        Vector<Real> normal = secondDeriv.subtract(unitTangent.multiply(projection));

        return normal.normalize();
    }
}
