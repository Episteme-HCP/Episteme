package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a ray in 3D space defined by an origin and a direction.
 */
public class Ray3D {

    private final Point3D origin;
    private final Vector3D direction;

    public Ray3D(Point3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Point3D getOrigin() {
        return origin;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public Point3D pointAt(Real t) {
        return origin.add(direction.multiply(t));
    }
}
