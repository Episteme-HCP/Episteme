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
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;


/**
 * This class implements a convolution filter. This class is merely a
 * wrapper for the Java 2D <code>ConvolveOp</code>, but implements the
 * Generation5 JDK's <code>Filter</code>.
 *
 * @author James Matthews
 */
public class ConvolutionFilter extends Filter {
    /** The kernel to be used. */
    protected Kernel kernel;

/**
     * Creates a new instance of ConvolutionFilter
     */
    public ConvolutionFilter() {
    }

/**
     * Creates a new instance of a convolution filter with the given kernel.
     *
     * @param kernel the kernel to use.
     */
    public ConvolutionFilter(Kernel kernel) {
        setKernel(kernel);
    }

    /**
     * Set the kernel to be used.
     *
     * @param kernel the kernel.
     */
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    /**
     * Return the kernel currently set.
     *
     * @return the kernel.
     */
    public Kernel getKernel() {
        return kernel;
    }

    /**
     * Convolve the image using the kernel.
     *
     * @param image the input image.
     * @param output the pre-allocated output image (optional).
     *
     * @return the output image.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public BufferedImage filter(BufferedImage image, BufferedImage output) {
        if (kernel == null) {
            throw new IllegalArgumentException("kernel not set.");
        }

        // Verify our output image
        output = verifyOutput(output, image);

        // Set up the convolution operation
        BufferedImageOp operation = new ConvolveOp(kernel);

        // Filter and return the image
        return operation.filter(image, output);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Convolution";
    }
}
