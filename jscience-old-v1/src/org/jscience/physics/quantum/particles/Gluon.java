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
 * A class representing gluons.
 *
 * @author Mark Hale
 * @version 1.5
 */
public final class Gluon extends GaugeBoson {
    public final static int RED_ANTIGREEN = 12;
    public final static int RED_ANTIBLUE = 13;
    public final static int GREEN_ANTIRED = 21;
    public final static int GREEN_ANTIBLUE = 23;
    public final static int BLUE_ANTIRED = 31;
    public final static int BLUE_ANTIGREEN = 32;
    /**
     * The state (rr~-gg~)/sqrt(2).
     */
    public final static int MIXED_RED_GREEN = 11 - 22;
    /**
     * The state (rr~+gg~-2bb~)/sqrt(6).
     */
    public final static int MIXED_RED_GREEN_2BLUE = 11 + 22 - 2 * (33);

    /**
     * Constructs a gluon.
     */
    public Gluon() {
    }

    /**
     * The color.
     */
    public int color;

    /**
     * Returns the rest mass (MeV).
     *
     * @return 0.0
     */
    public double restMass() {
        return 0.0;
    }

    /**
     * Returns the number of 1/2 units of spin.
     *
     * @return 2
     */
    public int spin() {
        return 2;
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
     * Returns the antiparticle of this particle.
     */
    public QuantumParticle anti() {
        return new Gluon();
    }

    /**
     * Returns true if qp is the antiparticle.
     */
    public boolean isAnti(QuantumParticle qp) {
        return (qp != null) && (qp instanceof Gluon);
    }

    /**
     * Returns a string representing this class.
     */
    public String toString() {
        return new String("Gluon");
    }
}


