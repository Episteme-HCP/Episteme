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
 * Represents a line in 3D Euclidean space.
 * <p>
 * Defined by a point P and a direction vector V: L(t) = P + tV.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Line3D implements GeometricObject<Point3D> {

    private final Vector3D point;
    private final Vector3D direction;

    /**
     * Creates a line from a point and a direction.
     * 
     * @param point     a point on the line
     * @param direction the direction vector of the line (will be normalized)
     */
    public Line3D(Vector3D point, Vector3D direction) {
        this.point = point;
        // Normalize direction to ensure consistent parameterization
        // We need to cast the result of normalize() back to Vector3D or create a new
        // one
        // Since normalize() returns Vector<Real>, we construct a new Vector3D
        // This assumes direction is not zero.
        if (direction.norm().equals(Real.ZERO)) {
            throw new IllegalArgumentException("Direction vector cannot be zero");
        }

        // Manual normalization to keep it as Vector3D without complex casting
        Real norm = direction.norm();
        this.direction = new Vector3D(
                direction.x().divide(norm),
                direction.y().divide(norm),
                direction.z().divide(norm));
    }

    public Vector3D getPoint() {
        return point;
    }

    public Vector3D getDirection() {
        return direction;
    }

    /**
     * Calculates the distance from a point to this line.
     * 
     * @param p the point
     * @return the perpendicular distance
     */
    public Real distance(Vector3D p) {
        // d = ||(p - point) x direction|| / ||direction||
        // Since direction is normalized, ||direction|| = 1
        // d = ||(p - point) x direction||

        Vector3D diff = new Vector3D(
                p.x().subtract(point.x()),
                p.y().subtract(point.y()),
                p.z().subtract(point.z()));

        return diff.cross(direction).norm();
    }

    /**
     * Checks if a point lies on this line (within tolerance).
     * 
     * @param p         the point
     * @param tolerance the error tolerance
     * @return true if p is on the line
     */
    public boolean contains(Vector3D p, double tolerance) {
        return distance(p).doubleValue() < tolerance;
    }

    // GeometricObject implementation
    @Override
    public int dimension() {
        return 1; // A line is 1-dimensional
    }

    @Override
    public int ambientDimension() {
        return 3; // Lives in 3D space
    }

    public boolean containsPoint(Point3D p) {
        // Convert Point3D to Vector3D for distance check
        Vector3D v = new Vector3D(p.getX(), p.getY(), p.getZ());
        return distance(v).doubleValue() < 1e-12;
    }

    @Override
    public String description() {
        return "Line3D through " + point + " in direction " + direction;
    }

    /**
     * Computes the intersection point with another line.
     * 
     * @param other the other line
     * @return the intersection point, or null if lines are parallel or skew
     */
    public Vector3D intersection(Line3D other) {
        // Use the closest points approach
        // If the distance between closest points is ~0, they intersect

        Vector3D p1 = this.point;
        Vector3D d1 = this.direction;
        Vector3D p2 = other.point;
        Vector3D d2 = other.direction;

        // w0 = p1 - p2
        Vector3D w0 = new Vector3D(
                p1.x().subtract(p2.x()),
                p1.y().subtract(p2.y()),
                p1.z().subtract(p2.z()));

        Real a = dot(d1, d1); // always > 0
        Real b = dot(d1, d2);
        Real c = dot(d2, d2); // always > 0
        Real d = dot(d1, w0);
        Real e = dot(d2, w0);

        Real denom = a.multiply(c).subtract(b.multiply(b));

        // Check if parallel (denom ~= 0)
        if (Math.abs(denom.doubleValue()) < 1e-12) {
            return null; // Lines are parallel
        }

        Real sc = b.multiply(e).subtract(c.multiply(d)).divide(denom);
        Real tc = a.multiply(e).subtract(b.multiply(d)).divide(denom);

        // Points on each line
        Vector3D point1 = new Vector3D(
                p1.x().add(sc.multiply(d1.x())),
                p1.y().add(sc.multiply(d1.y())),
                p1.z().add(sc.multiply(d1.z())));

        Vector3D point2 = new Vector3D(
                p2.x().add(tc.multiply(d2.x())),
                p2.y().add(tc.multiply(d2.y())),
                p2.z().add(tc.multiply(d2.z())));

        // Check if they're actually the same point (intersection)
        double dist = new Vector3D(
                point1.x().subtract(point2.x()),
                point1.y().subtract(point2.y()),
                point1.z().subtract(point2.z())).norm().doubleValue();

        if (dist < 1e-10) {
            return point1;
        }

        return null; // Skew lines
    }

    /**
     * Computes the closest point on this line to another line.
     * 
     * @param other the other line
     * @return the closest point on this line to the other line
     */
    public Vector3D closestPoint(Line3D other) {
        Vector3D p1 = this.point;
        Vector3D d1 = this.direction;
        Vector3D p2 = other.point;
        Vector3D d2 = other.direction;

        // w0 = p1 - p2
        Vector3D w0 = new Vector3D(
                p1.x().subtract(p2.x()),
                p1.y().subtract(p2.y()),
                p1.z().subtract(p2.z()));

        Real a = dot(d1, d1);
        Real b = dot(d1, d2);
        Real c = dot(d2, d2);
        Real d = dot(d1, w0);
        Real e = dot(d2, w0);

        Real denom = a.multiply(c).subtract(b.multiply(b));

        Real sc;
        if (Math.abs(denom.doubleValue()) < 1e-12) {
            // Lines are parallel, any point on this line works, use projection
            sc = Real.ZERO;
        } else {
            sc = b.multiply(e).subtract(c.multiply(d)).divide(denom);
        }

        return new Vector3D(
                p1.x().add(sc.multiply(d1.x())),
                p1.y().add(sc.multiply(d1.y())),
                p1.z().add(sc.multiply(d1.z())));
    }

    private static Real dot(Vector3D a, Vector3D b) {
        return a.x().multiply(b.x())
                .add(a.y().multiply(b.y()))
                .add(a.z().multiply(b.z()));
    }
}
