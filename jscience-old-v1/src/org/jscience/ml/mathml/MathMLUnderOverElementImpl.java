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
import org.w3c.dom.mathml.MathMLUnderOverElement;


/**
 * Implements a MathML under-over element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLUnderOverElementImpl extends MathMLElementImpl
    implements MathMLUnderOverElement {
/**
     * Constructs a MathML under-over element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLUnderOverElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAccentunder() {
        if (getLocalName().equals("mover")) {
            return null;
        }

        return getAttribute("accentunder");
    }

    /**
     * DOCUMENT ME!
     *
     * @param accentunder DOCUMENT ME!
     */
    public void setAccentunder(String accentunder) {
        setAttribute("accentunder", accentunder);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAccent() {
        if (getLocalName().equals("munder")) {
            return null;
        }

        return getAttribute("accent");
    }

    /**
     * DOCUMENT ME!
     *
     * @param accent DOCUMENT ME!
     */
    public void setAccent(String accent) {
        setAttribute("accent", accent);
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
    public MathMLElement getUnderscript() {
        if (getLocalName().equals("mover")) {
            return null;
        }

        return (MathMLElement) item(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param underscript DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setUnderscript(MathMLElement underscript)
        throws DOMException {
        if (getLocalName().equals("mover")) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "Cannot set a subscript for msup");
        }

        replaceChild(underscript, item(1));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getOverscript() {
        if (getLocalName().equals("munder")) {
            return null;
        }

        if (getLocalName().equals("mover")) {
            return (MathMLElement) item(1);
        } else {
            return (MathMLElement) item(2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param overscript DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setOverscript(MathMLElement overscript)
        throws DOMException {
        if (getLocalName().equals("munder")) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "Cannot set a superscript for msub");
        }

        if (getLocalName().equals("mover")) {
            replaceChild(overscript, item(1));
        } else {
            replaceChild(overscript, item(2));
        }
    }
}
