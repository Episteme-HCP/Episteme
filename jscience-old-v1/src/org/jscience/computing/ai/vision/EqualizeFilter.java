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
 * This filter performs histogram equalization on their a greyscale or RGB
 * image.
 *
 * @author James Matthews
 */
public class EqualizeFilter extends Filter {
/**
     * Creates a new instance of EqualizeFilter
     */
    public EqualizeFilter() {
    }

    /**
     * Equalizes an images using its histogram.
     *
     * @param image input image.
     * @param output the pre-allocated output image (optional).
     *
     * @return the output image.
     */
    public java.awt.image.BufferedImage filter(BufferedImage image,
        BufferedImage output) {
        // Create our histogram
        Histogram hist = new Histogram(image);

        // Verify our output
        output = verifyOutput(output, image);

        // Set up the rasters
        Raster in_pixels = image.getRaster();
        WritableRaster out_pixels = output.getRaster();

        int g = 0;
        double alpha = 255.0 / (image.getWidth() * image.getHeight());

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                g = (int) (alpha * hist.getCumulativeFrequency(in_pixels.getSample(
                            i, j, 0), 0));
                out_pixels.setSample(i, j, 0, g);

                if (hist.isGreyscale() == false) {
                    g = (int) (alpha * hist.getCumulativeFrequency(in_pixels.getSample(
                                i, j, 1), 1));
                    out_pixels.setSample(i, j, 1, g);
                    g = (int) (alpha * hist.getCumulativeFrequency(in_pixels.getSample(
                                i, j, 2), 2));
                    out_pixels.setSample(i, j, 2, g);
                }
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
        return "Histogram Equalization";
    }
}
