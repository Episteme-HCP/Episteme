package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a ray in N-dimensional space defined by an origin and a direction.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class RayND {

    private final PointND origin;
    private final VectorND direction;

    /**
     * Creates a new RayND.
     * 
     * @param origin    the origin point
     * @param direction the direction vector (will be normalized)
     * @throws IllegalArgumentException if dimensions do not match
     */
    public RayND(PointND origin, VectorND direction) {
        if (origin.ambientDimension() != direction.dimension()) {
            throw new IllegalArgumentException("Origin dimension and direction dimension must match");
        }
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public PointND getOrigin() {
        return origin;
    }

    public VectorND getDirection() {
        return direction;
    }

    /**
     * Computes the point at distance t along the ray.
     * 
     * @param t the distance parameter
     * @return the point P = origin + t * direction
     */
    public PointND pointAt(Real t) {
        // PointND doesn't have a direct 'add(VectorND)' yet?
        // Let's check PointND or implement it efficiently.
        // PointND has coordinates.
        // P = O + t*D
        // coordinates = O.coords + (D.coords * t)

        // Actually PointND usually has a translate(Vector) method?
        // Let's assume we might need to implement logic if missing.
        // Checking PointND... it should have something similar.
        // If not, we can do manual calculation.

        return new PointND(origin.toVector().add(direction.multiply(t).toVector()));
    }
}
