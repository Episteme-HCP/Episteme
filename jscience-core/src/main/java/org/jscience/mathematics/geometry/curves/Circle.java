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

package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.geometry.ParametricCurve;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.Arrays;

/**
 * Represents a circle as a parametric curve.
 * <p>
 * 2D circle: C(t) = center + (r*cos(t), r*sin(t))
 * 3D circle: C(t) = center + r*(cos(t)*u + sin(t)*v)
 * where u and v are orthonormal vectors in the plane of the circle.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Circle implements ParametricCurve {

    private final PointND center;
    private final Real radius;
    private final Vector<Real> u; // First basis vector in plane
    private final Vector<Real> v; // Second basis vector in plane
    private final int dimension;

    /**
     * Creates a 2D circle in the XY plane.
     * 
     * @param center the center point
     * @param radius the radius
     */
    public Circle(PointND center, Real radius) {
        if (center.ambientDimension() < 2) {
            throw new IllegalArgumentException("Circle requires at least 2D space");
        }

        this.center = center;
        this.radius = radius;
        this.dimension = center.ambientDimension();

        // Default to XY plane
        this.u = createUnitVector(0, dimension);
        this.v = createUnitVector(1, dimension);
    }

    /**
     * Creates a circle in 3D space with specified plane orientation.
     * 
     * @param center the center point
     * @param radius the radius
     * @param normal the normal vector to the plane (will be normalized)
     */
    public Circle(PointND center, Real radius, Vector<Real> normal) {
        if (center.ambientDimension() != 3 || normal.dimension() != 3) {
            throw new IllegalArgumentException("This constructor is for 3D circles only");
        }

        this.center = center;
        this.radius = radius;
        this.dimension = 3;

        // Create orthonormal basis in the plane perpendicular to normal
        Vector<Real> normalizedNormal = normal.normalize();

        // Find a vector not parallel to normal
        Vector<Real> arbitrary = Math.abs(normalizedNormal.get(0).doubleValue()) < 0.9 ? createUnitVector(0, 3)
                : createUnitVector(1, 3);

        // u = arbitrary - (arbitraryÃ‚Â·normal)*normal, then normalize
        Real projection = arbitrary.dot(normalizedNormal);
        this.u = arbitrary.subtract(normalizedNormal.multiply(projection)).normalize();

        // v = normal Ãƒâ€” u
        this.v = normalizedNormal.cross(u);
    }

    @Override
    public PointND at(Real t) {
        // C(t) = center + radius*(cos(t)*u + sin(t)*v)
        Real cosT = Real.of(Math.cos(t.doubleValue()));
        Real sinT = Real.of(Math.sin(t.doubleValue()));

        Vector<Real> offset = u.multiply(cosT).add(v.multiply(sinT)).multiply(radius);
        return new PointND(center.toVector().add(offset));
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public org.jscience.mathematics.geometry.VectorND tangent(Real t, Real h) {
        // Analytical derivative: C'(t) = radius*(-sin(t)*u + cos(t)*v)
        Real cosT = Real.of(Math.cos(t.doubleValue()));
        Real sinT = Real.of(Math.sin(t.doubleValue()));

        Vector<Real> tan = u.multiply(sinT.negate()).add(v.multiply(cosT)).multiply(radius);
        return new org.jscience.mathematics.geometry.VectorND(tan);
    }

    @Override
    public Real curvature(Real t, Real h) {
        // Circle has constant curvature = 1/radius
        return Real.ONE.divide(radius);
    }

    /**
     * Returns the center of the circle.
     * 
     * @return the center point
     */
    public PointND getCenter() {
        return center;
    }

    /**
     * Returns the radius of the circle.
     * 
     * @return the radius
     */
    public Real getRadius() {
        return radius;
    }

    /**
     * Returns the circumference of the circle.
     * 
     * @return 2Ãâ‚¬r
     */
    public Real circumference() {
        return radius.multiply(Real.of(2 * Math.PI));
    }

    /**
     * Creates a unit vector with 1 in the specified position.
     */
    private static Vector<Real> createUnitVector(int position, int dimension) {
        Real[] components = new Real[dimension];
        for (int i = 0; i < dimension; i++) {
            components[i] = (i == position) ? Real.ONE : Real.ZERO;
        }
        return new DenseVector<>(
                Arrays.asList(components),
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public String toString() {
        return "Circle(center=" + center + ", radius=" + radius + ")";
    }
}


