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

package org.jscience.mathematics.geometry.csg;

import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.geometry.Vector3D;
import org.jscience.mathematics.geometry.polyhedra.Polyhedron;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Constructive Solid Geometry (CSG) implementation.
 * <p>
 * Supports boolean operations on solid 3D meshes: Union, Subtract, Intersect.
 * Uses BSP trees for processing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CSG {

    private List<Polygon> polygons;

    public CSG() {
        this.polygons = new ArrayList<>();
    }

    private CSG(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    /**
     * Converts a Polyhedron to a CSG object.
     */
    public static CSG fromPolyhedron(Polyhedron polyhedron) {
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < polyhedron.faceCount(); i++) {
            List<Point3D> faceVerts = polyhedron.getFaceVertices(i);
            List<Vertex> vertices = new ArrayList<>();
            // Compute face normal for all vertices (flat shading)
            if (faceVerts.size() < 3)
                continue;

            // Calc normal
            Point3D p0 = faceVerts.get(0);
            Point3D p1 = faceVerts.get(1);
            Point3D p2 = faceVerts.get(2);
            Vector3D v1 = p1.subtract(p0);
            Vector3D v2 = p2.subtract(p0);
            Vector3D normal = v1.cross(v2).normalize();

            for (Point3D p : faceVerts) {
                vertices.add(new Vertex(p, normal));
            }
            polygons.add(new Polygon(vertices));
        }
        return new CSG(polygons);
    }

    /**
     * Converts this CSG object back to a Polyhedron.
     */
    public Polyhedron toPolyhedron() {
        List<Point3D> distinctPoints = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        // Simple deduplication strategy
        // Ideally use a spatial hash or just robust equals logic if point count is low

        for (Polygon poly : polygons) {
            int[] faceIndices = new int[poly.vertices.size()];
            for (int i = 0; i < poly.vertices.size(); i++) {
                Point3D p = poly.vertices.get(i).pos;
                int idx = indexOf(distinctPoints, p);
                if (idx == -1) {
                    idx = distinctPoints.size();
                    distinctPoints.add(p);
                }
                faceIndices[i] = idx;
            }
            faces.add(faceIndices);
        }

        return new Polyhedron(distinctPoints, faces);
    }

    private int indexOf(List<Point3D> points, Point3D p) {
        // Linear search is slow but robust for now. Point3D.equals checks coordinates.
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).equals(p))
                return i;
            // Also check distance for epsilon equality? Point3D.equals uses Real.equals
            // which is approximate?
            // Real uses BigDecimal or Double. If Double, equals is exact.
            // Probably need epsilon check here for robustness after CSG ops.
            if (points.get(i).distanceTo(p).doubleValue() < 1e-5)
                return i;
        }
        return -1;
    }

    public CSG union(CSG other) {
        Node a = new Node(this.clone().polygons);
        Node b = new Node(other.clone().polygons);

        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();

        a.build(b.allPolygons());
        return new CSG(a.allPolygons());
    }

    public CSG subtract(CSG other) {
        Node a = new Node(this.clone().polygons);
        Node b = new Node(other.clone().polygons);

        a.invert();
        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();
        a.build(b.allPolygons());
        a.invert();

        return new CSG(a.allPolygons());
    }

    public CSG intersect(CSG other) {
        Node a = new Node(this.clone().polygons);
        Node b = new Node(other.clone().polygons);

        a.invert();
        b.clipTo(a);
        b.invert();
        a.clipTo(b);
        b.clipTo(a);
        a.build(b.allPolygons());
        a.invert();

        return new CSG(a.allPolygons());
    }

    public CSG inverse() {
        return new CSG(polygons.stream().map(Polygon::flipped).collect(Collectors.toList()));
    }

    public CSG clone() {
        return new CSG(polygons.stream().map(Polygon::clone).collect(Collectors.toList()));
    }
}


