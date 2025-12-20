package org.jscience.physics.quantum.particles;

/**
 * A class representing antikaons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class AntiKaon extends Meson {
    /**
     * Constructs an antikaon.
     */
    public AntiKaon() {
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
     * @return -1
     */
    public final int strangeQN() {
        return -1;
    }
}


