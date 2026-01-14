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

import org.apache.xerces.dom.ElementNSImpl;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLNodeList;


/**
 * Implements a MathML element (and node list).
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLElementImpl extends ElementNSImpl implements MathMLElement,
    MathMLNodeList {
    /** DOCUMENT ME! */
    static final String mathmlURI = "http://www.w3.org/1998/Math/MathML";

/**
     * Constructs a MathML element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, mathmlURI, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClassName() {
        return getAttribute("class");
    }

    /**
     * DOCUMENT ME!
     *
     * @param className DOCUMENT ME!
     */
    public void setClassName(String className) {
        setAttribute("class", className);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMathElementStyle() {
        return getAttribute("style");
    }

    /**
     * DOCUMENT ME!
     *
     * @param mathElementStyle DOCUMENT ME!
     */
    public void setMathElementStyle(String mathElementStyle) {
        setAttribute("style", mathElementStyle);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return getAttribute("id");
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(String id) {
        setAttribute("id", id);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHref() {
        return getAttribute("xlink:href");
    }

    /**
     * DOCUMENT ME!
     *
     * @param href DOCUMENT ME!
     */
    public void setHref(String href) {
        setAttribute("xlink:href", href);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXref() {
        return getAttribute("xref");
    }

    /**
     * DOCUMENT ME!
     *
     * @param xref DOCUMENT ME!
     */
    public void setXref(String xref) {
        setAttribute("xref", xref);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
