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

package org.jscience.mathematics.loaders.mathml;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLRadicalElement;


/**
 * Implements a MathML radical element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLRadicalElementImpl extends MathMLElementImpl
    implements MathMLRadicalElement {
/**
     * Constructs a MathML radical element.
     *
     * @param owner         the document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLRadicalElementImpl(MathMLDocumentImpl owner,
        String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Gets the radicand of this element.
     *
     * @return the radicand element
     */
    public MathMLElement getRadicand() {
        return (MathMLElement) getFirstChild();
    }

    /**
     * Sets the radicand of this element.
     *
     * @param radicand the new radicand element
     */
    public void setRadicand(MathMLElement radicand) {
        replaceChild(radicand, getFirstChild());
    }

    /**
     * Gets the index of this radical (for mroot).
     *
     * @return the index element, or null for msqrt
     */
    public MathMLElement getIndex() {
        if (getLocalName().equals("msqrt")) {
            return null;
        }

        return (MathMLElement) item(1);
    }

    /**
     * Sets the index of this radical.
     *
     * @param index the new index element
     */
    public void setIndex(MathMLElement index) {
        replaceChild(index, item(1));
    }
}
