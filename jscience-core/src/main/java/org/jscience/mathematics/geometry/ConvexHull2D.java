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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.geometry.boundaries.ConvexPolygon2D;
import org.jscience.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Robust 2D Convex Hull implementation using the Monotone Chain algorithm.
 * <p>
 * Time complexity: O(n log n)
 * <p>
 * The algorithm:
 * <ol>
 *   <li>Sort points lexicographically (by x, then by y)</li>
 *   <li>Build lower hull by processing points left-to-right</li>
 *   <li>Build upper hull by processing points right-to-left</li>
 *   <li>Concatenate hulls (removing duplicate endpoints)</li>
 * </ol>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Convex_hull_algorithms">Convex Hull Algorithms</a>
 */
public final class ConvexHull2D {

    private ConvexHull2D() {}

    /**
     * Computes the convex hull of a set of 2D points.
     *
     * @param points the input points
     * @return the vertices of the convex hull in counter-clockwise order
     */
    public static List<Point2D> compute(List<Point2D> points) {
        if (points == null || points.size() <= 2) {
            return new ArrayList<>(points != null ? points : List.of());
        }

        int n = points.size();
        Point2D[] sorted = points.toArray(new Point2D[0]);
        Arrays.sort(sorted, (a, b) -> {
            int cmp = a.getX().compareTo(b.getX());
            if (cmp != 0) return cmp;
            return a.getY().compareTo(b.getY());
        });

        Point2D[] hull = new Point2D[2 * n];
        int k = 0;

        // Build lower hull
        for (int i = 0; i < n; i++) {
            while (k >= 2 && crossProduct(hull[k - 2], hull[k - 1], sorted[i]).compareTo(Real.ZERO) <= 0) {
                k--;
            }
            hull[k++] = sorted[i];
        }

        // Build upper hull
        for (int i = n - 2, t = k + 1; i >= 0; i--) {
            while (k >= t && crossProduct(hull[k - 2], hull[k - 1], sorted[i]).compareTo(Real.ZERO) <= 0) {
                k--;
            }
            hull[k++] = sorted[i];
        }

        List<Point2D> result = new ArrayList<>(k - 1);
        for (int i = 0; i < k - 1; i++) {
            result.add(hull[i]);
        }
        return result;
    }

    /**
     * Computes the convex hull from an array.
     *
     * @param points the input points
     * @return the hull vertices
     */
    public static List<Point2D> compute(Point2D... points) {
        return compute(Arrays.asList(points));
    }

    /**
     * Creates a ConvexPolygon2D from the hull.
     *
     * @param points the input points
     * @return a convex polygon boundary
     */
    public static ConvexPolygon2D computePolygon(List<Point2D> points) {
        List<Point2D> hull = compute(points);
        if (hull.size() < 3) {
            throw new IllegalArgumentException("Points are collinear or insufficient");
        }
        return new ConvexPolygon2D(hull);
    }

    /**
     * Checks if a set of points is already convex (in convex position).
     *
     * @param points the points to check (assumed in order)
     * @return true if convex
     */
    public static boolean isConvex(List<Point2D> points) {
        if (points.size() < 3) return true;
        
        Boolean sign = null;
        int n = points.size();
        
        for (int i = 0; i < n; i++) {
            Real cross = crossProduct(
                points.get(i),
                points.get((i + 1) % n),
                points.get((i + 2) % n)
            );
            
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

    /**
     * Computes the area of the convex hull.
     *
     * @param points the input points
     * @return the area
     */
    public static Real computeArea(List<Point2D> points) {
        List<Point2D> hull = compute(points);
        if (hull.size() < 3) return Real.ZERO;
        
        // Shoelace formula
        Real sum = Real.ZERO;
        int n = hull.size();
        for (int i = 0; i < n; i++) {
            Point2D curr = hull.get(i);
            Point2D next = hull.get((i + 1) % n);
            sum = sum.add(curr.getX().multiply(next.getY())
                        .subtract(next.getX().multiply(curr.getY())));
        }
        return sum.abs().divide(Real.of(2));
    }

    /**
     * Computes the perimeter of the convex hull.
     *
     * @param points the input points
     * @return the perimeter
     */
    public static Real computePerimeter(List<Point2D> points) {
        List<Point2D> hull = compute(points);
        if (hull.size() < 2) return Real.ZERO;
        
        Real sum = Real.ZERO;
        int n = hull.size();
        for (int i = 0; i < n; i++) {
            sum = sum.add(hull.get(i).distanceTo(hull.get((i + 1) % n)));
        }
        return sum;
    }

    /**
     * Finds the point on the hull farthest from a given point.
     *
     * @param points      the input points
     * @param queryPoint  the query point
     * @return the farthest hull vertex
     */
    public static Point2D findFarthest(List<Point2D> points, Point2D queryPoint) {
        List<Point2D> hull = compute(points);
        if (hull.isEmpty()) return null;
        
        Point2D farthest = hull.get(0);
        Real maxDist = farthest.distanceTo(queryPoint);
        
        for (Point2D p : hull) {
            Real dist = p.distanceTo(queryPoint);
            if (dist.compareTo(maxDist) > 0) {
                maxDist = dist;
                farthest = p;
            }
        }
        return farthest;
    }

    /**
     * Computes the diameter of the convex hull (rotating calipers).
     *
     * @param points the input points
     * @return the diameter (maximum distance between any two hull points)
     */
    public static Real computeDiameter(List<Point2D> points) {
        List<Point2D> hull = compute(points);
        if (hull.size() < 2) return Real.ZERO;
        
        // Simplified: brute force for now (O(n^2))
        // TODO: Implement rotating calipers for O(n)
        Real maxDist = Real.ZERO;
        int n = hull.size();
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Real dist = hull.get(i).distanceTo(hull.get(j));
                if (dist.compareTo(maxDist) > 0) {
                    maxDist = dist;
                }
            }
        }
        return maxDist;
    }

    /**
     * Computes the cross product of vectors (b-a) × (c-a).
     * Positive = counter-clockwise, Negative = clockwise, Zero = collinear.
     */
    private static Real crossProduct(Point2D a, Point2D b, Point2D c) {
        return b.getX().subtract(a.getX()).multiply(c.getY().subtract(a.getY()))
               .subtract(b.getY().subtract(a.getY()).multiply(c.getX().subtract(a.getX())));
    }
}
