package org.jscience.physics.quantum.particles;

/**
 * A class representing antisigmas.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class AntiSigma extends Hyperon {
    /**
     * Constructs an antisigma.
     */
    public AntiSigma() {
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
     * @return 2
     */
    public final int isospin() {
        return 2;
    }

    /**
     * Returns the strangeness number.
     *
     * @return 1
     */
    public final int strangeQN() {
        return 1;
    }
}


