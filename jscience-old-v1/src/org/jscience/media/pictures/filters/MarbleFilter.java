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

import org.jscience.media.pictures.filters.math.*;


/**
 * This filter applies a marbling effect to an image, displacing pixels by
 * random amounts.
 */
public class MarbleFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3658170437130333021L;

    /** DOCUMENT ME! */
    public float[] sinTable;

    /** DOCUMENT ME! */
    public float[] cosTable;

    /** DOCUMENT ME! */
    public float xScale = 4;

    /** DOCUMENT ME! */
    public float yScale = 4;

    /** DOCUMENT ME! */
    public float amount = 1;

    /** DOCUMENT ME! */
    public float turbulence = 1;

/**
     * Creates a new MarbleFilter object.
     */
    public MarbleFilter() {
        setEdgeAction(CLAMP);
    }

    /**
     * DOCUMENT ME!
     *
     * @param xScale DOCUMENT ME!
     */
    public void setXScale(float xScale) {
        this.xScale = xScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXScale() {
        return xScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yScale DOCUMENT ME!
     */
    public void setYScale(float yScale) {
        this.yScale = yScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYScale() {
        return yScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param turbulence DOCUMENT ME!
     */
    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        sinTable = new float[256];
        cosTable = new float[256];

        for (int i = 0; i < 256; i++) {
            float angle = (ImageMath.TWO_PI * i) / 256f * turbulence;
            sinTable[i] = (float) (-yScale * Math.sin(angle));
            cosTable[i] = (float) (yScale * Math.cos(angle));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int displacementMap(int x, int y) {
        return PixelUtils.clamp((int) (127 * (1 +
            Noise.noise2(x / xScale, y / xScale))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        int displacement = displacementMap(x, y);
        out[0] = x + sinTable[displacement];
        out[1] = y + cosTable[displacement];
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        initialize();
        super.imageComplete(status);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Marble...";
    }
}
