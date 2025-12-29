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
 * Represents a plane in 3D Euclidean space.
 * <p>
 * Defined by a normal vector N and a point P: (X - P) . N = 0
 * or ax + by + cz + d = 0.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Plane3D implements GeometricObject<Point3D> {

    private final Vector3D normal;
    private final Vector3D point;
    private final Real d; // The constant term in ax + by + cz + d = 0

    /**
     * Creates a plane from a point and a normal vector.
     * 
     * @param point  a point on the plane
     * @param normal the normal vector to the plane (will be normalized)
     */
    /**
     * Creates a plane from 3 points.
     * Computes the normal from the cross product of (b-a) and (c-a).
     */
    public static Plane3D fromPoints(Point3D a, Point3D b, Point3D c) {
        Vector3D v1 = b.subtract(a);
        Vector3D v2 = c.subtract(a);
        Vector3D normal = v1.cross(v2).normalize();
        return new Plane3D(new Vector3D(a.getX(), a.getY(), a.getZ()), normal);
    }

    public Plane3D(Vector3D point, Vector3D normal) {
        this.point = point;

        if (normal.norm().equals(Real.ZERO)) {
            // This might happen with degenerate triangles in CSG
            // Fallback or throw? CSG usually prefers robustness.
            // We'll throw for now as degenerate planes are invalid.
            throw new IllegalArgumentException("Normal vector cannot be zero");
        }

        Real normVal = normal.norm();
        // Normalize if not already (or close to 1)
        if (normVal.subtract(Real.ONE).abs().doubleValue() > 1e-6) {
            this.normal = normal.divide(normVal);
        } else {
            this.normal = normal;
        }

        // d = - (n . p)
        Real dot = this.normal.dot(point);
        this.d = dot.negate();
    }

    /**
     * Returns a flipped version of this plane (negated normal and d).
     */
    public Plane3D flipped() {
        return new Plane3D(point, normal.negate());
    }

    // Legacy support / alias
    public Plane3D flip() {
        return flipped();
    }

    public Vector3D getNormal() {
        return normal;
    }

    public Vector3D getPoint() {
        return point;
    }

    /**
     * Calculates the signed distance from a point to this plane.
     * Positive if on the side of the normal, negative otherwise.
     * 
     * @param p the point
     * @return the signed distance
     */
    public Real distance(Vector3D p) {
        // dist = (p . n) + d
        Real dot = p.x().multiply(normal.x())
                .add(p.y().multiply(normal.y()))
                .add(p.z().multiply(normal.z()));

        return dot.add(d);
    }

    /**
     * Projects a point onto this plane.
     * 
     * @param p the point to project
     * @return the projected point on the plane
     */
    public Vector3D project(Vector3D p) {
        // proj = p - distance * normal
        Real dist = distance(p);

        Real px = p.x().subtract(dist.multiply(normal.x()));
        Real py = p.y().subtract(dist.multiply(normal.y()));
        Real pz = p.z().subtract(dist.multiply(normal.z()));

        return new Vector3D(px, py, pz);
    }

    /**
     * Returns a negated version of this plane (flips the normal).
     * 
     * @return the negated plane
     */
    public Plane3D negate() {
        return new Plane3D(point, new Vector3D(
                normal.x().negate(),
                normal.y().negate(),
                normal.z().negate()));
    }

    // GeometricObject implementation
    @Override
    public int dimension() {
        return 2; // A plane is 2-dimensional
    }

    @Override
    public int ambientDimension() {
        return 3; // Lives in 3D space
    }

    public boolean containsPoint(Point3D p) {
        // Convert Point3D to Vector3D for distance check
        Vector3D v = new Vector3D(p.getX(), p.getY(), p.getZ());
        return Math.abs(distance(v).doubleValue()) < 1e-12;
    }

    @Override
    public String description() {
        return "Plane3D through " + point + " with normal " + normal;
    }

    /**
     * Computes the intersection of this plane with a line.
     * 
     * @param line the line
     * @return the intersection point, or null if the line is parallel to the plane
     */
    public Vector3D intersection(Line3D line) {
        // Plane equation: n . (p - p0) = 0, or n . p + d = 0
        // Line: p = L_point + t * L_dir
        // Substituting: n . (L_point + t * L_dir) + d = 0
        // t = -(n . L_point + d) / (n . L_dir)

        Vector3D lineDir = line.getDirection();
        Vector3D linePoint = line.getPoint();

        // n . lineDir
        Real denom = normal.x().multiply(lineDir.x())
                .add(normal.y().multiply(lineDir.y()))
                .add(normal.z().multiply(lineDir.z()));

        // Check if parallel (denom ~= 0)
        if (Math.abs(denom.doubleValue()) < 1e-12) {
            return null; // Line is parallel to plane
        }

        // n . linePoint + d
        Real numer = normal.x().multiply(linePoint.x())
                .add(normal.y().multiply(linePoint.y()))
                .add(normal.z().multiply(linePoint.z()))
                .add(d);

        Real t = numer.negate().divide(denom);

        // p = linePoint + t * lineDir
        return new Vector3D(
                linePoint.x().add(t.multiply(lineDir.x())),
                linePoint.y().add(t.multiply(lineDir.y())),
                linePoint.z().add(t.multiply(lineDir.z())));
    }

    /**
     * Computes the intersection of this plane with another plane.
     * 
     * @param other the other plane
     * @return the intersection line, or null if planes are parallel
     */
    public Line3D intersection(Plane3D other) {
        // Direction of intersection line is cross product of normals
        Vector3D dir = this.normal.cross(other.normal);

        // Check if parallel (cross product magnitude ~= 0)
        if (dir.norm().doubleValue() < 1e-12) {
            return null; // Planes are parallel
        }

        // Find a point on the intersection line
        // Solve the system of two plane equations with z=0 (or x=0 or y=0 depending on
        // which works)
        // n1.x*x + n1.y*y + n1.z*z + d1 = 0
        // n2.x*x + n2.y*y + n2.z*z + d2 = 0

        // Try z=0 first, solve for x,y
        double n1x = this.normal.x().doubleValue();
        double n1y = this.normal.y().doubleValue();
        double n2x = other.normal.x().doubleValue();
        double n2y = other.normal.y().doubleValue();
        double d1 = this.d.doubleValue();
        double d2 = other.d.doubleValue();

        double det = n1x * n2y - n1y * n2x;

        Real px, py, pz;
        if (Math.abs(det) > 1e-12) {
            // z = 0 works
            double x = (-d1 * n2y + d2 * n1y) / det;
            double y = (d1 * n2x - d2 * n1x) / det;
            px = Real.of(x);
            py = Real.of(y);
            pz = Real.ZERO;
        } else {
            // Try y=0
            double n1z = this.normal.z().doubleValue();
            double n2z = other.normal.z().doubleValue();
            det = n1x * n2z - n1z * n2x;

            if (Math.abs(det) > 1e-12) {
                double x = (-d1 * n2z + d2 * n1z) / det;
                double z = (d1 * n2x - d2 * n1x) / det;
                px = Real.of(x);
                py = Real.ZERO;
                pz = Real.of(z);
            } else {
                // Try x=0
                det = n1y * n2z - n1z * n2y;
                double y = (-d1 * n2z + d2 * n1z) / det;
                double z = (d1 * n2y - d2 * n1y) / det;
                px = Real.ZERO;
                py = Real.of(y);
                pz = Real.of(z);
            }
        }

        return new Line3D(new Vector3D(px, py, pz), dir);
    }
}
