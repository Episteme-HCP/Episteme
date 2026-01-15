package org.jscience.physics.relativity;

/**
 * The MinkowskiMetric class encapsulates the Minkowski metric.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class MinkowskiMetric extends Rank2Tensor {
/**
     * Constructs the Minkowski metric.
     */
    public MinkowskiMetric() {
        rank2[0][0] = 1.0;
        rank2[1][1] = rank2[2][2] = rank2[3][3] = -1.0;
    }
}
