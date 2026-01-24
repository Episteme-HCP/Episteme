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
import org.jscience.mathematics.numbers.real.Real;

import java.util.List;

/**
 * Interface for two-dimensional boundaries (regions in a plane).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Boundary2D extends Boundary<Point2D> {

    @Override
    default int getDimension() {
        return 2;
    }

    /**
     * Returns the vertices of this boundary in order.
     * @return list of vertices
     */
    List<Point2D> getVertices();

    /**
     * Returns the number of vertices.
     * @return vertex count
     */
    int getVertexCount();

    /**
     * Returns a specific vertex.
     *
     * @param index the vertex index
     * @return the vertex
     */
    Point2D getVertex(int index);

    /**
     * Returns the area of this 2D boundary.
     * @return the area
     */
    Real getArea();

    /**
     * Returns the perimeter of this 2D boundary.
     * @return the perimeter
     */
    Real getPerimeter();

    @Override
    default Real getMeasure() {
        return getArea();
    }

    @Override
    default Real getBoundaryMeasure() {
        return getPerimeter();
    }

    /**
     * Checks if this boundary is convex.
     * @return true if convex
     */
    boolean isConvex();

    /**
     * Rotates the boundary around the centroid.
     *
     * @param angleRadians rotation angle in radians
     * @return rotated boundary
     */
    Boundary2D rotate(Real angleRadians);

    /**
     * Rotates around a specific pivot point.
     *
     * @param angleRadians rotation angle
     * @param pivot        the pivot point
     * @return rotated boundary
     */
    Boundary2D rotateAround(Real angleRadians, Point2D pivot);

    /**
     * Creates an extruded 3D boundary from this 2D boundary.
     *
     * @param height the extrusion height
     * @return a 3D boundary
     */
    Boundary3D extrude(Real height);
}
