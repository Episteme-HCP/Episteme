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

package org.jscience.mathematics.geometry.triangulation;

import org.jscience.mathematics.geometry.Point2D;
import org.jscience.mathematics.geometry.Segment2D;

import java.util.*;

/**
 * Voronoi diagram computation in 2D.
 * <p>
 * A Voronoi diagram partitions the plane into regions closest to each site
 * point.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VoronoiDiagram {

    private final List<Point2D> sites;
    private final List<VoronoiCell> cells;
    private final List<Segment2D> edges;

    /**
     * Represents a Voronoi cell (region).
     */
    public static class VoronoiCell {
        private final Point2D site;
        private final List<Point2D> vertices;

        public VoronoiCell(Point2D site, List<Point2D> vertices) {
            this.site = site;
            this.vertices = new ArrayList<>(vertices);
        }

        public Point2D getSite() {
            return site;
        }

        public List<Point2D> getVertices() {
            return Collections.unmodifiableList(vertices);
        }

        public double area() {
            if (vertices.size() < 3)
                return 0;
            double area = 0;
            int n = vertices.size();
            for (int i = 0; i < n; i++) {
                Point2D p1 = vertices.get(i);
                Point2D p2 = vertices.get((i + 1) % n);
                area += p1.getX().doubleValue() * p2.getY().doubleValue()
                        - p2.getX().doubleValue() * p1.getY().doubleValue();
            }
            return Math.abs(area) / 2.0;
        }
    }

    /**
     * Constructs a Voronoi diagram from the given sites.
     */
    public VoronoiDiagram(List<Point2D> sites) {
        this.sites = new ArrayList<>(sites);
        this.cells = new ArrayList<>();
        this.edges = new ArrayList<>();
        compute();
    }

    private void compute() {
        if (sites.size() < 2)
            return;

        for (Point2D site : sites) {
            List<Point2D> cellVertices = computeCellBoundary(site);
            cells.add(new VoronoiCell(site, cellVertices));
        }

        computeEdges();
    }

    private List<Point2D> computeCellBoundary(Point2D site) {
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        for (Point2D s : sites) {
            minX = Math.min(minX, s.getX().doubleValue());
            maxX = Math.max(maxX, s.getX().doubleValue());
            minY = Math.min(minY, s.getY().doubleValue());
            maxY = Math.max(maxY, s.getY().doubleValue());
        }

        double margin = Math.max(maxX - minX, maxY - minY) * 0.5;
        minX -= margin;
        maxX += margin;
        minY -= margin;
        maxY += margin;

        List<Point2D> polygon = new ArrayList<>(Arrays.asList(
                Point2D.of(minX, minY),
                Point2D.of(maxX, minY),
                Point2D.of(maxX, maxY),
                Point2D.of(minX, maxY)));

        for (Point2D other : sites) {
            if (other.equals(site))
                continue;
            polygon = clipPolygonByBisector(polygon, site, other);
            if (polygon.isEmpty())
                break;
        }

        return polygon;
    }

    private List<Point2D> clipPolygonByBisector(List<Point2D> polygon, Point2D site, Point2D other) {
        if (polygon.size() < 3)
            return polygon;

        double siteX = site.getX().doubleValue();
        double siteY = site.getY().doubleValue();
        double otherX = other.getX().doubleValue();
        double otherY = other.getY().doubleValue();

        double midX = (siteX + otherX) / 2;
        double midY = (siteY + otherY) / 2;

        double dx = otherX - siteX;
        double dy = otherY - siteY;

        double nx = -dx;
        double ny = -dy;
        double len = Math.sqrt(nx * nx + ny * ny);
        nx /= len;
        ny /= len;

        List<Point2D> result = new ArrayList<>();
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point2D curr = polygon.get(i);
            Point2D next = polygon.get((i + 1) % n);

            double currX = curr.getX().doubleValue();
            double currY = curr.getY().doubleValue();
            double nextX = next.getX().doubleValue();
            double nextY = next.getY().doubleValue();

            double currDist = (currX - midX) * nx + (currY - midY) * ny;
            double nextDist = (nextX - midX) * nx + (nextY - midY) * ny;

            boolean currInside = currDist >= -1e-10;
            boolean nextInside = nextDist >= -1e-10;

            if (currInside) {
                result.add(curr);
            }

            if (currInside != nextInside) {
                double t = currDist / (currDist - nextDist);
                double ix = currX + t * (nextX - currX);
                double iy = currY + t * (nextY - currY);
                result.add(Point2D.of(ix, iy));
            }
        }

        return result;
    }

    private void computeEdges() {
        Set<String> addedEdges = new HashSet<>();

        for (VoronoiCell cell : cells) {
            List<Point2D> verts = cell.getVertices();
            for (int i = 0; i < verts.size(); i++) {
                Point2D p1 = verts.get(i);
                Point2D p2 = verts.get((i + 1) % verts.size());

                String key = edgeKey(p1, p2);
                if (!addedEdges.contains(key)) {
                    edges.add(new Segment2D(p1, p2));
                    addedEdges.add(key);
                }
            }
        }
    }

    private String edgeKey(Point2D p1, Point2D p2) {
        String k1 = String.format("%.6f,%.6f", p1.getX().doubleValue(), p1.getY().doubleValue());
        String k2 = String.format("%.6f,%.6f", p2.getX().doubleValue(), p2.getY().doubleValue());
        return k1.compareTo(k2) < 0 ? k1 + "-" + k2 : k2 + "-" + k1;
    }

    public List<Point2D> getSites() {
        return Collections.unmodifiableList(sites);
    }

    public List<VoronoiCell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public List<Segment2D> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public VoronoiCell findCell(Point2D p) {
        double minDist = Double.MAX_VALUE;
        VoronoiCell closest = null;

        for (VoronoiCell cell : cells) {
            double dist = distance(p, cell.getSite());
            if (dist < minDist) {
                minDist = dist;
                closest = cell;
            }
        }

        return closest;
    }

    private double distance(Point2D a, Point2D b) {
        double dx = a.getX().doubleValue() - b.getX().doubleValue();
        double dy = a.getY().doubleValue() - b.getY().doubleValue();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("VoronoiDiagram[sites=%d, cells=%d, edges=%d]",
                sites.size(), cells.size(), edges.size());
    }
}
