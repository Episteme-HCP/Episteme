package org.jscience.physics.quantum.particles;

/**
 * A class representing kaons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class Kaon extends Meson {
    /**
     * Constructs a kaon.
     */
    public Kaon() {
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
     * @return 1
     */
    public final int strangeQN() {
        return 1;
    }
}


