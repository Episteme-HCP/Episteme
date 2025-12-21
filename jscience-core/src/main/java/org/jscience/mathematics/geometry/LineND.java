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

/**
 * Represents an N-dimensional line.
 * <p>
 * A line is defined by a point and a direction vector. It extends infinitely
 * in both directions along the direction vector.
 * </p>
 * <p>
 * Parametric form: L(t) = point + t * direction, where t ∈ ℝ
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LineND implements GeometricObject<PointND> {

    private final PointND point;
    private final Vector<Real> direction;

    /**
     * Creates a line from a point and direction vector.
     * 
     * @param point     a point on the line
     * @param direction the direction vector (will be normalized)
     */
    public LineND(PointND point, Vector<Real> direction) {
        if (point == null || direction == null) {
            throw new IllegalArgumentException("Point and direction cannot be null");
        }
        if (point.ambientDimension() != direction.dimension()) {
            throw new IllegalArgumentException("Point and direction must have same dimension");
        }
        this.point = point;
        this.direction = direction.normalize();
    }

    /**
     * Creates a line passing through two points.
     * 
     * @param p1 the first point
     * @param p2 the second point
     */
    public static LineND through(PointND p1, PointND p2) {
        if (p1.ambientDimension() != p2.ambientDimension()) {
            throw new IllegalArgumentException("Points must have same dimension");
        }
        Vector<Real> direction = p2.toVector().subtract(p1.toVector());
        return new LineND(p1, direction);
    }

    @Override
    public int dimension() {
        return 1; // A line is 1-dimensional
    }

    @Override
    public int ambientDimension() {
        return point.ambientDimension();
    }

    public PointND getPoint() {
        return point;
    }

    public Vector<Real> getDirection() {
        return direction;
    }

    /**
     * Evaluates the line at parameter t.
     * <p>
     * Returns: point + t * direction
     * </p>
     * 
     * @param t the parameter
     * @return the point at parameter t
     */
    public PointND at(Real t) {
        return point.translate(direction.multiply(t));
    }

    public boolean containsPoint(PointND p) {
        if (p.ambientDimension() != this.ambientDimension()) {
            return false;
        }

        // Check if (p - point) is parallel to direction
        Vector<Real> diff = p.toVector().subtract(point.toVector());

        // For parallel vectors: diff = k * direction for some scalar k
        // Check by computing cross product (for 3D) or checking proportionality

        // General approach: project diff onto direction and check if residual is zero
        Real projection = diff.dot(direction);
        Vector<Real> projected = direction.multiply(projection);
        Vector<Real> residual = diff.subtract(projected);

        return residual.norm().doubleValue() < 1e-10;
    }

    /**
     * Finds the closest point on this line to a given point.
     * 
     * @param p the point
     * @return the closest point on the line
     */
    public PointND closestPoint(PointND p) {
        if (p.ambientDimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Point must have same dimension as line");
        }

        // Project (p - point) onto direction
        Vector<Real> diff = p.toVector().subtract(point.toVector());
        Real t = diff.dot(direction);

        return at(t);
    }

    /**
     * Computes the distance from a point to this line.
     * 
     * @param p the point
     * @return the distance
     */
    public Real distanceTo(PointND p) {
        PointND closest = closestPoint(p);
        return p.distanceTo(closest);
    }

    /**
     * Checks if this line is parallel to another line.
     * 
     * @param other the other line
     * @return true if parallel
     */
    public boolean isParallelTo(LineND other) {
        if (other.ambientDimension() != this.ambientDimension()) {
            return false;
        }

        // Lines are parallel if directions are parallel
        // Check if cross product is zero (for 3D) or directions are proportional
        Real dot = this.direction.dot(other.direction).abs();
        return Math.abs(dot.doubleValue() - 1.0) < 1e-10; // dot = ±1 means parallel
    }

    /**
     * Finds the intersection point with another line (if it exists).
     * <p>
     * Returns null if lines are parallel or skew (don't intersect).
     * </p>
     * 
     * @param other the other line
     * @return the intersection point, or null if no intersection
     */
    public PointND intersection(LineND other) {
        if (other.ambientDimension() != this.ambientDimension()) {
            return null;
        }

        if (isParallelTo(other)) {
            // Parallel lines: check if they're the same line
            if (this.containsPoint(other.point)) {
                return other.point; // Same line, any point works
            }
            return null; // Parallel but distinct
        }

        // For 2D and 3D, solve the system:
        // point1 + t1*dir1 = point2 + t2*dir2

        // This is a simplified approach for 2D/3D
        // For general ND, this requires solving a linear system

        // Find closest points on both lines
        PointND closest1 = this.closestPoint(other.point);
        PointND closest2 = other.closestPoint(this.point);

        // If they're the same, lines intersect
        if (closest1.distanceTo(closest2).doubleValue() < 1e-10) {
            return closest1;
        }

        return null; // Skew lines
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        return "Line(point=" + point + ", direction=" + direction + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LineND))
            return false;
        LineND other = (LineND) obj;

        // Two lines are equal if they contain the same points
        // Check if other.point is on this line and directions are parallel
        return this.containsPoint(other.point) && this.isParallelTo(other);
    }

    @Override
    public int hashCode() {
        // Hash based on direction (normalized)
        return direction.hashCode();
    }
}
