package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The ConstantTorque2D class provides an object for encapsulating constant
 * torques in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class ConstantTorque2D extends Torque2D {
    /** DOCUMENT ME! */
    private AbstractDoubleVector doubleVector;

/**
     * Constructs a torque for a 2D particle.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public ConstantTorque2D(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p);

        if (doubleVector != null) {
            if (doubleVector.getDimension() == 1) {
                //super(p);
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Vector must have 1 dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "ConstantTorque2D doesn't accept null arguments.");
        }
    }

    //gets the torque at t
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getVector(double t) {
        return doubleVector;
    }
}
