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
import org.w3c.dom.mathml.MathMLApplyElement;
import org.w3c.dom.mathml.MathMLElement;


/**
 * Implements a MathML <code>apply</code> element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLApplyElementImpl extends MathMLContentContainerImpl
    implements MathMLApplyElement {
    /**
     * Constructs a MathML <code>apply</code> element.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLApplyElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the operator element (first child).
     *
     * @return the operator element
     */
    public MathMLElement getOperator() {
        return (MathMLElement) getFirstChild();
    }

    /**
     * Sets the operator element.
     *
     * @param operator the operator element to set
     */
    public void setOperator(MathMLElement operator) {
        replaceChild(operator, getFirstChild());
    }

    /**
     * Returns the lower limit element.
     *
     * @return the lowlimit element
     */
    public MathMLElement getLowLimit() {
        return (MathMLElement) getNodeByName("lowlimit");
    }

    /**
     * Sets the lower limit element.
     *
     * @param lowlimit the lowlimit element to set
     *
     * @throws DOMException if the element cannot be set
     */
    public void setLowLimit(MathMLElement lowlimit) throws DOMException {
        setNodeByName(lowlimit, "lowlimit");
    }

    /**
     * Returns the upper limit element.
     *
     * @return the uplimit element
     */
    public MathMLElement getUpLimit() {
        return (MathMLElement) getNodeByName("uplimit");
    }

    /**
     * Sets the upper limit element.
     *
     * @param uplimit the uplimit element to set
     *
     * @throws DOMException if the element cannot be set
     */
    public void setUpLimit(MathMLElement uplimit) throws DOMException {
        setNodeByName(uplimit, "uplimit");
    }
}

