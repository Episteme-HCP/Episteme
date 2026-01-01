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
 * A point in 3D Euclidean space.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Point3D
        implements GeometricObject<Point3D>, org.jscience.mathematics.topology.MetricSpace<Point3D> {

    private final Real x;
    private final Real y;
    private final Real z;

    public static final Point3D ZERO = new Point3D(Real.ZERO, Real.ZERO, Real.ZERO);

    private Point3D(Real x, Real y, Real z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point3D of(double x, double y, double z) {
        return new Point3D(Real.of(x), Real.of(y), Real.of(z));
    }

    public static Point3D of(Real x, Real y, Real z) {
        return new Point3D(x, y, z);
    }

    public Real getX() {
        return x;
    }

    public Real getY() {
        return y;
    }

    public Real getZ() {
        return z;
    }

    @Override
    public Real distance(Point3D a, Point3D b) {
        return a.distanceTo(b);
    }

    // Set implementation (singleton set)
    public boolean isEmpty() {
        return false;
    }

    // TopologicalSpace implementation
    public boolean isOpen() {
        return false;
    }

    public boolean isClosed() {
        return true;
    }

    public Real distanceTo(Point3D other) {
        Real dx = x.subtract(other.x);
        Real dy = y.subtract(other.y);
        Real dz = z.subtract(other.z);
        return dx.multiply(dx).add(dy.multiply(dy)).add(dz.multiply(dz)).sqrt();
    }

    public Point3D add(Vector3D vector) {
        return new Point3D(x.add(vector.x()), y.add(vector.y()), z.add(vector.z()));
    }

    public Vector3D subtract(Point3D other) {
        return new Vector3D(x.subtract(other.x), y.subtract(other.y), z.subtract(other.z));
    }

    // GeometricObject implementation
    @Override
    public int dimension() {
        return 0; // A point is 0-dimensional
    }

    @Override
    public int ambientDimension() {
        return 3; // Lives in 3D space
    }

    public boolean containsPoint(Point3D point) {
        return this.equals(point);
    }

    @Override
    public boolean contains(Point3D point) {
        return containsPoint(point);
    }

    @Override
    public String description() {
        return "Point3D" + toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point3D))
            return false;
        Point3D other = (Point3D) obj;
        return x.equals(other.x) && y.equals(other.y) && z.equals(other.z);
    }

    @Override
    public int hashCode() {
        return x.hashCode() * 31 * 31 + y.hashCode() * 31 + z.hashCode();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Vector3D toVector() {
        return new Vector3D(x, y, z);
    }
}

