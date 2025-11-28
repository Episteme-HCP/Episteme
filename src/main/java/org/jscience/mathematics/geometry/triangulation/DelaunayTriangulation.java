package org.jscience.mathematics.geometry.triangulation;

import org.jscience.mathematics.number.Real;
import java.util.*;

/**
 * Voronoi diagram and Delaunay triangulation computation.
 * <p>
 * Delaunay triangulation: No point inside circumcircle of any triangle.
 * Voronoi diagram: Dual graph - regions closest to each point.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class DelaunayTriangulation {

    /**
     * Point in 2D space.
     */
    public static class Point {
        public final Real x, y;

        public Point(Real x, Real y) {
            this.x = x;
            this.y = y;
        }

        public Real distanceTo(Point other) {
            Real dx = x.subtract(other.x);
            Real dy = y.subtract(other.y);
            return dx.multiply(dx).add(dy.multiply(dy)).sqrt();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point))
                return false;
            Point p = (Point) obj;
            return x.equals(p.x) && y.equals(p.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     * Triangle defined by three points.
     */
    public static class Triangle {
        public final Point p1, p2, p3;
        private Point circumcenter;
        private Real circumradius;

        public Triangle(Point p1, Point p2, Point p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            computeCircumcircle();
        }

        private void computeCircumcircle() {
            // Circumcenter formula
            Real d = Real.of(2).multiply(
                    p1.x.multiply(p2.y.subtract(p3.y))
                            .add(p2.x.multiply(p3.y.subtract(p1.y)))
                            .add(p3.x.multiply(p1.y.subtract(p2.y))));

            if (d.abs().compareTo(Real.of(1e-10)) < 0) {
                // Degenerate triangle
                circumcenter = p1;
                circumradius = Real.ZERO;
                return;
            }

            Real p1sq = p1.x.multiply(p1.x).add(p1.y.multiply(p1.y));
            Real p2sq = p2.x.multiply(p2.x).add(p2.y.multiply(p2.y));
            Real p3sq = p3.x.multiply(p3.x).add(p3.y.multiply(p3.y));

            Real ux = p1sq.multiply(p2.y.subtract(p3.y))
                    .add(p2sq.multiply(p3.y.subtract(p1.y)))
                    .add(p3sq.multiply(p1.y.subtract(p2.y)))
                    .divide(d);

            Real uy = p1sq.multiply(p3.x.subtract(p2.x))
                    .add(p2sq.multiply(p1.x.subtract(p3.x)))
                    .add(p3sq.multiply(p2.x.subtract(p1.x)))
                    .divide(d);

            circumcenter = new Point(ux, uy);
            circumradius = circumcenter.distanceTo(p1);
        }

        public boolean containsInCircumcircle(Point p) {
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

        public boolean contains(Point p) {
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
    public static List<Triangle> triangulate(List<Point> points) {
        if (points.size() < 3) {
            throw new IllegalArgumentException("Need at least 3 points");
        }

        // Find bounding box
        Real minX = points.get(0).x, maxX = minX;
        Real minY = points.get(0).y, maxY = minY;

        for (Point p : points) {
            if (p.x.compareTo(minX) < 0)
                minX = p.x;
            if (p.x.compareTo(maxX) > 0)
                maxX = p.x;
            if (p.y.compareTo(minY) < 0)
                minY = p.y;
            if (p.y.compareTo(maxY) > 0)
                maxY = p.y;
        }

        // Create super-triangle containing all points
        Real dx = maxX.subtract(minX);
        Real dy = maxY.subtract(minY);
        Real dmax = dx.compareTo(dy) > 0 ? dx : dy;
        Real midX = minX.add(maxX).divide(Real.of(2));
        Real midY = minY.add(maxY).divide(Real.of(2));

        Point p1 = new Point(midX.subtract(dmax.multiply(Real.of(20))),
                midY.subtract(dmax));
        Point p2 = new Point(midX, midY.add(dmax.multiply(Real.of(20))));
        Point p3 = new Point(midX.add(dmax.multiply(Real.of(20))),
                midY.subtract(dmax));

        List<Triangle> triangles = new ArrayList<>();
        triangles.add(new Triangle(p1, p2, p3));

        // Bowyer-Watson algorithm
        for (Point point : points) {
            List<Triangle> badTriangles = new ArrayList<>();

            // Find triangles whose circumcircle contains point
            for (Triangle tri : triangles) {
                if (tri.containsInCircumcircle(point)) {
                    badTriangles.add(tri);
                }
            }

            // Find boundary polygon
            List<Point[]> polygon = new ArrayList<>();
            for (Triangle tri : badTriangles) {
                Point[] edges = {
                        new Point[] { tri.p1, tri.p2 },
                        new Point[] { tri.p2, tri.p3 },
                        new Point[] { tri.p3, tri.p1 }
                };

                for (Point[] edge : edges) {
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
            for (Point[] edge : polygon) {
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
        public final Point site;
        public final List<Point> vertices;

        public VoronoiCell(Point site, List<Point> vertices) {
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
    public static List<VoronoiCell> voronoiDiagram(List<Point> points) {
        List<Triangle> triangles = triangulate(points);
        Map<Point, List<Point>> voronoiMap = new HashMap<>();

        for (Point p : points) {
            voronoiMap.put(p, new ArrayList<>());
        }

        // Circumcenters form Voronoi vertices
        for (Triangle tri : triangles) {
            Point center = tri.circumcenter;
            voronoiMap.get(tri.p1).add(center);
            voronoiMap.get(tri.p2).add(center);
            voronoiMap.get(tri.p3).add(center);
        }

        List<VoronoiCell> cells = new ArrayList<>();
        for (Map.Entry<Point, List<Point>> entry : voronoiMap.entrySet()) {
            cells.add(new VoronoiCell(entry.getKey(), entry.getValue()));
        }

        return cells;
    }
}
