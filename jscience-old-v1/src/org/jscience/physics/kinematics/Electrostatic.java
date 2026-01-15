package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;

import org.jscience.physics.PhysicsConstants;


/**
 * The Electrostatic class provides an object for encapsulating
 * electrostatic forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class Electrostatic extends Interaction {
/**
     * Constructs an Electrostatic.
     *
     * @param p1 DOCUMENT ME!
     * @param p2 DOCUMENT ME!
     */
    public Electrostatic(AbstractClassicalParticle p1,
        AbstractClassicalParticle p2) {
        super(p1, p2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double potentialEnergy() {
        double di;
        double sum;

        sum = 0;

        for (int i = 0; i < getParticle1().getDimension(); i++) {
            di = getParticle1().getPosition().getPrimitiveElement(i) -
                getParticle2().getPosition().getPrimitiveElement(i);
            sum = sum + (di * di);
        }

        return (getParticle1().getCharge() * getParticle2().getCharge()) / (4 * Math.PI * PhysicsConstants.PERMITTIVITY * Math.sqrt(sum));
    }

    //gets the force
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new ElectricalForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class ElectricalForce extends Force {
        /** DOCUMENT ME! */
        private Electrostatic electrostatic;

/**
         * Creates a new ElectricalForce object.
         *
         * @param e DOCUMENT ME!
         */
        public ElectricalForce(Electrostatic e) {
            super(e.getParticle1());

            if (e != null) {
                //super(e.getParticle1());
                this.electrostatic = e;
            } else {
                throw new IllegalArgumentException(
                    "ElectricalForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Electrostatic getElectrostatic() {
            return electrostatic;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbstractDoubleVector getVector(double t) {
            double[] vec;

            double sum;
            double magnitude;

            vec = new double[getElectrostatic().getParticle1().getDimension()];
            sum = 0;

            for (int i = 0;
                    i < getElectrostatic().getParticle1().getDimension();
                    i++) {
                vec[i] = getElectrostatic().getParticle1().getPosition()
                             .getPrimitiveElement(i) -
                    getElectrostatic().getParticle2().getPosition()
                        .getPrimitiveElement(i);
                sum = sum + (vec[i] * vec[i]);
            }

            magnitude = (getElectrostatic().getParticle1().getCharge() * getElectrostatic()
                                                                             .getParticle2()
                                                                             .getCharge()) / (4 * Math.PI * PhysicsConstants.PERMITTIVITY * Math.sqrt(sum));

            //TODO can you check that there is no sign error here please ?
            for (int i = 0;
                    i < getElectrostatic().getParticle1().getDimension();
                    i++) {
                vec[i] = magnitude * vec[i];
            }

            return new DoubleVector(vec);
        }
    }
}
