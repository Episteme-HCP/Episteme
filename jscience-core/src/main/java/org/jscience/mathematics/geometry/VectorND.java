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
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents an N-dimensional geometric vector.
 * <p>
 * A geometric vector represents a direction and magnitude in N-dimensional
 * space.
 * Unlike points (which represent positions), vectors represent displacements,
 * velocities, forces, etc.
 * </p>
 * <p>
 * Key distinction from algebraic vectors:
 * - VectorND is specifically for geometric operations (cross product, angles,
 * etc.)
 * - Vector<Real> is for general linear algebra
 * - VectorND wraps Vector<Real> but adds geometric semantics
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VectorND extends DenseVector<Real> implements
        org.jscience.mathematics.topology.MetricSpace<org.jscience.mathematics.linearalgebra.Vector<Real>> {

    private static final org.jscience.mathematics.sets.Reals REALS = org.jscience.mathematics.sets.Reals.getInstance();

    public VectorND(List<Real> components) {
        super(components, REALS);
    }

    public VectorND(Vector<Real> vector) {
        super(vectorToList(vector), REALS);
    }

    // Helper to convert Vector to List for super constructor
    private static List<Real> vectorToList(Vector<Real> v) {
        List<Real> list = new ArrayList<>(v.dimension());
        for (int i = 0; i < v.dimension(); i++) {
            list.add(v.get(i));
        }
        return list;
    }

    public static VectorND of(Real... components) {
        return new VectorND(Arrays.asList(components));
    }

    public static VectorND of(double... components) {
        Real[] reals = new Real[components.length];
        for (int i = 0; i < components.length; i++) {
            reals[i] = Real.of(components[i]);
        }
        return new VectorND(Arrays.asList(reals));
    }

    public static VectorND zero(int dimension) {
        List<Real> zeros = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            zeros.add(Real.ZERO);
        }
        return new VectorND(zeros);
    }

    public static VectorND unitVector(int dimension, int direction) {
        if (direction < 0 || direction >= dimension) {
            throw new IllegalArgumentException("Direction must be in [0, " + (dimension - 1) + "]");
        }
        List<Real> components = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            components.add(i == direction ? Real.ONE : Real.ZERO);
        }
        return new VectorND(components);
    }

    public Vector<Real> toVector() {
        return this;
    }

    public VectorND add(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        return new VectorND(super.add(other));
    }

    public VectorND subtract(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        return new VectorND(super.subtract(other));
    }

    public VectorND multiply(Real scalar) {
        return new VectorND(super.multiply(scalar));
    }

    public VectorND divide(Real scalar) {
        List<Real> divided = new ArrayList<>();
        for (int i = 0; i < dimension(); i++) {
            divided.add(get(i).divide(scalar));
        }
        return new VectorND(divided);
    }

    public VectorND negate() {
        return new VectorND(super.negate());
    }

    public Real dot(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        return super.dot(other);
    }

    // Overrides for MetricSpace
    @Override
    public Real distance(org.jscience.mathematics.linearalgebra.Vector<Real> a,
            org.jscience.mathematics.linearalgebra.Vector<Real> b) {
        return a.subtract(b).norm();
    }

    @Override
    public boolean contains(org.jscience.mathematics.linearalgebra.Vector<Real> element) {
        return this.equals(element);
    }

    @Override
    public boolean containsPoint(org.jscience.mathematics.linearalgebra.Vector<Real> p) {
        return this.equals(p);
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

    public VectorND cross(VectorND other) {
        if (this.dimension() != 3 || other.dimension() != 3) {
            throw new IllegalArgumentException("Cross product is only defined for 3D vectors");
        }
        // Need explicit cross implementation or cast to Vector3D if possible?
        // DenseVector might not implement cross fully or return Vector<E>.
        // Let's implement manually using values.
        Real x1 = get(0);
        Real y1 = get(1);
        Real z1 = get(2);
        Real x2 = other.get(0);
        Real y2 = other.get(1);
        Real z2 = other.get(2);

        Real cx = y1.multiply(z2).subtract(z1.multiply(y2));
        Real cy = z1.multiply(x2).subtract(x1.multiply(z2));
        Real cz = x1.multiply(y2).subtract(y1.multiply(x2));

        return new VectorND(Arrays.asList(cx, cy, cz));
    }

    public Real normSquared() {
        return dot(this);
    }

    public VectorND normalize() {
        Real magnitude = norm();
        if (magnitude.equals(Real.ZERO)) {
            throw new ArithmeticException("Cannot normalize zero vector");
        }
        return divide(magnitude);
    }

    public boolean isUnit() {
        return Math.abs(norm().doubleValue() - 1.0) < 1e-10;
    }

    public boolean isZero() {
        return norm().doubleValue() < 1e-10;
    }

    public Real angleTo(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        Real dotProduct = dot(other);
        Real magnitudeProduct = norm().multiply(other.norm());
        Real cosTheta = dotProduct.divide(magnitudeProduct);
        double cosVal = Math.max(-1.0, Math.min(1.0, cosTheta.doubleValue()));
        return Real.of(Math.acos(cosVal));
    }

    // ... kept implies methods ...

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Vector(");
        for (int i = 0; i < dimension(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(get(i));
        }
        sb.append(")");
        return sb.toString();
    }
}
