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

package org.jscience.physics.quantum;

import org.jscience.physics.RelativisticParticle;


/**
 * A class representing quantum particles.
 *
 * @author Mark Hale
 * @version 1.5
 */
public abstract class QuantumParticle extends RelativisticParticle {
    /** The number of 1/2 units of the z-component of spin. */
    public int spinZ;

/**
     * Constructs a quantum particle.
     */
    public QuantumParticle() {
    }

    /**
     * Returns the number of 1/2 units of spin.
     *
     * @return DOCUMENT ME!
     */
    public abstract int spin();

    /**
     * Returns the number of 1/2 units of isospin.
     *
     * @return DOCUMENT ME!
     */
    public abstract int isospin();

    /**
     * Returns the number of 1/2 units of the z-component of isospin.
     *
     * @return DOCUMENT ME!
     */
    public abstract int isospinZ();

    /**
     * Returns the electric charge.
     *
     * @return DOCUMENT ME!
     */
    public abstract int charge();

    /**
     * Returns the electron lepton number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int eLeptonQN();

    /**
     * Returns the muon lepton number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int muLeptonQN();

    /**
     * Returns the tau lepton number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int tauLeptonQN();

    /**
     * Returns the baryon number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int baryonQN();

    /**
     * Returns the strangeness number.
     *
     * @return DOCUMENT ME!
     */
    public abstract int strangeQN();

    /**
     * Returns the antiparticle of this particle.
     *
     * @return DOCUMENT ME!
     */
    public abstract QuantumParticle anti();

    /**
     * Returns true if qp is the antiparticle.
     *
     * @param qp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isAnti(QuantumParticle qp);
}
