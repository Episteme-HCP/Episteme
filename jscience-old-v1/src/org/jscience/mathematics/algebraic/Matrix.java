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
 * The Matrix superclass provides an abstract encapsulation for traditional 2D
 * matrices. Concrete implementations of this class should implement
 * additional interfaces. See subclasses.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */

//see http://skal.planet-d.net/demo/matrixfaq.htm
public interface Matrix extends Hypermatrix {
    // TODO: This interface should be made generic: Matrix<T extends Ring.Member> or similar.
    /**
     * Returns the number of rows.
     *
     * @return DOCUMENT ME!
     */
    public int numRows();

    /**
     * Returns the number of columns.
     *
     * @return DOCUMENT ME!
     */
    public int numColumns();

    /**
     * Returns the element at position i,j.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Number getElement(int i, int j) throws IllegalDimensionException;

    /**
     * Converts a matrix to an array.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number[][] toArray(Matrix v);

    /**
     * Returns the ith row.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getRow(int i);

    /**
     * Returns the ith column.
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getColumn(int j);

    /**
     * Returns the transpose of this matrix.
     *
     * @return DOCUMENT ME!
     */
    public Matrix transpose();
}
