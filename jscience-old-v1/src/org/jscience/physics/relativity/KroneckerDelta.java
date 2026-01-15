package org.jscience.physics.relativity;

/**
 * The KroneckerDelta class encapsulates the Kronecker delta.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class KroneckerDelta extends Rank2Tensor {
/**
     * Constructs the Kronecker delta.
     */
    public KroneckerDelta() {
        rank2[0][0] = rank2[1][1] = rank2[2][2] = rank2[3][3] = 1.0;
    }
}
