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
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class LifeFilter extends BinaryFilter {
/**
     * Creates a new LifeFilter object.
     */
    public LifeFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        if ((status == IMAGEERROR) || (status == IMAGEABORTED)) {
            consumer.imageComplete(status);

            return;
        }

        int width = originalSpace.width;
        int height = originalSpace.height;
        int index = 0;
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                int pixel = inPixels[(y * width) + x];
                int a = pixel & 0xff000000;
                int neighbours = 0;

                for (int row = -1; row <= 1; row++) {
                    int iy = y + row;
                    int ioffset;

                    if ((0 <= iy) && (iy < height)) {
                        ioffset = iy * width;

                        for (int col = -1; col <= 1; col++) {
                            int ix = x + col;

                            if (!((row == 0) && (col == 0)) && (0 <= ix) &&
                                    (ix < width)) {
                                int rgb = inPixels[ioffset + ix];

                                if (blackFunction.isBlack(rgb)) {
                                    neighbours++;
                                }
                            }
                        }
                    }
                }

                if (blackFunction.isBlack(pixel)) {
                    outPixels[index++] = ((neighbours == 2) ||
                        (neighbours == 3)) ? pixel : 0xffffffff;
                } else {
                    outPixels[index++] = (neighbours == 3) ? 0xff000000 : pixel;
                }
            }
        }

        consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels, 0,
            width);
        consumer.imageComplete(status);
        inPixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Binary/Life";
    }
}
