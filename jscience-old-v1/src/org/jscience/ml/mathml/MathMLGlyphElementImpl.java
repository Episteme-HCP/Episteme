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

import org.w3c.dom.mathml.MathMLGlyphElement;


/**
 * Implements a MathML glyph element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLGlyphElementImpl extends MathMLElementImpl
    implements MathMLGlyphElement {
/**
     * Constructs a MathML glyph element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLGlyphElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAlt() {
        return getAttribute("alt");
    }

    /**
     * DOCUMENT ME!
     *
     * @param alt DOCUMENT ME!
     */
    public void setAlt(String alt) {
        setAttribute("alt", alt);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFontfamily() {
        return getAttribute("fontfamily");
    }

    /**
     * DOCUMENT ME!
     *
     * @param fontfamily DOCUMENT ME!
     */
    public void setFontfamily(String fontfamily) {
        setAttribute("fontfamily", fontfamily);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIndex() {
        return Integer.parseInt(getAttribute("index"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void setIndex(int index) {
        setAttribute("index", Integer.toString(index));
    }
}
