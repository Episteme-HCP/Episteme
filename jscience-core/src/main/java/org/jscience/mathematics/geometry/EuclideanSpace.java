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
import org.jscience.mathematics.topology.Metric;

/**
 * Represents a Euclidean space.
 * <p>
 * A Euclidean space is an affine space equipped with a metric (distance
 * function)
 * and an inner product. This allows measurement of distances, angles, and
 * norms.
 * </p>
 * <p>
 * Key properties:
 * - All affine space operations
 * - Distance measurement between points
 * - Norm (length) of vectors
 * - Inner product (dot product)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface EuclideanSpace<V> extends AffineSpace<V> {

    /**
     * Returns the metric for this Euclidean space.
     * <p>
     * The metric defines the distance function d(x,y).
     * </p>
     * 
     * @return the metric
     */
    Metric<V> metric();

    /**
     * Computes the distance between two points.
     * <p>
     * This is a convenience method that delegates to the metric.
     * </p>
     * 
     * @param a the first point
     * @param b the second point
     * @return the distance between a and b
     */
    default Real distance(V a, V b) {
        return metric().distance(a, b);
    }

    /**
     * Computes the norm (length) of a vector.
     * <p>
     * For Euclidean spaces, this is the L2 norm: Ã¢Ë†Å¡(xÃ¢â€šÂÃ‚Â² + xÃ¢â€šâ€šÃ‚Â² + ... + xÃ¢â€šâ„¢Ã‚Â²)
     * </p>
     * 
     * @param vector the vector
     * @return the norm of the vector
     */
    Real norm(V vector);

    /**
     * Computes the inner product (dot product) of two vectors.
     * <p>
     * For Euclidean spaces: Ã¢Å¸Â¨u,vÃ¢Å¸Â© = uÃ¢â€šÂvÃ¢â€šÂ + uÃ¢â€šâ€švÃ¢â€šâ€š + ... + uÃ¢â€šâ„¢vÃ¢â€šâ„¢
     * </p>
     * 
     * @param u the first vector
     * @param v the second vector
     * @return the inner product
     */
    Real innerProduct(V u, V v);

    /**
     * Computes the angle between two vectors.
     * <p>
     * Returns the angle in radians, in the range [0, Ãâ‚¬].
     * Uses the formula: cos(ÃŽÂ¸) = Ã¢Å¸Â¨u,vÃ¢Å¸Â© / (Ã¢â‚¬â€“uÃ¢â‚¬â€“ Ã¢â‚¬â€“vÃ¢â‚¬â€“)
     * </p>
     * 
     * @param u the first vector
     * @param v the second vector
     * @return the angle in radians
     */
    default Real angle(V u, V v) {
        Real dot = innerProduct(u, v);
        Real normU = norm(u);
        Real normV = norm(v);
        Real cosTheta = dot.divide(normU.multiply(normV));
        return Real.of(Math.acos(cosTheta.doubleValue()));
    }

    /**
     * Checks if two vectors are orthogonal (perpendicular).
     * <p>
     * Vectors are orthogonal if their inner product is zero.
     * </p>
     * 
     * @param u the first vector
     * @param v the second vector
     * @return true if orthogonal
     */
    default boolean areOrthogonal(V u, V v) {
        return innerProduct(u, v).abs().doubleValue() < 1e-10;
    }
}



