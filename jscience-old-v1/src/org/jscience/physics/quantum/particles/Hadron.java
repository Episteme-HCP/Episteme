package org.jscience.physics.quantum.particles;

import org.jscience.physics.quantum.QuantumParticle;

/**
 * A class representing hadrons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class Hadron extends QuantumParticle {
    /**
     * Constructs a hadron.
     */
    public Hadron() {
    }

    /**
     * Returns the electron lepton number.
     *
     * @return 0
     */
    public final int eLeptonQN() {
        return 0;
    }

    /**
     * Returns the muon lepton number.
     *
     * @return 0
     */
    public final int muLeptonQN() {
        return 0;
    }

    /**
     * Returns the tau lepton number.
     *
     * @return 0
     */
    public final int tauLeptonQN() {
        return 0;
    }
}


