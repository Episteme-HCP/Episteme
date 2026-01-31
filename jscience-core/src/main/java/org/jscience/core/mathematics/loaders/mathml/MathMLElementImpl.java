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

import org.apache.xerces.dom.ElementNSImpl;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLNodeList;


/**
 * Implements a MathML element (and node list).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLElementImpl extends ElementNSImpl implements MathMLElement,
    MathMLNodeList {
    /** The MathML namespace URI. */
    static final String mathmlURI = "http://www.w3.org/1998/Math/MathML";

    /**
     * Constructs a MathML element.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, mathmlURI, qualifiedName);
    }

    /**
     * Returns the class attribute value.
     *
     * @return the class name
     */
    public String getClassName() {
        return getAttribute("class");
    }

    /**
     * Sets the class attribute value.
     *
     * @param className the class name to set
     */
    public void setClassName(String className) {
        setAttribute("class", className);
    }

    /**
     * Returns the style attribute value.
     *
     * @return the element style
     */
    public String getMathElementStyle() {
        return getAttribute("style");
    }

    /**
     * Sets the style attribute value.
     *
     * @param mathElementStyle the style to set
     */
    public void setMathElementStyle(String mathElementStyle) {
        setAttribute("style", mathElementStyle);
    }

    /**
     * Returns the id attribute value.
     *
     * @return the element id
     */
    public String getId() {
        return getAttribute("id");
    }

    /**
     * Sets the id attribute value.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        setAttribute("id", id);
    }

    /**
     * Returns the xlink:href attribute value.
     *
     * @return the href link
     */
    public String getHref() {
        return getAttribute("xlink:href");
    }

    /**
     * Sets the xlink:href attribute value.
     *
     * @param href the href link to set
     */
    public void setHref(String href) {
        setAttribute("xlink:href", href);
    }

    /**
     * Returns the xref attribute value.
     *
     * @return the cross-reference
     */
    public String getXref() {
        return getAttribute("xref");
    }

    /**
     * Sets the xref attribute value.
     *
     * @param xref the cross-reference to set
     */
    public void setXref(String xref) {
        setAttribute("xref", xref);
    }

    /**
     * Returns the nearest ancestor math element.
     *
     * @return the owner math element, or null if this is the math element
     */
    public MathMLMathElement getOwnerMathElement() {
        if (this instanceof MathMLMathElement) {
            return null;
        }

        Node parent = getParentNode();

        while (!(parent instanceof MathMLMathElement)) {
            parent = parent.getParentNode();
        }

        return (MathMLMathElement) parent;
    }
}

