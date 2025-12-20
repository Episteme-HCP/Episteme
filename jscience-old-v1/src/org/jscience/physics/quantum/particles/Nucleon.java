package org.jscience.physics.quantum.particles;

/**
 * A class representing nucleons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class Nucleon extends Baryon {
    /**
     * Constructs a nucleon.
     */
    public Nucleon() {
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
     * @return 0
     */
    public final int strangeQN() {
        return 0;
    }
}


