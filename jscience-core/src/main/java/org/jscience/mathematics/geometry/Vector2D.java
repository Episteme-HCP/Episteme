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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * A 2D vector in Euclidean space.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Vector2D extends org.jscience.mathematics.linearalgebra.vectors.DenseVector<Real>
        implements GeometricObject<Point2D>,
        org.jscience.mathematics.topology.MetricSpace<org.jscience.mathematics.linearalgebra.Vector<Real>> {

    private static final org.jscience.mathematics.sets.Reals REALS = org.jscience.mathematics.sets.Reals.getInstance();

    public Vector2D(double x, double y) {
        super(java.util.Arrays.asList(Real.of(x), Real.of(y)), REALS);
    }

    public Vector2D(Real x, Real y) {
        super(java.util.Arrays.asList(x, y), REALS);
    }

    public static Vector2D of(double x, double y) {
        return new Vector2D(x, y);
    }

    public static Vector2D of(Real x, Real y) {
        return new Vector2D(x, y);
    }

    public Real getX() {
        return get(0);
    }

    public Real getY() {
        return get(1);
    }

    // Overrides for performance and covariance
    public Vector2D add(Vector2D other) {
        return new Vector2D(getX().add(other.getX()), getY().add(other.getY()));
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(getX().subtract(other.getX()), getY().subtract(other.getY()));
    }

    public Vector2D multiply(Real scalar) {
        return new Vector2D(getX().multiply(scalar), getY().multiply(scalar));
    }

    public Vector2D negate() {
        return new Vector2D(getX().negate(), getY().negate());
    }

    public Vector2D normalize() {
        Real n = this.norm();
        if (n.equals(Real.ZERO))
            return new Vector2D(0, 0);
        return this.multiply(n.inverse());
    }

    public Real dot(Vector2D other) {
        return getX().multiply(other.getX()).add(getY().multiply(other.getY()));
    }

    // Explicit scale method if needed
    public Vector2D scale(Real scalar) {
        return multiply(scalar);
    }

    // MetricSpace implementation
    @Override
    public Real distance(org.jscience.mathematics.linearalgebra.Vector<Real> a,
            org.jscience.mathematics.linearalgebra.Vector<Real> b) {
        return a.subtract(b).norm();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public String toString() {
        return "<" + getX() + ", " + getY() + ">";
    }

    // GeometricObject

    @Override
    public int ambientDimension() {
        return 2;
    }

    /**
     * Checks if a point lies at the same position as this vector (interpreted as
     * position vector).
     */
    public boolean containsPoint(Point2D p) {
        return getX().equals(p.getX()) && getY().equals(p.getY());
    }

    @Override
    public boolean containsPoint(org.jscience.mathematics.linearalgebra.Vector<Real> p) {
        return this.equals(p);
    }

    @Override
    public boolean contains(org.jscience.mathematics.linearalgebra.Vector<Real> p) {
        return containsPoint(p);
    }

    @Override
    public String description() {
        return "Vector2D" + toString();
    }
}

