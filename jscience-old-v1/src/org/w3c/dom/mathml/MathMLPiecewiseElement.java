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
 * @version $Revision: 1.2 $
 */
public interface MathMLPiecewiseElement extends MathMLContentElement {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLNodeList getPieces();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLContentElement getOtherwise();

    /**
     * DOCUMENT ME!
     *
     * @param otherwise DOCUMENT ME!
     */
    public void setOtherwise(MathMLContentElement otherwise);

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public MathMLCaseElement getCase(int index);

    /**
     * DOCUMENT ME!
     *
     * @param index   DOCUMENT ME!
     * @param newCase DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLCaseElement setCase(int index, MathMLCaseElement newCase)
            throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public void deleteCase(int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLCaseElement removeCase(int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index   DOCUMENT ME!
     * @param newCase DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLCaseElement insertCase(int index, MathMLCaseElement newCase)
            throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement getCaseValue(int index)
            throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement setCaseValue(int index,
                                             MathMLContentElement value) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement getCaseCondition(int index)
            throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index     DOCUMENT ME!
     * @param condition DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement setCaseCondition(int index,
                                                 MathMLContentElement condition) throws DOMException;
}
;
