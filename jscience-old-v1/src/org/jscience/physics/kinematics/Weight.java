package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The Weight class provides an object for encapsulating weight forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class Weight extends Object {
    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle;

    /** DOCUMENT ME! */
    private AbstractDoubleVector doubleVector;

/**
     * Constructs a weight.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector the weight vector.
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public Weight(AbstractClassicalParticle p, AbstractDoubleVector doubleVector) {
        if ((p != null) && (doubleVector != null)) {
            if (doubleVector.getDimension() == particle.getDimension()) {
                this.particle = p;
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Particle and vector must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "Weight doesn't accept null arguments.");
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
    public AbstractDoubleVector getVector() {
        return doubleVector;
    }

    //doubleVector must have the same dimension than particle
    /**
     * DOCUMENT ME!
     *
     * @param doubleVector DOCUMENT ME!
     */
    public void setVector(AbstractDoubleVector doubleVector) {
        if (doubleVector != null) {
            if (doubleVector.getDimension() == particle.getDimension()) {
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Particle and vector must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set vector with null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double potentialEnergy(AbstractDoubleVector position) {
        double rr;
        double dx;

        if (position != null) {
            if (position.getDimension() == particle.getDimension()) {
                rr = 0.0;

                for (int i = 0; i < particle.getDimension(); i++) {
                    dx = (particle.getPosition().getPrimitiveElement(i) -
                        position.getPrimitiveElement(i)) * doubleVector.getPrimitiveElement(i);
                    rr += dx;
                }

                return particle.getMass() * rr; //TODO, please check my formula
            } else {
                throw new IllegalDimensionException(
                    "Particle and position must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't get the potential energy with null arguments.");
        }
    }

    //gets the force
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new WeightForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class WeightForce extends ForceND {
        /** DOCUMENT ME! */
        private Weight weight;

/**
         * Creates a new WeightForce object.
         *
         * @param w DOCUMENT ME!
         */
        public WeightForce(Weight w) {
            super(w.getParticle());

            if (w != null) {
                //super(w.getParticle());
                this.weight = w;
            } else {
                throw new IllegalArgumentException(
                    "WeightForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Weight getWeight() {
            return weight;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbstractDoubleVector getVector(double t) {
            return getWeight().getVector()
                       .scalarMultiply(getParticle().getMass());
        }
    }
}
