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

package org.jscience.ml.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLContentElement;
import org.w3c.dom.mathml.MathMLMatrixrowElement;


/**
 * Implements a MathML <code>matrixrow</code> element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLMatrixrowElementImpl extends MathMLElementImpl
    implements MathMLMatrixrowElement {
/**
     * Constructs a MathML <code>matrixrow</code> element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLMatrixrowElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNEntries() {
        return getEntriesGetLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param newEntry DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param newEntry DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void deleteEntry(int index) throws DOMException {
        removeEntry(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
