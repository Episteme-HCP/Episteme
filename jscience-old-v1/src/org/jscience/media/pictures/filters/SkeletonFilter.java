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


// Based on an algorithm by Zhang and Suen (CACM, March 1984, 236-239).
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class SkeletonFilter extends BinaryFilter {
    /** DOCUMENT ME! */
    private final static byte[] skeletonTable = {
            0, 0, 0, 1, 0, 0, 1, 3, 0, 0, 3, 1, 1, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0,
            0, 2, 0, 2, 0, 3, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
            0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 3, 0, 2, 2, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0,
            0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 2, 0, 0, 1, 3, 1, 0, 0, 1, 3, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 1, 3, 0, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 1, 0, 0,
            0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 1, 0, 0, 0, 0, 2, 2, 0, 0, 2,
            0, 0, 0
        };

/**
     * Creates a new SkeletonFilter object.
     */
    public SkeletonFilter() {
        newColor = 0xffffffff;
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

        try {
            int width = originalSpace.width;
            int height = originalSpace.height;
            int[] outPixels = new int[width * height];

            int count = 0;
            int black = 0xff000000;
            int white = 0xffffffff;

            for (int i = 0; i < iterations; i++) {
                count = 0;

                for (int pass = 0; pass < 2; pass++) {
                    for (int y = 1; y < (height - 1); y++) {
                        int offset = (y * width) + 1;

                        for (int x = 1; x < (width - 1); x++) {
                            int pixel = inPixels[offset];

                            if (pixel == black) {
                                int tableIndex = 0;

                                if (inPixels[offset - width - 1] == black) {
                                    tableIndex |= 1;
                                }

                                if (inPixels[offset - width] == black) {
                                    tableIndex |= 2;
                                }

                                if (inPixels[offset - width + 1] == black) {
                                    tableIndex |= 4;
                                }

                                if (inPixels[offset + 1] == black) {
                                    tableIndex |= 8;
                                }

                                if (inPixels[offset + width + 1] == black) {
                                    tableIndex |= 16;
                                }

                                if (inPixels[offset + width] == black) {
                                    tableIndex |= 32;
                                }

                                if (inPixels[(offset + width) - 1] == black) {
                                    tableIndex |= 64;
                                }

                                if (inPixels[offset - 1] == black) {
                                    tableIndex |= 128;
                                }

                                int code = skeletonTable[tableIndex];

                                if (pass == 1) {
                                    if ((code == 2) || (code == 3)) {
                                        if (colormap != null) {
                                            pixel = colormap.getColor((float) i / iterations);
                                        } else {
                                            pixel = newColor;
                                        }

                                        count++;
                                    }
                                } else {
                                    if ((code == 1) || (code == 3)) {
                                        if (colormap != null) {
                                            pixel = colormap.getColor((float) i / iterations);
                                        } else {
                                            pixel = newColor;
                                        }

                                        count++;
                                    }
                                }
                            }

                            outPixels[offset++] = pixel;
                        }
                    }

                    if (pass == 0) {
                        inPixels = outPixels;
                        outPixels = new int[width * height];
                    }
                }

                if (count == 0) {
                    break;
                }
            }

            consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels,
                0, width);
            consumer.imageComplete(status);
            inPixels = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Binary/Skeletonize...";
    }
}
