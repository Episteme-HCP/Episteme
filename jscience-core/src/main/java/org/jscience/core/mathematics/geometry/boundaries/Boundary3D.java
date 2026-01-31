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

package org.jscience.core.mathematics.geometry.boundaries;

import org.jscience.core.mathematics.geometry.Point3D;
import org.jscience.core.mathematics.geometry.Vector3D;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.List;

/**
 * Interface for three-dimensional boundaries (volumes).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Boundary3D extends Boundary<Point3D> {

    @Override
    default int getDimension() {
        return 3;
    }

    /**
     * Represents a face of the 3D boundary.
     */
    interface Face {
        /**
         * Returns the vertices of this face.
         * @return list of vertices
         */
        List<Point3D> getVertices();

        /**
         * Returns the outward-facing normal vector.
         * @return the normal
         */
        Vector3D getNormal();

        /**
         * Returns the area of this face.
         * @return the area
         */
        Real getArea();
    }

    /**
     * Returns all vertices of this 3D boundary.
     * @return list of vertices
     */
    List<Point3D> getVertices();

    /**
     * Returns all faces of this 3D boundary.
     * @return list of faces
     */
    List<Face> getFaces();

    /**
     * Returns the volume of this 3D boundary.
     * @return the volume
     */
    Real getVolume();

    /**
     * Returns the surface area.
     * @return the surface area
     */
    Real getSurfaceArea();

    @Override
    default Real getMeasure() {
        return getVolume();
    }

    @Override
    default Real getBoundaryMeasure() {
        return getSurfaceArea();
    }

    /**
     * Checks if this is a convex polyhedron.
     * @return true if convex
     */
    boolean isConvex();

    /**
     * Rotates around the X axis.
     *
     * @param angleRadians rotation angle
     * @return rotated boundary
     */
    Boundary3D rotateX(Real angleRadians);

    /**
     * Rotates around the Y axis.
     *
     * @param angleRadians rotation angle
     * @return rotated boundary
     */
    Boundary3D rotateY(Real angleRadians);

    /**
     * Rotates around the Z axis.
     *
     * @param angleRadians rotation angle
     * @return rotated boundary
     */
    Boundary3D rotateZ(Real angleRadians);

    /**
     * Rotates around an arbitrary axis.
     *
     * @param axis         the rotation axis
     * @param angleRadians rotation angle
     * @return rotated boundary
     */
    Boundary3D rotateAround(Vector3D axis, Real angleRadians);

    /**
     * Projects this 3D boundary onto the XY plane.
     * @return a 2D boundary
     */
    Boundary2D projectXY();

    /**
     * Projects this 3D boundary onto the XZ plane.
     * @return a 2D boundary
     */
    Boundary2D projectXZ();

    /**
     * Projects this 3D boundary onto the YZ plane.
     * @return a 2D boundary
     */
    Boundary2D projectYZ();

    /**
     * Slices this boundary with a plane.
     *
     * @param planeNormal the plane normal
     * @param pointOnPlane a point on the plane
     * @return the 2D cross-section
     */
    Boundary2D slice(Vector3D planeNormal, Point3D pointOnPlane);
}

