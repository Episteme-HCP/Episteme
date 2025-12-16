package org.jscience.mathematics.geometry.csg.d2;

import org.jscience.mathematics.geometry.Point2D;

/**
 * Represents an edge (segment) in 2D CSG.
 * Analogous to Polygon in 3D CSG. Implements Graph.Edge for graph algorithms.
 */
public class Edge implements org.jscience.mathematics.discrete.Graph.Edge<Point2D> {

    public final Point2D start;
    public final Point2D end;
    public final org.jscience.mathematics.geometry.Line2D line;

    public Edge(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
        org.jscience.mathematics.geometry.Vector2D dir = org.jscience.mathematics.geometry.Vector2D.of(
                end.getX().subtract(start.getX()),
                end.getY().subtract(start.getY()));
        this.line = new org.jscience.mathematics.geometry.Line2D(start, dir);
    }

    public Edge(Point2D start, Point2D end, org.jscience.mathematics.geometry.Line2D line) {
        this.start = start;
        this.end = end;
        this.line = line;
    }

    @Override
    public Point2D source() {
        return start;
    }

    @Override
    public Point2D target() {
        return end;
    }

    public Edge clone() {
        return new Edge(start, end, line);
    }

    public Edge flipped() {
        return new Edge(end, start, line.flipped());
    }

    @Override
    public String toString() {
        return "Edge[" + start + " -> " + end + "]";
    }
}
