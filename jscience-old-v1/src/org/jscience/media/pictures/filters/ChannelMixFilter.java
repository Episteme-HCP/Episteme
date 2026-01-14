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
public class ChannelMixFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4578927872126740383L;

    /** DOCUMENT ME! */
    public int blueGreen;

    /** DOCUMENT ME! */
    public int redBlue;

    /** DOCUMENT ME! */
    public int greenRed;

    /** DOCUMENT ME! */
    public int intoR;

    /** DOCUMENT ME! */
    public int intoG;

    /** DOCUMENT ME! */
    public int intoB;

/**
     * Creates a new ChannelMixFilter object.
     */
    public ChannelMixFilter() {
        canFilterIndexColorModel = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param blueGreen DOCUMENT ME!
     */
    public void setBlueGreen(int blueGreen) {
        this.blueGreen = blueGreen;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBlueGreen() {
        return blueGreen;
    }

    /**
     * DOCUMENT ME!
     *
     * @param redBlue DOCUMENT ME!
     */
    public void setRedBlue(int redBlue) {
        this.redBlue = redBlue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRedBlue() {
        return redBlue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param greenRed DOCUMENT ME!
     */
    public void setGreenRed(int greenRed) {
        this.greenRed = greenRed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGreenRed() {
        return greenRed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param intoR DOCUMENT ME!
     */
    public void setIntoR(int intoR) {
        this.intoR = intoR;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIntoR() {
        return intoR;
    }

    /**
     * DOCUMENT ME!
     *
     * @param intoG DOCUMENT ME!
     */
    public void setIntoG(int intoG) {
        this.intoG = intoG;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIntoG() {
        return intoG;
    }

    /**
     * DOCUMENT ME!
     *
     * @param intoB DOCUMENT ME!
     */
    public void setIntoB(int intoB) {
        this.intoB = intoB;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIntoB() {
        return intoB;
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
        int nr = PixelUtils.clamp((((intoR * ((blueGreen * g) +
                ((255 - blueGreen) * b))) / 255) + ((255 - intoR) * r)) / 255);
        int ng = PixelUtils.clamp((((intoG * ((redBlue * b) +
                ((255 - redBlue) * r))) / 255) + ((255 - intoG) * g)) / 255);
        int nb = PixelUtils.clamp((((intoB * ((greenRed * r) +
                ((255 - greenRed) * g))) / 255) + ((255 - intoB) * b)) / 255);

        return a | (nr << 16) | (ng << 8) | nb;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Mix Channels...";
    }
}
