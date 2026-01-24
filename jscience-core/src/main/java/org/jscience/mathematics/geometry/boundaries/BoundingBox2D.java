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
import org.jscience.mathematics.numbers.real.Real;

import java.util.List;
import java.util.Objects;

/**
 * Axis-aligned bounding box in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BoundingBox2D implements BoundingBox<Point2D> {

    private static final long serialVersionUID = 1L;

    private final Point2D min;
    private final Point2D max;

    /**
     * Creates a bounding box from min and max corners.
     *
     * @param min the minimum corner (lower-left)
     * @param max the maximum corner (upper-right)
     */
    public BoundingBox2D(Point2D min, Point2D max) {
        this.min = Objects.requireNonNull(min, "Min cannot be null");
        this.max = Objects.requireNonNull(max, "Max cannot be null");
    }

    /**
     * Creates a bounding box from coordinate values.
     */
    public BoundingBox2D(Real minX, Real minY, Real maxX, Real maxY) {
        this(new Point2D(minX, minY), new Point2D(maxX, maxY));
    }

    /**
     * Creates a bounding box enclosing all points.
     *
     * @param points the points to enclose
     * @return the bounding box
     */
    public static BoundingBox2D fromPoints(List<Point2D> points) {
        if (points == null || points.isEmpty()) {
            return new BoundingBox2D(Point2D.ORIGIN, Point2D.ORIGIN);
        }

        Real minX = points.get(0).getX();
        Real maxX = minX;
        Real minY = points.get(0).getY();
        Real maxY = minY;

        for (Point2D p : points) {
            if (p.getX().compareTo(minX) < 0) minX = p.getX();
            if (p.getX().compareTo(maxX) > 0) maxX = p.getX();
            if (p.getY().compareTo(minY) < 0) minY = p.getY();
            if (p.getY().compareTo(maxY) > 0) maxY = p.getY();
        }

        return new BoundingBox2D(new Point2D(minX, minY), new Point2D(maxX, maxY));
    }

    @Override
    public Point2D getMin() {
        return min;
    }

    @Override
    public Point2D getMax() {
        return max;
    }

    /**
     * Returns the width (x extent).
     * @return width
     */
    public Real getWidth() {
        return max.getX().subtract(min.getX());
    }

    /**
     * Returns the height (y extent).
     * @return height
     */
    public Real getHeight() {
        return max.getY().subtract(min.getY());
    }

    /**
     * Returns the center point.
     * @return center
     */
    public Point2D getCenter() {
        return new Point2D(
            min.getX().add(max.getX()).divide(Real.of(2)),
            min.getY().add(max.getY()).divide(Real.of(2))
        );
    }

    /**
     * Returns the area.
     * @return area
     */
    public Real getArea() {
        return getWidth().multiply(getHeight());
    }

    @Override
    public boolean contains(Point2D point) {
        if (point == null) return false;
        return point.getX().compareTo(min.getX()) >= 0 &&
               point.getX().compareTo(max.getX()) <= 0 &&
               point.getY().compareTo(min.getY()) >= 0 &&
               point.getY().compareTo(max.getY()) <= 0;
    }

    @Override
    public boolean intersects(BoundingBox<Point2D> other) {
        if (other == null) return false;
        Point2D oMin = other.getMin();
        Point2D oMax = other.getMax();
        
        return min.getX().compareTo(oMax.getX()) <= 0 &&
               max.getX().compareTo(oMin.getX()) >= 0 &&
               min.getY().compareTo(oMax.getY()) <= 0 &&
               max.getY().compareTo(oMin.getY()) >= 0;
    }

    @Override
    public BoundingBox<Point2D> include(Point2D point) {
        if (point == null) return this;
        
        Real newMinX = point.getX().compareTo(min.getX()) < 0 ? point.getX() : min.getX();
        Real newMinY = point.getY().compareTo(min.getY()) < 0 ? point.getY() : min.getY();
        Real newMaxX = point.getX().compareTo(max.getX()) > 0 ? point.getX() : max.getX();
        Real newMaxY = point.getY().compareTo(max.getY()) > 0 ? point.getY() : max.getY();
        
        return new BoundingBox2D(new Point2D(newMinX, newMinY), new Point2D(newMaxX, newMaxY));
    }

    @Override
    public BoundingBox<Point2D> merge(BoundingBox<Point2D> other) {
        if (other == null) return this;
        
        Point2D oMin = other.getMin();
        Point2D oMax = other.getMax();
        
        Real newMinX = oMin.getX().compareTo(min.getX()) < 0 ? oMin.getX() : min.getX();
        Real newMinY = oMin.getY().compareTo(min.getY()) < 0 ? oMin.getY() : min.getY();
        Real newMaxX = oMax.getX().compareTo(max.getX()) > 0 ? oMax.getX() : max.getX();
        Real newMaxY = oMax.getY().compareTo(max.getY()) > 0 ? oMax.getY() : max.getY();
        
        return new BoundingBox2D(new Point2D(newMinX, newMinY), new Point2D(newMaxX, newMaxY));
    }

    @Override
    public String toString() {
        return String.format("BoundingBox2D[(%s,%s)-(%s,%s)]", 
            min.getX(), min.getY(), max.getX(), max.getY());
    }
}
