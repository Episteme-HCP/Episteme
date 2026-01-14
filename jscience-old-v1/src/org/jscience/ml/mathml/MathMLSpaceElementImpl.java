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

import org.w3c.dom.mathml.MathMLSpaceElement;


/**
 * Implements a MathML space element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLSpaceElementImpl extends MathMLElementImpl
    implements MathMLSpaceElement {
/**
     * Constructs a MathML space element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLSpaceElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getWidth() {
        return getAttribute("width");
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(String width) {
        setAttribute("width", width);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHeight() {
        return getAttribute("height");
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(String height) {
        setAttribute("height", height);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDepth() {
        return getAttribute("depth");
    }

    /**
     * DOCUMENT ME!
     *
     * @param depth DOCUMENT ME!
     */
    public void setDepth(String depth) {
        setAttribute("depth", depth);
    }
}
