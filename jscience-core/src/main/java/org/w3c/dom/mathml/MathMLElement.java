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

import org.w3c.dom.Element;


/**
 * This interface represents the MathML MathMLElement element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLElement extends Element {
    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @return the requested attribute or element
     */
    public String getClassName();

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @param className the value to set
     */
    public void setClassName(String className);

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @return the requested attribute or element
     */
    public String getMathElementStyle();

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @param mathElementStyle the value to set
     */
    public void setMathElementStyle(String mathElementStyle);

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @return the requested attribute or element
     */
    public String getId();

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @param id the value to set
     */
    public void setId(String id);

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @return the requested attribute or element
     */
    public String getXref();

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @param xref the value to set
     */
    public void setXref(String xref);

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @return the requested attribute or element
     */
    public String getHref();

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @param href the value to set
     */
    public void setHref(String href);

    /**
 * This interface represents the MathML MathMLElement element.
     *
     * @return the requested attribute or element
     */
    public MathMLMathElement getOwnerMathElement();
}
;
