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

package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;


/**
 * This interface represents the MathML MathMLScriptElement element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLScriptElement extends MathMLPresentationElement {
    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @return the requested attribute or element
     */
    public String getSubscriptshift();

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @param subscriptshift the value to set
     */
    public void setSubscriptshift(String subscriptshift);

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @return the requested attribute or element
     */
    public String getSuperscriptshift();

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @param superscriptshift the value to set
     */
    public void setSuperscriptshift(String superscriptshift);

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getBase();

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @param base the value to set
     */
    public void setBase(MathMLElement base);

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getSubscript();

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @param subscript the value to set
     * @throws DOMException if an error occurs
     */
    public void setSubscript(MathMLElement subscript) throws DOMException;

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLElement getSuperscript();

    /**
 * This interface represents the MathML MathMLScriptElement element.
     *
     * @param superscript the value to set
     * @throws DOMException if an error occurs
     */
    public void setSuperscript(MathMLElement superscript)
            throws DOMException;
}
;
