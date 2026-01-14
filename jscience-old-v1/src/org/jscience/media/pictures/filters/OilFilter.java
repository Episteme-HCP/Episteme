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
public class OilFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1722613531684653826L;

    /** DOCUMENT ME! */
    public int range = 3;

/**
     * Creates a new OilFilter object.
     */
    public OilFilter() {
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
        int[] rHistogram = new int[256];
        int[] gHistogram = new int[256];
        int[] bHistogram = new int[256];
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int i = 0; i < 256; i++)
                    rHistogram[i] = gHistogram[i] = bHistogram[i] = 0;

                for (int row = -range; row <= range; row++) {
                    int iy = y + row;
                    int ioffset;

                    if ((0 <= iy) && (iy < height)) {
                        ioffset = iy * width;

                        for (int col = -range; col <= range; col++) {
                            int ix = x + col;

                            if ((0 <= ix) && (ix < width)) {
                                int rgb = inPixels[ioffset + ix];
                                rHistogram[(rgb >> 16) & 0xff]++;
                                gHistogram[(rgb >> 8) & 0xff]++;
                                bHistogram[rgb & 0xff]++;
                            }
                        }
                    }
                }

                int r = 0;
                int g = 0;
                int b = 0;

                for (int i = 1; i < 256; i++) {
                    if (rHistogram[i] > rHistogram[r]) {
                        r = i;
                    }

                    if (gHistogram[i] > gHistogram[g]) {
                        g = i;
                    }

                    if (bHistogram[i] > bHistogram[b]) {
                        b = i;
                    }
                }

                outPixels[index++] = 0xff000000 | (r << 16) | (g << 8) | b;
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
        return "Stylize/Oil...";
    }
}
