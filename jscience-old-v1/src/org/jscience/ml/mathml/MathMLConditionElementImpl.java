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

import org.w3c.dom.mathml.MathMLApplyElement;
import org.w3c.dom.mathml.MathMLConditionElement;


/**
 * Implements a MathML condition element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLConditionElementImpl extends MathMLElementImpl
    implements MathMLConditionElement {
/**
     * Constructs a MathML condition element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLConditionElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLApplyElement getCondition() {
        return (MathMLApplyElement) getFirstChild();
    }

    /**
     * DOCUMENT ME!
     *
     * @param condition DOCUMENT ME!
     */
    public void setCondition(MathMLApplyElement condition) {
        replaceChild(condition, getFirstChild());
    }
}
