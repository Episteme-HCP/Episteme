package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The ConstantForceND class provides an object for encapsulating constant
 * forces in ND.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example: weight in ND. You can also prefer to use the Weight class
public class ConstantForceND extends ForceND {
    /** DOCUMENT ME! */
    private AbstractDoubleVector doubleVector;

/**
     * Constructs a force for a ND particle.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public ConstantForceND(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p);

        if ((p != null) && (doubleVector != null)) {
            if (p.getDimension() == doubleVector.getDimension()) {
                //super(p);
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Particle and Vector must have the same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "ConstantForceND doesn't accept null arguments.");
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
