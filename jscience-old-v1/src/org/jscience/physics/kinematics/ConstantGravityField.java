package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;


/**
 * The ConstantGravityField class provides an object for encapsulating
 * constant gravity fields forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//this is the g vector of Weight = mass * g
public class ConstantGravityField extends ConstantField {
/**
     * Constructs the field.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     */
    public ConstantGravityField(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p, doubleVector);
    }

    /**
     * Creates the force acting on a particle in this field.
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new ConstantGravityFieldForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class ConstantGravityFieldForce extends Force {
        /** DOCUMENT ME! */
        private ConstantGravityField constantGravityField;

/**
         * Creates a new ConstantGravityFieldForce object.
         *
         * @param g DOCUMENT ME!
         */
        public ConstantGravityFieldForce(ConstantGravityField g) {
            super(g.getParticle());

            if (g != null) {
                //super(g.getParticle());
                this.constantGravityField = g;
            } else {
                throw new IllegalArgumentException(
                    "ConstantGravityFieldForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ConstantGravityField getConstantGravityField() {
            return constantGravityField;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbstractDoubleVector getVector(double t) {
            return getConstantGravityField().getVector()
                       .scalarMultiply(getConstantGravityField().getParticle()
                                           .getMass());
        }
    }
}
