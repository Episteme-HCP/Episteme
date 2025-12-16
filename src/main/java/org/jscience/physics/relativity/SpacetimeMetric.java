package org.jscience.physics.relativity;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.topology.MetricSpace;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface representing a metric tensor field in spacetime.
 * <p>
 * Implementations define how the metric varies with coordinates.
 * Coordinates are provided as a {@link Vector4D} which represents the
 * tuple $(x^0, x^1, x^2, x^3)$ in the specific coordinate system used by the
 * metric.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 3.2
 */
public interface SpacetimeMetric extends MetricSpace<Vector4D> {

    /**
     * Calculates the covariant metric tensor $g_{\mu\nu}$ at the given event
     * coordinates.
     * <p>
     * Returns a rank-2 tensor of shape [4, 4].
     * </p>
     * 
     * @param point the spacetime coordinates
     * @return the metric tensor instance
     */
    Tensor<Real> getMetricTensor(Vector4D point);

    /**
     * Calculates the Christoffel symbols $\Gamma^\lambda_{\mu\nu}$ at the given
     * event coordinates.
     * <p>
     * Returns a rank-3 tensor of shape [4, 4, 4].
     * First index is upper ($\lambda$), second and third are lower ($\mu, \nu$).
     * </p>
     * 
     * @param point the spacetime coordinates
     * @return the Christoffel symbols tensor
     */
    default Tensor<Real> getChristoffelSymbols(Vector4D point) {
        throw new UnsupportedOperationException("Christoffel symbols calculation not implemented");
    }

    /**
     * Calculates the spacetime interval (distance) between two events.
     * <p>
     * Note: In Lorentzian geometry, this is the proper time/distance.
     * </p>
     */
    @Override
    default Real distance(Vector4D a, Vector4D b) {
        // dx = b - a
        // ds^2 = g_uv dx^u dx^v
        // For local approximation, we use g at midpoint or at 'a'.
        // This is a naive approximation for finite distances in curved spacetime
        // (geodesic distance requires integration).
        throw new UnsupportedOperationException(
                "Geodesic distance calculation requires path integration / geodesic equation solver.");
    }

    // --- TopologicalSpace Defaults ---

    @Override
    default boolean containsPoint(Vector4D point) {
        return true;
    } // Defined everywhere usually

    @Override
    default boolean isOpen() {
        return true;
    }

    @Override
    default boolean isClosed() {
        return true;
    }

    @Override
    default String description() {
        return "Spacetime manifold equipped with a metric tensor";
    }
}
