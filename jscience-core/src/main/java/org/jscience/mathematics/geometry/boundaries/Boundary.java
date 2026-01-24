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

import org.jscience.mathematics.numbers.real.Real;

import java.io.Serializable;

/**
 * Base interface for all geometric boundaries.
 * <p>
 * A boundary represents a closed region in N-dimensional space. This interface
 * defines common operations for containment testing, area/volume calculation,
 * and boundary manipulation.
 *
 * @param <P> the point type used for this boundary
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Boundary<P> extends Serializable {

    /**
     * Returns the dimensionality of this boundary.
     * @return 0 for point, 1 for line, 2 for polygon, 3 for polyhedron
     */
    int getDimension();

    /**
     * Checks if a point is contained within this boundary.
     *
     * @param point the point to test
     * @return true if point is inside or on the boundary
     */
    boolean contains(P point);

    /**
     * Checks if this boundary is empty.
     * @return true if the boundary has no extent
     */
    boolean isEmpty();

    /**
     * Returns the centroid of this boundary.
     * @return the geometric center
     */
    P getCentroid();

    /**
     * Returns the bounding box of this boundary.
     * @return an axis-aligned bounding box
     */
    BoundingBox<P> getBoundingBox();

    /**
     * Computes the measure (length/area/volume) of this boundary.
     * For 2D: area, for 3D: volume, for 1D: length.
     *
     * @return the measure as a Real value
     */
    Real getMeasure();

    /**
     * Computes the boundary measure (perimeter/surface area).
     * For 2D: perimeter, for 3D: surface area.
     *
     * @return the boundary measure
     */
    Real getBoundaryMeasure();

    /**
     * Checks if this boundary intersects with another.
     *
     * @param other the other boundary
     * @return true if boundaries intersect
     */
    boolean intersects(Boundary<P> other);

    /**
     * Computes the union of this boundary with another.
     *
     * @param other the other boundary
     * @return the union boundary
     */
    Boundary<P> union(Boundary<P> other);

    /**
     * Computes the intersection of this boundary with another.
     *
     * @param other the other boundary
     * @return the intersection boundary
     */
    Boundary<P> intersection(Boundary<P> other);

    /**
     * Computes the convex hull of this boundary.
     * @return the convex hull
     */
    Boundary<P> convexHull();

    /**
     * Creates a copy of this boundary translated by a vector.
     *
     * @param offset the translation vector (as point)
     * @return translated boundary
     */
    Boundary<P> translate(P offset);

    /**
     * Creates a scaled copy of this boundary.
     *
     * @param factor the scaling factor
     * @return scaled boundary
     */
    Boundary<P> scale(Real factor);
}
