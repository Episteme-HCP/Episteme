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

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting between boundary dimensions.
 * <p>
 * Provides methods to:
 * <ul>
 *   <li>Lift 2D boundaries to 3D (on a plane)</li>
 *   <li>Project 3D boundaries to 2D</li>
 *   <li>Extrude 2D to 3D volumes</li>
 *   <li>Convert between coordinate systems</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class BoundaryConverter {

    private BoundaryConverter() {}

    // ===== 2D to 3D Conversion =====

    /**
     * Lifts a 2D boundary onto the XY plane at z=0.
     *
     * @param boundary2D the 2D boundary
     * @return the 3D boundary on the XY plane
     */
    public static Boundary3D liftToXY(Boundary2D boundary2D) {
        return liftToXY(boundary2D, Real.ZERO);
    }

    /**
     * Lifts a 2D boundary onto a plane parallel to XY at given z.
     *
     * @param boundary2D the 2D boundary
     * @param z          the z-coordinate of the plane
     * @return the 3D boundary
     */
    public static Boundary3D liftToXY(Boundary2D boundary2D, Real z) {
        List<Point2D> verts2d = boundary2D.getVertices();
        List<ConvexPolyhedron3D.TriangularFace> faces = new ArrayList<>();
        
        // Create a flat surface (single face for simple cases)
        List<Point3D> verts3d = new ArrayList<>();
        for (Point2D p : verts2d) {
            verts3d.add(new Point3D(p.getX(), p.getY(), z));
        }
        
        // Triangulate the polygon
        int n = verts3d.size();
        for (int i = 1; i < n - 1; i++) {
            faces.add(new ConvexPolyhedron3D.TriangularFace(
                verts3d.get(0), verts3d.get(i), verts3d.get(i + 1)
            ));
        }
        
        return new ConvexPolyhedron3D(faces);
    }

    /**
     * Lifts a 2D boundary onto the XZ plane at y=0.
     *
     * @param boundary2D the 2D boundary
     * @return the 3D boundary on the XZ plane
     */
    public static Boundary3D liftToXZ(Boundary2D boundary2D) {
        return liftToXZ(boundary2D, Real.ZERO);
    }

    /**
     * Lifts a 2D boundary onto a plane parallel to XZ at given y.
     *
     * @param boundary2D the 2D boundary
     * @param y          the y-coordinate of the plane
     * @return the 3D boundary
     */
    public static Boundary3D liftToXZ(Boundary2D boundary2D, Real y) {
        List<Point2D> verts2d = boundary2D.getVertices();
        List<ConvexPolyhedron3D.TriangularFace> faces = new ArrayList<>();
        
        List<Point3D> verts3d = new ArrayList<>();
        for (Point2D p : verts2d) {
            verts3d.add(new Point3D(p.getX(), y, p.getY()));
        }
        
        int n = verts3d.size();
        for (int i = 1; i < n - 1; i++) {
            faces.add(new ConvexPolyhedron3D.TriangularFace(
                verts3d.get(0), verts3d.get(i), verts3d.get(i + 1)
            ));
        }
        
        return new ConvexPolyhedron3D(faces);
    }

    /**
     * Lifts a 2D boundary onto the YZ plane at x=0.
     *
     * @param boundary2D the 2D boundary
     * @return the 3D boundary on the YZ plane
     */
    public static Boundary3D liftToYZ(Boundary2D boundary2D) {
        return liftToYZ(boundary2D, Real.ZERO);
    }

    /**
     * Lifts a 2D boundary onto a plane parallel to YZ at given x.
     *
     * @param boundary2D the 2D boundary
     * @param x          the x-coordinate of the plane
     * @return the 3D boundary
     */
    public static Boundary3D liftToYZ(Boundary2D boundary2D, Real x) {
        List<Point2D> verts2d = boundary2D.getVertices();
        List<ConvexPolyhedron3D.TriangularFace> faces = new ArrayList<>();
        
        List<Point3D> verts3d = new ArrayList<>();
        for (Point2D p : verts2d) {
            verts3d.add(new Point3D(x, p.getX(), p.getY()));
        }
        
        int n = verts3d.size();
        for (int i = 1; i < n - 1; i++) {
            faces.add(new ConvexPolyhedron3D.TriangularFace(
                verts3d.get(0), verts3d.get(i), verts3d.get(i + 1)
            ));
        }
        
        return new ConvexPolyhedron3D(faces);
    }

    /**
     * Extrudes a 2D boundary into a 3D prism.
     *
     * @param boundary2D the 2D boundary
     * @param height     the extrusion height
     * @return the 3D prism
     */
    public static Boundary3D extrude(Boundary2D boundary2D, Real height) {
        return ConvexPolyhedron3D.extrude(boundary2D, height);
    }

    /**
     * Extrudes along a vector direction.
     *
     * @param boundary2D the 2D boundary
     * @param direction  the extrusion direction vector
     * @return the 3D boundary
     */
    public static Boundary3D extrudeAlong(Boundary2D boundary2D, Vector3D direction) {
        List<Point2D> verts2d = boundary2D.getVertices();
        int n = verts2d.size();
        
        List<Point3D> bottom = new ArrayList<>(n);
        List<Point3D> top = new ArrayList<>(n);
        
        for (Point2D p : verts2d) {
            Point3D base = new Point3D(p.getX(), p.getY(), Real.ZERO);
            bottom.add(base);
            top.add(new Point3D(
                base.getX().add(direction.getX()),
                base.getY().add(direction.getY()),
                base.getZ().add(direction.getZ())
            ));
        }

        List<ConvexPolyhedron3D.TriangularFace> faces = new ArrayList<>();

        // Bottom face
        for (int i = 1; i < n - 1; i++) {
            faces.add(new ConvexPolyhedron3D.TriangularFace(bottom.get(0), bottom.get(i + 1), bottom.get(i)));
        }

        // Top face
        for (int i = 1; i < n - 1; i++) {
            faces.add(new ConvexPolyhedron3D.TriangularFace(top.get(0), top.get(i), top.get(i + 1)));
        }

        // Side faces
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            faces.add(new ConvexPolyhedron3D.TriangularFace(bottom.get(i), bottom.get(j), top.get(i)));
            faces.add(new ConvexPolyhedron3D.TriangularFace(top.get(i), bottom.get(j), top.get(j)));
        }

        return new ConvexPolyhedron3D(faces);
    }

    // ===== 3D to 2D Projection =====

    /**
     * Projects a 3D boundary onto the XY plane.
     *
     * @param boundary3D the 3D boundary
     * @return the 2D projection
     */
    public static Boundary2D projectToXY(Boundary3D boundary3D) {
        return boundary3D.projectXY();
    }

    /**
     * Projects a 3D boundary onto the XZ plane.
     *
     * @param boundary3D the 3D boundary
     * @return the 2D projection
     */
    public static Boundary2D projectToXZ(Boundary3D boundary3D) {
        return boundary3D.projectXZ();
    }

    /**
     * Projects a 3D boundary onto the YZ plane.
     *
     * @param boundary3D the 3D boundary
     * @return the 2D projection
     */
    public static Boundary2D projectToYZ(Boundary3D boundary3D) {
        return boundary3D.projectYZ();
    }

    /**
     * Projects a 3D boundary onto an arbitrary plane.
     *
     * @param boundary3D  the 3D boundary
     * @param planeNormal the plane normal
     * @return the 2D projection
     */
    public static Boundary2D projectToPlane(Boundary3D boundary3D, Vector3D planeNormal) {
        // Find basis vectors for the plane
        Vector3D n = planeNormal.normalize();
        Vector3D u = findPerpendicularVector(n).normalize();
        Vector3D v = n.cross(u).normalize();
        
        List<Point3D> verts3d = boundary3D.getVertices();
        List<Point2D> verts2d = new ArrayList<>();
        
        for (Point3D p : verts3d) {
            // Project onto plane using dot product with basis vectors
            Vector3D pv = new Vector3D(p.getX(), p.getY(), p.getZ());
            Real x = pv.dot(u);
            Real y = pv.dot(v);
            verts2d.add(new Point2D(x, y));
        }
        
        return ConvexPolygon2D.fromPoints(verts2d);
    }

    private static Vector3D findPerpendicularVector(Vector3D v) {
        // Find a vector perpendicular to v
        if (v.getX().abs().compareTo(v.getY().abs()) < 0) {
            return new Vector3D(Real.ONE, Real.ZERO, Real.ZERO).cross(v);
        } else {
            return new Vector3D(Real.ZERO, Real.ONE, Real.ZERO).cross(v);
        }
    }

    // ===== Point Conversion =====

    /**
     * Converts a 2D point to 3D on the XY plane (z=0).
     *
     * @param point2D the 2D point
     * @return the 3D point
     */
    public static Point3D toPoint3D(Point2D point2D) {
        return new Point3D(point2D.getX(), point2D.getY(), Real.ZERO);
    }

    /**
     * Converts a 2D point to 3D with specified z.
     *
     * @param point2D the 2D point
     * @param z       the z coordinate
     * @return the 3D point
     */
    public static Point3D toPoint3D(Point2D point2D, Real z) {
        return new Point3D(point2D.getX(), point2D.getY(), z);
    }

    /**
     * Projects a 3D point to 2D on the XY plane.
     *
     * @param point3D the 3D point
     * @return the 2D point
     */
    public static Point2D toPoint2D(Point3D point3D) {
        return new Point2D(point3D.getX(), point3D.getY());
    }

    /**
     * Projects a 3D point to 2D on the XZ plane.
     *
     * @param point3D the 3D point
     * @return the 2D point (x, z)
     */
    public static Point2D toPoint2DXZ(Point3D point3D) {
        return new Point2D(point3D.getX(), point3D.getZ());
    }

    /**
     * Projects a 3D point to 2D on the YZ plane.
     *
     * @param point3D the 3D point
     * @return the 2D point (y, z)
     */
    public static Point2D toPoint2DYZ(Point3D point3D) {
        return new Point2D(point3D.getY(), point3D.getZ());
    }

    // ===== Fuzzy Boundary Conversion =====

    /**
     * Converts a fuzzy 2D boundary to crisp by alpha-cut.
     *
     * @param fuzzy the fuzzy boundary
     * @param alpha the alpha threshold [0, 1]
     * @return the crisp boundary
     */
    public static Boundary2D toCrisp(FuzzyBoundary2D fuzzy, Real alpha) {
        return fuzzy.alphaCut(alpha);
    }

    /**
     * Creates a fuzzy boundary from a crisp boundary with a buffer.
     *
     * @param crisp       the crisp boundary
     * @param bufferWidth the width of the fuzzy transition zone
     * @return the fuzzy boundary
     */
    public static FuzzyBoundary2D toFuzzy(Boundary2D crisp, Real bufferWidth) {
        return FuzzyBoundary2D.withBuffer(crisp, bufferWidth);
    }

    // ===== Composite Boundary Flattening =====

    /**
     * Flattens a composite boundary to a single convex hull.
     *
     * @param composite the composite boundary
     * @return the convex hull containing all regions
     */
    public static Boundary2D toConvexHull(CompositeBoundary2D composite) {
        return (Boundary2D) composite.convexHull();
    }

    /**
     * Splits a composite boundary into its individual regions.
     *
     * @param composite the composite boundary
     * @return list of individual boundaries
     */
    public static List<Boundary2D> toIndividualRegions(CompositeBoundary2D composite) {
        return composite.getInclusions();
    }
}
