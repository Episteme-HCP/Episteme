package org.jscience.mathematics.geometry.csg;

import org.jscience.mathematics.geometry.Plane3D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a convex polygon in the CSG mesh.
 */
public class Polygon {
    public final List<Vertex> vertices;
    public final Plane3D plane;

    public Polygon(List<Vertex> vertices) {
        this.vertices = vertices;
        this.plane = Plane3D.fromPoints(
                vertices.get(0).pos,
                vertices.get(1).pos,
                vertices.get(2).pos);
    }

    public Polygon(List<Vertex> vertices, Plane3D plane) {
        this.vertices = vertices;
        this.plane = plane;
    }

    public Polygon clone() {
        // Plane3D is immutable, so no need to clone deep?
        // But if we want consistent API... Plane3D doesn't implement Cloneable?
        // Plane3D is value-like. Just reuse or creating new one (but Plane3D is
        // immutable).
        // Let's reuse.
        return new Polygon(vertices.stream().map(Vertex::clone).collect(Collectors.toList()), plane);
    }

    public void flip() {
        // In-place flip...
    }

    public Polygon flipped() {
        List<Vertex> newVerts = new ArrayList<>();
        // Reverse vertices and flip normals
        for (int i = vertices.size() - 1; i >= 0; i--) {
            newVerts.add(vertices.get(i).flipped());
        }
        return new Polygon(newVerts, plane.flipped());
    }

    /**
     * Triangulates this polygon using Delaunay triangulation.
     * <p>
     * Projects vertices to a 2D plane dominant to the normal, triangulates,
     * and reconstructs 3D triangles.
     * </p>
     * 
     * @return list of triangular Polygons
     */
    public List<Polygon> triangulate() {
        if (vertices.size() < 3)
            return new ArrayList<>();
        if (vertices.size() == 3) {
            List<Polygon> list = new ArrayList<>();
            list.add(this.clone());
            return list;
        }

        // 1. Project to 2D
        // Find dominant axis of normal to project to (XY, YZ, or XZ) to avoid
        // degenerate mapping
        org.jscience.mathematics.geometry.Vector3D n = plane.getNormal();
        double nx = Math.abs(n.getX());
        double ny = Math.abs(n.getY());
        double nz = Math.abs(n.getZ());

        List<org.jscience.mathematics.geometry.Point2D> points2D = new ArrayList<>();
        for (Vertex v : vertices) {
            if (nx > ny && nx > nz) {
                // Project to YZ
                points2D.add(org.jscience.mathematics.geometry.Point2D.of(v.pos.getY(), v.pos.getZ()));
            } else if (ny > nz) {
                // Project to XZ
                points2D.add(org.jscience.mathematics.geometry.Point2D.of(v.pos.getX(), v.pos.getZ()));
            } else {
                // Project to XY
                points2D.add(org.jscience.mathematics.geometry.Point2D.of(v.pos.getX(), v.pos.getY()));
            }
        }

        // 2. Triangulate
        List<org.jscience.mathematics.geometry.triangulation.DelaunayTriangulation.Triangle> tris = org.jscience.mathematics.geometry.triangulation.DelaunayTriangulation
                .triangulate(points2D);

        // 3. Map back to 3D Polygons through vertex mapping
        // Robustness note: Delaunay might create new points or swap edges?
        // Standard Delaunay keeps existing vertices. We need to map 2D point back to
        // original Vertex.
        // Since we created points2D in order, we can map back by index or coordinate
        // equality.
        // But DelaunayTriangulation returns Triangles with Point2D objects.
        // We need to find which index each Point2D corresponds to.

        List<Polygon> result = new ArrayList<>();
        for (org.jscience.mathematics.geometry.triangulation.DelaunayTriangulation.Triangle t : tris) {
            Vertex v1 = findVertex(points2D, t.p1);
            Vertex v2 = findVertex(points2D, t.p2);
            Vertex v3 = findVertex(points2D, t.p3);

            if (v1 != null && v2 != null && v3 != null) {
                List<Vertex> triVerts = new ArrayList<>();
                triVerts.add(v1.clone());
                triVerts.add(v2.clone());
                triVerts.add(v3.clone());
                result.add(new Polygon(triVerts, this.plane));
            }
        }
        return result;
    }

    private Vertex findVertex(List<org.jscience.mathematics.geometry.Point2D> projected,
            org.jscience.mathematics.geometry.Point2D p) {
        for (int i = 0; i < projected.size(); i++) {
            if (projected.get(i).equals(p)) {
                return vertices.get(i);
            }
        }
        return null;
    }
}
