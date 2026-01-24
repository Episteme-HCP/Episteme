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
import org.jscience.mathematics.geometry.ConvexHull2D;
import org.jscience.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A convex polygon in 2D space.
 * <p>
 * This is the simplest form of 2D boundary - a convex polygon defined by
 * its vertices in counter-clockwise order.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ConvexPolygon2D implements Boundary2D {

    private static final long serialVersionUID = 1L;

    private final List<Point2D> vertices;
    private transient Real cachedArea;
    private transient Real cachedPerimeter;
    private transient Point2D cachedCentroid;
    private transient BoundingBox2D cachedBoundingBox;

    /**
     * Creates a convex polygon from vertices.
     * Vertices should be in counter-clockwise order.
     *
     * @param vertices the polygon vertices
     */
    public ConvexPolygon2D(List<Point2D> vertices) {
        Objects.requireNonNull(vertices, "Vertices cannot be null");
        if (vertices.size() < 3) {
            throw new IllegalArgumentException("Polygon requires at least 3 vertices");
        }
        this.vertices = new ArrayList<>(vertices);
    }

    /**
     * Creates a convex polygon from vertex array.
     *
     * @param vertices the vertices
     */
    public ConvexPolygon2D(Point2D... vertices) {
        this(Arrays.asList(vertices));
    }

    /**
     * Creates a convex polygon from a set of points by computing their convex hull.
     *
     * @param points the input points
     * @return the convex polygon
     */
    public static ConvexPolygon2D fromPoints(List<Point2D> points) {
        List<Point2D> hull = ConvexHull2D.compute(points);
        if (hull.size() < 3) {
            throw new IllegalArgumentException("Points are collinear or insufficient");
        }
        return new ConvexPolygon2D(hull);
    }

    // ===== Factory Methods =====

    /**
     * Creates a regular polygon.
     *
     * @param center   the center point
     * @param radius   distance from center to vertices
     * @param sides    number of sides (minimum 3)
     * @return regular polygon
     */
    public static ConvexPolygon2D regular(Point2D center, Real radius, int sides) {
        if (sides < 3) {
            throw new IllegalArgumentException("Regular polygon needs at least 3 sides");
        }
        List<Point2D> verts = new ArrayList<>(sides);
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides - Math.PI / 2;
            Real x = center.getX().add(radius.multiply(Real.of(Math.cos(angle))));
            Real y = center.getY().add(radius.multiply(Real.of(Math.sin(angle))));
            verts.add(new Point2D(x, y));
        }
        return new ConvexPolygon2D(verts);
    }

    /**
     * Creates a triangle.
     */
    public static ConvexPolygon2D triangle(Point2D a, Point2D b, Point2D c) {
        return new ConvexPolygon2D(a, b, c);
    }

    /**
     * Creates a rectangle.
     */
    public static ConvexPolygon2D rectangle(Point2D min, Point2D max) {
        return new ConvexPolygon2D(
            min,
            new Point2D(max.getX(), min.getY()),
            max,
            new Point2D(min.getX(), max.getY())
        );
    }

    // ===== Boundary2D Implementation =====

    @Override
    public List<Point2D> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

    @Override
    public Point2D getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public Real getArea() {
        if (cachedArea != null) return cachedArea;
        
        // Shoelace formula
        Real sum = Real.ZERO;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Point2D curr = vertices.get(i);
            Point2D next = vertices.get((i + 1) % n);
            sum = sum.add(curr.getX().multiply(next.getY())
                        .subtract(next.getX().multiply(curr.getY())));
        }
        cachedArea = sum.abs().divide(Real.of(2));
        return cachedArea;
    }

    @Override
    public Real getPerimeter() {
        if (cachedPerimeter != null) return cachedPerimeter;
        
        Real sum = Real.ZERO;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Point2D curr = vertices.get(i);
            Point2D next = vertices.get((i + 1) % n);
            sum = sum.add(curr.distanceTo(next));
        }
        cachedPerimeter = sum;
        return cachedPerimeter;
    }

    @Override
    public boolean isConvex() {
        return true; // By definition
    }

    @Override
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    @Override
    public Point2D getCentroid() {
        if (cachedCentroid != null) return cachedCentroid;
        
        Real sumX = Real.ZERO;
        Real sumY = Real.ZERO;
        for (Point2D v : vertices) {
            sumX = sumX.add(v.getX());
            sumY = sumY.add(v.getY());
        }
        int n = vertices.size();
        cachedCentroid = new Point2D(sumX.divide(Real.of(n)), sumY.divide(Real.of(n)));
        return cachedCentroid;
    }

    @Override
    public BoundingBox<Point2D> getBoundingBox() {
        if (cachedBoundingBox != null) return cachedBoundingBox;
        cachedBoundingBox = BoundingBox2D.fromPoints(vertices);
        return cachedBoundingBox;
    }

    @Override
    public boolean contains(Point2D point) {
        if (point == null) return false;
        
        // For convex polygon: point is inside if on the same side of all edges
        int n = vertices.size();
        Boolean sign = null;
        
        for (int i = 0; i < n; i++) {
            Point2D a = vertices.get(i);
            Point2D b = vertices.get((i + 1) % n);
            
            Real cross = crossProduct(a, b, point);
            int currentSign = cross.compareTo(Real.ZERO);
            
            if (currentSign != 0) {
                if (sign == null) {
                    sign = currentSign > 0;
                } else if ((currentSign > 0) != sign) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Real crossProduct(Point2D a, Point2D b, Point2D c) {
        return b.getX().subtract(a.getX()).multiply(c.getY().subtract(a.getY()))
               .subtract(b.getY().subtract(a.getY()).multiply(c.getX().subtract(a.getX())));
    }

    @Override
    public boolean intersects(Boundary<Point2D> other) {
        // Check if any vertex is in the other boundary or vice versa
        for (Point2D v : vertices) {
            if (other.contains(v)) return true;
        }
        if (other instanceof Boundary2D) {
            for (Point2D v : ((Boundary2D) other).getVertices()) {
                if (this.contains(v)) return true;
            }
        }
        // TODO: Edge intersection check for more robust detection
        return false;
    }

    @Override
    public Boundary<Point2D> union(Boundary<Point2D> other) {
        // Create composite with both regions
        CompositeBoundary2D result = new CompositeBoundary2D();
        result.addInclusion(this);
        if (other instanceof Boundary2D) {
            result.addInclusion((Boundary2D) other);
        }
        return result;
    }

    @Override
    public Boundary<Point2D> intersection(Boundary<Point2D> other) {
        // Sutherland-Hodgman for convex-convex intersection
        // Simplified: return null if no intersection
        if (!this.intersects(other)) return null;
        // TODO: Implement proper polygon clipping
        return this;
    }

    @Override
    public Boundary<Point2D> convexHull() {
        return this; // Already convex
    }

    @Override
    public Boundary<Point2D> translate(Point2D offset) {
        List<Point2D> newVerts = new ArrayList<>(vertices.size());
        for (Point2D v : vertices) {
            newVerts.add(new Point2D(v.getX().add(offset.getX()), v.getY().add(offset.getY())));
        }
        return new ConvexPolygon2D(newVerts);
    }

    @Override
    public Boundary<Point2D> scale(Real factor) {
        Point2D center = getCentroid();
        List<Point2D> newVerts = new ArrayList<>(vertices.size());
        for (Point2D v : vertices) {
            Real dx = v.getX().subtract(center.getX()).multiply(factor);
            Real dy = v.getY().subtract(center.getY()).multiply(factor);
            newVerts.add(new Point2D(center.getX().add(dx), center.getY().add(dy)));
        }
        return new ConvexPolygon2D(newVerts);
    }

    @Override
    public Boundary2D rotate(Real angleRadians) {
        return rotateAround(angleRadians, getCentroid());
    }

    @Override
    public Boundary2D rotateAround(Real angleRadians, Point2D pivot) {
        double angle = angleRadians.doubleValue();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        
        List<Point2D> newVerts = new ArrayList<>(vertices.size());
        for (Point2D v : vertices) {
            double dx = v.getX().subtract(pivot.getX()).doubleValue();
            double dy = v.getY().subtract(pivot.getY()).doubleValue();
            double newX = cos * dx - sin * dy;
            double newY = sin * dx + cos * dy;
            newVerts.add(new Point2D(
                pivot.getX().add(Real.of(newX)),
                pivot.getY().add(Real.of(newY))
            ));
        }
        return new ConvexPolygon2D(newVerts);
    }

    @Override
    public Boundary3D extrude(Real height) {
        return ConvexPolyhedron3D.extrude(this, height);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ConvexPolygon2D)) return false;
        ConvexPolygon2D other = (ConvexPolygon2D) obj;
        return vertices.equals(other.vertices);
    }

    @Override
    public int hashCode() {
        return vertices.hashCode();
    }

    @Override
    public String toString() {
        return String.format("ConvexPolygon2D[%d vertices, area=%s]", vertices.size(), getArea());
    }
}
