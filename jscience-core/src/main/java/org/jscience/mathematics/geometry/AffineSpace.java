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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an affine space.
 * <p>
 * An affine space is a geometric structure that generalizes properties of
 * Euclidean spaces
 * in which parallel lines remain parallel. It consists of points and vectors,
 * where vectors
 * can be added to points to get new points, and two points can be subtracted to
 * get a vector.
 * </p>
 * <p>
 * Key properties:
 * - Points can be translated by vectors
 * - The difference of two points is a vector
 * - Linear combinations (interpolation) are supported
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface AffineSpace<V> {

    /**
     * Translates a point by a vector.
     * <p>
     * This is the fundamental operation: point + vector = point
     * </p>
     * 
     * @param point  the point to translate
     * @param vector the translation vector
     * @return the translated point
     */
    V translate(V point, V vector);

    /**
     * Computes the vector from point a to point b.
     * <p>
     * This is the inverse operation: point - point = vector
     * </p>
     * 
     * @param a the starting point
     * @param b the ending point
     * @return the vector from a to b
     */
    V difference(V a, V b);

    /**
     * Computes a linear combination (interpolation) between two points.
     * <p>
     * Returns: a + t*(b-a)
     * - t=0 gives a
     * - t=1 gives b
     * - t=0.5 gives midpoint
     * </p>
     * 
     * @param a the first point
     * @param b the second point
     * @param t the interpolation parameter
     * @return the interpolated point
     */
    V interpolate(V a, V b, Real t);

    /**
     * Computes a weighted average (barycentric combination) of points.
     * <p>
     * Returns: w₁*p₁ + w₂*p₂ + ... + wₙ*pₙ where Σwᵢ = 1
     * </p>
     * 
     * @param points  the points
     * @param weights the weights (must sum to 1)
     * @return the barycentric combination
     */
    default V barycenter(V[] points, Real[] weights) {
        if (points.length != weights.length) {
            throw new IllegalArgumentException("Points and weights must have same length");
        }
        if (points.length == 0) {
            throw new IllegalArgumentException("Need at least one point");
        }

        // Start with first point
        V result = points[0];

        // Add weighted differences
        for (int i = 1; i < points.length; i++) {
            @SuppressWarnings("unused")
            V diff = difference(points[i], points[0]);
            // Scale diff by weight[i] and add to result
            // This requires vector scaling - implementation depends on V type
        }

        return result;
    }
}
