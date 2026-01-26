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

package org.jscience.mathematics.geometry.boundaries;

import org.jscience.mathematics.geometry.Point2D;
import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.geometry.Vector3D;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.geometry.ConvexHull2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A convex polyhedron in 3D space.
 * <p>
 * Defined by a set of triangular faces with outward-facing normals.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ConvexPolyhedron3D implements Boundary3D {

    private static final long serialVersionUID = 1L;

    /**
     * Represents a triangular face.
     */
    public static class TriangularFace implements Face {
        private final Point3D v1, v2, v3;
        private final Vector3D normal;
        private final Real area;

        public TriangularFace(Point3D v1, Point3D v2, Point3D v3) {
            this.v1 = Objects.requireNonNull(v1);
            this.v2 = Objects.requireNonNull(v2);
            this.v3 = Objects.requireNonNull(v3);
            
            Vector3D edge1 = v2.subtract(v1);
            Vector3D edge2 = v3.subtract(v1);
            Vector3D cross = edge1.cross(edge2);
            this.area = cross.magnitude().divide(Real.of(2));
            this.normal = cross.magnitude().isZero() ? Vector3D.ZERO : cross.normalize();
        }

        @Override
        public List<Point3D> getVertices() {
            return List.of(v1, v2, v3);
        }

        @Override
        public Vector3D getNormal() {
            return normal;
        }

        @Override
        public Real getArea() {
            return area;
        }

        /**
         * Signed distance from a point to the plane of this face.
         * Positive = in front of face (outside), negative = behind.
         */
        public Real signedDistanceTo(Point3D p) {
            return p.subtract(v1).dot(normal);
        }

        /**
         * Checks if a point is in front of this face (outside the polyhedron).
         */
        public boolean isInFront(Point3D p) {
            return signedDistanceTo(p).compareTo(Real.ZERO) > 0;
        }
    }

    private final List<TriangularFace> faces;
    private final List<Point3D> vertices;
    private transient Real cachedVolume;
    private transient Real cachedSurfaceArea;
    private transient Point3D cachedCentroid;

    /**
     * Creates a convex polyhedron from faces.
     *
     * @param faces the triangular faces
     */
    public ConvexPolyhedron3D(List<TriangularFace> faces) {
        this.faces = new ArrayList<>(Objects.requireNonNull(faces));
        this.vertices = extractUniqueVertices(faces);
    }

    private static List<Point3D> extractUniqueVertices(List<TriangularFace> faces) {
        // Simple extraction - doesn't remove duplicates for now
        List<Point3D> verts = new ArrayList<>();
        for (TriangularFace f : faces) {
            verts.addAll(f.getVertices());
        }
        return verts;
    }

    // ===== Factory Methods =====

    /**
     * Creates a tetrahedron from 4 points.
     */
    public static ConvexPolyhedron3D tetrahedron(Point3D a, Point3D b, Point3D c, Point3D d) {
        List<TriangularFace> faces = List.of(
            new TriangularFace(a, b, c),
            new TriangularFace(a, c, d),
            new TriangularFace(a, d, b),
            new TriangularFace(b, d, c)
        );
        return new ConvexPolyhedron3D(faces);
    }

    /**
     * Creates a box (rectangular parallelepiped).
     */
    public static ConvexPolyhedron3D box(Point3D min, Point3D max) {
        Real x0 = min.getX(), y0 = min.getY(), z0 = min.getZ();
        Real x1 = max.getX(), y1 = max.getY(), z1 = max.getZ();

        Point3D v000 = Point3D.of(x0, y0, z0);
        Point3D v001 = Point3D.of(x0, y0, z1);
        Point3D v010 = Point3D.of(x0, y1, z0);
        Point3D v011 = Point3D.of(x0, y1, z1);
        Point3D v100 = Point3D.of(x1, y0, z0);
        Point3D v101 = Point3D.of(x1, y0, z1);
        Point3D v110 = Point3D.of(x1, y1, z0);
        Point3D v111 = Point3D.of(x1, y1, z1);

        // 12 triangles (2 per face)
        List<TriangularFace> faces = List.of(
            // Bottom (z=0)
            new TriangularFace(v000, v010, v100),
            new TriangularFace(v100, v010, v110),
            // Top (z=1)
            new TriangularFace(v001, v101, v011),
            new TriangularFace(v011, v101, v111),
            // Front (y=0)
            new TriangularFace(v000, v100, v001),
            new TriangularFace(v001, v100, v101),
            // Back (y=1)
            new TriangularFace(v010, v011, v110),
            new TriangularFace(v110, v011, v111),
            // Left (x=0)
            new TriangularFace(v000, v001, v010),
            new TriangularFace(v010, v001, v011),
            // Right (x=1)
            new TriangularFace(v100, v110, v101),
            new TriangularFace(v101, v110, v111)
        );
        return new ConvexPolyhedron3D(faces);
    }

    /**
     * Extrudes a 2D polygon into a 3D prism.
     *
     * @param polygon the base polygon
     * @param height  the extrusion height
     * @return the extruded polyhedron
     */
    public static ConvexPolyhedron3D extrude(Boundary2D polygon, Real height) {
        List<Point2D> verts2d = polygon.getVertices();
        int n = verts2d.size();
        
        // Create bottom and top vertices
        List<Point3D> bottom = new ArrayList<>(n);
        List<Point3D> top = new ArrayList<>(n);
        
        for (Point2D p : verts2d) {
            bottom.add(Point3D.of(p.getX(), p.getY(), Real.ZERO));
            top.add(Point3D.of(p.getX(), p.getY(), height));
        }

        List<TriangularFace> faces = new ArrayList<>();

        // Bottom face (triangulate)
        for (int i = 1; i < n - 1; i++) {
            faces.add(new TriangularFace(bottom.get(0), bottom.get(i + 1), bottom.get(i)));
        }

        // Top face
        for (int i = 1; i < n - 1; i++) {
            faces.add(new TriangularFace(top.get(0), top.get(i), top.get(i + 1)));
        }

        // Side faces (2 triangles each)
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            faces.add(new TriangularFace(bottom.get(i), bottom.get(j), top.get(i)));
            faces.add(new TriangularFace(top.get(i), bottom.get(j), top.get(j)));
        }

        return new ConvexPolyhedron3D(faces);
    }

    // ===== Boundary3D Implementation =====

    @Override
    public List<Point3D> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    @Override
    public List<Face> getFaces() {
        return Collections.unmodifiableList(faces);
    }

    @Override
    public Real getVolume() {
        if (cachedVolume != null) return cachedVolume;
        
        // Sum of signed tetrahedron volumes
        Point3D origin = Point3D.ZERO;
        Real sum = Real.ZERO;
        
        for (TriangularFace f : faces) {
            List<Point3D> verts = f.getVertices();
            Real tetVol = tetrahedronVolume(origin, verts.get(0), verts.get(1), verts.get(2));
            sum = sum.add(tetVol);
        }
        
        cachedVolume = sum.abs();
        return cachedVolume;
    }

    private Real tetrahedronVolume(Point3D a, Point3D b, Point3D c, Point3D d) {
        Vector3D ab = b.subtract(a);
        Vector3D ac = c.subtract(a);
        Vector3D ad = d.subtract(a);
        return ab.dot(ac.cross(ad)).divide(Real.of(6));
    }

    @Override
    public Real getSurfaceArea() {
        if (cachedSurfaceArea != null) return cachedSurfaceArea;
        
        Real sum = Real.ZERO;
        for (TriangularFace f : faces) {
            sum = sum.add(f.getArea());
        }
        cachedSurfaceArea = sum;
        return cachedSurfaceArea;
    }

    @Override
    public boolean isConvex() {
        return true; // By definition
    }

    @Override
    public boolean isEmpty() {
        return faces.isEmpty();
    }

    @Override
    public Point3D getCentroid() {
        if (cachedCentroid != null) return cachedCentroid;
        
        Real sumX = Real.ZERO, sumY = Real.ZERO, sumZ = Real.ZERO;
        for (Point3D v : vertices) {
            sumX = sumX.add(v.getX());
            sumY = sumY.add(v.getY());
            sumZ = sumZ.add(v.getZ());
        }
        int n = vertices.size();
        cachedCentroid = Point3D.of(
            sumX.divide(Real.of(n)),
            sumY.divide(Real.of(n)),
            sumZ.divide(Real.of(n))
        );
        return cachedCentroid;
    }

    @Override
    public BoundingBox<Point3D> getBoundingBox() {
        return BoundingBox3D.fromPoints(vertices);
    }

    @Override
    public boolean contains(Point3D point) {
        if (point == null) return false;
        
        // For convex polyhedron: point is inside if behind all faces
        for (TriangularFace f : faces) {
            if (f.isInFront(point)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean intersects(Boundary<Point3D> other) {
        // Check if any vertex is inside the other
        for (Point3D v : vertices) {
            if (other.contains(v)) return true;
        }
        if (other instanceof Boundary3D) {
            for (Point3D v : ((Boundary3D) other).getVertices()) {
                if (this.contains(v)) return true;
            }
        }
        return false;
    }

    @Override
    public Boundary<Point3D> union(Boundary<Point3D> other) {
        throw new UnsupportedOperationException("3D union not yet implemented");
    }

    @Override
    public Boundary<Point3D> intersection(Boundary<Point3D> other) {
        throw new UnsupportedOperationException("3D intersection not yet implemented");
    }

    @Override
    public Boundary<Point3D> convexHull() {
        return this;
    }

    @Override
    public Boundary<Point3D> translate(Point3D offset) {
        List<TriangularFace> newFaces = new ArrayList<>();
        for (TriangularFace f : faces) {
            List<Point3D> verts = f.getVertices();
            newFaces.add(new TriangularFace(
                translatePoint(verts.get(0), offset),
                translatePoint(verts.get(1), offset),
                translatePoint(verts.get(2), offset)
            ));
        }
        return new ConvexPolyhedron3D(newFaces);
    }

    private Point3D translatePoint(Point3D p, Point3D offset) {
        return Point3D.of(
            p.getX().add(offset.getX()),
            p.getY().add(offset.getY()),
            p.getZ().add(offset.getZ())
        );
    }

    @Override
    public Boundary<Point3D> scale(Real factor) {
        Point3D center = getCentroid();
        List<TriangularFace> newFaces = new ArrayList<>();
        for (TriangularFace f : faces) {
            List<Point3D> verts = f.getVertices();
            newFaces.add(new TriangularFace(
                scalePoint(verts.get(0), center, factor),
                scalePoint(verts.get(1), center, factor),
                scalePoint(verts.get(2), center, factor)
            ));
        }
        return new ConvexPolyhedron3D(newFaces);
    }

    private Point3D scalePoint(Point3D p, Point3D center, Real factor) {
        return Point3D.of(
            center.getX().add(p.getX().subtract(center.getX()).multiply(factor)),
            center.getY().add(p.getY().subtract(center.getY()).multiply(factor)),
            center.getZ().add(p.getZ().subtract(center.getZ()).multiply(factor))
        );
    }

    @Override
    public Boundary3D rotateX(Real angleRadians) {
        return rotateAround(new Vector3D(Real.ONE, Real.ZERO, Real.ZERO), angleRadians);
    }

    @Override
    public Boundary3D rotateY(Real angleRadians) {
        return rotateAround(new Vector3D(Real.ZERO, Real.ONE, Real.ZERO), angleRadians);
    }

    @Override
    public Boundary3D rotateZ(Real angleRadians) {
        return rotateAround(new Vector3D(Real.ZERO, Real.ZERO, Real.ONE), angleRadians);
    }

    @Override
    public Boundary3D rotateAround(Vector3D axis, Real angleRadians) {
        Vector3D k = axis.normalize();
        double theta = angleRadians.doubleValue();
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        
        List<TriangularFace> newFaces = new ArrayList<>();
        Point3D center = getCentroid();
        
        for (TriangularFace f : faces) {
            List<Point3D> verts = f.getVertices();
            newFaces.add(new TriangularFace(
                rotatePoint(verts.get(0), center, k, cos, sin),
                rotatePoint(verts.get(1), center, k, cos, sin),
                rotatePoint(verts.get(2), center, k, cos, sin)
            ));
        }
        return new ConvexPolyhedron3D(newFaces);
    }

    private Point3D rotatePoint(Point3D p, Point3D center, Vector3D k, double cos, double sin) {
        // Translate to origin
        Vector3D v = p.subtract(center);
        
        // Rodrigues' rotation formula
        // v_rot = v * cos + (k x v) * sin + k * (k . v) * (1 - cos)
        Vector3D kCrossV = k.cross(v);
        Real kDotV = k.dot(v);
        
        double vx = v.getX();
        double vy = v.getY();
        double vz = v.getZ();
        
        double kx = k.getX();
        double ky = k.getY();
        double kz = k.getZ();
        double kDotVVal = kDotV.doubleValue();
        double kCrossVx = kCrossV.getX();
        double kCrossVy = kCrossV.getY();
        double kCrossVz = kCrossV.getZ();

        double rx = vx * cos + kCrossVx * sin + kx * kDotVVal * (1 - cos);
        double ry = vy * cos + kCrossVy * sin + ky * kDotVVal * (1 - cos);
        double rz = vz * cos + kCrossVz * sin + kz * kDotVVal * (1 - cos);
        
        // Translate back
        return Point3D.of(
            center.getX().add(Real.of(rx)),
            center.getY().add(Real.of(ry)),
            center.getZ().add(Real.of(rz))
        );
    }
    
    @Override
    public Boundary2D projectXY() {
        List<Point2D> projected = new ArrayList<>();
        for (Point3D v : vertices) {
            projected.add(Point2D.of(v.getX(), v.getY()));
        }
        return ConvexPolygon2D.fromPoints(projected);
    }

    @Override
    public Boundary2D projectXZ() {
        List<Point2D> projected = new ArrayList<>();
        for (Point3D v : vertices) {
            projected.add(Point2D.of(v.getX(), v.getZ()));
        }
        return ConvexPolygon2D.fromPoints(projected);
    }

    @Override
    public Boundary2D projectYZ() {
        List<Point2D> projected = new ArrayList<>();
        for (Point3D v : vertices) {
            projected.add(Point2D.of(v.getY(), v.getZ()));
        }
        return ConvexPolygon2D.fromPoints(projected);
    }

    @Override
    public Boundary2D slice(Vector3D planeNormal, Point3D pointOnPlane) {
        Vector3D normal = planeNormal.normalize();
        List<Point3D> intersectionPoints = new ArrayList<>();
        
        // Find intersection of edges with the plane
        // An edge is defined by two vertices of a face.
        // We iterate unique edges from faces.
        // Simple approach: Check all pairs in faces (inefficient but safe for now)
        
        for (TriangularFace f : faces) {
            List<Point3D> v = f.getVertices();
            checkEdgeIntersection(v.get(0), v.get(1), normal, pointOnPlane, intersectionPoints);
            checkEdgeIntersection(v.get(1), v.get(2), normal, pointOnPlane, intersectionPoints);
            checkEdgeIntersection(v.get(2), v.get(0), normal, pointOnPlane, intersectionPoints);
        }
        
        if (intersectionPoints.size() < 3) return new ConvexPolygon2D(new ArrayList<>());
        
        // Project 3D points to 2D
        // Create a basis for the plane
        // Z' = normal
        // X' = arbitrary vector orthogonal to normal
        // Y' = Z' cross X'
        
        Vector3D arbitrary = Math.abs(normal.getX()) < 0.9 ? 
            new Vector3D(Real.ONE, Real.ZERO, Real.ZERO) : 
            new Vector3D(Real.ZERO, Real.ONE, Real.ZERO);

        Vector3D basisX = normal.cross(arbitrary).normalize();
        Vector3D basisY = normal.cross(basisX).normalize();
        
        List<Point2D> projected = new ArrayList<>();
        Point3D origin = intersectionPoints.get(0); // Use first point as local origin to keep numbers small
        
        for (Point3D p : intersectionPoints) {
            Vector3D diff = p.subtract(origin);
            projected.add(Point2D.of(diff.dot(basisX), diff.dot(basisY)));
        }
        
        // ConvexHull2D to order vertices correctly
        return ConvexHull2D.computePolygon(projected);
    }

    private void checkEdgeIntersection(Point3D a, Point3D b, Vector3D normal, Point3D p0, List<Point3D> points) {
        Real distA = a.subtract(p0).dot(normal);
        Real distB = b.subtract(p0).dot(normal);
        
        // Check if signs are different (one pos, one neg) -> intersection
        if ((distA.compareTo(Real.ZERO) > 0) != (distB.compareTo(Real.ZERO) > 0)) {
            Real diff = distB.subtract(distA);
            if (!diff.isZero()) {
                Real t = distA.negate().divide(diff);
                double tVal = t.doubleValue();
                
                double abX = b.getX().doubleValue() - a.getX().doubleValue();
                double abY = b.getY().doubleValue() - a.getY().doubleValue();
                double abZ = b.getZ().doubleValue() - a.getZ().doubleValue();

                Point3D intersection = Point3D.of(
                    Real.of(a.getX().doubleValue() + abX * tVal),
                    Real.of(a.getY().doubleValue() + abY * tVal),
                    Real.of(a.getZ().doubleValue() + abZ * tVal)
                );
                
                // Avoid duplicates
                boolean duplicate = false;
                for (Point3D existing : points) {
                    if (existing.distanceTo(intersection).compareTo(Real.of(1e-9)) < 0) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    points.add(intersection);
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("ConvexPolyhedron3D[%d faces, %d vertices, volume=%s]",
            faces.size(), vertices.size(), getVolume());
    }
}
