package org.jscience.mathematics.geometry.csg.d2;

import org.jscience.mathematics.geometry.Point2D;
import org.jscience.mathematics.geometry.Line2D;
import org.jscience.mathematics.geometry.Vector2D;
import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * BSP Tree Node for 2D CSG. Analogous to Node in 3D CSG.
 */
public class Node2D {
    private static final Real EPSILON = Real.of(1e-9);

    private Line2D line;
    private Node2D front;
    private Node2D back;
    private List<Edge> edges;

    public Node2D() {
        this.edges = new ArrayList<>();
    }

    public Node2D(List<Edge> edges) {
        this();
        if (edges != null && !edges.isEmpty()) {
            build(edges);
        }
    }

    public Node2D clone() {
        Node2D node = new Node2D();
        node.line = this.line; // Line2D is immutable
        node.front = (this.front != null) ? this.front.clone() : null;
        node.back = (this.back != null) ? this.back.clone() : null;
        node.edges = new ArrayList<>();
        for (Edge e : edges) {
            node.edges.add(e.clone());
        }
        return node;
    }

    public void invert() {
        for (int i = 0; i < edges.size(); i++) {
            edges.set(i, edges.get(i).flipped());
        }
        if (line != null) {
            line = line.flipped();
        }
        if (front != null)
            front.invert();
        if (back != null)
            back.invert();
        Node2D temp = front;
        front = back;
        back = temp;
    }

    public List<Edge> clipEdges(List<Edge> inputEdges) {
        if (line == null)
            return new ArrayList<>(inputEdges);

        List<Edge> frontEdges = new ArrayList<>();
        List<Edge> backEdges = new ArrayList<>();

        for (Edge edge : inputEdges) {
            splitEdge(edge, frontEdges, backEdges, frontEdges, backEdges);
        }

        if (front != null) {
            frontEdges = front.clipEdges(frontEdges);
        }
        if (back != null) {
            backEdges = back.clipEdges(backEdges);
        } else {
            backEdges = new ArrayList<>();
        }

        frontEdges.addAll(backEdges);
        return frontEdges;
    }

    public void clipTo(Node2D bsp) {
        this.edges = bsp.clipEdges(this.edges);
        if (front != null)
            front.clipTo(bsp);
        if (back != null)
            back.clipTo(bsp);
    }

    public List<Edge> allEdges() {
        List<Edge> result = new ArrayList<>(edges);
        if (front != null)
            result.addAll(front.allEdges());
        if (back != null)
            result.addAll(back.allEdges());
        return result;
    }

    public void build(List<Edge> inputEdges) {
        if (inputEdges == null || inputEdges.isEmpty())
            return;

        if (line == null) {
            line = inputEdges.get(0).line;
        }

        List<Edge> frontEdges = new ArrayList<>();
        List<Edge> backEdges = new ArrayList<>();

        for (Edge edge : inputEdges) {
            splitEdge(edge, this.edges, this.edges, frontEdges, backEdges);
        }

        if (!frontEdges.isEmpty()) {
            if (front == null)
                front = new Node2D();
            front.build(frontEdges);
        }
        if (!backEdges.isEmpty()) {
            if (back == null)
                back = new Node2D();
            back.build(backEdges);
        }
    }

    /**
     * Splits an edge by this node's line into
     * FRONT/BACK/COPLANAR_FRONT/COPLANAR_BACK.
     */
    private void splitEdge(Edge edge, List<Edge> coplanarFront, List<Edge> coplanarBack,
            List<Edge> frontList, List<Edge> backList) {
        int COPLANAR = 0;
        int FRONT = 1;
        int BACK = 2;

        Real dist1 = line.signedDistance(edge.start);
        Real dist2 = line.signedDistance(edge.end);

        int type1 = (dist1.compareTo(EPSILON) > 0) ? FRONT : (dist1.compareTo(EPSILON.negate()) < 0) ? BACK : COPLANAR;
        int type2 = (dist2.compareTo(EPSILON) > 0) ? FRONT : (dist2.compareTo(EPSILON.negate()) < 0) ? BACK : COPLANAR;

        int edgeType = type1 | type2;

        switch (edgeType) {
            case 0: // COPLANAR
                // Check if edge normal aligns with line normal
                Vector2D lineNormal = line.getNormal();
                Vector2D edgeDir = Vector2D.of(
                        edge.end.getX().subtract(edge.start.getX()),
                        edge.end.getY().subtract(edge.start.getY()));
                Vector2D edgeNormal = Vector2D.of(edgeDir.getY().negate(), edgeDir.getX());
                Real dot = lineNormal.dot(edgeNormal);
                if (dot.compareTo(Real.ZERO) >= 0) {
                    coplanarFront.add(edge);
                } else {
                    coplanarBack.add(edge);
                }
                break;
            case 1: // FRONT
                frontList.add(edge);
                break;
            case 2: // BACK
                backList.add(edge);
                break;
            case 3: // SPANNING
                // Find intersection point
                Real t = dist1.divide(dist1.subtract(dist2));
                Real ix = edge.start.getX().add(edge.end.getX().subtract(edge.start.getX()).multiply(t));
                Real iy = edge.start.getY().add(edge.end.getY().subtract(edge.start.getY()).multiply(t));
                Point2D intersection = Point2D.of(ix, iy);

                if (type1 == FRONT) {
                    frontList.add(new Edge(edge.start, intersection, edge.line));
                    backList.add(new Edge(intersection, edge.end, edge.line));
                } else {
                    backList.add(new Edge(edge.start, intersection, edge.line));
                    frontList.add(new Edge(intersection, edge.end, edge.line));
                }
                break;
        }
    }
}
