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

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLScriptElement;


/**
 * Implements a MathML script element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLScriptElementImpl extends MathMLElementImpl
    implements MathMLScriptElement {
/**
     * Constructs a MathML script element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLScriptElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSubscriptshift() {
        if (getLocalName().equals("msup")) {
            return null;
        }

        return getAttribute("subscriptshift");
    }

    /**
     * DOCUMENT ME!
     *
     * @param subscriptshift DOCUMENT ME!
     */
    public void setSubscriptshift(String subscriptshift) {
        setAttribute("subscriptshift", subscriptshift);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSuperscriptshift() {
        if (getLocalName().equals("msub")) {
            return null;
        }

        return getAttribute("superscriptshift");
    }

    /**
     * DOCUMENT ME!
     *
     * @param superscriptshift DOCUMENT ME!
     */
    public void setSuperscriptshift(String superscriptshift) {
        setAttribute("superscriptshift", superscriptshift);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getBase() {
        return (MathMLElement) getFirstChild();
    }

    /**
     * DOCUMENT ME!
     *
     * @param base DOCUMENT ME!
     */
    public void setBase(MathMLElement base) {
        replaceChild(base, getFirstChild());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getSubscript() {
        if (getLocalName().equals("msup")) {
            return null;
        }

        return (MathMLElement) item(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param subscript DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setSubscript(MathMLElement subscript) throws DOMException {
        if (getLocalName().equals("msup")) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "Cannot set a subscript for msup");
        }

        replaceChild(subscript, item(1));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getSuperscript() {
        if (getLocalName().equals("msub")) {
            return null;
        }

        if (getLocalName().equals("msup")) {
            return (MathMLElement) item(1);
        } else {
            return (MathMLElement) item(2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param superscript DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setSuperscript(MathMLElement superscript)
        throws DOMException {
        if (getLocalName().equals("msub")) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "Cannot set a superscript for msub");
        }

        if (getLocalName().equals("msup")) {
            replaceChild(superscript, item(1));
        } else {
            replaceChild(superscript, item(2));
        }
    }
}
