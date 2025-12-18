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
 * @param <V> the type representing both points and vectors
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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
     * For Euclidean spaces, this is the L2 norm: √(x₁² + x₂² + ... + xₙ²)
     * </p>
     * 
     * @param vector the vector
     * @return the norm of the vector
     */
    Real norm(V vector);

    /**
     * Computes the inner product (dot product) of two vectors.
     * <p>
     * For Euclidean spaces: ⟨u,v⟩ = u₁v₁ + u₂v₂ + ... + uₙvₙ
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
     * Returns the angle in radians, in the range [0, π].
     * Uses the formula: cos(θ) = ⟨u,v⟩ / (‖u‖ ‖v‖)
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

