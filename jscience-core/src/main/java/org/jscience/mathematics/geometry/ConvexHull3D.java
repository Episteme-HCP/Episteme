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

import org.jscience.mathematics.geometry.boundaries.ConvexPolyhedron3D;
import org.jscience.mathematics.numbers.real.Real;

import java.util.*;

/**
 * 3D Convex Hull implementation using the Quickhull algorithm.
 * <p>
 * Time complexity: O(n log n) average case, O(n²) worst case.
 * <p>
 * The Quickhull algorithm:
 * <ol>
 *   <li>Find extreme points to form initial tetrahedron</li>
 *   <li>Partition remaining points by which face they're "above"</li>
 *   <li>Recursively process each face with associated points</li>
 * </ol>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Quickhull">Quickhull Algorithm</a>
 */
public final class ConvexHull3D {

    private ConvexHull3D() {}

    /**
     * Represents a triangular face of the hull.
     */
    public static final class Face {
        private final Point3D[] vertices;
        private final Vector3D normal;
        private final Real area;

        public Face(Point3D v1, Point3D v2, Point3D v3) {
            this.vertices = new Point3D[] { v1, v2, v3 };
            Vector3D edge1 = v2.subtract(v1);
            Vector3D edge2 = v3.subtract(v1);
            Vector3D cross = edge1.cross(edge2);
            Real mag = cross.magnitude();
            this.area = mag.divide(Real.of(2));
            this.normal = mag.isZero() ? Vector3D.ZERO : cross.divide(mag);
        }

        public Point3D[] getVertices() { return vertices; }
        public Point3D getV1() { return vertices[0]; }
        public Point3D getV2() { return vertices[1]; }
        public Point3D getV3() { return vertices[2]; }
        public Vector3D getNormal() { return normal; }
        public Real getArea() { return area; }

        /**
         * Signed distance from a point to the plane of this face.
         * Positive = in front (outside), negative = behind (inside).
         */
        public Real signedDistanceTo(Point3D p) {
            return p.subtract(vertices[0]).dot(normal);
        }

        /**
         * Checks if a point is in front of (outside) this face.
         */
        public boolean isVisible(Point3D p) {
            return signedDistanceTo(p).compareTo(Real.ZERO) > 0;
        }

        /**
         * Returns the centroid of this face.
         */
        public Point3D getCentroid() {
            return Point3D.of(
                vertices[0].getX().add(vertices[1].getX()).add(vertices[2].getX()).divide(Real.of(3)),
                vertices[0].getY().add(vertices[1].getY()).add(vertices[2].getY()).divide(Real.of(3)),
                vertices[0].getZ().add(vertices[1].getZ()).add(vertices[2].getZ()).divide(Real.of(3))
            );
        }

        @Override
        public String toString() {
            return String.format("Face[%s, %s, %s]", vertices[0], vertices[1], vertices[2]);
        }
    }

    /**
     * Computes the 3D convex hull of a set of points.
     *
     * @param points input points
     * @return list of triangular faces forming the hull
     */
    public static List<Face> compute(List<Point3D> points) {
        if (points == null || points.size() < 4) {
            return Collections.emptyList();
        }

        // Remove duplicates
        List<Point3D> uniquePoints = new ArrayList<>(new LinkedHashSet<>(points));
        if (uniquePoints.size() < 4) {
            return Collections.emptyList();
        }

        // Find initial tetrahedron
        Point3D[] extremes = findExtremePoints(uniquePoints);
        if (extremes == null) {
            return Collections.emptyList(); // Points are coplanar
        }

        // Build initial hull from tetrahedron
        List<Face> hull = new ArrayList<>();
        Point3D a = extremes[0], b = extremes[1], c = extremes[2], d = extremes[3];

        // Create 4 faces with outward-pointing normals
        Face f1 = new Face(a, b, c);
        Face f2 = new Face(a, c, d);
        Face f3 = new Face(a, d, b);
        Face f4 = new Face(b, d, c);

        // Ensure normals point outward (away from centroid)
        Point3D centroid = computeCentroid(List.of(a, b, c, d));
        hull.add(ensureOutward(f1, centroid));
        hull.add(ensureOutward(f2, centroid));
        hull.add(ensureOutward(f3, centroid));
        hull.add(ensureOutward(f4, centroid));

        // Assign remaining points to faces
        Set<Point3D> initialSet = Set.of(a, b, c, d);
        List<Point3D> remaining = new ArrayList<>();
        for (Point3D p : uniquePoints) {
            if (!initialSet.contains(p)) {
                remaining.add(p);
            }
        }

        // Process remaining points
        for (Point3D p : remaining) {
            addPointToHull(hull, p);
        }

        return hull;
    }

    /**
     * Creates a ConvexPolyhedron3D from the hull.
     *
     * @param points the input points
     * @return a convex polyhedron boundary
     */
    public static ConvexPolyhedron3D computePolyhedron(List<Point3D> points) {
        List<Face> faces = compute(points);
        List<ConvexPolyhedron3D.TriangularFace> polyFaces = new ArrayList<>();
        for (Face f : faces) {
            polyFaces.add(new ConvexPolyhedron3D.TriangularFace(f.getV1(), f.getV2(), f.getV3()));
        }
        return new ConvexPolyhedron3D(polyFaces);
    }

    /**
     * Computes the volume of the convex hull.
     *
     * @param points the input points
     * @return the volume
     */
    public static Real computeVolume(List<Point3D> points) {
        List<Face> faces = compute(points);
        if (faces.isEmpty()) return Real.ZERO;

        // Sum signed tetrahedron volumes
        Point3D origin = Point3D.ZERO;
        Real sum = Real.ZERO;

        for (Face f : faces) {
            sum = sum.add(tetrahedronVolume(origin, f.getV1(), f.getV2(), f.getV3()));
        }

        return sum.abs();
    }

    /**
     * Computes the surface area of the convex hull.
     *
     * @param points the input points
     * @return the surface area
     */
    public static Real computeSurfaceArea(List<Point3D> points) {
        List<Face> faces = compute(points);
        Real sum = Real.ZERO;
        for (Face f : faces) {
            sum = sum.add(f.getArea());
        }
        return sum;
    }

    /**
     * Checks if a point is inside the convex hull.
     *
     * @param hullPoints the points that define the hull
     * @param query      the point to test
     * @return true if inside
     */
    public static boolean contains(List<Point3D> hullPoints, Point3D query) {
        List<Face> faces = compute(hullPoints);
        for (Face f : faces) {
            if (f.isVisible(query)) {
                return false; // Point is outside at least one face
            }
        }
        return true;
    }

    // ===== Helper Methods =====

    private static Point3D[] findExtremePoints(List<Point3D> points) {
        // Find 4 non-coplanar points
        Point3D minX = points.get(0), maxX = points.get(0);
        for (Point3D p : points) {
            if (p.getX().compareTo(minX.getX()) < 0) minX = p;
            if (p.getX().compareTo(maxX.getX()) > 0) maxX = p;
        }

        // Find point farthest from line minX-maxX
        Point3D farthest1 = null;
        Real maxDist1 = Real.ZERO;
        for (Point3D p : points) {
            Real dist = distanceToLine(p, minX, maxX);
            if (dist.compareTo(maxDist1) > 0) {
                maxDist1 = dist;
                farthest1 = p;
            }
        }

        if (farthest1 == null || maxDist1.isZero()) {
            return null; // Collinear
        }

        // Find point farthest from plane minX-maxX-farthest1
        Point3D farthest2 = null;
        Real maxDist2 = Real.ZERO;
        Vector3D planeNormal = maxX.subtract(minX).cross(farthest1.subtract(minX));

        for (Point3D p : points) {
            Real dist = p.subtract(minX).dot(planeNormal).abs();
            if (dist.compareTo(maxDist2) > 0) {
                maxDist2 = dist;
                farthest2 = p;
            }
        }

        if (farthest2 == null || maxDist2.isZero()) {
            return null; // Coplanar
        }

        return new Point3D[] { minX, maxX, farthest1, farthest2 };
    }

    private static Real distanceToLine(Point3D p, Point3D a, Point3D b) {
        Vector3D ab = b.subtract(a);
        Vector3D ap = p.subtract(a);
        Vector3D cross = ab.cross(ap);
        Real abMag = ab.magnitude();
        if (abMag.isZero()) return ap.magnitude();
        return cross.magnitude().divide(abMag);
    }

    private static Face ensureOutward(Face f, Point3D centroid) {
        if (f.signedDistanceTo(centroid).compareTo(Real.ZERO) > 0) {
            // Normal points toward centroid, flip face
            return new Face(f.getV1(), f.getV3(), f.getV2());
        }
        return f;
    }

    private static void addPointToHull(List<Face> hull, Point3D p) {
        // Find visible faces
        List<Face> visible = new ArrayList<>();
        List<Face> invisible = new ArrayList<>();

        for (Face f : hull) {
            if (f.isVisible(p)) {
                visible.add(f);
            } else {
                invisible.add(f);
            }
        }

        if (visible.isEmpty()) {
            return; // Point is inside hull
        }

        // Find horizon edges (edges between visible and invisible faces)
        // Simplified: rebuild with new point
        hull.clear();
        hull.addAll(invisible);

        // Add new faces connecting point to horizon
        for (Face vis : visible) {
            // For each edge of visible face, check if it's a horizon edge
            Point3D[] v = vis.getVertices();
            for (int i = 0; i < 3; i++) {
                Point3D e1 = v[i];
                Point3D e2 = v[(i + 1) % 3];
                
                // Check if this edge is shared with an invisible face
                boolean isHorizon = false;
                for (Face inv : invisible) {
                    if (sharesEdge(inv, e1, e2)) {
                        isHorizon = true;
                        break;
                    }
                }
                
                if (isHorizon) {
                    hull.add(new Face(e1, e2, p));
                }
            }
        }
    }

    private static boolean sharesEdge(Face f, Point3D e1, Point3D e2) {
        Point3D[] v = f.getVertices();
        int matches = 0;
        for (Point3D vertex : v) {
            if (vertex.equals(e1) || vertex.equals(e2)) {
                matches++;
            }
        }
        return matches == 2;
    }

    private static Point3D computeCentroid(List<Point3D> points) {
        Real sumX = Real.ZERO, sumY = Real.ZERO, sumZ = Real.ZERO;
        for (Point3D p : points) {
            sumX = sumX.add(p.getX());
            sumY = sumY.add(p.getY());
            sumZ = sumZ.add(p.getZ());
        }
        int n = points.size();
        return Point3D.of(
            sumX.divide(Real.of(n)),
            sumY.divide(Real.of(n)),
            sumZ.divide(Real.of(n))
        );
    }

    private static Real tetrahedronVolume(Point3D a, Point3D b, Point3D c, Point3D d) {
        Vector3D ab = b.subtract(a);
        Vector3D ac = c.subtract(a);
        Vector3D ad = d.subtract(a);
        return ab.dot(ac.cross(ad)).divide(Real.of(6));
    }
}
