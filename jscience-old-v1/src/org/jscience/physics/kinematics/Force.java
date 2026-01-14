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


/**
 * A superclass for forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Force extends Object implements java.io.Serializable {
    /** DOCUMENT ME! */
    private AbstractClassicalParticle particle; //the callback particle

/**
     * Constructs a force.
     *
     * @param p DOCUMENT ME!
     */
    public Force(AbstractClassicalParticle p) {
        this.particle = p;

        //next line commented out because user has to decide if the force that COULD act on the particle really does act on the particle
        //p.addForce(this);
    }

    //the number of dimension of this force
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return particle.getDimension();
    }

    //the particle on which this force acts on
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractClassicalParticle getParticle() {
        return particle;
    }

    //gets the force at t
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AbstractDoubleVector getVector(double t);

    //the actual force intensity in Newtons
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getIntensity(double t) {
        return getVector(t).norm();
    }
}
