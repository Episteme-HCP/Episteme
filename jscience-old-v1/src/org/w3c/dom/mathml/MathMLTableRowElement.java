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

package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface MathMLTableRowElement extends MathMLPresentationElement {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRowalign();

    /**
     * DOCUMENT ME!
     *
     * @param rowalign DOCUMENT ME!
     */
    public void setRowalign(String rowalign);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnalign();

    /**
     * DOCUMENT ME!
     *
     * @param columnalign DOCUMENT ME!
     */
    public void setColumnalign(String columnalign);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getGroupalign();

    /**
     * DOCUMENT ME!
     *
     * @param groupalign DOCUMENT ME!
     */
    public void setGroupalign(String groupalign);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLNodeList getCells();

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLTableCellElement insertEmptyCell(int index)
        throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param newCell DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLTableCellElement insertCell(MathMLTableCellElement newCell,
        int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param newCell DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLTableCellElement setCell(MathMLTableCellElement newCell,
        int index);

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void deleteCell(int index);
}
;
