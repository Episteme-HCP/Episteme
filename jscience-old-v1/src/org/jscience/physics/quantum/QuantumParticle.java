package org.jscience.physics.quantum;

import org.jscience.physics.RelativisticParticle;


/**
 * A class representing quantum particles.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class QuantumParticle extends RelativisticParticle {
    /** The number of 1/2 units of the z-component of spin. */
    public int spinZ;

/**
     * Constructs a quantum particle.
     */
    public QuantumParticle() {
    }

    /**
     * Returns the number of 1/2 units of spin.
     *
     * @return DOCUMENT ME!
     */
    public abstract int spin();

    /**
     * Returns the number of 1/2 units of isospin.
     *
     * @return DOCUMENT ME!
     */
    public abstract int isospin();

    /**
     * Returns the number of 1/2 units of the z-component of isospin.
     *
     * @return DOCUMENT ME!
     */
    public abstract int isospinZ();

    /**
     * Returns the electric charge.
     *
     * @return DOCUMENT ME!
     */
    public abstract int charge();

    /**
     * Returns the electron lepton number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int eLeptonQN();

    /**
     * Returns the muon lepton number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int muLeptonQN();

    /**
     * Returns the tau lepton number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int tauLeptonQN();

    /**
     * Returns the baryon number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int baryonQN();

    /**
     * Returns the strangeness number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int strangeQN();

    /**
     * Returns the antiparticle of this particle.
     *
     * @return DOCUMENT ME!
     */
    public abstract QuantumParticle anti();

    /**
     * Returns true if qp is the antiparticle.
     *
     * @param qp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isAnti(QuantumParticle qp);
}
