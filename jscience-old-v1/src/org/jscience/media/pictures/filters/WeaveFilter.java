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

import java.awt.image.RGBImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class WeaveFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4847932412277504482L;

    /** DOCUMENT ME! */
    private float xWidth = 16;

    /** DOCUMENT ME! */
    private float yWidth = 16;

    /** DOCUMENT ME! */
    private float xGap = 6;

    /** DOCUMENT ME! */
    private float yGap = 6;

    /** DOCUMENT ME! */
    private int rows = 4;

    /** DOCUMENT ME! */
    private int cols = 4;

    /** DOCUMENT ME! */
    private int rgbX = 0xffff8080;

    /** DOCUMENT ME! */
    private int rgbY = 0xff8080ff;

    /** DOCUMENT ME! */
    private boolean useImageColors = true;

    /** DOCUMENT ME! */
    private boolean roundThreads = false;

    /** DOCUMENT ME! */
    private boolean shadeCrossings = true;

    /** DOCUMENT ME! */
    public int[][] matrix = {
            { 0, 1, 0, 1 },
            { 1, 0, 1, 0 },
            { 0, 1, 0, 1 },
            { 1, 0, 1, 0 },
        };

/**
     * Creates a new WeaveFilter object.
     */
    public WeaveFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param xGap DOCUMENT ME!
     */
    public void setXGap(float xGap) {
        this.xGap = xGap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xWidth DOCUMENT ME!
     */
    public void setXWidth(float xWidth) {
        this.xWidth = xWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXWidth() {
        return xWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yWidth DOCUMENT ME!
     */
    public void setYWidth(float yWidth) {
        this.yWidth = yWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYWidth() {
        return yWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXGap() {
        return xGap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yGap DOCUMENT ME!
     */
    public void setYGap(float yGap) {
        this.yGap = yGap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYGap() {
        return yGap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param matrix DOCUMENT ME!
     */
    public void setCrossings(int[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[][] getCrossings() {
        return matrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @param useImageColors DOCUMENT ME!
     */
    public void setUseImageColors(boolean useImageColors) {
        this.useImageColors = useImageColors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getUseImageColors() {
        return useImageColors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roundThreads DOCUMENT ME!
     */
    public void setRoundThreads(boolean roundThreads) {
        this.roundThreads = roundThreads;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getRoundThreads() {
        return roundThreads;
    }

    /**
     * DOCUMENT ME!
     *
     * @param shadeCrossings DOCUMENT ME!
     */
    public void setShadeCrossings(boolean shadeCrossings) {
        this.shadeCrossings = shadeCrossings;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getShadeCrossings() {
        return shadeCrossings;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param rgb DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int filterRGB(int x, int y, int rgb) {
        x += (xWidth + (xGap / 2));
        y += (yWidth + (yGap / 2));

        float nx = ImageMath.mod(x, xWidth + xGap);
        float ny = ImageMath.mod(y, yWidth + yGap);
        int ix = (int) (x / (xWidth + xGap));
        int iy = (int) (y / (yWidth + yGap));
        boolean inX = nx < xWidth;
        boolean inY = ny < yWidth;
        float dX;
        float dY;
        float cX;
        float cY;
        int lrgbX;
        int lrgbY;

        if (roundThreads) {
            dX = Math.abs((xWidth / 2) - nx) / xWidth / 2;
            dY = Math.abs((yWidth / 2) - ny) / yWidth / 2;
        } else {
            dX = dY = 0;
        }

        if (shadeCrossings) {
            cX = ImageMath.smoothStep(xWidth / 2, (xWidth / 2) + xGap,
                    Math.abs((xWidth / 2) - nx));
            cY = ImageMath.smoothStep(yWidth / 2, (yWidth / 2) + yGap,
                    Math.abs((yWidth / 2) - ny));
        } else {
            cX = cY = 0;
        }

        if (useImageColors) {
            lrgbX = lrgbY = rgb;
        } else {
            lrgbX = rgbX;
            lrgbY = rgbY;
        }

        int v;
        int ixc = ix % cols;
        int iyr = iy % rows;
        int m = matrix[iyr][ixc];

        if (inX) {
            if (inY) {
                v = (m == 1) ? lrgbX : lrgbY;
                v = ImageMath.mixColors(2 * ((m == 1) ? dX : dY), v, 0xff000000);
            } else {
                if (shadeCrossings) {
                    if (m != matrix[(iy + 1) % rows][ixc]) {
                        if (m == 0) {
                            cY = 1 - cY;
                        }

                        cY *= 0.5f;
                        lrgbX = ImageMath.mixColors(cY, lrgbX, 0xff000000);
                    } else if (m == 0) {
                        lrgbX = ImageMath.mixColors(0.5f, lrgbX, 0xff000000);
                    }
                }

                v = ImageMath.mixColors(2 * dX, lrgbX, 0xff000000);
            }
        } else if (inY) {
            if (shadeCrossings) {
                if (m != matrix[iyr][(ix + 1) % cols]) {
                    if (m == 1) {
                        cX = 1 - cX;
                    }

                    cX *= 0.5f;
                    lrgbY = ImageMath.mixColors(cX, lrgbY, 0xff000000);
                } else if (m == 1) {
                    lrgbY = ImageMath.mixColors(0.5f, lrgbY, 0xff000000);
                }
            }

            v = ImageMath.mixColors(2 * dY, lrgbY, 0xff000000);
        } else {
            v = 0x00000000;
        }

        return v;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Texture/Weave...";
    }
}
