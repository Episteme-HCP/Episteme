package org.jscience.physics;

import org.jscience.JScience;

import org.jscience.physics.relativity.Rank1Tensor;


/**
 * The RelativisticParticle class provides an object for encapsulating
 * relativistic particles.
 *
 * @author Mark Hale
 * @version 1.0
 */
public abstract class RelativisticParticle extends Particle {
    /** Position 4-vector. */
    public Rank1Tensor position = new Rank1Tensor(0.0, 0.0, 0.0, 0.0);

    /** Momentum 4-vector. */
    public Rank1Tensor momentum = new Rank1Tensor(0.0, 0.0, 0.0, 0.0);

/**
     * Constructs a relativistic particle.
     */
    public RelativisticParticle() {
    }

    /**
     * Rest mass.
     *
     * @return DOCUMENT ME!
     */
    public abstract double restMass();

    /**
     * Compares two particles for equality.
     *
     * @param p a relativistic particle
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object p) {
        return (p != null) && (p instanceof RelativisticParticle) &&
        (position.equals(((RelativisticParticle) p).position)) &&
        (momentum.equals(((RelativisticParticle) p).momentum)) &&
        (Math.abs(restMass() - ((RelativisticParticle) p).restMass()) <= Double.valueOf(JScience.getProperty(
                "tolerance")).doubleValue());
    }
}
