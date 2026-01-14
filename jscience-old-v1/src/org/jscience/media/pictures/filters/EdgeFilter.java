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
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class EdgeFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1084121755410916989L;

    /** DOCUMENT ME! */
    protected float[] vEdgeMatrix = Kernel.PREWITT_V;

    /** DOCUMENT ME! */
    protected float[] hEdgeMatrix = Kernel.PREWITT_H;

/**
     * Creates a new EdgeFilter object.
     */
    public EdgeFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param vEdgeMatrix DOCUMENT ME!
     */
    public void setVEdgeMatrix(float[] vEdgeMatrix) {
        this.vEdgeMatrix = vEdgeMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float[] getVEdgeMatrix() {
        return vEdgeMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hEdgeMatrix DOCUMENT ME!
     */
    public void setHEdgeMatrix(float[] hEdgeMatrix) {
        this.hEdgeMatrix = hEdgeMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float[] getHEdgeMatrix() {
        return hEdgeMatrix;
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
        int index = 0;
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                int rh = 0;
                int gh = 0;
                int bh = 0;
                int rv = 0;
                int gv = 0;
                int bv = 0;
                int a = inPixels[(y * width) + x] & 0xff000000;

                for (int row = -1; row <= 1; row++) {
                    int iy = y + row;
                    int ioffset;

                    if ((0 <= iy) && (iy < height)) {
                        ioffset = iy * width;
                    } else {
                        ioffset = y * width;
                    }

                    int moffset = (3 * (row + 1)) + 1;

                    for (int col = -1; col <= 1; col++) {
                        int ix = x + col;

                        if (!((0 <= ix) && (ix < width))) {
                            ix = x;
                        }

                        int rgb = inPixels[ioffset + ix];
                        float h = hEdgeMatrix[moffset + col];
                        float v = vEdgeMatrix[moffset + col];

                        r = (rgb & 0xff0000) >> 16;
                        g = (rgb & 0x00ff00) >> 8;
                        b = rgb & 0x0000ff;
                        rh += (int) (h * r);
                        gh += (int) (h * g);
                        bh += (int) (h * b);
                        rv += (int) (v * r);
                        gv += (int) (v * g);
                        bv += (int) (v * b);
                    }
                }

                r = (int) (Math.sqrt((rh * rh) + (rv * rv)) / 1.8);
                g = (int) (Math.sqrt((gh * gh) + (gv * gv)) / 1.8);
                b = (int) (Math.sqrt((bh * bh) + (bv * bv)) / 1.8);
                r = PixelUtils.clamp(r);
                g = PixelUtils.clamp(g);
                b = PixelUtils.clamp(b);
                outPixels[index++] = a | (r << 16) | (g << 8) | b;
            }
        }

        consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels, 0,
            width);
        consumer.imageComplete(status);
        inPixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Detect Edges";
    }
}
