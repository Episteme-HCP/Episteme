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

package org.jscience.mathematics.geometry.triangulation;

import org.jscience.mathematics.geometry.Point2D;
import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Voronoi diagram and Delaunay triangulation computation.
 * <p>
 * Delaunay triangulation: No point inside circumcircle of any triangle.
 * Voronoi diagram: Dual graph - regions closest to each point.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DelaunayTriangulation {

    /**
     * Triangle defined by three points.
     */
    public static class Triangle {
        public final Point2D p1, p2, p3;
        public final Point2D circumcenter;
        public final Real circumradius;

        public Triangle(Point2D p1, Point2D p2, Point2D p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            // Compute circumcircle immediately
            Real d = Real.of(2).multiply(
                    p1.getX().multiply(p2.getY().subtract(p3.getY()))
                            .add(p2.getX().multiply(p3.getY().subtract(p1.getY())))
                            .add(p3.getX().multiply(p1.getY().subtract(p2.getY()))));

            if (d.abs().compareTo(Real.of(1e-10)) < 0) {
                // Degenerate triangle
                this.circumcenter = p1;
                this.circumradius = Real.ZERO;
            } else {
                Real p1sq = p1.getX().multiply(p1.getX()).add(p1.getY().multiply(p1.getY()));
                Real p2sq = p2.getX().multiply(p2.getX()).add(p2.getY().multiply(p2.getY()));
                Real p3sq = p3.getX().multiply(p3.getX()).add(p3.getY().multiply(p3.getY()));

                Real ux = p1sq.multiply(p2.getY().subtract(p3.getY()))
                        .add(p2sq.multiply(p3.getY().subtract(p1.getY())))
                        .add(p3sq.multiply(p1.getY().subtract(p2.getY())))
                        .divide(d);

                Real uy = p1sq.multiply(p3.getX().subtract(p2.getX()))
                        .add(p2sq.multiply(p1.getX().subtract(p3.getX())))
                        .add(p3sq.multiply(p2.getX().subtract(p1.getX())))
                        .divide(d);

                this.circumcenter = Point2D.of(ux, uy);
                this.circumradius = this.circumcenter.distanceTo(p1);
            }
        }

        public boolean containsInCircumcircle(Point2D p) {
            return circumcenter.distanceTo(p).compareTo(circumradius) < 0;
        }

        public boolean sharesEdgeWith(Triangle other) {
            int shared = 0;
            if (p1.equals(other.p1) || p1.equals(other.p2) || p1.equals(other.p3))
                shared++;
            if (p2.equals(other.p1) || p2.equals(other.p2) || p2.equals(other.p3))
                shared++;
            if (p3.equals(other.p1) || p3.equals(other.p2) || p3.equals(other.p3))
                shared++;
            return shared == 2;
        }

        public boolean contains(Point2D p) {
            return p.equals(p1) || p.equals(p2) || p.equals(p3);
        }
    }

    /**
     * Computes Delaunay triangulation using Bowyer-Watson algorithm.
     * <p>
     * O(n log n) expected time.
     * </p>
     * 
     * @param points input points
     * @return list of triangles forming triangulation
     */
    public static List<Triangle> triangulate(List<Point2D> points) {
        if (points.size() < 3) {
            throw new IllegalArgumentException("Need at least 3 points");
        }

        // Find bounding box
        Real minX = points.get(0).getX(), maxX = minX;
        Real minY = points.get(0).getY(), maxY = minY;

        for (Point2D p : points) {
            if (p.getX().compareTo(minX) < 0)
                minX = p.getX();
            if (p.getX().compareTo(maxX) > 0)
                maxX = p.getX();
            if (p.getY().compareTo(minY) < 0)
                minY = p.getY();
            if (p.getY().compareTo(maxY) > 0)
                maxY = p.getY();
        }

        // Create super-triangle containing all points
        Real dx = maxX.subtract(minX);
        Real dy = maxY.subtract(minY);
        Real dmax = dx.compareTo(dy) > 0 ? dx : dy;
        Real midX = minX.add(maxX).divide(Real.of(2));
        Real midY = minY.add(maxY).divide(Real.of(2));

        Point2D p1 = Point2D.of(midX.subtract(dmax.multiply(Real.of(20))),
                midY.subtract(dmax));
        Point2D p2 = Point2D.of(midX, midY.add(dmax.multiply(Real.of(20))));
        Point2D p3 = Point2D.of(midX.add(dmax.multiply(Real.of(20))),
                midY.subtract(dmax));

        List<Triangle> triangles = new ArrayList<>();
        triangles.add(new Triangle(p1, p2, p3));

        // Bowyer-Watson algorithm
        for (Point2D point : points) {
            List<Triangle> badTriangles = new ArrayList<>();

            // Find triangles whose circumcircle contains point
            for (Triangle tri : triangles) {
                if (tri.containsInCircumcircle(point)) {
                    badTriangles.add(tri);
                }
            }

            // Find boundary polygon
            List<Point2D[]> polygon = new ArrayList<>();
            for (Triangle tri : badTriangles) {
                // Create edges as 2D array
                Point2D[][] edges = new Point2D[3][2];
                edges[0] = new Point2D[] { tri.p1, tri.p2 };
                edges[1] = new Point2D[] { tri.p2, tri.p3 };
                edges[2] = new Point2D[] { tri.p3, tri.p1 };

                for (Point2D[] edge : edges) {
                    boolean shared = false;
                    for (Triangle other : badTriangles) {
                        if (other == tri)
                            continue;
                        if ((other.contains(edge[0]) && other.contains(edge[1]))) {
                            shared = true;
                            break;
                        }
                    }
                    if (!shared) {
                        polygon.add(edge);
                    }
                }
            }

            // Remove bad triangles
            triangles.removeAll(badTriangles);

            // Add new triangles from point to polygon edges
            for (Point2D[] edge : polygon) {
                triangles.add(new Triangle(edge[0], edge[1], point));
            }
        }

        // Remove triangles containing super-triangle vertices
        triangles.removeIf(tri -> tri.contains(p1) || tri.contains(p2) || tri.contains(p3));

        return triangles;
    }

    /**
     * Voronoi cell (region closest to a point).
     */
    public static class VoronoiCell {
        public final Point2D site;
        public final List<Point2D> vertices;

        public VoronoiCell(Point2D site, List<Point2D> vertices) {
            this.site = site;
            this.vertices = vertices;
        }
    }

    /**
     * Computes Voronoi diagram from Delaunay triangulation.
     * <p>
     * Voronoi vertices = circumcenters of Delaunay triangles.
     * </p>
     */
    public static List<VoronoiCell> voronoiDiagram(List<Point2D> points) {
        List<Triangle> triangles = triangulate(points);
        Map<Point2D, List<Point2D>> voronoiMap = new HashMap<>();

        for (Point2D p : points) {
            voronoiMap.put(p, new ArrayList<>());
        }

        // Circumcenters form Voronoi vertices
        for (Triangle tri : triangles) {
            Point2D center = tri.circumcenter;
            voronoiMap.get(tri.p1).add(center);
            voronoiMap.get(tri.p2).add(center);
            voronoiMap.get(tri.p3).add(center);
        }

        List<VoronoiCell> cells = new ArrayList<>();
        for (Map.Entry<Point2D, List<Point2D>> entry : voronoiMap.entrySet()) {
            cells.add(new VoronoiCell(entry.getKey(), entry.getValue()));
        }

        return cells;
    }
}


