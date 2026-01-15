package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The Field class provides an object for encapsulating fields forces.
 * Fields are constant vectors of space and time.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class ConstantField extends Field {
    /** DOCUMENT ME! */
    private AbstractDoubleVector doubleVector; //the vector field

/**
     * Constructs the ConstantField.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public ConstantField(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p);

        if (doubleVector != null) {
            if (doubleVector.getDimension() == getParticle().getDimension()) {
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Particle and field must have the same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "ConstantField doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getVector() {
        return doubleVector;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doubleVector DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setVector(AbstractDoubleVector doubleVector) {
        if (doubleVector != null) {
            if (doubleVector.getDimension() == getParticle().getDimension()) {
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Particle and field must have the same dimension.");
            }
        } else {
            throw new IllegalArgumentException("You can't set a null field.");
        }
    }
}
