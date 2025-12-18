package org.jscience.mathematics.dynamical;

/**
 * Represents a discrete dynamical map on R^n (using double[] for performance).
 */
public interface DiscreteMap extends DynamicalMap<double[]> {
    /**
     * Returns the dimension of the state space.
     *
     * @return the dimension
     */
    int dimensions();

    /**
     * Returns the Hausdorff dimension (fractal dimension) of the attractor, if
     * known.
     *
     * @return the Hausdorff dimension
     */
    double hausdorffDimension();
}
