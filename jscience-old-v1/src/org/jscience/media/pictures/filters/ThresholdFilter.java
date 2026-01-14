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
public class ThresholdFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1899610620205446828L;

    /** DOCUMENT ME! */
    private int lowerThreshold;

    /** DOCUMENT ME! */
    private int lowerThreshold3;

    /** DOCUMENT ME! */
    private int upperThreshold;

    /** DOCUMENT ME! */
    private int upperThreshold3;

    /** DOCUMENT ME! */
    private int white = 0xffffff;

    /** DOCUMENT ME! */
    private int black = 0x000000;

/**
     * Creates a new ThresholdFilter object.
     */
    public ThresholdFilter() {
        this(127);
    }

/**
     * Creates a new ThresholdFilter object.
     *
     * @param t DOCUMENT ME!
     */
    public ThresholdFilter(int t) {
        setLowerThreshold(t);
        setUpperThreshold(t);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lowerThreshold DOCUMENT ME!
     */
    public void setLowerThreshold(int lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
        lowerThreshold3 = lowerThreshold * 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLowerThreshold() {
        return lowerThreshold;
    }

    /**
     * DOCUMENT ME!
     *
     * @param upperThreshold DOCUMENT ME!
     */
    public void setUpperThreshold(int upperThreshold) {
        this.upperThreshold = upperThreshold;
        upperThreshold3 = upperThreshold * 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getUpperThreshold() {
        return upperThreshold;
    }

    /**
     * DOCUMENT ME!
     *
     * @param white DOCUMENT ME!
     */
    public void setWhite(int white) {
        this.white = white;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWhite() {
        return white;
    }

    /**
     * DOCUMENT ME!
     *
     * @param black DOCUMENT ME!
     */
    public void setBlack(int black) {
        this.black = black;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBlack() {
        return black;
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
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        int l = r + g + b;

        if (l < lowerThreshold3) {
            return a | black;
        } else if (l > upperThreshold3) {
            return a | white;
        }

        return rgb;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Threshold...";
    }
}
