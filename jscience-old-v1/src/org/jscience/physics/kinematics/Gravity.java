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

import org.jscience.physics.PhysicsConstants;


/**
 * The Gravity class provides an object for encapsulating Gravity forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class Gravity extends Interaction {
/**
     * Constructs a Gravity.
     *
     * @param p1 DOCUMENT ME!
     * @param p2 DOCUMENT ME!
     */
    public Gravity(AbstractClassicalParticle p1, AbstractClassicalParticle p2) {
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

        return (-PhysicsConstants.GRAVITATION * getParticle1().getMass() * getParticle2()
                                                                               .getMass()) / Math.sqrt(sum);
    }

    //gets the force
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new GravityForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class GravityForce extends Force {
        /** DOCUMENT ME! */
        private Gravity gravity;

/**
         * Creates a new GravityForce object.
         *
         * @param g DOCUMENT ME!
         */
        public GravityForce(Gravity g) {
            super(g.getParticle1());

            if (g != null) {
                //super(g.getParticle1());
                this.gravity = g;
            } else {
                throw new IllegalArgumentException(
                    "GravityForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Gravity getGravity() {
            return gravity;
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

            vec = new double[getGravity().getParticle1().getDimension()];
            sum = 0;

            for (int i = 0; i < getGravity().getParticle1().getDimension();
                    i++) {
                vec[i] = getGravity().getParticle1().getPosition()
                             .getPrimitiveElement(i) -
                    getGravity().getParticle2().getPosition()
                        .getPrimitiveElement(i);
                sum = sum + (vec[i] * vec[i]);
            }

            magnitude = (-PhysicsConstants.GRAVITATION * getGravity()
                                                             .getParticle1()
                                                             .getMass() * getGravity()
                                                                              .getParticle2()
                                                                              .getMass()) / Math.sqrt(sum);

            for (int i = 0; i < getGravity().getParticle1().getDimension();
                    i++) {
                vec[i] = magnitude * vec[i];
            }

            return new DoubleVector(vec);
        }
    }
}
