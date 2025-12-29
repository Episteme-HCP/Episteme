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

/**
 * Represents an N-dimensional hyperplane.
 * <p>
 * A hyperplane is an (N-1)-dimensional affine subspace of an N-dimensional
 * space.
 * It divides the space into two half-spaces.
 * </p>
 * <p>
 * Defined by: {x | ⟨x - point, normal⟩ = 0}
 * where point is any point on the hyperplane and normal is the normal vector.
 * </p>
 * <p>
 * Examples:
 * - In 2D: a hyperplane is a line
 * - In 3D: a hyperplane is a plane
 * - In 4D: a hyperplane is a 3D space
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Hyperplane implements GeometricObject<PointND> {

    private final PointND point;
    private final Vector<Real> normal;

    /**
     * Creates a hyperplane from a point and normal vector.
     * 
     * @param point  a point on the hyperplane
     * @param normal the normal vector (will be normalized)
     */
    public Hyperplane(PointND point, Vector<Real> normal) {
        if (point == null || normal == null) {
            throw new IllegalArgumentException("Point and normal cannot be null");
        }
        if (point.ambientDimension() != normal.dimension()) {
            throw new IllegalArgumentException("Point and normal must have same dimension");
        }
        this.point = point;
        this.normal = normal.normalize();
    }

    /**
     * Creates a hyperplane from N points (for N-dimensional space).
     * <p>
     * The points must be affinely independent.
     * </p>
     * 
     * @param points the points defining the hyperplane
     */
    public static Hyperplane through(PointND... points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        int dim = points[0].ambientDimension();
        if (points.length != dim) {
            throw new IllegalArgumentException("Need exactly " + dim + " points for " + dim + "D hyperplane");
        }

        // For 3D plane through 3 points: normal = (p1-p0) × (p2-p0)
        // For general ND: compute normal from (N-1) vectors

        // Simplified for 3D case
        if (dim == 3 && points.length == 3) {
            Vector<Real> v1 = points[1].toVector().subtract(points[0].toVector());
            Vector<Real> v2 = points[2].toVector().subtract(points[0].toVector());
            Vector<Real> normal = v1.cross(v2);
            return new Hyperplane(points[0], normal);
        }

        throw new UnsupportedOperationException("General ND hyperplane construction not yet implemented");
    }

    @Override
    public int dimension() {
        return point.ambientDimension() - 1;
    }

    @Override
    public int ambientDimension() {
        return point.ambientDimension();
    }

    public PointND getPoint() {
        return point;
    }

    public Vector<Real> getNormal() {
        return normal;
    }

    public boolean containsPoint(PointND p) {
        if (p.ambientDimension() != this.ambientDimension()) {
            return false;
        }

        // Check if ⟨p - point, normal⟩ = 0
        Vector<Real> diff = p.toVector().subtract(point.toVector());
        Real dot = diff.dot(normal);

        return Math.abs(dot.doubleValue()) < 1e-10;
    }

    /**
     * Computes the signed distance from a point to this hyperplane.
     * <p>
     * Positive if on the side of the normal, negative otherwise.
     * </p>
     * 
     * @param p the point
     * @return the signed distance
     */
    public Real signedDistance(PointND p) {
        if (p.ambientDimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Point must have same dimension as hyperplane");
        }

        Vector<Real> diff = p.toVector().subtract(point.toVector());
        return diff.dot(normal);
    }

    /**
     * Computes the unsigned distance from a point to this hyperplane.
     * 
     * @param p the point
     * @return the distance
     */
    public Real distanceTo(PointND p) {
        return signedDistance(p).abs();
    }

    /**
     * Projects a point onto this hyperplane.
     * 
     * @param p the point to project
     * @return the projected point on the hyperplane
     */
    public PointND project(PointND p) {
        if (p.ambientDimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Point must have same dimension as hyperplane");
        }

        Real dist = signedDistance(p);
        Vector<Real> offset = normal.multiply(dist);
        return new PointND(p.toVector().subtract(offset));
    }

    /**
     * Checks which side of the hyperplane a point is on.
     * 
     * @param p the point
     * @return positive if on normal side, negative if opposite, 0 if on plane
     */
    public int side(PointND p) {
        Real dist = signedDistance(p);
        double d = dist.doubleValue();
        if (Math.abs(d) < 1e-10)
            return 0;
        return d > 0 ? 1 : -1;
    }

    /**
     * Checks if this hyperplane is parallel to another.
     * 
     * @param other the other hyperplane
     * @return true if parallel
     */
    public boolean isParallelTo(Hyperplane other) {
        if (other.ambientDimension() != this.ambientDimension()) {
            return false;
        }

        // Hyperplanes are parallel if normals are parallel
        Real dot = this.normal.dot(other.normal).abs();
        return Math.abs(dot.doubleValue() - 1.0) < 1e-10;
    }

    /**
     * Finds the intersection with a line.
     * <p>
     * Returns null if line is parallel to the hyperplane.
     * </p>
     * 
     * @param line the line
     * @return the intersection point, or null if parallel
     */
    public PointND intersection(LineND line) {
        if (line.ambientDimension() != this.ambientDimension()) {
            return null;
        }

        // Check if line is parallel to hyperplane
        Real dotDirNormal = line.getDirection().dot(normal);
        if (Math.abs(dotDirNormal.doubleValue()) < 1e-10) {
            // Line is parallel
            if (this.containsPoint(line.getPoint())) {
                return line.getPoint(); // Line lies in hyperplane
            }
            return null; // Line is parallel but not in hyperplane
        }

        // Solve: ⟨(point_line + t*dir) - point_plane, normal⟩ = 0
        // t = ⟨point_plane - point_line, normal⟩ / ⟨dir, normal⟩

        Vector<Real> diff = point.toVector().subtract(line.getPoint().toVector());
        Real numerator = diff.dot(normal);
        Real t = numerator.divide(dotDirNormal);

        return line.at(t);
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        return "Hyperplane(point=" + point + ", normal=" + normal + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Hyperplane))
            return false;
        Hyperplane other = (Hyperplane) obj;

        // Two hyperplanes are equal if they contain the same points
        return this.containsPoint(other.point) && this.isParallelTo(other);
    }

    @Override
    public int hashCode() {
        return normal.hashCode();
    }
}
