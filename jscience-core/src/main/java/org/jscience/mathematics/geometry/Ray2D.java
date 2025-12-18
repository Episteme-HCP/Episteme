package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a ray in 2D space.
 */
public class Ray2D {
    private final Point2D origin;
    private final Vector2D direction;

    public Ray2D(Point2D origin, Vector2D direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Point2D getOrigin() {
        return origin;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public Point2D pointAt(Real t) {
        return origin.add(direction.scale(t));
    }
}
