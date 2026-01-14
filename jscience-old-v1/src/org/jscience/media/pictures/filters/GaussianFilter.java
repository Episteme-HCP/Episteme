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
 * A filter which applies Gaussian blur to an image. This is a subclass of
 * ConvolveFilter which simply creates a kernel with a Gaussian distribution
 * for blurring.
 *
 * @author Jerry Huxtable
 */
public class GaussianFilter extends ConvolveFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5377089073023183684L;

    /** DOCUMENT ME! */
    protected int radius;

    /** DOCUMENT ME! */
    private Kernel[] kernels;

/**
     * Construct a Gaussian filter
     */
    public GaussianFilter() {
        this(2);
    }

/**
     * Construct a Gaussian filter
     *
     * @param radius blur radius in pixels
     */
    public GaussianFilter(int radius) {
        setRadius(radius);
    }

    /**
     * Set the radius of the kernel, and hence the amount of blur. The
     * bigger the radius, the longer this filter will take.
     *
     * @param radius the radius of the blur in pixels.
     */
    public void setRadius(int radius) {
        this.radius = radius;

        //		setKernel(makeKernel(radius));
        kernels = separatedKernels(radius);
    }

    /**
     * Get the radius of the kernel.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
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

        int[] outPixels = new int[width * height];
        convolveH(kernels[0], inPixels, outPixels, width, height, alpha);
        convolveV(kernels[1], outPixels, inPixels, width, height, alpha);

        consumer.setPixels(0, 0, width, height, defaultRGBModel, inPixels, 0,
            width);
        consumer.imageComplete(status);
        inPixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Kernel makeKernel(int radius) {
        int rows = (radius * 2) + 1;
        int cols = rows;
        float[] matrix = new float[rows * cols];
        float sigma = (float) radius / 3;
        float sigma22 = 2 * sigma * sigma;
        float sigmaPi2 = 2 * ImageMath.PI * sigma;
        float radius2 = radius * radius;
        float total = 0;
        int index = 0;

        for (int row = -radius; row <= radius; row++) {
            for (int col = -radius; col <= radius; col++) {
                float distance = (row * row) + (col * col);

                if (distance > radius2) {
                    matrix[index] = 0;
                } else {
                    matrix[index] = (float) Math.exp(-(distance) / sigma22) / sigmaPi2;
                }

                total += matrix[index];
                index++;
            }
        }

        for (int i = 0; i < (rows * cols); i++)
            matrix[i] /= total;

        Kernel kernel = new Kernel(rows, cols, matrix);

        return kernel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Kernel[] separatedKernels(int radius) {
        int rows = (radius * 2) + 1;
        float[] matrix = new float[rows];
        float sigma = (float) radius / 3;
        float sigma22 = 2 * sigma * sigma;
        float sigmaPi2 = 2 * ImageMath.PI * sigma;
        float sqrtSigmaPi2 = (float) Math.sqrt(sigmaPi2);
        float radius2 = radius * radius;
        float total = 0;
        int index = 0;

        for (int row = -radius; row <= radius; row++) {
            float distance = row * row;

            if (distance > radius2) {
                matrix[index] = 0;
            } else {
                matrix[index] = (float) Math.exp(-(distance) / sigma22) / sqrtSigmaPi2;
            }

            total += matrix[index];
            index++;
        }

        for (int i = 0; i < rows; i++)
            matrix[i] /= total;

        return new Kernel[] {
            new Kernel(1, rows, matrix), new Kernel(rows, 1, matrix),
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Gaussian Blur...";
    }
}
