package org.jscience.physics.quantum.particles;

/**
 * A class representing xis.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class Xi extends Hyperon {
    /**
     * Constructs a xi.
     */
    public Xi() {
    }

    /**
     * Returns the number of 1/2 units of spin.
     *
     * @return 1
     */
    public final int spin() {
        return 1;
    }

    /**
     * Returns the number of 1/2 units of isospin.
     *
     * @return 1
     */
    public final int isospin() {
        return 1;
    }

    /**
     * Returns the strangeness number.
     *
     * @return -2
     */
    public final int strangeQN() {
        return -2;
    }
}


