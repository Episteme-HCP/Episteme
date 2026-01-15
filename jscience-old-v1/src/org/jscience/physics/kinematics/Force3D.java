package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The Force3D class provides an object for encapsulating forces in 3D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example: weight in 3D.
public abstract class Force3D extends Force {
/**
     * Constructs a force for a 3D particle.
     *
     * @param p DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public Force3D(AbstractClassicalParticle p) {
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
                "Force3D doesn't accept null arguments.");
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
