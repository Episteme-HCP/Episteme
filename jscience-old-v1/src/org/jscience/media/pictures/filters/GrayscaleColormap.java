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

package org.jscience.media.pictures.filters;

/**
 * A grayscale colormap. Black is 0, white is 1.
 */
public class GrayscaleColormap implements Colormap, java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -6015170137060961021L;

/**
     * Creates a new GrayscaleColormap object.
     */
    public GrayscaleColormap() {
    }

    /**
     * Convert a value in the range 0..1 to an RGB color.
     *
     * @param v a value in the range 0..1
     *
     * @return an RGB color
     */
    public int getColor(float v) {
        int n = (int) (v * 255);

        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }

        return 0xff000000 | (n << 16) | (n << 8) | n;
    }
}
