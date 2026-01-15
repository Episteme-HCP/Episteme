package org.jscience.physics.kinematics;

/**
 * The Field class provides an object for encapsulating fields forces. This
 * is to define the dynamics of particles in a space where particles are not
 * only affected by their mutual influence but also by a much bigger force
 * that drives them depending on their position and is not affected by their
 * presence.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Field {
    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle;

/**
     * Constructs the field.
     *
     * @param p DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Field(AbstractClassicalParticle p) {
        if (p != null) {
            this.particle = p;
        } else {
            throw new IllegalArgumentException(
                "Field doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractClassicalParticle getParticle() {
        return particle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setParticle(AbstractClassicalParticle p) {
        if (p != null) {
            this.particle = p;
        } else {
            throw new IllegalArgumentException(
                "You can't set a particle with null arguments.");
        }
    }

    /**
     * Creates the force acting on a particle in this field.
     *
     * @return DOCUMENT ME!
     */
    public abstract Force createForce();
}
