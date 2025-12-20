package org.jscience.physics.quantum.particles;

/**
 * A class representing antibaryons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class AntiBaryon extends AntiHadron {
    /**
     * Constructs an antibaryon.
     */
    public AntiBaryon() {
    }

    /**
     * Returns the baryon number.
     *
     * @return -1
     */
    public final int baryonQN() {
        return -1;
    }
}


