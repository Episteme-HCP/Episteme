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

package org.w3c.dom.mathml;

/**
 * This interface represents the MathML MathMLPresentationToken element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLPresentationToken extends MathMLPresentationElement {
    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @return the requested attribute or element
     */
    public String getMathvariant();

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @param mathvariant the value to set
     */
    public void setMathvariant(String mathvariant);

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @return the requested attribute or element
     */
    public String getMathsize();

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @param mathsize the value to set
     */
    public void setMathsize(String mathsize);

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @return the requested attribute or element
     */
    public String getMathcolor();

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @param mathcolor the value to set
     */
    public void setMathcolor(String mathcolor);

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @return the requested attribute or element
     */
    public String getMathbackground();

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @param mathbackground the value to set
     */
    public void setMathbackground(String mathbackground);

    /**
 * This interface represents the MathML MathMLPresentationToken element.
     *
     * @return the requested attribute or element
     */
    public MathMLNodeList getContents();
}
;
