package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The Torque3D class provides an object for encapsulating torques in 3D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Torque3D extends Force {
/**
     * Constructs a torque for a 3D particle.
     *
     * @param p DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public Torque3D(AbstractClassicalParticle p) {
        super(p);

        if (p != null) {
            if (p.getDimension() == 3) {
                //super(p);
            } else {
                throw new IllegalDimensionException(
                    "Particle must have 3 dimensions.");
            }
        } else {
            throw new IllegalArgumentException(
                "Torque3D doesn't accept null arguments.");
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
    public abstract AbstractDoubleVector getVector(double t);
}
