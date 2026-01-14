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

import org.jscience.util.IllegalDimensionException;


/**
 * This class defines an interface for Banded Matrices (see interface).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractBandedMatrix extends AbstractSquareMatrix
    implements BandedMatrix {
    /**
     * DOCUMENT ME!
     */
    private int k1;

    /**
     * DOCUMENT ME!
     */
    private int k2;

/**
     * Constructs a matrix.
     *
     * @param rows DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractBandedMatrix(int rows, int k1, int k2) {
        super(rows);

        if ((k1 >= 0) && (k2 >= 0) && (k1 <= (rows - 1)) && (k2 <= (rows - 1))) {
            this.k1 = k1;
            this.k2 = k2;
        } else {
            throw new IllegalArgumentException(
                "k1 and k2 must be between 0 and rows - 1.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK1() {
        return k1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK2() {
        return k2;
    }
}
