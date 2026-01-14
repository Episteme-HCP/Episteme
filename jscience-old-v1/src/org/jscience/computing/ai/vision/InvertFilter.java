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

package org.jscience.computing.ai.vision;

import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;


/**
 * This filter inverts an image. The filter is simply implemented using
 * Java's <code>LookupOp</code>, defining the lookup table as: <code> lut[i] =
 * (byte)(255-i); </code>
 *
 * @author James Matthews
 */
public class InvertFilter extends Filter {
/**
     * Creates a new instance of InvertFilter
     */
    public InvertFilter() {
    }

    /**
     * Invert the input image.
     *
     * @param image the input image.
     * @param output the output image (optional).
     *
     * @return the inverted image.
     */
    public java.awt.image.BufferedImage filter(BufferedImage image,
        BufferedImage output) {
        output = verifyOutput(output, image);

        byte[] lut = new byte[256];

        for (int i = 0; i < 256; i++) {
            lut[i] = (byte) (255 - i);
        }

        LookupOp invert = new LookupOp(new ByteLookupTable(0, lut), null);

        return invert.filter(image, output);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Invert";
    }
}
