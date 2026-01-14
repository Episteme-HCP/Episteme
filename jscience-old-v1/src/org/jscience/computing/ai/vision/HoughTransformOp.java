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
 * This abstract class is for subclasses to implement different derivates
 * of Hough Transforms. Note that as this is more of an operation, not a
 * filter, this class does <i>not</i> implement the <code>Filter</code>
 * interface.
 *
 * @author James Matthews
 */
public abstract class HoughTransformOp {
/**
     * Creates a new instance of HoughTransform
     */
    public HoughTransformOp() {
    }

    /**
     * Run the Hough transform on the input image.
     *
     * @param img the input image.
     */
    public abstract void run(BufferedImage img);

    /**
     * Return a BufferedImage of the accumulator. How this is
     * implemented is up to the programmer, since accumulators can be
     * multidimensional.
     *
     * @return the accumulator image.
     */
    public abstract BufferedImage getAccumulatorImage();

    /**
     * Return an image of the Hough Transform results superimposed on
     * the input image.
     *
     * @param img the input image.
     * @param threshold the accumulator threshold.
     *
     * @return the superimposed image.
     */
    public abstract BufferedImage getSuperimposed(BufferedImage img,
        double threshold);
}
