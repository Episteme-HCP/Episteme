/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.loaders.mathml;

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;


/**
 * Implements a MathML under-over element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLUnderOverElementImpl extends MathMLElementImpl
    implements MathMLUnderOverElement {
/**
     * Constructs a MathML under-over element.
     *
     * @param owner the value to set
     * @param qualifiedName the value to set
     */
    public MathMLUnderOverElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * documentation for this item.
     *
     * @return the requested attribute or element
     */
    public String getAccentunder() {
        if (getLocalName().equals("mover")) {
            return null;
        }

        return getAttribute("accentunder");
    }

    /**
     * documentation for this item.
     *
     * @param accentunder the value to set
     */
    public void setAccentunder(String accentunder) {
        setAttribute("accentunder", accentunder);
    }

    /**
     * documentation for this item.
     *
     * @return the requested attribute or element
     */
    public String getAccent() {
        if (getLocalName().equals("munder")) {
            return null;
        }

        return getAttribute("accent");
    }

    /**
     * documentation for this item.
     *
     * @param accent the value to set
     */
    public void setAccent(String accent) {
        setAttribute("accent", accent);
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
    public MathMLElement getUnderscript() {
        if (getLocalName().equals("mover")) {
            return null;
        }

        return (MathMLElement) item(1);
    }

    /**
     * documentation for this item.
     *
     * @param underscript the value to set
     *
     * @throws DOMException if an error occurs
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
     * documentation for this item.
     *
     * @return the requested attribute or element
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
     * documentation for this item.
     *
     * @param overscript the value to set
     *
     * @throws DOMException if an error occurs
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

