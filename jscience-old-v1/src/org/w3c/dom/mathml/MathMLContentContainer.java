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
public interface MathMLContentContainer extends MathMLContentElement,
    MathMLContainer {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNBoundVariables();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLConditionElement getCondition();

    /**
     * DOCUMENT ME!
     *
     * @param condition DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setCondition(MathMLConditionElement condition)
        throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getOpDegree();

    /**
     * DOCUMENT ME!
     *
     * @param opDegree DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setOpDegree(MathMLElement opDegree) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getDomainOfApplication();

    /**
     * DOCUMENT ME!
     *
     * @param domainOfApplication DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setDomainOfApplication(MathMLElement domainOfApplication)
        throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getMomentAbout();

    /**
     * DOCUMENT ME!
     *
     * @param momentAbout DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setMomentAbout(MathMLElement momentAbout)
        throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLBvarElement getBoundVariable(int index);

    /**
     * DOCUMENT ME!
     *
     * @param newBVar DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLBvarElement insertBoundVariable(MathMLBvarElement newBVar,
        int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param newBVar DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLBvarElement setBoundVariable(MathMLBvarElement newBVar,
        int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void deleteBoundVariable(int index);

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLBvarElement removeBoundVariable(int index);
}
;
