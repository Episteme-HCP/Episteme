package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The Force2D class provides an object for encapsulating forces in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example: weight in 2D.
public abstract class Force2D extends Force {
/**
     * Constructs a force for a 2D particle.
     *
     * @param p DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public Force2D(AbstractClassicalParticle p) {
        super(p);

        if (p != null) {
            if (p.getDimension() == 2) {
                //super(p);
            } else {
                throw new IllegalDimensionException(
                    "Particle must have 2 dimensions.");
            }
        } else {
            throw new IllegalArgumentException(
                "Force2D doesn't accept null arguments.");
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
