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
 * This interface represents the MathML MathMLContentContainer element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLContentContainer extends MathMLContentElement,
    MathMLContainer {
    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @return the requested attribute or element
     */
    public int getNBoundVariables();

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @return the requested attribute or element
     */
    public MathMLConditionElement getCondition();

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param condition the value to set
     *
     * @throws DOMException if an error occurs
     */
    public void setCondition(MathMLConditionElement condition)
        throws DOMException;

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getOpDegree();

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param opDegree the value to set
     *
     * @throws DOMException if an error occurs
     */
    public void setOpDegree(MathMLElement opDegree) throws DOMException;

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getDomainOfApplication();

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param domainOfApplication the value to set
     *
     * @throws DOMException if an error occurs
     */
    public void setDomainOfApplication(MathMLElement domainOfApplication)
        throws DOMException;

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getMomentAbout();

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param momentAbout the value to set
     *
     * @throws DOMException if an error occurs
     */
    public void setMomentAbout(MathMLElement momentAbout)
        throws DOMException;

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param index the value to set
     *
     * @return the requested attribute or element
     */
    public MathMLBvarElement getBoundVariable(int index);

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param newBVar the value to set
     * @param index the value to set
     *
     * @return the requested attribute or element
     *
     * @throws DOMException if an error occurs
     */
    public MathMLBvarElement insertBoundVariable(MathMLBvarElement newBVar,
        int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param newBVar the value to set
     * @param index the value to set
     *
     * @return the requested attribute or element
     *
     * @throws DOMException if an error occurs
     */
    public MathMLBvarElement setBoundVariable(MathMLBvarElement newBVar,
        int index) throws DOMException;

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param index the value to set
     */
    public void deleteBoundVariable(int index);

    /**
 * This interface represents the MathML MathMLContentContainer element.
     *
     * @param index the value to set
     *
     * @return the requested attribute or element
     */
    public MathMLBvarElement removeBoundVariable(int index);
}
;
