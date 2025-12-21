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

/**
 * A line in 2D Euclidean space.
 * <p>
 * Represented in parametric form: P(t) = point + t * direction
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Line2D implements GeometricObject<Point2D> {

    private final Point2D point;
    private final Vector2D direction;

    public Line2D(Point2D point, Vector2D direction) {
        this.point = point;
        this.direction = direction.normalize();
    }

    public Point2D getPoint() {
        return point;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public Point2D pointAt(Real t) {
        return point.add(direction.scale(t));
    }

    public Real distanceTo(Point2D p) {
        // Distance from point to line
        Vector2D v = Vector2D.of(p.getX().subtract(point.getX()), p.getY().subtract(point.getY()));
        Real projection = v.dot(direction);
        Point2D closest = pointAt(projection);
        return p.distanceTo(closest);
    }

    // GeometricObject implementation
    @Override
    public int dimension() {
        return 1; // A line is 1-dimensional
    }

    @Override
    public int ambientDimension() {
        return 2; // Lives in 2D space
    }

    public boolean containsPoint(Point2D p) {
        return distanceTo(p).doubleValue() < 1e-12;
    }

    @Override
    public String description() {
        return "Line2D through " + point + " in direction " + direction;
    }

    @Override
    public String toString() {
        return "Line[" + point + " + t" + direction + "]";
    }

    public Vector2D getNormal() {
        return Vector2D.of(direction.getY().negate(), direction.getX());
    }

    /**
     * Calculates the signed distance from a point to this line.
     * Positive if on the side of the normal (left/right depending on system),
     * negative otherwise.
     * 
     * @param p the point
     * @return the signed distance
     */
    public Real signedDistance(Point2D p) {
        Vector2D v = Vector2D.of(p.getX().subtract(point.getX()), p.getY().subtract(point.getY()));
        return v.dot(getNormal());
    }

    /**
     * Returns a flipped version of this line (reverses direction).
     */
    public Line2D flipped() {
        return new Line2D(point, direction.negate());
    }

    /**
     * Computes the intersection point with another line.
     * 
     * @param other the other line
     * @return the intersection point, or null if lines are parallel
     */
    public Point2D intersection(Line2D other) {
        // Parametric form: P1 = point1 + t * dir1, P2 = point2 + s * dir2
        // At intersection: point1 + t * dir1 = point2 + s * dir2

        Real x1 = this.point.getX();
        Real y1 = this.point.getY();
        Real dx1 = this.direction.getX();
        Real dy1 = this.direction.getY();

        Real x2 = other.point.getX();
        Real y2 = other.point.getY();
        Real dx2 = other.direction.getX();
        Real dy2 = other.direction.getY();

        // Solve: dx1 * t - dx2 * s = x2 - x1
        // dy1 * t - dy2 * s = y2 - y1

        // Using Cramer's rule
        Real det = dy1.multiply(dx2).subtract(dx1.multiply(dy2));

        // Check if parallel (det ~= 0)
        if (Math.abs(det.doubleValue()) < 1e-12) {
            return null; // Lines are parallel
        }

        Real diffX = x2.subtract(x1);
        Real diffY = y2.subtract(y1);

        Real t = diffY.multiply(dx2).subtract(diffX.multiply(dy2)).divide(det);

        return Point2D.of(
                x1.add(t.multiply(dx1)),
                y1.add(t.multiply(dy1)));
    }
}