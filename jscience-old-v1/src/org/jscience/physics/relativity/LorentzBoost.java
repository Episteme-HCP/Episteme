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

package org.jscience.physics.relativity;

import org.jscience.mathematics.algebraic.matrices.DoubleVector;


/**
 * The LorentzBoost class encapsulates the Lorentz boosts.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class LorentzBoost extends Rank2Tensor {
/**
     * Constructs a Lorentz boost.
     *
     * @param v velocity
     */
    public LorentzBoost(DoubleVector v) {
        this(v.getPrimitiveElement(0), v.getPrimitiveElement(1),
            v.getPrimitiveElement(2));
    }

/**
     * Constructs a Lorentz boost.
     *
     * @param vx x-velocity
     * @param vy y-velocity
     * @param vz z-velocity
     */
    public LorentzBoost(double vx, double vy, double vz) {
        final double vv = (vx * vx) + (vy * vy) + (vz * vz);
        final double gamma = 1.0 / Math.sqrt(1.0 - vv);
        final double k = (gamma - 1.0) / vv;
        rank2[0][0] = gamma;
        rank2[0][1] = rank2[1][0] = -gamma * vx;
        rank2[0][2] = rank2[2][0] = -gamma * vy;
        rank2[0][3] = rank2[3][0] = -gamma * vz;
        rank2[1][1] = 1.0 + (k * vx * vx);
        rank2[1][2] = rank2[2][1] = k * vx * vy;
        rank2[1][3] = rank2[3][1] = k * vx * vz;
        rank2[2][2] = 1.0 + (k * vy * vy);
        rank2[2][3] = rank2[3][2] = k * vy * vz;
        rank2[3][3] = 1.0 + (k * vz * vz);
    }
}
