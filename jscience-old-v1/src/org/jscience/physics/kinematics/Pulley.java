package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * A class to represent an ideal pulley (change force direction but keep
 * strength).
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//Imagine a mass hanging a rope. The rope goes into a pulley to another part of the system
//this is an ideal pulley just used to change the direction of a Force
//subclass it to implement drag or radius
public class Pulley extends Object {
    /** DOCUMENT ME! */
    private Force f1;

    /** DOCUMENT ME! */
    private Force f2;

    //F1 is the pull on the rope
    /**
     * Creates a new Pulley object.
     *
     * @param f1 DOCUMENT ME!
     * @param f2 DOCUMENT ME!
     */
    public Pulley(Force f1, Force f2) {
        if ((f1 != null) && (f2 != null)) {
            if (f1.getDimension() == f2.getDimension()) {
                this.f1 = f1;
                this.f2 = f2;
            } else {
                throw new IllegalDimensionException(
                    "Forces must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "Pulley doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Force getForce1() {
        return f1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Force getForce2() {
        return f2;
    }

    //computes a new strength for the vector2 force that matches the f1 vector strength but keeps the original direction
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getForce2Vector(double t) {
        //normalize f2.getVector(t)
        //multiply by the length of f1
        AbstractDoubleVector vector;

        vector = f2.getVector(t);
        vector.normalize();

        return (AbstractDoubleVector) vector.scalarMultiply(f1.getVector(t));
    }
}
