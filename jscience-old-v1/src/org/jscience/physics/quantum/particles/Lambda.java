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

package org.jscience.physics.quantum.particles;

import org.jscience.physics.quantum.QuantumParticle;

/**
 * A class representing lambdas.
 *
 * @author Mark Hale
 * @version 1.5
 */
public final class Lambda extends Hyperon {
    /**
     * Constructs a lambda.
     */
    public Lambda() {
    }

    /**
     * Returns the rest mass (MeV).
     *
     * @return 1115.683
     */
    public double restMass() {
        return 1115.683;
    }

    /**
     * Returns the number of 1/2 units of spin.
     *
     * @return 1
     */
    public int spin() {
        return 1;
    }

    /**
     * Returns the number of 1/2 units of isospin.
     *
     * @return 0
     */
    public int isospin() {
        return 0;
    }

    /**
     * Returns the number of 1/2 units of the z-component of isospin.
     *
     * @return 0
     */
    public int isospinZ() {
        return 0;
    }

    /**
     * Returns the electric charge.
     *
     * @return 0
     */
    public int charge() {
        return 0;
    }

    /**
     * Returns the strangeness number.
     *
     * @return -1
     */
    public int strangeQN() {
        return -1;
    }

    /**
     * Returns the quark composition.
     */
    public QuantumParticle[] quarks() {
        QuantumParticle comp[] = {new Up(), new Down(), new Strange()};
        return comp;
    }

    /**
     * Returns the antiparticle of this particle.
     */
    public QuantumParticle anti() {
        return new AntiLambda();
    }

    /**
     * Returns true if qp is the antiparticle.
     */
    public boolean isAnti(QuantumParticle qp) {
        return (qp != null) && (qp instanceof AntiLambda);
    }

    /**
     * Returns a string representing this class.
     */
    public String toString() {
        return new String("Lambda");
    }
}


