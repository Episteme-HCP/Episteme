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

package org.jscience.mathematics.loaders.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLMatrixElement;
import org.w3c.dom.mathml.MathMLMatrixrowElement;
import org.w3c.dom.mathml.MathMLNodeList;


/**
 * Implements a MathML <code>matrix</code> element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLMatrixElementImpl extends MathMLElementImpl
    implements MathMLMatrixElement {
    /**
     * Constructs a MathML <code>matrix</code> element.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLMatrixElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the number of rows in the matrix.
     *
     * @return the row count
     */
    public int getNrows() {
        return getRowsGetLength();
    }

    /**
     * Returns the number of columns in the matrix.
     *
     * @return the column count
     */
    public int getNcols() {
        return getRow(1).getNEntries();
    }

    /**
     * Returns a list of all matrix rows.
     *
     * @return the list of row elements
     */
    public MathMLNodeList getRows() {
        return new MathMLNodeList() {
                public int getLength() {
                    return getRowsGetLength();
                }

                public Node item(int index) {
                    return getRowsItem(index);
                }
            };
    }

    /**
     * Returns the row at the specified index.
     *
     * @param index the row index (1-based)
     *
     * @return the matrix row element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLMatrixrowElement getRow(int index) throws DOMException {
        Node row = getRowsItem(index - 1);

        if (row == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLMatrixrowElement) row;
    }

    /**
     * Sets the row at the specified index.
     *
     * @param newRow the new row element
     * @param index  the row index (1-based)
     *
     * @return the new row element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLMatrixrowElement setRow(MathMLMatrixrowElement newRow,
        int index) throws DOMException {
        final int rowsLength = getRowsGetLength();

        if ((index < 1) || (index > (rowsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if (index == (rowsLength + 1)) {
            return (MathMLMatrixrowElement) appendChild(newRow);
        } else {
            return (MathMLMatrixrowElement) replaceChild(newRow,
                getRowsItem(index - 1));
        }
    }

    /**
     * Inserts a row at the specified index.
     *
     * @param newRow the new row element to insert
     * @param index  the index where to insert (0 for end)
     *
     * @return the inserted row element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLMatrixrowElement insertRow(MathMLMatrixrowElement newRow,
        int index) throws DOMException {
        final int rowsLength = getRowsGetLength();

        if ((index < 0) || (index > (rowsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if ((index == 0) || (index == (rowsLength + 1))) {
            return (MathMLMatrixrowElement) appendChild(newRow);
        } else {
            return (MathMLMatrixrowElement) insertBefore(newRow,
                getRowsItem(index - 1));
        }
    }

    /**
     * Removes the row at the specified index.
     *
     * @param index the row index (1-based)
     *
     * @return the removed row element
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public MathMLMatrixrowElement removeRow(int index)
        throws DOMException {
        Node row = getRowsItem(index - 1);

        if (row == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLMatrixrowElement) removeChild(row);
    }

    /**
     * Deletes the row at the specified index.
     *
     * @param index the row index (1-based)
     *
     * @throws DOMException INDEX_SIZE_ERR if out of bounds
     */
    public void deleteRow(int index) throws DOMException {
        removeRow(index);
    }

    /**
     * Returns the number of matrixrow elements.
     *
     * @return the row count
     */
    private int getRowsGetLength() {
        final int length = getLength();
        int numRows = 0;

        for (int i = 0; i < length; i++) {
            if (item(i) instanceof MathMLMatrixrowElement) {
                numRows++;
            }
        }

        return numRows;
    }

    /**
     * Returns the row at the specified 0-based index.
     *
     * @param index the 0-based index
     *
     * @return the row node, or null if out of bounds
     */
    private Node getRowsItem(int index) {
        final int rowsLength = getRowsGetLength();

        if ((index < 0) || (index >= rowsLength)) {
            return null;
        }

        Node node = null;
        int n = -1;

        for (int i = 0; n < index; i++) {
            node = item(i);

            if (node instanceof MathMLMatrixrowElement) {
                n++;
            }
        }

        return node;
    }
}
