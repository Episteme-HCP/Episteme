package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;


/**
 * The ConstantForceND class provides an object for encapsulating constant
 * forces in ND.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example: weight in ND.
public abstract class ForceND extends Force {
/**
     * Constructs a force for a ND particle.
     *
     * @param p DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ForceND(AbstractClassicalParticle p) {
        super(p);

        if (p != null) {
            //super(p);
        } else {
            throw new IllegalArgumentException(
                "ForceND doesn't accept null arguments.");
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
    public abstract AbstractDoubleVector getVector(double t);
}
