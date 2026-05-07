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

package org.episteme.core.mathematics.loaders.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLContentElement;
import org.w3c.dom.mathml.MathMLMatrixrowElement;


/**
 * Implements a MathML <code>matrixrow</code> element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLMatrixrowElementImpl extends MathMLElementImpl
    implements MathMLMatrixrowElement {
    /**
     * Constructs a MathML <code>matrixrow</code> element.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element type
     */
    public MathMLMatrixrowElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the number of entries in the matrix row.
     *
     * @return the number of entries
     */
    public int getNEntries() {
        return getEntriesGetLength();
    }

    /**
     * Returns the entry at the specified index.
     *
     * @param index the index of the entry to retrieve (1-based)
     *
     * @return the entry at the given index
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
     */
    public MathMLContentElement getEntry(int index) throws DOMException {
        Node entry = getEntriesItem(index - 1);

        if (entry == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLContentElement) entry;
    }

    /**
     * Sets the entry at the specified index, replacing any existing entry.
     *
     * @param newEntry the new entry to set
     * @param index    the index where the entry should be set (1-based)
     *
     * @return the new entry
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
     */
    public MathMLContentElement setEntry(MathMLContentElement newEntry,
        int index) throws DOMException {
        final int entriesLength = getEntriesGetLength();

        if ((index < 1) || (index > (entriesLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if (index == (entriesLength + 1)) {
            return (MathMLContentElement) appendChild(newEntry);
        } else {
            return (MathMLContentElement) replaceChild(newEntry,
                getEntriesItem(index - 1));
        }
    }

    /**
     * Inserts a new entry at the specified index.
     *
     * @param newEntry the new entry to insert
     * @param index    the index where the entry should be inserted (0-based)
     *
     * @return the new entry
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
     */
    public MathMLContentElement insertEntry(MathMLContentElement newEntry,
        int index) throws DOMException {
        final int entriesLength = getEntriesGetLength();

        if ((index < 0) || (index > (entriesLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        if ((index == 0) || (index == (entriesLength + 1))) {
            return (MathMLContentElement) appendChild(newEntry);
        } else {
            return (MathMLContentElement) insertBefore(newEntry,
                getEntriesItem(index - 1));
        }
    }

    /**
     * Removes the entry at the specified index.
     *
     * @param index the index of the entry to remove (1-based)
     *
     * @return the removed entry
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
     */
    public MathMLContentElement removeEntry(int index)
        throws DOMException {
        Node entry = getEntriesItem(index - 1);

        if (entry == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR,
                "Index out of bounds");
        }

        return (MathMLContentElement) removeChild(entry);
    }

    /**
     * Deletes the entry at the specified index.
     *
     * @param index the index of the entry to delete (1-based)
     *
     * @throws DOMException INDEX_SIZE_ERR if the index is out of bounds
     */
    public void deleteEntry(int index) throws DOMException {
        removeEntry(index);
    }

    /**
     * Helper method to calculate the number of entries.
     *
     * @return the number of entry children
     */
    private int getEntriesGetLength() {
        final int length = getLength();
        int numEntries = 0;

        for (int i = 0; i < length; i++) {
            if (item(i) instanceof MathMLContentElement) {
                numEntries++;
            }
        }

        return numEntries;
    }

    /**
     * Helper method to retrieve an entry by its index.
     *
     * @param index the 0-based index of the entry
     * @return the entry node, or null if index is out of bounds
     */
    private Node getEntriesItem(int index) {
        final int entriesLength = getEntriesGetLength();

        if ((index < 0) || (index >= entriesLength)) {
            return null;
        }

        Node node = null;
        int n = -1;

        for (int i = 0; n < index; i++) {
            node = item(i);

            if (node instanceof MathMLContentElement) {
                n++;
            }
        }

        return node;
    }
}

