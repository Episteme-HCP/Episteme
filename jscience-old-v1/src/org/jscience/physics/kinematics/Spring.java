/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The Spring class provides an object for encapsulating spring forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class Spring extends Object {
    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle;

    /** DOCUMENT ME! */
    private AbstractDoubleVector position;

    /** DOCUMENT ME! */
    private double coefficient;

/**
     * Constructs a mechanical spring.
     *
     * @param p           DOCUMENT ME!
     * @param position    DOCUMENT ME!
     * @param coefficient the spring constant.
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public Spring(AbstractClassicalParticle p, AbstractDoubleVector position,
        double coefficient) {
        if ((p != null) && (position != null)) {
            if (p.getDimension() == position.getDimension()) {
                this.particle = p;
                this.position = position;
                this.coefficient = coefficient;
            } else {
                throw new IllegalDimensionException(
                    "Particle and position must be of same dimension.");
            }
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
    public AbstractDoubleVector getPosition() {
        return position;
    }

    //position must have the same dimension than particle
    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     */
    public void setPosition(AbstractDoubleVector position) {
        if (position != null) {
            if (position.getDimension() == particle.getDimension()) {
                this.position = position;
            } else {
                throw new IllegalDimensionException(
                    "Particle and position must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set position with null arguments.");
        }
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
     * Returns the potential energy of a particle attached to this
     * spring.
     *
     * @return DOCUMENT ME!
     */
    public double potentialEnergy() {
        double rr;
        double dx;

        rr = 0.0;

        for (int i = 0; i < particle.getDimension(); i++) {
            dx = particle.getPosition().getPrimitiveElement(i) -
                position.getPrimitiveElement(i);
            rr += (dx * dx);
        }

        return (coefficient * rr) / 2.0;
    }

    //the oscillation time in seconds if any, if not 0
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPeriod() {
        return 2 * Math.PI * Math.sqrt(particle.getMass() / coefficient);
    }

    //gets the force
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new SpringForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class SpringForce extends ForceND {
        /** DOCUMENT ME! */
        private Spring spring;

/**
         * Creates a new SpringForce object.
         *
         * @param s DOCUMENT ME!
         */
        public SpringForce(Spring s) {
            super(s.getParticle());

            if (s != null) {
                //super(s.getParticle());
                this.spring = s;
            } else {
                throw new IllegalArgumentException(
                    "SpringForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Spring getSpring() {
            return spring;
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

            vec = new double[spring.getPosition().getDimension()];

            for (int i = 0; i < getParticle().getDimension(); i++)
                vec[i] = -coefficient * (getParticle().getPosition()
                                             .getPrimitiveElement(i) -
                    spring.getPosition().getPrimitiveElement(i));

            return new DoubleVector(vec);
        }
    }
}
