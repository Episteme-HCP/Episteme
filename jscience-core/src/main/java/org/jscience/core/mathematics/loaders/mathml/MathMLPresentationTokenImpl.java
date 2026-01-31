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

import org.w3c.dom.mathml.MathMLNodeList;
import org.w3c.dom.mathml.MathMLPresentationToken;


/**
 * Implements a MathML presentation token.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLPresentationTokenImpl extends MathMLElementImpl
    implements MathMLPresentationToken {
/**
     * Constructs a MathML presentation token.
     *
     * @param owner         the document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLPresentationTokenImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Gets the mathvariant attribute.
     *
     * @return the variant name (e.g., "bold", "italic")
     */
    public String getMathvariant() {
        return getAttribute("mathvariant");
    }

    /**
     * Sets the mathvariant attribute.
     *
     * @param mathvariant the variant name
     */
    public void setMathvariant(String mathvariant) {
        setAttribute("mathvariant", mathvariant);
    }

    /**
     * Gets the mathsize attribute.
     *
     * @return the size (e.g., "small", "14pt")
     */
    public String getMathsize() {
        return getAttribute("mathsize");
    }

    /**
     * Sets the mathsize attribute.
     *
     * @param mathsize the size
     */
    public void setMathsize(String mathsize) {
        setAttribute("mathsize", mathsize);
    }

    /**
     * Gets the mathfamily attribute.
     *
     * @return the font family
     */
    public String getMathfamily() {
        return getAttribute("mathfamily");
    }

    /**
     * Sets the mathfamily attribute.
     *
     * @param mathfamily the font family
     */
    public void setMathfamily(String mathfamily) {
        setAttribute("mathfamily", mathfamily);
    }

    /**
     * Gets the mathcolor attribute.
     *
     * @return the color (e.g., "#000000", "red")
     */
    public String getMathcolor() {
        return getAttribute("mathcolor");
    }

    /**
     * Sets the mathcolor attribute.
     *
     * @param mathcolor the color
     */
    public void setMathcolor(String mathcolor) {
        setAttribute("mathcolor", mathcolor);
    }

    /**
     * Gets the mathbackground attribute.
     *
     * @return the background color
     */
    public String getMathbackground() {
        return getAttribute("mathbackground");
    }

    /**
     * Sets the mathbackground attribute.
     *
     * @param mathbackground the background color
     */
    public void setMathbackground(String mathbackground) {
        setAttribute("mathbackground", mathbackground);
    }

    /**
     * Gets the content nodes of this token.
     *
     * @return the list of content nodes
     */
    public MathMLNodeList getContents() {
        return (MathMLNodeList) getChildNodes();
    }
}

