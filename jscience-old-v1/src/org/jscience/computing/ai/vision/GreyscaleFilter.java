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
 * This class converts an RGB colour to a greyscale image.
 *
 * @author James Matthews
 */
public class GreyscaleFilter extends Filter {
    /** BT709 Greyscale: Red:   0.2125 Green: 0.7154 Blue:  0.0721 */
    public static final int BT709 = 0;

    /** Y-Greyscale (YIQ/NTSC): Red:   0.299 Green: 0.587 Blue:  0.114 */
    public static final int Y = 1;

    /** RMY Greyscale: Red:   0.5 Green: 0.419 Blue:  0.081 */
    public static final int RMY = 2;

    /** The greyscale type (BT709, Y or RMY) */
    protected int greyscaleType = BT709;

/**
     * Creates a new instance of GreyscaleFilter
     */
    public GreyscaleFilter() {
    }

    /**
     * Convert an RGB image to greyscale.
     *
     * @param image the input image.
     * @param output the output image (optional).
     *
     * @return the output image.
     */
    public java.awt.image.BufferedImage filter(BufferedImage image,
        BufferedImage output) {
        // Verify our output variable
        output = verifyOutput(output, image, BufferedImage.TYPE_BYTE_GRAY);

        Raster in_pixels = image.getRaster();
        WritableRaster out_pixels = output.getRaster();

        if ((image.getType() == image.TYPE_BYTE_GRAY) ||
                (image.getType() == image.TYPE_USHORT_GRAY)) {
            return image;
        }

        int r;
        int g;
        int b;
        int gc;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                r = in_pixels.getSample(i, j, 0);
                g = in_pixels.getSample(i, j, 1);
                b = in_pixels.getSample(i, j, 2);

                gc = calculateGrey(r, g, b);
                out_pixels.setSample(i, j, 0, gc);
            }
        }

        return output;
    }

    /**
     * A static utility function to generate a greyscale image. Simply
     * implemented as: <code> GreyscaleFilter grey = new GreyscaleFilter();
     * return grey.filter(input, output); </code>
     *
     * @param input the input image.
     * @param output the output image (optional).
     *
     * @return the greyscale image.
     */
    public static BufferedImage toGrey(BufferedImage input, BufferedImage output) {
        GreyscaleFilter grey = new GreyscaleFilter();

        return grey.filter(input, output);
    }

    /**
     * Calculate the grey value according to the grey conversion set.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     *
     * @return the calculated grey value.
     */
    protected int calculateGrey(int r, int g, int b) {
        switch (greyscaleType) {
        case BT709:
            return (int) ((0.2125 * r) + (0.7154 * g) + (0.0721 * b));

        case Y:
            return (int) ((0.299 * r) + (0.587 * g) + (0.114 * b));

        case RMY:
            return (int) ((0.5 * r) + (0.419 * g) + (0.081 * b));
        }

        return 255;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Greyscale";
    }
}
