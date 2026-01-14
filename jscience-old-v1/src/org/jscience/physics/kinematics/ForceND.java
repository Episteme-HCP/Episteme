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
 * The ConstantForceND class provides an object for encapsulating constant
 * forces in ND.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example: weight in ND.
public abstract class ForceND extends Force {
/**
     * Constructs a force for a ND particle.
     *
     * @param p DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ForceND(AbstractClassicalParticle p) {
        super(p);

        if (p != null) {
            //super(p);
        } else {
            throw new IllegalArgumentException(
                "ForceND doesn't accept null arguments.");
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
