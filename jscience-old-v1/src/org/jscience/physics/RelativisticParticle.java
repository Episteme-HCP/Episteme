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

package org.jscience.physics;

import org.jscience.JScience;

import org.jscience.physics.relativity.Rank1Tensor;


/**
 * The RelativisticParticle class provides an object for encapsulating
 * relativistic particles.
 *
 * @author Mark Hale
 * @version 1.0
 */
public abstract class RelativisticParticle extends Particle {
    /** Position 4-vector. */
    public Rank1Tensor position = new Rank1Tensor(0.0, 0.0, 0.0, 0.0);

    /** Momentum 4-vector. */
    public Rank1Tensor momentum = new Rank1Tensor(0.0, 0.0, 0.0, 0.0);

/**
     * Constructs a relativistic particle.
     */
    public RelativisticParticle() {
    }

    /**
     * Rest mass.
     *
     * @return DOCUMENT ME!
     */
    public abstract double restMass();

    /**
     * Compares two particles for equality.
     *
     * @param p a relativistic particle
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object p) {
        return (p != null) && (p instanceof RelativisticParticle) &&
        (position.equals(((RelativisticParticle) p).position)) &&
        (momentum.equals(((RelativisticParticle) p).momentum)) &&
        (Math.abs(restMass() - ((RelativisticParticle) p).restMass()) <= Double.valueOf(JScience.getProperty(
                "tolerance")).doubleValue());
    }
}
