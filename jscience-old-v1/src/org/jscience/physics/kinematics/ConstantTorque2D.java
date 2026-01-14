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

import org.jscience.util.IllegalDimensionException;


/**
 * The ConstantTorque2D class provides an object for encapsulating constant
 * torques in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class ConstantTorque2D extends Torque2D {
    /** DOCUMENT ME! */
    private AbstractDoubleVector doubleVector;

/**
     * Constructs a torque for a 2D particle.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public ConstantTorque2D(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p);

        if (doubleVector != null) {
            if (doubleVector.getDimension() == 1) {
                //super(p);
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Vector must have 1 dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "ConstantTorque2D doesn't accept null arguments.");
        }
    }

    //gets the torque at t
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getVector(double t) {
        return doubleVector;
    }
}
