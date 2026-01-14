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

import org.jscience.util.IllegalDimensionException;


/**
 * The Interaction class provides an object for encapsulating dynamics
 * related to mutual influence.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Interaction {
    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle1; //the force is applied on this particle, you actually have to build the symetric interaction for the other particle (this is just in case you would like the interaction to be NOT symetric).

    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle2;

/**
     * Constructs the Interaction.
     *
     * @param p1 DOCUMENT ME!
     * @param p2 DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public Interaction(AbstractClassicalParticle p1,
        AbstractClassicalParticle p2) {
        if ((p1 != null) && (p2 != null)) {
            if (p1.getDimension() == p2.getDimension()) {
                this.particle1 = p1;
                this.particle2 = p2;
            } else {
                throw new IllegalDimensionException(
                    "Particles must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "Interaction doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractClassicalParticle getParticle1() {
        return particle1;
    }

    //Particles must be of same dimension
    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setParticle1(AbstractClassicalParticle p) {
        if (p != null) {
            if (p.getDimension() == particle2.getDimension()) {
                this.particle1 = p;
            } else {
                throw new IllegalDimensionException(
                    "Particles must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a particle with null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractClassicalParticle getParticle2() {
        return particle2;
    }

    //Particles must be of same dimension
    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setParticle2(AbstractClassicalParticle p) {
        if (p != null) {
            if (p.getDimension() == particle1.getDimension()) {
                this.particle2 = p;
            } else {
                throw new IllegalDimensionException(
                    "Particles must be of same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a particle with null arguments.");
        }
    }

    /**
     * Returns the potential energy of a particle in this field.
     *
     * @return DOCUMENT ME!
     */
    public abstract double potentialEnergy();

    /**
     * Creates the force acting on a particle in this field.
     *
     * @return DOCUMENT ME!
     */
    public abstract Force createForce();
}
