package org.jscience.physics.quantum.particles;

/**
 * A class representing mesons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class Meson extends Hadron {
    /**
     * Constructs a meson.
     */
    public Meson() {
    }

    /**
     * Returns the number of 1/2 units of spin.
     *
     * @return 0
     */
    public final int spin() {
        return 0;
    }

    /**
     * Returns the baryon number.
     *
     * @return 0
     */
    public final int baryonQN() {
        return 0;
    }
}


