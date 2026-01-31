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

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLScriptElement;


/**
 * Implements a MathML script element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLScriptElementImpl extends MathMLElementImpl
    implements MathMLScriptElement {
/**
     * Constructs a MathML script element.
     *
     * @param owner the value to set
     * @param qualifiedName the value to set
     */
    public MathMLScriptElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * documentation for this item.
     *
     * @return the requested attribute or element
     */
    public String getSubscriptshift() {
        if (getLocalName().equals("msup")) {
            return null;
        }

        return getAttribute("subscriptshift");
    }

    /**
     * documentation for this item.
     *
     * @param subscriptshift the value to set
     */
    public void setSubscriptshift(String subscriptshift) {
        setAttribute("subscriptshift", subscriptshift);
    }

    /**
     * documentation for this item.
     *
     * @return the requested attribute or element
     */
    public String getSuperscriptshift() {
        if (getLocalName().equals("msub")) {
            return null;
        }

        return getAttribute("superscriptshift");
    }

    /**
     * documentation for this item.
     *
     * @param superscriptshift the value to set
     */
    public void setSuperscriptshift(String superscriptshift) {
        setAttribute("superscriptshift", superscriptshift);
    }

    /**
     * documentation for this item.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getBase() {
        return (MathMLElement) getFirstChild();
    }

    /**
     * documentation for this item.
     *
     * @param base the value to set
     */
    public void setBase(MathMLElement base) {
        replaceChild(base, getFirstChild());
    }

    /**
     * documentation for this item.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getSubscript() {
        if (getLocalName().equals("msup")) {
            return null;
        }

        return (MathMLElement) item(1);
    }

    /**
     * documentation for this item.
     *
     * @param subscript the value to set
     *
     * @throws DOMException if an error occurs
     */
    public void setSubscript(MathMLElement subscript) throws DOMException {
        if (getLocalName().equals("msup")) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "Cannot set a subscript for msup");
        }

        replaceChild(subscript, item(1));
    }

    /**
     * documentation for this item.
     *
     * @return the requested attribute or element
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
     * documentation for this item.
     *
     * @param superscript the value to set
     *
     * @throws DOMException if an error occurs
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

