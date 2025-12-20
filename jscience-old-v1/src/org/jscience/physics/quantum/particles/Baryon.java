package org.jscience.physics.quantum.particles;

/**
 * A class representing baryons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class Baryon extends Hadron {
    /**
     * Constructs a baryon.
     */
    public Baryon() {
    }

    /**
     * Returns the baryon number.
     *
     * @return 1
     */
    public final int baryonQN() {
        return 1;
    }
}


