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


/**
 * Implements simple Gaussian smoothing. This extends
 * <code>ConvolutionFilter</code> and calculates the kernel automatically from
 * the standard deviation passed.
 *
 * @author James Matthews
 */
public class GaussianFilter extends ConvolutionFilter {
    /** The standard deviation used in the gaussian spread (defaults to 1.0). */
    protected double standardDeviation;

/**
     * Creates a new instance of GaussianFilter
     */
    public GaussianFilter() {
        this(1.0);
    }

/**
     * Creates a new instance of GaussianFilter, with the standard deviation.
     *
     * @param standardDeviation the standard deviation.
     */
    public GaussianFilter(double standardDeviation) {
        setStandardDeviation(standardDeviation);
    }

    /**
     * Utility method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println(
                "usage: java GaussianFilter <input> <output> {stddev}");

            return;
        }

        try {
            BufferedImage in = javax.imageio.ImageIO.read(new java.io.File(
                        args[0]));

            GaussianFilter blur = new GaussianFilter();

            if (args.length > 2) {
                blur.setStandardDeviation(Double.parseDouble(args[2]));
            }

            // Do the filtering
            BufferedImage out = blur.filter(in);

            // Write the image (FIXME: currently always JPG...)
            javax.imageio.ImageIO.write(out, "jpg", new java.io.File(args[1]));
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }

    /**
     * This creates a gaussian filter with a given standard deviation.
     * This method is exposed as public and static as a utility method.
     *
     * @param stddev the standard deviation.
     *
     * @return the kernel.
     */
    public static float[] createKernel(double stddev) {
        int radius = (int) Math.ceil(4.0 * stddev);
        int size = (2 * radius) + 1;

        float[] kernel = new float[size * size];

        double r;
        double s = 2.0 * stddev * stddev;
        double total = 0.0;

        int i = 0;

        for (int y = -radius; y <= radius; ++y) {
            for (int x = -radius; x <= radius; ++x, ++i) {
                r = Math.sqrt((x * x) + (y * y));
                kernel[i] = (float) Math.exp((-r * r) / s);

                total += kernel[i];
            }
        }

        // Normalize the data
        for (i = 0; i < kernel.length; i++)
            kernel[i] /= total;

        return kernel;
    }

    /**
     * Calculate the size of the kernel required for the standard
     * deviation passed.
     *
     * @param stddev the standard deviation.
     *
     * @return the kernel size.
     */
    protected static int getSize(double stddev) {
        int r = (int) Math.ceil(4.0 * stddev);

        return (2 * r) + 1;
    }

    /**
     * Get the standard deviation.
     *
     * @return the current standard deviation.
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * Set the standard deviation used. The kernel is automatically
     * regenerated when this method is called.
     *
     * @param standardDeviation the new standard deviation to use.
     */
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;

        int size = getSize(standardDeviation);

        setKernel(new java.awt.image.Kernel(size, size,
                createKernel(standardDeviation)));
    }
}
