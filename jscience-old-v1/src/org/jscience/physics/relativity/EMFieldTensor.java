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
 * The EMFieldTensor class encapsulates the electromagnetic field tensor.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class EMFieldTensor extends Rank2Tensor {
/**
     * Constructs an electromagnetic field tensor.
     *
     * @param E electric field
     * @param B magnetic field
     */
    public EMFieldTensor(DoubleVector E, DoubleVector B) {
        rank2[0][0] = rank2[1][1] = rank2[2][2] = rank2[3][3] = 0.0;
        rank2[1][0] = E.getPrimitiveElement(0);
        rank2[2][0] = E.getPrimitiveElement(1);
        rank2[3][0] = E.getPrimitiveElement(2);
        rank2[3][2] = B.getPrimitiveElement(0);
        rank2[1][3] = B.getPrimitiveElement(1);
        rank2[2][1] = B.getPrimitiveElement(2);
        rank2[0][1] = -rank2[1][0];
        rank2[0][2] = -rank2[2][0];
        rank2[0][3] = -rank2[3][0];
        rank2[2][3] = -rank2[3][2];
        rank2[3][1] = -rank2[1][3];
        rank2[1][2] = -rank2[2][1];
    }
}
