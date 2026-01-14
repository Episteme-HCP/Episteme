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
 * The Force2D class provides an object for encapsulating forces in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example: weight in 2D.
public abstract class Force2D extends Force {
/**
     * Constructs a force for a 2D particle.
     *
     * @param p DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public Force2D(AbstractClassicalParticle p) {
        super(p);

        if (p != null) {
            if (p.getDimension() == 2) {
                //super(p);
            } else {
                throw new IllegalDimensionException(
                    "Particle must have 2 dimensions.");
            }
        } else {
            throw new IllegalArgumentException(
                "Force2D doesn't accept null arguments.");
        }
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
}
