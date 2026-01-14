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

package org.jscience.mathematics.geometry.csg.d2;

import org.jscience.mathematics.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Constructive Solid Geometry (CSG) implementation for 2D.
 *
 * This class performs 2D CSG operations (union, subtract, intersect) using
 * a BSP tree approach with Edge objects. Unlike CSG (3D), this implementation
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CSG2D {

    private List<Edge> edges;

    public CSG2D(List<Edge> edges) {
        this.edges = new ArrayList<>(edges);
    }

    /**
     * Creates a CSG2D from a list of points representing a polygon.
     * Assumes points are ordered counter-clockwise (CCW).
     */
    public static CSG2D fromPoints(List<Point2D> points) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point2D p1 = points.get(i);
            Point2D p2 = points.get((i + 1) % points.size());
            edges.add(new Edge(p1, p2));
        }
        return new CSG2D(edges);
    }

    public CSG2D union(CSG2D other) {
        Node2D a = new Node2D(this.clone().edges);
        Node2D b = new Node2D(other.clone().edges);

        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();

        a.build(b.allEdges());
        return new CSG2D(a.allEdges());
    }

    public CSG2D subtract(CSG2D other) {
        Node2D a = new Node2D(this.clone().edges);
        Node2D b = new Node2D(other.clone().edges);

        a.invert();
        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();
        a.build(b.allEdges());
        a.invert();

        return new CSG2D(a.allEdges());
    }

    public CSG2D intersect(CSG2D other) {
        Node2D a = new Node2D(this.clone().edges);
        Node2D b = new Node2D(other.clone().edges);

        a.invert();
        b.clipTo(a);
        b.invert();
        a.clipTo(b);
        b.clipTo(a);
        a.build(b.allEdges());
        a.invert();

        return new CSG2D(a.allEdges());
    }

    public CSG2D clone() {
        return new CSG2D(edges.stream().map(Edge::clone).collect(Collectors.toList()));
    }

    /**
     * Returns the internal edge list for direct access.
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Convert back to Polygons (List of point lists) if needed.
     * This requires chaining edges.
     */
    public List<List<Point2D>> getPolygons() {
        List<List<Point2D>> polygons = new ArrayList<>();
        if (edges.isEmpty())
            return polygons;

        List<Edge> remaining = new ArrayList<>(edges);

        while (!remaining.isEmpty()) {
            List<Point2D> currentPoly = new ArrayList<>();
            Edge startEdge = remaining.remove(0);
            currentPoly.add(startEdge.start);

            Edge currentFn = startEdge;
            boolean closed = false;

            while (!closed) {
                // Find edge starting at currentFn.end
                Point2D target = currentFn.end;
                Edge next = null;

                // Simple search O(N^2) total - optimize with map later if needed
                for (int i = 0; i < remaining.size(); i++) {
                    Edge candidate = remaining.get(i);
                    // Approximate equality check needed for floats
                    if (candidate.start.distanceSquared(target)
                            .compareTo(org.jscience.mathematics.numbers.real.Real.of(1e-10)) < 0) {
                        next = candidate;
                        remaining.remove(i);
                        break;
                    }
                }

                if (next != null) {
                    currentPoly.add(next.start);
                    currentFn = next;
                    if (currentFn.end.distanceSquared(startEdge.start)
                            .compareTo(org.jscience.mathematics.numbers.real.Real.of(1e-10)) < 0) {
                        closed = true;
                    }
                } else {
                    // Open chain or error? Just stop for this poly.
                    // For CSG results, usually closed loops.
                    currentPoly.add(currentFn.end);
                    closed = true;
                }
            }
            polygons.add(currentPoly);
        }
        return polygons;
    }
}


