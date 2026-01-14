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

/**
 * A mean filter implemented as a convolution filter.
 *
 * @author James Matthews
 *
 * @see org.generation5.vision.ConvolutionFilter
 */
public class MeanFilter extends ConvolutionFilter {
    /** The neighbourhood size. */
    protected int neighbourhoodSize = 1;

/**
     * Creates a new instance of MeanFilter. The default constructor sets the
     * neighbourhood size at 3.
     */
    public MeanFilter() {
        this(3);
    }

/**
     * Creates a new instance of MeanFilter of the specified neighbourhood
     * size.
     *
     * @param neighbourhoodSize the neighbourhood size.
     */
    public MeanFilter(int neighbourhoodSize) {
        setNeighbourhoodSize(neighbourhoodSize);
    }

    /**
     * Set the neighbourhood size. Note that the neighbourhood size
     * must be an odd number. Any even number is increased to make it an odd
     * number.
     *
     * @param ns the new neighbourhood size.
     */
    public void setNeighbourhoodSize(int ns) {
        if ((ns % 2) == 0) {
            ns = ns + 1;
        }

        neighbourhoodSize = ns;

        int kernel = ns * ns;

        float frac = 1.0f / (float) kernel;
        float[] blurKernel = new float[kernel];

        for (int i = 0; i < kernel; i++) {
            blurKernel[i] = frac;
        }

        setKernel(new java.awt.image.Kernel(ns, ns, blurKernel));
    }

    /**
     * Retrieve the current neighbourhood size.
     *
     * @return the neighbourhood size.
     */
    public int getNeighbourhoodSize() {
        return neighbourhoodSize;
    }

    /**
     * Blur the image using the mean kernel.
     *
     * @param image the input image.
     *
     * @return the output image.
     */
    public java.awt.image.BufferedImage filter(
        java.awt.image.BufferedImage image) {
        return super.filter(image);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Mean Filter (" + neighbourhoodSize + "x" + neighbourhoodSize +
        ")";
    }

    /**
     * Utility method for the class.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println(
                "usage: java MeanFilter <input> <output> {neighbourhood}");

            return;
        }

        try {
            java.awt.image.BufferedImage in = javax.imageio.ImageIO.read(new java.io.File(
                        args[0]));

            MeanFilter mean = new MeanFilter();

            //
            // Retrieve any possible optional parameters
            //
            if (args.length > 2) {
                mean.setNeighbourhoodSize(Integer.parseInt(args[2]));
            }

            // Do the filtering
            java.awt.image.BufferedImage out = mean.filter(in);

            // Write the image (FIXME: currently always JPG...)
            javax.imageio.ImageIO.write(out, "jpg", new java.io.File(args[1]));
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }
}
