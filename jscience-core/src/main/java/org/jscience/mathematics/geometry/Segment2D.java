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
 * Represents a line segment in 2D space.
 * <p>
 * A 2D segment is defined by two endpoints in 2D Euclidean space.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Segment2D implements GeometricObject<Point2D> {

    private final Point2D start;
    private final Point2D end;

    /**
     * Creates a 2D segment from two endpoints.
     * 
     * @param start the start point
     * @param end   the end point
     */
    public Segment2D(Point2D start, Point2D end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Endpoints cannot be null");
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public int dimension() {
        return 1; // A segment is 1-dimensional
    }

    @Override
    public int ambientDimension() {
        return 2; // 2D space
    }

    public Point2D getStart() {
        return start;
    }

    public Point2D getEnd() {
        return end;
    }

    /**
     * Returns the midpoint of this segment.
     * 
     * @return the midpoint
     */
    public Point2D midpoint() {
        Real midX = start.getX().add(end.getX()).multiply(Real.of(0.5));
        Real midY = start.getY().add(end.getY()).multiply(Real.of(0.5));
        return Point2D.of(midX, midY);
    }

    /**
     * Returns the length of this segment.
     * 
     * @return the length
     */
    public Real length() {
        return start.distanceTo(end);
    }

    /**
     * Interpolates along the segment.
     * 
     * @param t the parameter (0 = start, 1 = end)
     * @return the interpolated point
     */
    public Point2D at(Real t) {
        Real x = start.getX().add(end.getX().subtract(start.getX()).multiply(t));
        Real y = start.getY().add(end.getY().subtract(start.getY()).multiply(t));
        return Point2D.of(x, y);
    }

    public boolean containsPoint(Point2D p) {
        // Check if p is collinear with start and end
        Real dx1 = p.getX().subtract(start.getX());
        Real dy1 = p.getY().subtract(start.getY());
        Real dx2 = end.getX().subtract(start.getX());
        Real dy2 = end.getY().subtract(start.getY());

        // Cross product should be zero for collinearity
        Real cross = dx1.multiply(dy2).subtract(dy1.multiply(dx2));
        if (Math.abs(cross.doubleValue()) > 1e-10) {
            return false;
        }

        // Check if p is between start and end
        Real distStart = start.distanceTo(p);
        Real distEnd = end.distanceTo(p);
        Real segmentLength = length();

        Real sum = distStart.add(distEnd);
        return Math.abs(sum.subtract(segmentLength).doubleValue()) < 1e-10;
    }

    /**
     * Converts to SegmentND.
     * 
     * @return the N-dimensional representation
     */
    public SegmentND toSegmentND() {
        PointND startND = PointND.of(start.getX(), start.getY());
        PointND endND = PointND.of(end.getX(), end.getY());
        return new SegmentND(startND, endND);
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        return "Segment2D(" + start + " Ã¢â€ â€™ " + end + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Segment2D))
            return false;
        Segment2D other = (Segment2D) obj;
        return (start.equals(other.start) && end.equals(other.end)) ||
                (start.equals(other.end) && end.equals(other.start));
    }

    @Override
    public int hashCode() {
        return start.hashCode() + end.hashCode();
    }
}


