package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;


/**
 * The Drag class provides an object for encapsulating drag forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class Drag extends Object {
    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle;

    /** DOCUMENT ME! */
    private double coefficient;

/**
     * Constructs a drag.
     *
     * @param p           DOCUMENT ME!
     * @param coefficient the drag constant.
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Drag(AbstractClassicalParticle p, double coefficient) {
        if (p != null) {
            this.particle = p;
            this.coefficient = coefficient;
        } else {
            throw new IllegalArgumentException(
                "Spring doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractClassicalParticle getParticle() {
        return particle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCoefficient() {
        return coefficient;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coefficient DOCUMENT ME!
     */
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double potentialEnergy() {
        return 0;
    }

    //gets the force
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new DragForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class DragForce extends ForceND {
        /** DOCUMENT ME! */
        private Drag drag;

/**
         * Creates a new DragForce object.
         *
         * @param d DOCUMENT ME!
         */
        public DragForce(Drag d) {
            super(d.getParticle());

            if (d != null) {
                //super(d.getParticle());
                this.drag = d;
            } else {
                throw new IllegalArgumentException(
                    "DragForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Drag getDrag() {
            return drag;
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
            double k;

            vec = new double[getParticle().getDimension()];
            k = -getDrag().getCoefficient() * getParticle().speed();

            for (int i = 0; i < getParticle().getDimension(); i++)
                vec[i] = k * getParticle().getVelocity().getPrimitiveElement(i);

            return new DoubleVector(vec);
        }
    }
}
