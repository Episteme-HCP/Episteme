package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.Double3Vector;

import org.jscience.util.IllegalDimensionException;


/**
 * The MagneticField class provides an object for encapsulating magnetic
 * fields forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//this is the B vector of F = q * v * B
public class ConstantMagneticField3D extends ConstantField {
/**
     * Constructs the field.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public ConstantMagneticField3D(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p, doubleVector);

        if (p.getDimension() == 3) {
        } else {
            throw new IllegalDimensionException(
                "The ConstantMagneticField3D must be of dimension 3.");
        }
    }

    /**
     * Creates the force acting on a particle in this field.
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new ConstantMagneticField3DForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class ConstantMagneticField3DForce extends Force {
        /** DOCUMENT ME! */
        private ConstantMagneticField3D constantMagneticField3D;

/**
         * Creates a new ConstantMagneticField3DForce object.
         *
         * @param b DOCUMENT ME!
         */
        public ConstantMagneticField3DForce(ConstantMagneticField3D b) {
            super(b.getParticle());

            if (b != null) {
                //super(b.getParticle());
                this.constantMagneticField3D = b;
            } else {
                throw new IllegalArgumentException(
                    "ConstantMagneticField3DForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ConstantMagneticField3D getConstantMagneticField3D() {
            return constantMagneticField3D;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbstractDoubleVector getVector(double t) {
            return (AbstractDoubleVector) ((Double3Vector) (getConstantMagneticField3D()
                                                                .getParticle()
                                                                .getVelocity()
                                                                .scalarMultiply(getConstantMagneticField3D()
                                                                                    .getParticle()
                                                                                    .getCharge()))).multiply(getConstantMagneticField3D()
                                                                                                                 .getVector());
        }
    }
}
