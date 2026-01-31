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

import org.w3c.dom.mathml.MathMLPaddedElement;


/**
 * Implements a MathML padded element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLPaddedElementImpl extends MathMLPresentationContainerImpl
    implements MathMLPaddedElement {
    /**
     * Constructs a MathML padded element.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element type
     */
    public MathMLPaddedElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the value of the <code>width</code> attribute.
     *
     * @return the width
     */
    public String getWidth() {
        return getAttribute("width");
    }

    /**
     * Sets the value of the <code>width</code> attribute.
     *
     * @param width the width to set
     */
    public void setWidth(String width) {
        setAttribute("width", width);
    }

    /**
     * Returns the value of the <code>lspace</code> attribute.
     *
     * @return the left space
     */
    public String getLspace() {
        return getAttribute("lspace");
    }

    /**
     * Sets the value of the <code>lspace</code> attribute.
     *
     * @param lspace the left space to set
     */
    public void setLspace(String lspace) {
        setAttribute("lspace", lspace);
    }

    /**
     * Returns the value of the <code>height</code> attribute.
     *
     * @return the height
     */
    public String getHeight() {
        return getAttribute("height");
    }

    /**
     * Sets the value of the <code>height</code> attribute.
     *
     * @param height the height to set
     */
    public void setHeight(String height) {
        setAttribute("height", height);
    }

    /**
     * Returns the value of the <code>depth</code> attribute.
     *
     * @return the depth
     */
    public String getDepth() {
        return getAttribute("depth");
    }

    /**
     * Sets the value of the <code>depth</code> attribute.
     *
     * @param depth the depth to set
     */
    public void setDepth(String depth) {
        setAttribute("depth", depth);
    }
}

