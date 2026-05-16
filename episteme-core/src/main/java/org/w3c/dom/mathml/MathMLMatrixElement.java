/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;


/**
 * This interface represents the <code>matrix</code> element in MathML Content.
 * A matrix is a rectangular array of elements.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLMatrixElement extends MathMLContentElement {
    /**
     * Returns the number of rows in this matrix.
     *
     * @return the number of rows.
     */
    public int getNrows();

    /**
     * Returns the number of columns in this matrix.
     *
     * @return the number of columns.
     */
    public int getNcols();

    /**
     * Returns a list of the rows in this matrix.
     *
     * @return the list of rows.
     */
    public MathMLNodeList getRows();

    /**
     * Returns the <code>index</code>-th row of this matrix.
     *
     * @param index the index of the row to retrieve (1-based index).
     * @return the row at the specified index.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public MathMLMatrixrowElement getRow(int index) throws DOMException;

    /**
     * Inserts a new row into the matrix at the specified <code>index</code>.
     *
     * @param newRow the new row to insert.
     * @param index  the index at which the new row is to be inserted.
     * @return the newly inserted row.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public MathMLMatrixrowElement insertRow(MathMLMatrixrowElement newRow,
        int index) throws DOMException;

    /**
     * Replaces the row at the specified <code>index</code> with a new row.
     *
     * @param newRow the new row to set.
     * @param index  the index of the row to replace.
     * @return the newly set row.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public MathMLMatrixrowElement setRow(MathMLMatrixrowElement newRow,
        int index) throws DOMException;

    /**
     * Deletes the row at the specified <code>index</code>.
     *
     * @param index the index of the row to delete.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public void deleteRow(int index) throws DOMException;

    /**
     * Removes and returns the row at the specified <code>index</code>.
     *
     * @param index the index of the row to remove.
     * @return the removed row.
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds.
     */
    public MathMLMatrixrowElement removeRow(int index)
        throws DOMException;
}
;
