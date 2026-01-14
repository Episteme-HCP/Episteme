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
 * A Filter to draw grids and check patterns.
 */
public class CheckFilter extends RGBImageFilter {
    /** DOCUMENT ME! */
    private int xScale = 8;

    /** DOCUMENT ME! */
    private int yScale = 8;

    /** DOCUMENT ME! */
    private int foreground = 0xffffffff;

    /** DOCUMENT ME! */
    private int background = 0xff000000;

    /** DOCUMENT ME! */
    private int fuzziness = 0;

    /** DOCUMENT ME! */
    private float angle = 0.0f;

    /** DOCUMENT ME! */
    private int operation;

    /** DOCUMENT ME! */
    private float m00 = 1.0f;

    /** DOCUMENT ME! */
    private float m01 = 0.0f;

    /** DOCUMENT ME! */
    private float m10 = 0.0f;

    /** DOCUMENT ME! */
    private float m11 = 1.0f;

/**
     * Creates a new CheckFilter object.
     */
    public CheckFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param foreground DOCUMENT ME!
     */
    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getForeground() {
        return foreground;
    }

    /**
     * DOCUMENT ME!
     *
     * @param background DOCUMENT ME!
     */
    public void setBackground(int background) {
        this.background = background;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBackground() {
        return background;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xScale DOCUMENT ME!
     */
    public void setXScale(int xScale) {
        this.xScale = xScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getXScale() {
        return xScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yScale DOCUMENT ME!
     */
    public void setYScale(int yScale) {
        this.yScale = yScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getYScale() {
        return yScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fuzziness DOCUMENT ME!
     */
    public void setFuzziness(int fuzziness) {
        this.fuzziness = fuzziness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFuzziness() {
        return fuzziness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param operation DOCUMENT ME!
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOperation() {
        return operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     */
    public void setAngle(float angle) {
        this.angle = angle;

        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        m00 = cos;
        m01 = sin;
        m10 = -sin;
        m11 = cos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAngle() {
        return angle;
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
        float nx = ((m00 * x) + (m01 * y)) / xScale;
        float ny = ((m10 * x) + (m11 * y)) / yScale;
        float f = (((int) (nx + 100000) % 2) != ((int) (ny + 100000) % 2))
            ? 1.0f : 0.0f;

        if (fuzziness != 0) {
            float fuzz = (fuzziness / 100.0f);
            float fx = ImageMath.smoothPulse(0, fuzz, 1 - fuzz, 1,
                    ImageMath.mod(nx, 1));
            float fy = ImageMath.smoothPulse(0, fuzz, 1 - fuzz, 1,
                    ImageMath.mod(ny, 1));

            if (f == 0.0) {
                f = 0.5f - (fx * fy);
            } else {
                f = 0.5f + (fx * fy);
            }
        }

        return ImageMath.mixColors(f, foreground, background);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Texture/Checkerboard...";
    }
}
