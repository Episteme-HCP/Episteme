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

package org.jscience.core.mathematics.loaders.mathml;

import org.w3c.dom.mathml.MathMLActionElement;


/**
 * Implements a MathML action element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLActionElementImpl extends MathMLPresentationContainerImpl
    implements MathMLActionElement {
/**
     * Constructs a MathML action element.
     *
     * @param owner         the document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLActionElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Gets the action type attribute of the action element.
     *
     * @return the action type
     */
    public String getActiontype() {
        return getAttribute("actiontype");
    }

    /**
     * Sets the action type attribute of the action element.
     *
     * @param actiontype the new action type
     */
    public void setActiontype(String actiontype) {
        setAttribute("actiontype", actiontype);
    }

    /**
     * Gets the selection attribute of the action element.
     *
     * @return the selection value
     */
    public String getSelection() {
        return getAttribute("selection");
    }

    /**
     * Sets the selection attribute of the action element.
     *
     * @param selection the new selection value
     */
    public void setSelection(String selection) {
        setAttribute("selection", selection);
    }
}

