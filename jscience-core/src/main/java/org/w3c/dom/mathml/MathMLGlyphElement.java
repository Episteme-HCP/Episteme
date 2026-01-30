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

/**
 * This interface provides support for the mglyph element, used to display 
 * non-standard symbols.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLGlyphElement extends MathMLPresentationElement {
    /**
     * Returns the alternative text for the glyph.
     *
     * @return the alternative text string.
     */
    public String getAlt();

    /**
     * Sets the alternative text for the glyph.
     *
     * @param alt the alternative text string to set.
     */
    public void setAlt(String alt);

    /**
     * Returns the font family from which the glyph should be taken.
     *
     * @return the font family string.
     */
    public String getFontfamily();

    /**
     * Sets the font family from which the glyph should be taken.
     *
     * @param fontfamily the font family string to set.
     */
    public void setFontfamily(String fontfamily);

    /**
     * Returns the index of the glyph within the specified font.
     *
     * @return the integer index of the glyph.
     */
    public int getIndex();

    /**
     * Sets the index of the glyph within the specified font.
     *
     * @param index the integer index to set.
     */
    public void setIndex(int index);
}
;
