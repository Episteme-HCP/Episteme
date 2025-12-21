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
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Computational geometry algorithms for 2D.
 * <p>
 * Includes: convex hull, closest pair, line segment intersection, etc.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ComputationalGeometry2D {

    private ComputationalGeometry2D() {
        // Utility class
    }

    /**
     * Convex Hull using Graham Scan - O(n log n).
     * 
     * @param points the input points
     * @return the convex hull as a list of points in counterclockwise order
     */
    public static List<Point2D> convexHull(List<Point2D> points) {
        if (points.size() < 3)
            return new ArrayList<>(points);

        // Find point with lowest y (and leftmost if tie)
        Point2D pivot = points.get(0);
        for (Point2D p : points) {
            if (p.getY().compareTo(pivot.getY()) < 0 ||
                    (p.getY().equals(pivot.getY()) && p.getX().compareTo(pivot.getX()) < 0)) {
                pivot = p;
            }
        }

        final Point2D finalPivot = pivot;
        List<Point2D> sorted = new ArrayList<>(points);
        sorted.remove(pivot);

        // Sort by polar angle
        sorted.sort((p1, p2) -> {
            Real angle1 = polarAngle(finalPivot, p1);
            Real angle2 = polarAngle(finalPivot, p2);
            return angle1.compareTo(angle2);
        });

        List<Point2D> hull = new ArrayList<>();
        hull.add(pivot);
        if (!sorted.isEmpty()) {
            hull.add(sorted.get(0));
        }

        for (int i = 1; i < sorted.size(); i++) {
            while (hull.size() > 1
                    && !isLeftTurn(hull.get(hull.size() - 2), hull.get(hull.size() - 1), sorted.get(i))) {
                hull.remove(hull.size() - 1);
            }
            hull.add(sorted.get(i));
        }

        return hull;
    }

    private static Real polarAngle(Point2D pivot, Point2D p) {
        Real dx = p.getX().subtract(pivot.getX());
        Real dy = p.getY().subtract(pivot.getY());
        return Real.of(Math.atan2(dy.doubleValue(), dx.doubleValue()));
    }

    private static boolean isLeftTurn(Point2D a, Point2D b, Point2D c) {
        Real cross = b.getX().subtract(a.getX()).multiply(c.getY().subtract(a.getY()))
                .subtract(b.getY().subtract(a.getY()).multiply(c.getX().subtract(a.getX())));
        return cross.compareTo(Real.ZERO) > 0;
    }

    /**
     * Closest pair of points - O(n log n) divide and conquer.
     * 
     * @param points the input points
     * @return array of two closest points, or null if fewer than 2 points
     */
    public static Point2D[] closestPair(List<Point2D> points) {
        if (points.size() < 2)
            return null;

        List<Point2D> sortedByX = new ArrayList<>(points);
        sortedByX.sort(Comparator.comparing(Point2D::getX));

        return closestPairRecursive(sortedByX);
    }

    private static Point2D[] closestPairRecursive(List<Point2D> points) {
        if (points.size() <= 3) {
            return bruteForceClosest(points);
        }

        int mid = points.size() / 2;
        Point2D midPoint = points.get(mid);

        List<Point2D> left = new ArrayList<>(points.subList(0, mid));
        List<Point2D> right = new ArrayList<>(points.subList(mid, points.size()));

        Point2D[] leftPair = closestPairRecursive(left);
        Point2D[] rightPair = closestPairRecursive(right);

        Real minDist = leftPair[0].distanceTo(leftPair[1]);
        Point2D[] result = leftPair;

        if (rightPair[0].distanceTo(rightPair[1]).compareTo(minDist) < 0) {
            minDist = rightPair[0].distanceTo(rightPair[1]);
            result = rightPair;
        }

        // Check strip
        List<Point2D> strip = new ArrayList<>();
        for (Point2D p : points) {
            if (p.getX().subtract(midPoint.getX()).abs().compareTo(minDist) < 0) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size()
                    && strip.get(j).getY().subtract(strip.get(i).getY()).compareTo(minDist) < 0; j++) {
                Real d = strip.get(i).distanceTo(strip.get(j));
                if (d.compareTo(minDist) < 0) {
                    minDist = d;
                    result = new Point2D[] { strip.get(i), strip.get(j) };
                }
            }
        }

        return result;
    }

    private static Point2D[] bruteForceClosest(List<Point2D> points) {
        Real minDist = Real.of(Double.MAX_VALUE);
        Point2D[] result = new Point2D[] { points.get(0), points.get(1) };

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Real d = points.get(i).distanceTo(points.get(j));
                if (d.compareTo(minDist) < 0) {
                    minDist = d;
                    result = new Point2D[] { points.get(i), points.get(j) };
                }
            }
        }

        return result;
    }

    /**
     * Check if two line segments intersect.
     * 
     * @param p1 first endpoint of segment 1
     * @param q1 second endpoint of segment 1
     * @param p2 first endpoint of segment 2
     * @param q2 second endpoint of segment 2
     * @return true if segments intersect
     */
    public static boolean segmentsIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        // Collinear cases
        if (o1 == 0 && onSegment(p1, p2, q1))
            return true;
        if (o2 == 0 && onSegment(p1, q2, q1))
            return true;
        if (o3 == 0 && onSegment(p2, p1, q2))
            return true;
        if (o4 == 0 && onSegment(p2, q1, q2))
            return true;

        return false;
    }

    /**
     * Computes the orientation of triplet (p, q, r).
     * 
     * @return 0 if collinear, 1 if clockwise, 2 if counterclockwise
     */
    public static int orientation(Point2D p, Point2D q, Point2D r) {
        Real val = q.getY().subtract(p.getY()).multiply(r.getX().subtract(q.getX()))
                .subtract(q.getX().subtract(p.getX()).multiply(r.getY().subtract(q.getY())));

        if (val.compareTo(Real.ZERO) == 0)
            return 0;
        return val.compareTo(Real.ZERO) > 0 ? 1 : 2;
    }

    private static boolean onSegment(Point2D p, Point2D q, Point2D r) {
        return q.getX().compareTo(p.getX().min(r.getX())) >= 0 && q.getX().compareTo(p.getX().max(r.getX())) <= 0 &&
                q.getY().compareTo(p.getY().min(r.getY())) >= 0 && q.getY().compareTo(p.getY().max(r.getY())) <= 0;
    }

    /**
     * Computes the area of a polygon defined by its vertices.
     * 
     * @param vertices the polygon vertices in order
     * @return the area (positive if counterclockwise, negative if clockwise)
     */
    public static Real polygonArea(List<Point2D> vertices) {
        if (vertices.size() < 3) {
            return Real.ZERO;
        }

        Real area = Real.ZERO;
        int n = vertices.size();

        for (int i = 0; i < n; i++) {
            Point2D p1 = vertices.get(i);
            Point2D p2 = vertices.get((i + 1) % n);
            area = area.add(p1.getX().multiply(p2.getY()).subtract(p2.getX().multiply(p1.getY())));
        }

        return area.divide(Real.of(2));
    }

    /**
     * Checks if a point is inside a polygon using ray casting.
     * 
     * @param point   the point to test
     * @param polygon the polygon vertices
     * @return true if point is inside the polygon
     */
    public static boolean pointInPolygon(Point2D point, List<Point2D> polygon) {
        int n = polygon.size();
        int count = 0;

        for (int i = 0; i < n; i++) {
            Point2D p1 = polygon.get(i);
            Point2D p2 = polygon.get((i + 1) % n);

            if (point.getY().compareTo(p1.getY().min(p2.getY())) > 0 &&
                    point.getY().compareTo(p1.getY().max(p2.getY())) <= 0) {

                Real xIntersect = p1.getX().add(
                        point.getY().subtract(p1.getY())
                                .divide(p2.getY().subtract(p1.getY()))
                                .multiply(p2.getX().subtract(p1.getX())));

                if (point.getX().compareTo(xIntersect) < 0) {
                    count++;
                }
            }
        }

        return count % 2 == 1;
    }

    // =================================================================================
    // NEW RAY INTERSECTION METHODS
    // =================================================================================

    /**
     * Intersects a ray with a line segment.
     * 
     * @param ray the ray
     * @param p1  first endpoint of segment
     * @param p2  second endpoint of segment
     * @return distance t to intersection, or null if no intersection
     */
    public static Real intersectRaySegment(Ray2D ray, Point2D p1, Point2D p2) {
        Vector2D v1 = Vector2D.of(ray.getOrigin().getX(), ray.getOrigin().getY());
        Vector2D v2 = Vector2D.of(p1.getX(), p1.getY());
        Vector2D v3 = Vector2D.of(p2.getX(), p2.getY());

        Vector2D d1 = ray.getDirection();
        Vector2D d2 = v3.subtract(v2);

        Real cross = d1.getX().multiply(d2.getY()).subtract(d1.getY().multiply(d2.getX()));

        if (cross.abs().doubleValue() < 1e-8)
            return null; // Parallel

        Vector2D vDist = v2.subtract(v1);
        Real t1 = vDist.getX().multiply(d2.getY()).subtract(vDist.getY().multiply(d2.getX())).divide(cross);
        Real t2 = vDist.getX().multiply(d1.getY()).subtract(vDist.getY().multiply(d1.getX())).divide(cross);

        if (t1.doubleValue() >= 0 && t2.doubleValue() >= 0 && t2.doubleValue() <= 1.0) {
            return t1;
        }

        return null;
    }

    /**
     * Intersects a ray with a circle.
     */
    public static Real intersectRayCircle(Ray2D ray, Point2D center, Real radius) {
        Vector2D oc = Vector2D.of(
                ray.getOrigin().getX().subtract(center.getX()),
                ray.getOrigin().getY().subtract(center.getY()));

        Real a = ray.getDirection().dot(ray.getDirection());
        Real b = Real.of(2).multiply(oc.dot(ray.getDirection()));
        Real c = oc.dot(oc).subtract(radius.multiply(radius));

        Real discriminant = b.multiply(b).subtract(Real.of(4).multiply(a).multiply(c));

        if (discriminant.doubleValue() < 0)
            return null;

        Real sqrtDisc = discriminant.sqrt();
        Real t1 = b.negate().subtract(sqrtDisc).divide(Real.of(2).multiply(a));
        Real t2 = b.negate().add(sqrtDisc).divide(Real.of(2).multiply(a));

        if (t1.doubleValue() > 0)
            return t1;
        if (t2.doubleValue() > 0)
            return t2;

        return null;
    }
}