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
 * The Field class provides an object for encapsulating fields forces.
 * Fields are constant vectors of space and time.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class ConstantField extends Field {
    /** DOCUMENT ME! */
    private AbstractDoubleVector doubleVector; //the vector field

/**
     * Constructs the ConstantField.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public ConstantField(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p);

        if (doubleVector != null) {
            if (doubleVector.getDimension() == getParticle().getDimension()) {
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Particle and field must have the same dimension.");
            }
        } else {
            throw new IllegalArgumentException(
                "ConstantField doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getVector() {
        return doubleVector;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doubleVector DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setVector(AbstractDoubleVector doubleVector) {
        if (doubleVector != null) {
            if (doubleVector.getDimension() == getParticle().getDimension()) {
                this.doubleVector = doubleVector;
            } else {
                throw new IllegalDimensionException(
                    "Particle and field must have the same dimension.");
            }
        } else {
            throw new IllegalArgumentException("You can't set a null field.");
        }
    }
}
