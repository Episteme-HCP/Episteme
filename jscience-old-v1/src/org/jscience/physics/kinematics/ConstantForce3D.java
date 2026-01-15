package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The ConstantForce3D class provides an object for encapsulating constant
 * forces in 3D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example: weight in 3D. You can also prefer to use the Weight class
public class ConstantForce3D extends Force3D {
    /** DOCUMENT ME! */
    private AbstractDoubleVector doubleVector;

/**
     * Constructs a force for a 3D particle.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public ConstantForce3D(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p);

        if (doubleVector != null) {
            if (doubleVector.getDimension() == 3) {
                //super(p);
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Vector must have 3 dimensions.");
            }
        } else {
            throw new IllegalArgumentException(
                "ConstantForce3D doesn't accept null arguments.");
        }
    }

    //gets the force at t
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
