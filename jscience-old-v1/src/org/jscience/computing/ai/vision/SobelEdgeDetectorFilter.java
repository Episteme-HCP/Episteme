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
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


/**
 * This filter performs a simple edge detection algorithm (Sobel).
 *
 * @author James Matthews
 */
public class SobelEdgeDetectorFilter extends Filter {
/**
     * Creates a new instance of SobelEdgeDetector
     */
    public SobelEdgeDetectorFilter() {
    }

    /**
     * Run the edge detection algorithm on the image passed. This
     * filter will work on greyscale or RGB images.
     *
     * @param image the input image
     * @param output the output image (optional).
     *
     * @return the image with the edges detected
     */
    public BufferedImage filter(BufferedImage image, BufferedImage output) {
        output = verifyOutput(output, image);

        Raster in_pixels = image.getRaster();
        WritableRaster out_pixels = output.getRaster();

        short gc;
        int a;
        int b;
        int c;
        int d;
        int e;
        int f;
        int g;
        int h;
        int z;

        float sobscale = 1;
        float offsetval = 0;

        int i = 1;
        int j = 0;
        int final_i = image.getWidth() - 1;
        int final_j = image.getHeight() - 1;

        for (int bnd = 0; bnd < in_pixels.getNumBands(); bnd++) {
            for (j = 1; j < final_j; j++) {
                a = in_pixels.getSample(i - 1, j - 1, bnd);
                b = in_pixels.getSample(i, j - 1, bnd);
                d = in_pixels.getSample(i - 1, j, bnd);
                f = in_pixels.getSample(i - 1, j + 1, bnd);
                g = in_pixels.getSample(i, j + 1, bnd);
                z = in_pixels.getSample(i, j, bnd);

                for (i = 1; i < final_i; i++) {
                    c = in_pixels.getSample(i + 1, j - 1, bnd);
                    e = in_pixels.getSample(i + 1, j, bnd);
                    h = in_pixels.getSample(i + 1, j + 1, bnd);

                    int hor = (a + d + f) - (c + e + h); // The Sobel algorithm

                    if (hor < 0) {
                        hor = -hor;
                    }

                    int vert = (a + b + c) - (f + g + h);

                    if (vert < 0) {
                        vert = -vert;
                    }

                    gc = (short) (sobscale * (hor + vert));
                    gc = (short) (gc + offsetval);

                    gc = (short) ((gc > 255) ? 255 : gc);

                    out_pixels.setSample(i, j, bnd, gc);

                    a = b;
                    b = c;
                    d = z;
                    f = g;
                    g = h;
                    z = e;
                }

                i = 1;
            }
        }

        return output;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Sobel Edge Detector";
    }
}
