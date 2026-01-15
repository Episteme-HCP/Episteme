package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;


/**
 * A superclass for forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Force extends Object implements java.io.Serializable {
    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle; //the callback particle

/**
     * Constructs a force.
     *
     * @param p DOCUMENT ME!
     */
    public Force(AbstractClassicalParticle p) {
        this.particle = p;

        //next line commented out because user has to decide if the force that COULD act on the particle really does act on the particle
        //p.addForce(this);
    }

    //the number of dimension of this force
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return particle.getDimension();
    }

    //the particle on which this force acts on
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractClassicalParticle getParticle() {
        return particle;
    }

    //gets the force at t
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AbstractDoubleVector getVector(double t);

    //the actual force intensity in Newtons
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getIntensity(double t) {
        return getVector(t).norm();
    }
}
