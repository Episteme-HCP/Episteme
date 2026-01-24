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

import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.numbers.real.Real;

import java.util.List;
import java.util.Objects;

/**
 * Axis-aligned bounding box in 3D.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BoundingBox3D implements BoundingBox<Point3D> {

    private static final long serialVersionUID = 1L;

    private final Point3D min;
    private final Point3D max;

    public BoundingBox3D(Point3D min, Point3D max) {
        this.min = Objects.requireNonNull(min);
        this.max = Objects.requireNonNull(max);
    }

    public static BoundingBox3D fromPoints(List<Point3D> points) {
        if (points == null || points.isEmpty()) {
            return new BoundingBox3D(Point3D.ORIGIN, Point3D.ORIGIN);
        }

        Real minX = points.get(0).getX(), maxX = minX;
        Real minY = points.get(0).getY(), maxY = minY;
        Real minZ = points.get(0).getZ(), maxZ = minZ;

        for (Point3D p : points) {
            if (p.getX().compareTo(minX) < 0) minX = p.getX();
            if (p.getX().compareTo(maxX) > 0) maxX = p.getX();
            if (p.getY().compareTo(minY) < 0) minY = p.getY();
            if (p.getY().compareTo(maxY) > 0) maxY = p.getY();
            if (p.getZ().compareTo(minZ) < 0) minZ = p.getZ();
            if (p.getZ().compareTo(maxZ) > 0) maxZ = p.getZ();
        }

        return new BoundingBox3D(new Point3D(minX, minY, minZ), new Point3D(maxX, maxY, maxZ));
    }

    @Override
    public Point3D getMin() { return min; }

    @Override
    public Point3D getMax() { return max; }

    public Real getWidth() { return max.getX().subtract(min.getX()); }
    public Real getHeight() { return max.getY().subtract(min.getY()); }
    public Real getDepth() { return max.getZ().subtract(min.getZ()); }

    public Point3D getCenter() {
        return new Point3D(
            min.getX().add(max.getX()).divide(Real.of(2)),
            min.getY().add(max.getY()).divide(Real.of(2)),
            min.getZ().add(max.getZ()).divide(Real.of(2))
        );
    }

    public Real getVolume() {
        return getWidth().multiply(getHeight()).multiply(getDepth());
    }

    @Override
    public boolean contains(Point3D point) {
        if (point == null) return false;
        return point.getX().compareTo(min.getX()) >= 0 &&
               point.getX().compareTo(max.getX()) <= 0 &&
               point.getY().compareTo(min.getY()) >= 0 &&
               point.getY().compareTo(max.getY()) <= 0 &&
               point.getZ().compareTo(min.getZ()) >= 0 &&
               point.getZ().compareTo(max.getZ()) <= 0;
    }

    @Override
    public boolean intersects(BoundingBox<Point3D> other) {
        if (other == null) return false;
        Point3D oMin = other.getMin();
        Point3D oMax = other.getMax();
        
        return min.getX().compareTo(oMax.getX()) <= 0 &&
               max.getX().compareTo(oMin.getX()) >= 0 &&
               min.getY().compareTo(oMax.getY()) <= 0 &&
               max.getY().compareTo(oMin.getY()) >= 0 &&
               min.getZ().compareTo(oMax.getZ()) <= 0 &&
               max.getZ().compareTo(oMin.getZ()) >= 0;
    }

    @Override
    public BoundingBox<Point3D> include(Point3D point) {
        if (point == null) return this;
        
        Real newMinX = point.getX().compareTo(min.getX()) < 0 ? point.getX() : min.getX();
        Real newMinY = point.getY().compareTo(min.getY()) < 0 ? point.getY() : min.getY();
        Real newMinZ = point.getZ().compareTo(min.getZ()) < 0 ? point.getZ() : min.getZ();
        Real newMaxX = point.getX().compareTo(max.getX()) > 0 ? point.getX() : max.getX();
        Real newMaxY = point.getY().compareTo(max.getY()) > 0 ? point.getY() : max.getY();
        Real newMaxZ = point.getZ().compareTo(max.getZ()) > 0 ? point.getZ() : max.getZ();
        
        return new BoundingBox3D(
            new Point3D(newMinX, newMinY, newMinZ),
            new Point3D(newMaxX, newMaxY, newMaxZ)
        );
    }

    @Override
    public BoundingBox<Point3D> merge(BoundingBox<Point3D> other) {
        if (other == null) return this;
        
        Point3D oMin = other.getMin();
        Point3D oMax = other.getMax();
        
        Real newMinX = oMin.getX().compareTo(min.getX()) < 0 ? oMin.getX() : min.getX();
        Real newMinY = oMin.getY().compareTo(min.getY()) < 0 ? oMin.getY() : min.getY();
        Real newMinZ = oMin.getZ().compareTo(min.getZ()) < 0 ? oMin.getZ() : min.getZ();
        Real newMaxX = oMax.getX().compareTo(max.getX()) > 0 ? oMax.getX() : max.getX();
        Real newMaxY = oMax.getY().compareTo(max.getY()) > 0 ? oMax.getY() : max.getY();
        Real newMaxZ = oMax.getZ().compareTo(max.getZ()) > 0 ? oMax.getZ() : max.getZ();
        
        return new BoundingBox3D(
            new Point3D(newMinX, newMinY, newMinZ),
            new Point3D(newMaxX, newMaxY, newMaxZ)
        );
    }

    @Override
    public String toString() {
        return String.format("BoundingBox3D[%s - %s]", min, max);
    }
}
