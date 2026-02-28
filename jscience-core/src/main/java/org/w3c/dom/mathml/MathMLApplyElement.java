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
 * This interface represents the <code>apply</code> element in MathML Content.
 * It is used to apply a function or operator to its arguments.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLApplyElement extends MathMLContentContainer {
    /**
     * Returns the operator applied by this element.
     *
     * @return the operator.
     */
    public MathMLElement getOperator();

    /**
     * Sets the operator to be applied by this element.
     *
     * @param operator the operator to set.
     */
    public void setOperator(MathMLElement operator);

    /**
     * Returns the lower limit for the operator (e.g., for summation or integration).
     *
     * @return the lower limit.
     */
    public MathMLElement getLowLimit();

    /**
     * Sets the lower limit for the operator.
     *
     * @param lowLimit the lower limit to set.
     * @throws DOMException HIERARCHY_REQUEST_ERR if the element is not allowed as a lower limit.
     */
    public void setLowLimit(MathMLElement lowLimit) throws DOMException;

    /**
     * Returns the upper limit for the operator.
     *
     * @return the upper limit.
     */
    public MathMLElement getUpLimit();

    /**
     * Sets the upper limit for the operator.
     *
     * @param upLimit the upper limit to set.
     * @throws DOMException HIERARCHY_REQUEST_ERR if the element is not allowed as an upper limit.
     */
    public void setUpLimit(MathMLElement upLimit) throws DOMException;
}
;
