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

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class NoiseFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1970783813893879484L;

    /** DOCUMENT ME! */
    private int amount = 25;

    /** DOCUMENT ME! */
    private boolean gaussian = false;

    /** DOCUMENT ME! */
    private boolean monochrome = false;

    /** DOCUMENT ME! */
    private Random randomNumbers = new Random();

/**
     * Creates a new NoiseFilter object.
     */
    public NoiseFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gaussian DOCUMENT ME!
     */
    public void setGaussian(boolean gaussian) {
        this.gaussian = gaussian;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getGaussian() {
        return gaussian;
    }

    /**
     * DOCUMENT ME!
     *
     * @param monochrome DOCUMENT ME!
     */
    public void setMonochrome(boolean monochrome) {
        this.monochrome = monochrome;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getMonochrome() {
        return monochrome;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int random(int x) {
        x += (int) ((gaussian ? randomNumbers.nextGaussian()
                              : ((2 * randomNumbers.nextFloat()) - 1)) * amount);

        if (x < 0) {
            x = 0;
        } else if (x > 0xff) {
            x = 0xff;
        }

        return x;
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

        if (monochrome) {
            int n = (int) ((gaussian ? randomNumbers.nextGaussian()
                                     : ((2 * randomNumbers.nextFloat()) - 1)) * amount);
            r = PixelUtils.clamp(r + n);
            g = PixelUtils.clamp(g + n);
            b = PixelUtils.clamp(b + n);
        } else {
            r = random(r);
            g = random(g);
            b = random(b);
        }

        return a | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Add Noise...";
    }
}
