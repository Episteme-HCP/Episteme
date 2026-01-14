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

import java.awt.*;


/**
 * A Filter to pixellate images.
 */
public class BlockFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 8077109551486196569L;

    /** DOCUMENT ME! */
    private int blockSize = 2;

/**
     * Creates a new BlockFilter object.
     */
    public BlockFilter() {
    }

    /**
     * Set the pixel block size
     *
     * @param blockSize the number of pixels along each block edge
     */
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Get the pixel block size
     *
     * @return the number of pixels along each block edge
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transform(int x, int y, Point out) {
        out.x = (x / blockSize) * blockSize;
        out.y = (y / blockSize) * blockSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (x / blockSize) * blockSize;
        out[1] = (y / blockSize) * blockSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Mosaic...";
    }
}
