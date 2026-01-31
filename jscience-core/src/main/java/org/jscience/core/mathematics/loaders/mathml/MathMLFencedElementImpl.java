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

import org.w3c.dom.mathml.MathMLFencedElement;


/**
 * Implements a MathML fenced element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLFencedElementImpl extends MathMLPresentationContainerImpl
    implements MathMLFencedElement {
/**
     * Constructs a MathML fenced element.
     *
     * @param owner         the document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLFencedElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Gets the opening delimiter of the fenced element.
     *
     * @return the opening delimiter
     */
    public String getOpen() {
        return getAttribute("open");
    }

    /**
     * Sets the opening delimiter of the fenced element.
     *
     * @param open the new opening delimiter
     */
    public void setOpen(String open) {
        setAttribute("open", open);
    }

    /**
     * Gets the closing delimiter of the fenced element.
     *
     * @return the closing delimiter
     */
    public String getClose() {
        return getAttribute("close");
    }

    /**
     * Sets the closing delimiter of the fenced element.
     *
     * @param close the new closing delimiter
     */
    public void setClose(String close) {
        setAttribute("close", close);
    }

    /**
     * Gets the separators used between elements in the fenced element.
     *
     * @return the separators string
     */
    public String getSeparators() {
        return getAttribute("separators");
    }

    /**
     * Sets the separators used between elements in the fenced element.
     *
     * @param separators the new separators string
     */
    public void setSeparators(String separators) {
        setAttribute("separators", separators);
    }
}

