package org.jscience.mathematics.geometry.csg;

import org.jscience.mathematics.geometry.Plane3D;
import java.util.ArrayList;
import java.util.List;

/**
 * Node in the BSP tree for CSG operations.
 */
public class Node {
    private Plane3D plane;
    private Node front;
    private Node back;
    private List<Polygon> polygons;

    public Node() {
        this(null);
    }

    public Node(List<Polygon> polygons) {
        this.polygons = new ArrayList<>();
        if (polygons != null) {
            this.build(polygons);
        }
    }

    public Node clone() {
        Node node = new Node();
        // Plane3D is immutable, so direct assignment is fine
        node.plane = this.plane;
        node.front = this.front == null ? null : this.front.clone();
        node.back = this.back == null ? null : this.back.clone();
        node.polygons = new ArrayList<>(this.polygons.size());
        for (Polygon p : this.polygons) {
            node.polygons.add(p.clone());
        }
        return node;
    }

    public void invert() {
        for (int i = 0; i < polygons.size(); i++) {
            polygons.set(i, polygons.get(i).flipped());
        }
        if (plane != null)
            plane = plane.flipped();
        if (front != null)
            front.invert();
        if (back != null)
            back.invert();

        Node temp = front;
        front = back;
        back = temp;
    }

    public List<Polygon> clipPolygons(List<Polygon> list) {
        if (this.plane == null)
            return new ArrayList<>(list);

        List<Polygon> listFront = new ArrayList<>();
        List<Polygon> listBack = new ArrayList<>();

        for (Polygon p : list) {
            CSGUtils.splitPolygon(this.plane, p, listFront, listBack, listFront, listBack);
        }

        if (this.front != null)
            listFront = this.front.clipPolygons(listFront);
        if (this.back != null)
            listBack = this.back.clipPolygons(listBack);
        else
            listBack.clear();

        listFront.addAll(listBack);
        return listFront;
    }

    public void clipTo(Node bsp) {
        this.polygons = bsp.clipPolygons(this.polygons);
        if (this.front != null)
            this.front.clipTo(bsp);
        if (this.back != null)
            this.back.clipTo(bsp);
    }

    public List<Polygon> allPolygons() {
        List<Polygon> list = new ArrayList<>(this.polygons);
        if (this.front != null)
            list.addAll(this.front.allPolygons());
        if (this.back != null)
            list.addAll(this.back.allPolygons());
        return list;
    }

    public void build(List<Polygon> polygons) {
        if (polygons.isEmpty())
            return;

        if (this.plane == null) {
            // Plane3D from Polygon
            this.plane = polygons.get(0).plane;
        }

        List<Polygon> listFront = new ArrayList<>();
        List<Polygon> listBack = new ArrayList<>();

        for (Polygon p : polygons) {
            CSGUtils.splitPolygon(this.plane, p, this.polygons, this.polygons, listFront, listBack);
        }

        if (!listFront.isEmpty()) {
            if (this.front == null)
                this.front = new Node();
            this.front.build(listFront);
        }
        if (!listBack.isEmpty()) {
            if (this.back == null)
                this.back = new Node();
            this.back.build(listBack);
        }
    }
}
