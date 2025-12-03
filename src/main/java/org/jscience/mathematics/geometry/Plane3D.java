package org.jscience.mathematics.geometry;

import org.jscience.mathematics.number.Real;

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
public class Plane3D {

    private final Vector3D normal;
    private final Vector3D point;
    private final Real d; // The constant term in ax + by + cz + d = 0

    /**
     * Creates a plane from a point and a normal vector.
     * 
     * @param point  a point on the plane
     * @param normal the normal vector to the plane (will be normalized)
     */
    public Plane3D(Vector3D point, Vector3D normal) {
        this.point = point;

        if (normal.norm().equals(Real.ZERO)) {
            throw new IllegalArgumentException("Normal vector cannot be zero");
        }

        Real normVal = normal.norm();
        this.normal = new Vector3D(
                normal.x().divide(normVal),
                normal.y().divide(normVal),
                normal.z().divide(normVal));

        // d = - (n . p)
        // dot product manually to ensure Real result
        Real dot = this.normal.x().multiply(point.x())
                .add(this.normal.y().multiply(point.y()))
                .add(this.normal.z().multiply(point.z()));

        this.d = dot.negate();
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
}
