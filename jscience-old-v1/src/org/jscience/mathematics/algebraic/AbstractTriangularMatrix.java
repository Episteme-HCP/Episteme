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
 * This class defines an interface for matrices whose components are all
 * zeros in the upper right or down left triangle.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractTriangularMatrix extends AbstractBandedMatrix
    implements TriangularMatrix {
/**
     * Creates a new AbstractTriangularMatrix object.
     *
     * @param rows DOCUMENT ME!
     */
    public AbstractTriangularMatrix(int rows, int k1, int k2) {
        super(rows, k1, k2);

        if (((k1 == 0) && (k2 == (rows - 1))) ||
                ((k1 == 0) && (k2 == (rows - 1)))) {
        } else {
            throw new IllegalArgumentException(
                "One of k1 and k2 must be equal to zero and the other to rows - 1.");
        }
    }
}
