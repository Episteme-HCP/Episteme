package org.jscience.physics.quantum.particles;

/**
 * A class representing antixis.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class AntiXi extends Hyperon {
    /**
     * Constructs an antixi.
     */
    public AntiXi() {
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
     * @return 2
     */
    public final int strangeQN() {
        return 2;
    }
}


