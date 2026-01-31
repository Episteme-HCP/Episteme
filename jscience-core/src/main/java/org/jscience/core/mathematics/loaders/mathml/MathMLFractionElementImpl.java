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

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLFractionElement;


/**
 * Implements a MathML fraction element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLFractionElementImpl extends MathMLElementImpl
    implements MathMLFractionElement {
/**
     * Constructs a MathML fraction element.
     *
     * @param owner         the document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLFractionElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Gets the line thickness attribute of the fraction.
     *
     * @return the line thickness
     */
    public String getLinethickness() {
        return getAttribute("linethickness");
    }

    /**
     * Sets the line thickness attribute of the fraction.
     *
     * @param linethickness the new line thickness
     */
    public void setLinethickness(String linethickness) {
        setAttribute("linethickness", linethickness);
    }

    /**
     * Gets the numerator element of the fraction.
     *
     * @return the numerator element
     */
    public MathMLElement getNumerator() {
        return (MathMLElement) getFirstChild();
    }

    /**
     * Sets the numerator element of the fraction.
     *
     * @param numerator the new numerator element
     */
    public void setNumerator(MathMLElement numerator) {
        replaceChild(numerator, getFirstChild());
    }

    /**
     * Gets the denominator element of the fraction.
     *
     * @return the denominator element
     */
    public MathMLElement getDenominator() {
        return (MathMLElement) item(1);
    }

    /**
     * Sets the denominator element of the fraction.
     *
     * @param denominator the new denominator element
     */
    public void setDenominator(MathMLElement denominator) {
        replaceChild(denominator, item(1));
    }
}

