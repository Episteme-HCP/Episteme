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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import java.util.Arrays;

/**
 * Represents a 4D vector, typically used for spacetime coordinates (ct, x, y,
 * z).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Vector4D extends DenseVector<Real> implements
        org.jscience.mathematics.topology.MetricSpace<org.jscience.mathematics.linearalgebra.Vector<Real>> {

    private static final Reals REALS = Reals.getInstance();

    public static final Vector4D ZERO = new Vector4D(0, 0, 0, 0);

    /**
     * Creates a new Vector4D from double coordinates.
     * 
     * @param t the t (time) coordinate
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector4D(double t, double x, double y, double z) {
        super(Arrays.asList(Real.of(t), Real.of(x), Real.of(y), Real.of(z)), REALS);
    }

    /**
     * Creates a new Vector4D from Real coordinates.
     * 
     * @param t the t (time) coordinate
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector4D(Real t, Real x, Real y, Real z) {
        super(Arrays.asList(t, x, y, z), REALS);
    }

    public Real ct() {
        return get(0);
    }

    public Real x() {
        return get(1);
    }

    public Real y() {
        return get(2);
    }

    public Real z() {
        return get(3);
    }

    public double getCt() {
        return ct().doubleValue();
    }

    public double getX() {
        return x().doubleValue();
    }

    public double getY() {
        return y().doubleValue();
    }

    public double getZ() {
        return z().doubleValue();
    }

    public Vector4D normalize() {
        Real n = this.normValue();
        if (n.equals(Real.ZERO)) {
            return ZERO;
        }
        return this.divide(n);
    }

    public Real normValue() {
        return ct().multiply(ct()).add(x().multiply(x())).add(y().multiply(y())).add(z().multiply(z())).sqrt();
    }

    public Vector4D multiply(Real scalar) {
        return new Vector4D(ct().multiply(scalar), x().multiply(scalar), y().multiply(scalar), z().multiply(scalar));
    }

    public Vector4D divide(Real scalar) {
        return new Vector4D(ct().divide(scalar), x().divide(scalar), y().divide(scalar), z().divide(scalar));
    }

    public Vector4D negate() {
        return new Vector4D(ct().negate(), x().negate(), y().negate(), z().negate());
    }

    @Override
    public Real distance(org.jscience.mathematics.linearalgebra.Vector<Real> a,
            org.jscience.mathematics.linearalgebra.Vector<Real> b) {
        return a.subtract(b).norm();
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

    public Vector4D subtract(Vector4D other) {
        return new Vector4D(ct().subtract(other.ct()), x().subtract(other.x()), y().subtract(other.y()),
                z().subtract(other.z()));
    }

    public Vector4D add(Vector4D other) {
        return new Vector4D(ct().add(other.ct()), x().add(other.x()), y().add(other.y()), z().add(other.z()));
    }
}


