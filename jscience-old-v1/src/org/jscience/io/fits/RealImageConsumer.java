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

package org.jscience.io.fits;

/**
 * This is the similar to an ImageConsumer, except that it handles double
 * valued pixels. This whole paradigm for handling images may get scrapped.
 */
public interface RealImageConsumer {
    /**
     * called by the image producer when it is done sending pixels.
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status);

    /**
     * called by the image producer to indicate the dimensions of the
     * image.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height);

    /**
     * Called by the producer to indicate the pixel value limits.
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    public void setMinMax(double min, double max);

    /**
     * Called by the image producer to indicate hints about how the
     * pixels will be delivered. The hint flags are the same as for an
     * ImageConsumer.
     *
     * @param hintflags DOCUMENT ME!
     */
    public void setHints(int hintflags);

    /**
     * the image producer calls this method to deliver a batch of
     * pixels
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, double[] pixels,
        int offset, int scansize);
} // end of RealImageConsumer interface
