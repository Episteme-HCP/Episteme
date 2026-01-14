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

package org.jscience.mathematics.algebraic;

/**
 * This class defines an interface for matrices defined such as if all matrix elements vanish outside a diagonally bordered  "band" of some range and size:
 * ai,j=0 if j<i-k1 or j>i+k2
 * for some
 * k1, k2 > 0.
 * The quantities k1,k2 are called the left and right halfbandwidth respectively. The bandwidth of the matrix is k1 + k2 + 1.
 * A band matrix with k1 = k2 = 0 is a diagonal matrix; a band matrix with k1 = k2 = 1 is a tridiagonal matrix. If one puts k1 = 0, k2 = n-1, one obtains the definition of a lower triangular matrix, for k1 = n-1, k2 = 0 an upper triangular matrix.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface BandedMatrix extends SquareMatrix {
    //left halfbandwidth
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK1();

    //right halfbandwidth
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK2();
}
