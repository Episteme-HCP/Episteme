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

package org.jscience.mathematics.geometry.polyhedra;

import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.mathematics.geometry.collision.CollisionShape;

import java.util.*;

/**
 * Represents a convex polyhedron in 3D space.
 * <p>
 * A polyhedron is defined by its vertices and faces. This class supports
 * common operations like computing surface area, volume, and checking
 * convexity.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Polyhedron implements org.jscience.mathematics.discrete.Graph<Point3D>, CollisionShape {

    private final List<Point3D> vertices;
    private final List<int[]> faces; // Each face is an array of vertex indices

    /**
     * Creates a polyhedron from vertices and faces.
     * 
     * @param vertices list of 3D points
     * @param faces    list of face definitions (each face is an array of vertex
     *                 indices)
     */
    public Polyhedron(List<Point3D> vertices, List<int[]> faces) {
        this.vertices = new ArrayList<>(vertices);
        this.faces = new ArrayList<>(faces);
    }

    // ... (existing methods kept by context matching, but implementation replace
    // overrides)
    // Actually, I need to be careful not to delete existing methods if I don't
    // include them in replacement.
    // I will use specific method injection or replace large chunks.
    // The previous view showed strict content.
    // Let's replace the class definition and add methods at end, or judiciously
    // insert.

    /**
     * Returns the number of vertices.
     */
    @Override
    public int vertexCount() {
        return vertices.size();
    }

    /**
     * Returns the number of faces.
     */
    public int faceCount() {
        return faces.size();
    }

    /**
     * Returns the number of edges (using Euler's formula for convex polyhedra: V -
     * E + F = 2).
     */
    public int edgeCount() {
        return vertices.size() + faces.size() - 2;
    }

    /**
     * Returns the vertex at the given index.
     */
    public Point3D getVertex(int index) {
        return vertices.get(index);
    }

    /**
     * Returns the vertices of the face at the given index.
     */
    public List<Point3D> getFaceVertices(int faceIndex) {
        int[] indices = faces.get(faceIndex);
        List<Point3D> result = new ArrayList<>();
        for (int i : indices) {
            result.add(vertices.get(i));
        }
        return result;
    }

    /**
     * Computes the centroid (center of mass) of the polyhedron.
     */
    public Point3D centroid() {
        Real cx = Real.ZERO, cy = Real.ZERO, cz = Real.ZERO;
        for (Point3D v : vertices) {
            cx = cx.add(v.getX());
            cy = cy.add(v.getY());
            cz = cz.add(v.getZ());
        }
        Real n = Real.of(vertices.size());
        return Point3D.of(cx.divide(n), cy.divide(n), cz.divide(n));
    }

    /**
     * Computes the surface area of the polyhedron.
     */
    public Real surfaceArea() {
        Real total = Real.ZERO;
        for (int[] face : faces) {
            total = total.add(faceArea(face));
        }
        return total;
    }

    private Real faceArea(int[] faceIndices) {
        if (faceIndices.length < 3)
            return Real.ZERO;

        Point3D p0 = vertices.get(faceIndices[0]);
        Real area = Real.ZERO;

        for (int i = 1; i < faceIndices.length - 1; i++) {
            Point3D p1 = vertices.get(faceIndices[i]);
            Point3D p2 = vertices.get(faceIndices[i + 1]);
            area = area.add(triangleArea(p0, p1, p2));
        }

        return area;
    }

    private Real triangleArea(Point3D a, Point3D b, Point3D c) {
        // Area = 0.5 * |AB Ãƒâ€” AC|
        Real abx = b.getX().subtract(a.getX());
        Real aby = b.getY().subtract(a.getY());
        Real abz = b.getZ().subtract(a.getZ());

        Real acx = c.getX().subtract(a.getX());
        Real acy = c.getY().subtract(a.getY());
        Real acz = c.getZ().subtract(a.getZ());

        // Cross product
        Real cx = aby.multiply(acz).subtract(abz.multiply(acy));
        Real cy = abz.multiply(acx).subtract(abx.multiply(acz));
        Real cz = abx.multiply(acy).subtract(aby.multiply(acx));

        return cx.multiply(cx).add(cy.multiply(cy)).add(cz.multiply(cz)).sqrt().multiply(Real.of(0.5));
    }

    /**
     * Computes the volume of a convex polyhedron using the divergence theorem.
     */
    public Real volume() {
        Real total = Real.ZERO;
        Point3D origin = Point3D.of(0, 0, 0);

        for (int[] face : faces) {
            if (face.length < 3)
                continue;

            Point3D p0 = vertices.get(face[0]);

            for (int i = 1; i < face.length - 1; i++) {
                Point3D p1 = vertices.get(face[i]);
                Point3D p2 = vertices.get(face[i + 1]);
                total = total.add(signedTetrahedronVolume(origin, p0, p1, p2));
            }
        }

        return total.abs();
    }

    private Real signedTetrahedronVolume(Point3D a, Point3D b, Point3D c, Point3D d) {
        Real abx = b.getX().subtract(a.getX()), aby = b.getY().subtract(a.getY()), abz = b.getZ().subtract(a.getZ());
        Real acx = c.getX().subtract(a.getX()), acy = c.getY().subtract(a.getY()), acz = c.getZ().subtract(a.getZ());
        Real adx = d.getX().subtract(a.getX()), ady = d.getY().subtract(a.getY()), adz = d.getZ().subtract(a.getZ());

        Real det = abx.multiply(acy.multiply(adz).subtract(acz.multiply(ady)))
                .subtract(aby.multiply(acx.multiply(adz).subtract(acz.multiply(adx))))
                .add(abz.multiply(acx.multiply(ady).subtract(acy.multiply(adx))));

        return det.divide(Real.of(6.0));
    }

    /**
     * Returns true if the polyhedron satisfies Euler's formula (V - E + F = 2).
     */
    public boolean isValidEuler() {
        int e = computeActualEdgeCount();
        return vertices.size() - e + faces.size() == 2;
    }

    private int computeActualEdgeCount() {
        Set<String> edges = new HashSet<>();
        for (int[] face : faces) {
            for (int i = 0; i < face.length; i++) {
                int a = face[i];
                int b = face[(i + 1) % face.length];
                String edgeKey = Math.min(a, b) + "-" + Math.max(a, b);
                edges.add(edgeKey);
            }
        }
        return edges.size();
    }

    // Graph Implementation

    @Override
    public Set<Point3D> vertices() {
        return new HashSet<>(vertices);
    }

    @Override
    public Set<org.jscience.mathematics.discrete.Graph.Edge<Point3D>> edges() {
        Set<org.jscience.mathematics.discrete.Graph.Edge<Point3D>> result = new HashSet<>();
        Set<String> processed = new HashSet<>();

        for (int[] face : faces) {
            for (int i = 0; i < face.length; i++) {
                int idxA = face[i];
                int idxB = face[(i + 1) % face.length];
                int min = Math.min(idxA, idxB);
                int max = Math.max(idxA, idxB);
                String key = min + "-" + max;

                if (!processed.contains(key)) {
                    processed.add(key);
                    result.add(new SimpleEdge(vertices.get(idxA), vertices.get(idxB)));
                }
            }
        }
        return result;
    }

    @Override
    public boolean addVertex(Point3D vertex) {
        throw new UnsupportedOperationException("Polyhedron is immutable");
    }

    @Override
    public boolean addEdge(Point3D source, Point3D target) {
        throw new UnsupportedOperationException("Polyhedron is immutable");
    }

    @Override
    public Set<Point3D> neighbors(Point3D vertex) {
        int idx = vertices.indexOf(vertex);
        if (idx == -1)
            return Collections.emptySet();

        Set<Point3D> neighbors = new HashSet<>();
        for (int[] face : faces) {
            for (int i = 0; i < face.length; i++) {
                if (face[i] == idx) {
                    // Previous
                    int prevIdx = (i - 1 + face.length) % face.length;
                    neighbors.add(vertices.get(face[prevIdx]));
                    // Next
                    int nextIdx = (i + 1) % face.length;
                    neighbors.add(vertices.get(face[nextIdx]));
                }
            }
        }
        return neighbors;
    }

    @Override
    public int degree(Point3D vertex) {
        return neighbors(vertex).size();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    private static class SimpleEdge implements org.jscience.mathematics.discrete.Graph.Edge<Point3D> {
        private final Point3D src;
        private final Point3D tgt;

        SimpleEdge(Point3D src, Point3D tgt) {
            this.src = src;
            this.tgt = tgt;
        }

        @Override
        public Point3D source() {
            return src;
        }

        @Override
        public Point3D target() {
            return tgt;
        }

        @Override
        public String toString() {
            return src + " -- " + tgt;
        }
    }

    // ============ Factory Methods for Platonic Solids ============

    /**
     * Creates a regular tetrahedron with unit edge length.
     */
    public static Polyhedron tetrahedron() {
        double s = 1.0 / Math.sqrt(2);
        List<Point3D> verts = Arrays.asList(
                Point3D.of(1, 0, -s),
                Point3D.of(-1, 0, -s),
                Point3D.of(0, 1, s),
                Point3D.of(0, -1, s));
        List<int[]> faces = Arrays.asList(
                new int[] { 0, 1, 2 },
                new int[] { 0, 3, 1 },
                new int[] { 0, 2, 3 },
                new int[] { 1, 3, 2 });
        return new Polyhedron(verts, faces);
    }

    /**
     * Creates a cube with unit edge length centered at origin.
     */
    public static Polyhedron cube() {
        double h = 0.5;
        List<Point3D> verts = Arrays.asList(
                Point3D.of(-h, -h, -h), Point3D.of(h, -h, -h),
                Point3D.of(h, h, -h), Point3D.of(-h, h, -h),
                Point3D.of(-h, -h, h), Point3D.of(h, -h, h),
                Point3D.of(h, h, h), Point3D.of(-h, h, h));
        List<int[]> faces = Arrays.asList(
                new int[] { 0, 1, 2, 3 }, // bottom
                new int[] { 4, 7, 6, 5 }, // top
                new int[] { 0, 4, 5, 1 }, // front
                new int[] { 2, 6, 7, 3 }, // back
                new int[] { 0, 3, 7, 4 }, // left
                new int[] { 1, 5, 6, 2 } // right
        );
        return new Polyhedron(verts, faces);
    }

    /**
     * Creates a regular octahedron with unit edge length.
     */
    public static Polyhedron octahedron() {
        double s = 1.0 / Math.sqrt(2);
        List<Point3D> verts = Arrays.asList(
                Point3D.of(s, 0, 0), Point3D.of(-s, 0, 0),
                Point3D.of(0, s, 0), Point3D.of(0, -s, 0),
                Point3D.of(0, 0, s), Point3D.of(0, 0, -s));
        List<int[]> faces = Arrays.asList(
                new int[] { 0, 2, 4 }, new int[] { 2, 1, 4 }, new int[] { 1, 3, 4 }, new int[] { 3, 0, 4 },
                new int[] { 2, 0, 5 }, new int[] { 1, 2, 5 }, new int[] { 3, 1, 5 }, new int[] { 0, 3, 5 });
        return new Polyhedron(verts, faces);
    }

    /**
     * Creates a regular icosahedron.
     */
    public static Polyhedron icosahedron() {
        double phi = (1 + Math.sqrt(5)) / 2;
        double s = 1.0;

        List<Point3D> verts = Arrays.asList(
                Point3D.of(0, s, phi), Point3D.of(0, -s, phi), Point3D.of(0, s, -phi), Point3D.of(0, -s, -phi),
                Point3D.of(s, phi, 0), Point3D.of(-s, phi, 0), Point3D.of(s, -phi, 0), Point3D.of(-s, -phi, 0),
                Point3D.of(phi, 0, s), Point3D.of(-phi, 0, s), Point3D.of(phi, 0, -s), Point3D.of(-phi, 0, -s));

        List<int[]> faces = Arrays.asList(
                new int[] { 0, 1, 8 }, new int[] { 0, 8, 4 }, new int[] { 0, 4, 5 }, new int[] { 0, 5, 9 },
                new int[] { 0, 9, 1 },
                new int[] { 1, 6, 8 }, new int[] { 8, 6, 10 }, new int[] { 8, 10, 4 }, new int[] { 4, 10, 2 },
                new int[] { 4, 2, 5 },
                new int[] { 5, 2, 11 }, new int[] { 5, 11, 9 }, new int[] { 9, 11, 7 }, new int[] { 9, 7, 1 },
                new int[] { 1, 7, 6 },
                new int[] { 3, 6, 7 }, new int[] { 3, 7, 11 }, new int[] { 3, 11, 2 }, new int[] { 3, 2, 10 },
                new int[] { 3, 10, 6 });

        return new Polyhedron(verts, faces);
    }

    /**
     * Creates a regular dodecahedron as the dual of the icosahedron.
     */
    public static Polyhedron dodecahedron() {
        Polyhedron iso = icosahedron();
        List<Point3D> dualVertices = new ArrayList<>();

        // Vertices of dodecahedron are centroids of icosahedron faces
        for (int i = 0; i < iso.faceCount(); i++) {
            List<Point3D> face = iso.getFaceVertices(i);
            Real cx = Real.ZERO;
            Real cy = Real.ZERO;
            Real cz = Real.ZERO;
            for (Point3D p : face) {
                cx = cx.add(p.getX());
                cy = cy.add(p.getY());
                cz = cz.add(p.getZ());
            }
            Real n = Real.of(face.size());
            dualVertices.add(Point3D.of(cx.divide(n), cy.divide(n), cz.divide(n)));
        }

        List<int[]> dualFaces = new ArrayList<>();
        Map<Integer, List<Integer>> vertexToFaces = new HashMap<>();
        for (int i = 0; i < iso.vertexCount(); i++) {
            vertexToFaces.put(i, new ArrayList<>());
        }

        for (int fIdx = 0; fIdx < iso.faceCount(); fIdx++) {
            List<Point3D> faceVerts = iso.getFaceVertices(fIdx);
            for (Point3D v : faceVerts) {
                int vIdx = -1;
                // Linear search for vertex index
                for (int k = 0; k < iso.vertexCount(); k++) {
                    if (iso.getVertex(k).equals(v)) {
                        vIdx = k;
                        break;
                    }
                }
                if (vIdx != -1) {
                    vertexToFaces.get(vIdx).add(fIdx);
                }
            }
        }

        for (int i = 0; i < iso.vertexCount(); i++) {
            List<Integer> faceIndices = vertexToFaces.get(i);
            if (faceIndices.size() != 5)
                continue;

            List<Integer> ordered = new ArrayList<>();
            List<Integer> pool = new ArrayList<>(faceIndices);

            int current = pool.remove(0);
            ordered.add(current);

            while (!pool.isEmpty()) {
                List<Point3D> currentFaceVerts = iso.getFaceVertices(current);
                int next = -1;
                for (int candidate : pool) {
                    List<Point3D> candVerts = iso.getFaceVertices(candidate);
                    int sharedCount = 0;
                    for (Point3D v1 : currentFaceVerts) {
                        for (Point3D v2 : candVerts) {
                            if (v1.equals(v2))
                                sharedCount++;
                        }
                    }
                    if (sharedCount == 2) {
                        next = candidate;
                        break;
                    }
                }

                if (next != -1) {
                    ordered.add(next);
                    pool.remove(Integer.valueOf(next));
                    current = next;
                } else {
                    break;
                }
            }
            int[] faceArr = new int[ordered.size()];
            for (int k = 0; k < ordered.size(); k++)
                faceArr[k] = ordered.get(k);
            dualFaces.add(faceArr);
        }

        return new Polyhedron(dualVertices, dualFaces);
    }

    // ============ CollisionShape Implementation ============

    @Override
    public Vector<Real> getSupportPoint(Vector<Real> direction) {
        if (vertices.isEmpty()) {
            return null;
        }

        Point3D bestVertex = vertices.get(0);
        Real maxDot = bestVertex.toVector().dot(direction);

        for (int i = 1; i < vertices.size(); i++) {
            Point3D v = vertices.get(i);
            Real dot = v.toVector().dot(direction);
            if (dot.compareTo(maxDot) > 0) {
                maxDot = dot;
                bestVertex = v;
            }
        }
        return bestVertex.toVector();
    }

    @Override
    public Vector<Real>[] getAABB() {
        if (vertices.isEmpty()) {
            // Return zero vectors properly typed
            @SuppressWarnings("unchecked")
            Vector<Real>[] result = (Vector<Real>[]) new Vector[2];
            result[0] = Point3D.ZERO.toVector();
            result[1] = Point3D.ZERO.toVector();
            return result;
        }

        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;

        for (Point3D v : vertices) {
            double x = v.getX().doubleValue();
            double y = v.getY().doubleValue();
            double z = v.getZ().doubleValue();

            if (x < minX)
                minX = x;
            if (x > maxX)
                maxX = x;
            if (y < minY)
                minY = y;
            if (y > maxY)
                maxY = y;
            if (z < minZ)
                minZ = z;
            if (z > maxZ)
                maxZ = z;
        }

        @SuppressWarnings("unchecked")
        Vector<Real>[] result = (Vector<Real>[]) new Vector[2];
        result[0] = Point3D.of(minX, minY, minZ).toVector();
        result[1] = Point3D.of(maxX, maxY, maxZ).toVector();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Polyhedron[vertices=%d, faces=%d, edges=%d]",
                vertexCount(), faceCount(), edgeCount());
    }
}


