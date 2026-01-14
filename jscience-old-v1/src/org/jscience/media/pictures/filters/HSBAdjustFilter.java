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
import java.awt.image.RGBImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class HSBAdjustFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4578927872126740383L;

    /** DOCUMENT ME! */
    public float hFactor;

    /** DOCUMENT ME! */
    public float sFactor;

    /** DOCUMENT ME! */
    public float bFactor;

    /** DOCUMENT ME! */
    private float[] hsb = new float[3];

/**
     * Creates a new HSBAdjustFilter object.
     */
    public HSBAdjustFilter() {
        this(0, 0, 0);
    }

/**
     * Creates a new HSBAdjustFilter object.
     *
     * @param r DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public HSBAdjustFilter(float r, float g, float b) {
        hFactor = r;
        sFactor = g;
        bFactor = b;
        canFilterIndexColorModel = true;
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
        Color.RGBtoHSB(r, g, b, hsb);
        hsb[0] += hFactor;

        while (hsb[0] < 0)
            hsb[0] += (Math.PI * 2);

        hsb[1] += sFactor;

        if (hsb[1] < 0) {
            hsb[1] = 0;
        } else if (hsb[1] > 1.0) {
            hsb[1] = 1.0f;
        }

        hsb[2] += bFactor;

        if (hsb[2] < 0) {
            hsb[2] = 0;
        } else if (hsb[2] > 1.0) {
            hsb[2] = 1.0f;
        }

        rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

        return a | (rgb & 0xffffff);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Adjust HSB...";
    }
}
