package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;


/**
 * The ElectrostaticField class provides an object for encapsulating
 * electrostatic fields forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//this is the E vector of F = q * E
public class ConstantElectrostaticField extends ConstantField {
/**
     * Constructs the field.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     */
    public ConstantElectrostaticField(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p, doubleVector);
    }

    /**
     * Creates the force acting on a particle in this field.
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new ConstantElectrostaticFieldForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class ConstantElectrostaticFieldForce extends Force {
        /** DOCUMENT ME! */
        private ConstantElectrostaticField constantElectrostaticField;

/**
         * Creates a new ConstantElectrostaticFieldForce object.
         *
         * @param e DOCUMENT ME!
         */
        public ConstantElectrostaticFieldForce(ConstantElectrostaticField e) {
            super(e.getParticle());

            if (e != null) {
                //super(e.getParticle());
                this.constantElectrostaticField = e;
            } else {
                throw new IllegalArgumentException(
                    "ConstantElectrostaticFieldForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ConstantElectrostaticField getConstantElectrostaticField() {
            return constantElectrostaticField;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbstractDoubleVector getVector(double t) {
            return getConstantElectrostaticField().getVector()
                       .scalarMultiply(getConstantElectrostaticField()
                                           .getParticle().getCharge());
        }
    }
}
