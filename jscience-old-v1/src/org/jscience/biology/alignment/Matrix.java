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

package org.jscience.biology.alignment;

/**
 * This interface defines a minimal set of operations that a matrix must
 * implement. This interface is used by the {@linkPlain Smawk} class to
 * provide a general services regardless of how the matrix is actually stored.
 *
 * @author Sergio A. de Carvalho Jr.
 * @see Smawk
 */
public interface Matrix {
    /**
     * Returns the value at an specified row and column.
     *
     * @param row row number of element to be retrieved
     * @param col column number of element to be retrieved
     *
     * @return value at row <CODE>row</CODE> column <CODE>col</CODE>
     */
    public int valueAt(int row, int col);

    /**
     * Returns the number of rows that this matrix has.
     *
     * @return number of rows
     */
    public int numRows();

    /**
     * Returns the number of columns that this matrix has.
     *
     * @return number of columns
     */
    public int numColumns();
}
