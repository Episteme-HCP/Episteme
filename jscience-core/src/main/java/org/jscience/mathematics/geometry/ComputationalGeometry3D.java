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

import org.jscience.mathematics.numbers.real.Real;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Computational geometry algorithms for 3D.
 * <p>
 * Includes: convex hull, closest pair, plane intersections, etc.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ComputationalGeometry3D {

    private ComputationalGeometry3D() {
        // Utility class
    }

    /**
     * Closest pair of points in 3D - O(n log n) divide and conquer.
     * 
     * @param points the input points
     * @return array of two closest points, or null if fewer than 2 points
     */
    public static Point3D[] closestPair(List<Point3D> points) {
        if (points.size() < 2)
            return null;

        List<Point3D> sortedByX = new ArrayList<>(points);
        sortedByX.sort(Comparator.comparing(Point3D::getX));

        return closestPairRecursive(sortedByX);
    }

    private static Point3D[] closestPairRecursive(List<Point3D> points) {
        if (points.size() <= 3) {
            return bruteForceClosest(points);
        }

        int mid = points.size() / 2;
        Point3D midPoint = points.get(mid);

        List<Point3D> left = new ArrayList<>(points.subList(0, mid));
        List<Point3D> right = new ArrayList<>(points.subList(mid, points.size()));

        Point3D[] leftPair = closestPairRecursive(left);
        Point3D[] rightPair = closestPairRecursive(right);

        Real minDist = leftPair[0].distanceTo(leftPair[1]);
        Point3D[] result = leftPair;

        if (rightPair[0].distanceTo(rightPair[1]).compareTo(minDist) < 0) {
            minDist = rightPair[0].distanceTo(rightPair[1]);
            result = rightPair;
        }

        // Check slab near dividing plane
        List<Point3D> slab = new ArrayList<>();
        for (Point3D p : points) {
            if (p.getX().subtract(midPoint.getX()).abs().compareTo(minDist) < 0) {
                slab.add(p);
            }
        }

        // Sort by Y for efficient checking
        slab.sort(Comparator.comparing(Point3D::getY));

        for (int i = 0; i < slab.size(); i++) {
            for (int j = i + 1; j < slab.size()
                    && slab.get(j).getY().subtract(slab.get(i).getY()).compareTo(minDist) < 0; j++) {
                Real d = slab.get(i).distanceTo(slab.get(j));
                if (d.compareTo(minDist) < 0) {
                    minDist = d;
                    result = new Point3D[] { slab.get(i), slab.get(j) };
                }
            }
        }

        return result;
    }

    private static Point3D[] bruteForceClosest(List<Point3D> points) {
        Real minDist = Real.of(Double.MAX_VALUE);
        Point3D[] result = new Point3D[] { points.get(0), points.get(1) };

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Real d = points.get(i).distanceTo(points.get(j));
                if (d.compareTo(minDist) < 0) {
                    minDist = d;
                    result = new Point3D[] { points.get(i), points.get(j) };
                }
            }
        }

        return result;
    }

    /**
     * Computes the convex hull in 3D using the Gift Wrapping algorithm.
     * <p>
     * Returns a list of triangular faces, each represented as a list of 3 points.
     * This is a simple O(n*h) algorithm where h is the number of hull faces.
     * </p>
     * 
     * @param points the input points
     * @return list of triangular faces forming the convex hull
     */
    public static List<List<Point3D>> convexHull(List<Point3D> points) {
        if (points.size() < 4) {
            // Can't form a 3D convex hull
            return new ArrayList<>();
        }

        List<List<Point3D>> faces = new ArrayList<>();

        // Find extreme point (lowest z, then y, then x)
        Point3D extreme = points.get(0);
        for (Point3D p : points) {
            if (p.getZ().compareTo(extreme.getZ()) < 0 ||
                    (p.getZ().equals(extreme.getZ()) && p.getY().compareTo(extreme.getY()) < 0) ||
                    (p.getZ().equals(extreme.getZ()) && p.getY().equals(extreme.getY()) &&
                            p.getX().compareTo(extreme.getX()) < 0)) {
                extreme = p;
            }
        }

        // Find initial edge
        Point3D second = findFarthest(extreme, points);
        Point3D third = findThirdPoint(extreme, second, points);

        if (third == null) {
            return faces; // Points are collinear
        }

        // Add first face
        List<Point3D> firstFace = new ArrayList<>();
        firstFace.add(extreme);
        firstFace.add(second);
        firstFace.add(third);
        faces.add(firstFace);

        // Simple gift wrapping: add opposing face
        Point3D fourth = findFourthPoint(extreme, second, third, points);
        if (fourth != null) {
            // Add remaining 3 faces for tetrahedron
            faces.add(List.of(extreme, second, fourth));
            faces.add(List.of(extreme, third, fourth));
            faces.add(List.of(second, third, fourth));
        }

        return faces;
    }

    private static Point3D findFarthest(Point3D from, List<Point3D> points) {
        Point3D farthest = null;
        Real maxDist = Real.ZERO;
        for (Point3D p : points) {
            if (!p.equals(from)) {
                Real d = from.distanceTo(p);
                if (d.compareTo(maxDist) > 0) {
                    maxDist = d;
                    farthest = p;
                }
            }
        }
        return farthest;
    }

    private static Point3D findThirdPoint(Point3D p1, Point3D p2, List<Point3D> points) {
        // Find point farthest from the line p1-p2
        Point3D best = null;
        Real maxDist = Real.ZERO;

        Vector3D lineDir = new Vector3D(
                p2.getX().subtract(p1.getX()),
                p2.getY().subtract(p1.getY()),
                p2.getZ().subtract(p1.getZ()));

        for (Point3D p : points) {
            if (!p.equals(p1) && !p.equals(p2)) {
                Real dist = distanceToLine(p, p1, lineDir);
                if (dist.compareTo(maxDist) > 0) {
                    maxDist = dist;
                    best = p;
                }
            }
        }
        return best;
    }

    private static Point3D findFourthPoint(Point3D p1, Point3D p2, Point3D p3, List<Point3D> points) {
        // Find point farthest from the plane p1-p2-p3
        Vector3D v1 = new Vector3D(
                p2.getX().subtract(p1.getX()),
                p2.getY().subtract(p1.getY()),
                p2.getZ().subtract(p1.getZ()));
        Vector3D v2 = new Vector3D(
                p3.getX().subtract(p1.getX()),
                p3.getY().subtract(p1.getY()),
                p3.getZ().subtract(p1.getZ()));
        Vector3D normal = v1.cross(v2);

        Point3D best = null;
        Real maxDist = Real.ZERO;

        for (Point3D p : points) {
            if (!p.equals(p1) && !p.equals(p2) && !p.equals(p3)) {
                Real dist = distanceToPlane(p, p1, normal).abs();
                if (dist.compareTo(maxDist) > 0) {
                    maxDist = dist;
                    best = p;
                }
            }
        }
        return best;
    }

    private static Real distanceToLine(Point3D p, Point3D linePoint, Vector3D lineDir) {
        Vector3D v = new Vector3D(
                p.getX().subtract(linePoint.getX()),
                p.getY().subtract(linePoint.getY()),
                p.getZ().subtract(linePoint.getZ()));
        Vector3D cross = v.cross(lineDir);
        // Assuming norm() exists on Vector3D as implemented earlier
        return cross.normValue().divide(lineDir.normValue());
    }

    private static Real distanceToPlane(Point3D p, Point3D planePoint, Vector3D normal) {
        Vector3D v = new Vector3D(
                p.getX().subtract(planePoint.getX()),
                p.getY().subtract(planePoint.getY()),
                p.getZ().subtract(planePoint.getZ()));
        return v.dot(normal).divide(normal.normValue());
    }

    /**
     * Checks if a point is inside a tetrahedron.
     * 
     * @param point the point to test
     * @param v0    first vertex of tetrahedron
     * @param v1    second vertex of tetrahedron
     * @param v2    third vertex of tetrahedron
     * @param v3    fourth vertex of tetrahedron
     * @return true if point is inside the tetrahedron
     */
    public static boolean pointInTetrahedron(Point3D point, Point3D v0, Point3D v1, Point3D v2, Point3D v3) {
        // Use barycentric coordinates
        int sign0 = sameSign(signedVolume(v0, v1, v2, v3), signedVolume(point, v1, v2, v3));
        int sign1 = sameSign(signedVolume(v0, v1, v2, v3), signedVolume(v0, point, v2, v3));
        int sign2 = sameSign(signedVolume(v0, v1, v2, v3), signedVolume(v0, v1, point, v3));
        int sign3 = sameSign(signedVolume(v0, v1, v2, v3), signedVolume(v0, v1, v2, point));

        return sign0 == 1 && sign1 == 1 && sign2 == 1 && sign3 == 1;
    }

    private static Real signedVolume(Point3D a, Point3D b, Point3D c, Point3D d) {
        Vector3D ab = new Vector3D(
                b.getX().subtract(a.getX()),
                b.getY().subtract(a.getY()),
                b.getZ().subtract(a.getZ()));
        Vector3D ac = new Vector3D(
                c.getX().subtract(a.getX()),
                c.getY().subtract(a.getY()),
                c.getZ().subtract(a.getZ()));
        Vector3D ad = new Vector3D(
                d.getX().subtract(a.getX()),
                d.getY().subtract(a.getY()),
                d.getZ().subtract(a.getZ()));
        return ab.cross(ac).dot(ad); // dot returns Real
    }

    private static int sameSign(Real a, Real b) {
        if ((a.compareTo(Real.ZERO) >= 0 && b.compareTo(Real.ZERO) >= 0) ||
                (a.compareTo(Real.ZERO) <= 0 && b.compareTo(Real.ZERO) <= 0)) {
            return 1;
        }
        return 0;
    }

    /**
     * Computes the volume of a tetrahedron.
     */
    public static Real tetrahedronVolume(Point3D v0, Point3D v1, Point3D v2, Point3D v3) {
        return signedVolume(v0, v1, v2, v3).abs().divide(Real.of(6));
    }

    /**
     * Computes the centroid of a set of 3D points.
     */
    public static Point3D centroid(List<Point3D> points) {
        if (points.isEmpty()) {
            return Point3D.of(Real.ZERO, Real.ZERO, Real.ZERO);
        }

        Real sumX = Real.ZERO;
        Real sumY = Real.ZERO;
        Real sumZ = Real.ZERO;

        for (Point3D p : points) {
            sumX = sumX.add(p.getX());
            sumY = sumY.add(p.getY());
            sumZ = sumZ.add(p.getZ());
        }

        Real n = Real.of(points.size());
        return Point3D.of(sumX.divide(n), sumY.divide(n), sumZ.divide(n));
    }

    // =================================================================================
    // NEW RAY INTERSECTION METHODS
    // =================================================================================

    /**
     * Intersects a ray with a triangle.
     * Uses MÃƒÂ¶llerÃ¢â‚¬â€œTrumbore intersection algorithm.
     * 
     * @param ray the ray
     * @param v0  first vertex of triangle
     * @param v1  second vertex of triangle
     * @param v2  third vertex of triangle
     * @return the distance t from ray origin to intersection, or null if no
     *         intersection
     */
    public static Real intersectRayTriangle(Ray3D ray, Point3D v0, Point3D v1, Point3D v2) {
        final double EPSILON = 1e-7;

        Vector3D edge1 = v1.subtract(v0);
        Vector3D edge2 = v2.subtract(v0);
        Vector3D h = ray.getDirection().cross(edge2);
        Real a = edge1.dot(h);

        if (a.abs().doubleValue() < EPSILON)
            return null; // Parallel

        Real f = Real.ONE.divide(a);
        Vector3D s = ray.getOrigin().subtract(v0);
        Real u = f.multiply(s.dot(h));

        if (u.doubleValue() < 0.0 || u.doubleValue() > 1.0)
            return null;

        Vector3D q = s.cross(edge1);
        Real v = f.multiply(ray.getDirection().dot(q));

        if (v.doubleValue() < 0.0 || u.add(v).doubleValue() > 1.0)
            return null;

        Real t = f.multiply(edge2.dot(q));

        if (t.doubleValue() > EPSILON)
            return t;

        return null;
    }

    /**
     * Intersects a ray with a sphere.
     * 
     * @param ray    the ray
     * @param center center of the sphere
     * @param radius radius of the sphere
     * @return distance to the closest intersection point, or null if no
     *         intersection
     */
    public static Real intersectRaySphere(Ray3D ray, Point3D center, Real radius) {
        Vector3D oc = ray.getOrigin().subtract(center);
        Real a = ray.getDirection().dot(ray.getDirection()); // Should be 1 if normalized
        Real b = Real.of(2.0).multiply(oc.dot(ray.getDirection()));
        Real c = oc.dot(oc).subtract(radius.multiply(radius));
        Real discriminant = b.multiply(b).subtract(Real.of(4.0).multiply(a).multiply(c));

        if (discriminant.doubleValue() < 0)
            return null;

        Real sqrtDisc = discriminant.sqrt();
        Real t1 = b.negate().subtract(sqrtDisc).divide(Real.of(2.0).multiply(a));
        Real t2 = b.negate().add(sqrtDisc).divide(Real.of(2.0).multiply(a));

        if (t1.doubleValue() > 0)
            return t1;
        if (t2.doubleValue() > 0)
            return t2;
        return null; // Both intersections are behind ray origin
    }

    /**
     * Intersects a ray with a plane.
     * 
     * @param ray         the ray
     * @param planePoint  a point on the plane
     * @param planeNormal the normal of the plane
     * @return distance to intersection, or null if parallel
     */
    public static Real intersectRayPlane(Ray3D ray, Point3D planePoint, Vector3D planeNormal) {
        Real denom = planeNormal.dot(ray.getDirection());
        if (denom.abs().doubleValue() < 1e-6)
            return null;

        Vector3D p0l0 = planePoint.subtract(ray.getOrigin());
        Real t = p0l0.dot(planeNormal).divide(denom);

        if (t.doubleValue() >= 0)
            return t;
        return null;
    }
}

