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
 * This interface represents the MathML MathMLPiecewiseElement element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLPiecewiseElement extends MathMLContentElement {
    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLNodeList getPieces();

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLContentElement getOtherwise();

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param otherwise the value to set
     */
    public void setOtherwise(MathMLContentElement otherwise);

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index the value to set
     * @return the requested attribute or element
     */
    public MathMLCaseElement getCase(int index);

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index   DOCUMENT ME!
     * @param newCase the value to set
     * @return the requested attribute or element
     * @throws DOMException if an error occurs
     */
    public MathMLCaseElement setCase(int index, MathMLCaseElement newCase)
            throws DOMException;

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index the value to set
     * @throws DOMException if an error occurs
     */
    public void deleteCase(int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index the value to set
     * @return the requested attribute or element
     * @throws DOMException if an error occurs
     */
    public MathMLCaseElement removeCase(int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index   DOCUMENT ME!
     * @param newCase the value to set
     * @return the requested attribute or element
     * @throws DOMException if an error occurs
     */
    public MathMLCaseElement insertCase(int index, MathMLCaseElement newCase)
            throws DOMException;

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index the value to set
     * @return the requested attribute or element
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement getCaseValue(int index)
            throws DOMException;

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index the value to set
     * @param value the value to set
     * @return the requested attribute or element
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement setCaseValue(int index,
                                             MathMLContentElement value) throws DOMException;

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index the value to set
     * @return the requested attribute or element
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement getCaseCondition(int index)
            throws DOMException;

    /**
 * This interface represents the MathML MathMLPiecewiseElement element.
     *
     * @param index     DOCUMENT ME!
     * @param condition the value to set
     * @return the requested attribute or element
     * @throws DOMException if an error occurs
     */
    public MathMLContentElement setCaseCondition(int index,
                                                 MathMLContentElement condition) throws DOMException;
}
;
