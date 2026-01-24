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

package org.jscience.mathematics.geometry.boundaries;

import org.jscience.mathematics.geometry.Point2D;
import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.numbers.real.Real;

import java.util.List;
import java.util.Objects;

/**
 * A zero-dimensional boundary representing a single point.
 * <p>
 * This is useful for representing exact locations, point-of-interest,
 * or degenerate boundaries.
 *
 * @param <P> the point type
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PointBoundary<P> implements Boundary<P> {

    private static final long serialVersionUID = 1L;

    private final P point;
    private final Real tolerance;

    /**
     * Creates a point boundary with zero tolerance.
     *
     * @param point the point
     */
    public PointBoundary(P point) {
        this(point, Real.ZERO);
    }

    /**
     * Creates a point boundary with a tolerance radius.
     * Points within tolerance distance are considered "contained".
     *
     * @param point     the center point
     * @param tolerance the tolerance radius
     */
    public PointBoundary(P point, Real tolerance) {
        this.point = Objects.requireNonNull(point, "Point cannot be null");
        this.tolerance = tolerance != null ? tolerance : Real.ZERO;
    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public boolean contains(P other) {
        if (other == null) return false;
        if (tolerance.isZero()) {
            return point.equals(other);
        }
        // Check within tolerance
        Real distance = computeDistance(other);
        return distance.compareTo(tolerance) <= 0;
    }

    private Real computeDistance(P other) {
        if (point instanceof Point2D && other instanceof Point2D) {
            return ((Point2D) point).distanceTo((Point2D) other);
        }
        if (point instanceof Point3D && other instanceof Point3D) {
            return ((Point3D) point).distanceTo((Point3D) other);
        }
        // Fallback
        return point.equals(other) ? Real.ZERO : Real.ONE;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public P getCentroid() {
        return point;
    }

    @Override
    public BoundingBox<P> getBoundingBox() {
        // Point bounding box is just the point itself
        if (point instanceof Point2D) {
            Point2D p = (Point2D) point;
            @SuppressWarnings("unchecked")
            BoundingBox<P> box = (BoundingBox<P>) new BoundingBox2D(p, p);
            return box;
        }
        if (point instanceof Point3D) {
            Point3D p = (Point3D) point;
            @SuppressWarnings("unchecked")
            BoundingBox<P> box = (BoundingBox<P>) new BoundingBox3D(p, p);
            return box;
        }
        return null;
    }

    @Override
    public Real getMeasure() {
        return Real.ZERO; // A point has zero measure
    }

    @Override
    public Real getBoundaryMeasure() {
        return Real.ZERO;
    }

    @Override
    public boolean intersects(Boundary<P> other) {
        return other.contains(point);
    }

    @Override
    public Boundary<P> union(Boundary<P> other) {
        // Union of point with something else
        if (other instanceof PointBoundary) {
            // Two points - return a line or composite
            return other; // Simplified
        }
        return other;
    }

    @Override
    public Boundary<P> intersection(Boundary<P> other) {
        if (other.contains(point)) {
            return this;
        }
        return null;
    }

    @Override
    public Boundary<P> convexHull() {
        return this;
    }

    @Override
    public Boundary<P> translate(P offset) {
        if (point instanceof Point2D && offset instanceof Point2D) {
            Point2D p = (Point2D) point;
            Point2D o = (Point2D) offset;
            @SuppressWarnings("unchecked")
            P newPoint = (P) new Point2D(p.getX().add(o.getX()), p.getY().add(o.getY()));
            return new PointBoundary<>(newPoint, tolerance);
        }
        if (point instanceof Point3D && offset instanceof Point3D) {
            Point3D p = (Point3D) point;
            Point3D o = (Point3D) offset;
            @SuppressWarnings("unchecked")
            P newPoint = (P) new Point3D(
                p.getX().add(o.getX()),
                p.getY().add(o.getY()),
                p.getZ().add(o.getZ())
            );
            return new PointBoundary<>(newPoint, tolerance);
        }
        return this;
    }

    @Override
    public Boundary<P> scale(Real factor) {
        return new PointBoundary<>(point, tolerance.multiply(factor));
    }

    /**
     * Returns the point.
     * @return the point
     */
    public P getPoint() {
        return point;
    }

    /**
     * Returns the tolerance radius.
     * @return the tolerance
     */
    public Real getTolerance() {
        return tolerance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PointBoundary)) return false;
        PointBoundary<?> other = (PointBoundary<?>) obj;
        return point.equals(other.point);
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }

    @Override
    public String toString() {
        return String.format("PointBoundary[%s]", point);
    }

    // ===== Static factory methods =====

    /**
     * Creates a 2D point boundary.
     */
    public static PointBoundary<Point2D> of2D(Real x, Real y) {
        return new PointBoundary<>(new Point2D(x, y));
    }

    /**
     * Creates a 2D point boundary with tolerance.
     */
    public static PointBoundary<Point2D> of2D(Real x, Real y, Real tolerance) {
        return new PointBoundary<>(new Point2D(x, y), tolerance);
    }

    /**
     * Creates a 3D point boundary.
     */
    public static PointBoundary<Point3D> of3D(Real x, Real y, Real z) {
        return new PointBoundary<>(new Point3D(x, y, z));
    }

    /**
     * Creates a 3D point boundary with tolerance.
     */
    public static PointBoundary<Point3D> of3D(Real x, Real y, Real z, Real tolerance) {
        return new PointBoundary<>(new Point3D(x, y, z), tolerance);
    }
}
