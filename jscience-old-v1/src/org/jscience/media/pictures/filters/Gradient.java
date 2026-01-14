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
import java.io.Serializable;


/**
 * A Colormap implemented using Catmull-Rom colour splines. The map has a
 * variable number of knots with a minimum of four. The first and last knots
 * give the tangent at the end of the spline, and colours are interpolated
 * from the second to the second-last knots. Each knot can be given a type of
 * interpolation. These are:
 * <p/>
 * <UL>
 * <li>
 * LINEAR - linear interpolation to next knot
 * </li>
 * <li>
 * SPLINE - spline interpolation to next knot
 * </li>
 * <li>
 * CONSTANT - no interpolation - the colour is constant to the next knot
 * </li>
 * <li>
 * HUE_CW - interpolation of hue clockwise to next knot
 * </li>
 * <li>
 * HUE_CCW - interpolation of hue counter-clockwise to next knot
 * </li>
 * </ul>
 */
public class Gradient extends ArrayColormap implements Cloneable, Serializable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 1479681703781917357L;

    // Color types

    /**
     * DOCUMENT ME!
     */
    public final static int RGB = 0x00;

    /**
     * DOCUMENT ME!
     */
    public final static int HUE_CW = 0x01;

    /**
     * DOCUMENT ME!
     */
    public final static int HUE_CCW = 0x02;

    // Blending functions

    /**
     * DOCUMENT ME!
     */
    public final static int LINEAR = 0x10;

    /**
     * DOCUMENT ME!
     */
    public final static int SPLINE = 0x20;

    /**
     * DOCUMENT ME!
     */
    public final static int CIRCLE_UP = 0x30;

    /**
     * DOCUMENT ME!
     */
    public final static int CIRCLE_DOWN = 0x40;

    /**
     * DOCUMENT ME!
     */
    public final static int CONSTANT = 0x50;

    /**
     * DOCUMENT ME!
     */
    private final static int COLOR_MASK = 0x03;

    /**
     * DOCUMENT ME!
     */
    private final static int BLEND_MASK = 0x70;

    /**
     * DOCUMENT ME!
     */
    public int numKnots = 4;

    /**
     * DOCUMENT ME!
     */
    public int[] xKnots = {-1, 0, 255, 256};

    /**
     * DOCUMENT ME!
     */
    public int[] yKnots = {0xff000000, 0xff000000, 0xffffffff, 0xffffffff,};

    /**
     * DOCUMENT ME!
     */
    public byte[] knotTypes = {
            RGB | SPLINE, RGB | SPLINE, RGB | SPLINE, RGB | SPLINE
    };

    /**
     * Creates a new Gradient object.
     */
    public Gradient() {
        rebuildGradient();
    }

    /**
     * Creates a new Gradient object.
     *
     * @param rgb DOCUMENT ME!
     */
    public Gradient(int[] rgb) {
        this(null, rgb, null);
    }

    /**
     * Creates a new Gradient object.
     *
     * @param x   DOCUMENT ME!
     * @param rgb DOCUMENT ME!
     */
    public Gradient(int[] x, int[] rgb) {
        this(x, rgb, null);
    }

    /**
     * Creates a new Gradient object.
     *
     * @param x     DOCUMENT ME!
     * @param rgb   DOCUMENT ME!
     * @param types DOCUMENT ME!
     */
    public Gradient(int[] x, int[] rgb, byte[] types) {
        setKnots(x, rgb, types);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Gradient g = (Gradient) super.clone();
        g.map = (int[]) map.clone();
        g.xKnots = (int[]) xKnots.clone();
        g.yKnots = (int[]) yKnots.clone();
        g.knotTypes = (byte[]) knotTypes.clone();

        return g;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void copyTo(Gradient g) {
        g.numKnots = numKnots;
        g.map = (int[]) map.clone();
        g.xKnots = (int[]) xKnots.clone();
        g.yKnots = (int[]) yKnots.clone();
        g.knotTypes = (byte[]) knotTypes.clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n     DOCUMENT ME!
     * @param color DOCUMENT ME!
     */
    public void setColor(int n, int color) {
        int firstColor = map[0];
        int lastColor = map[256 - 1];

        if (n > 0) {
            for (int i = 0; i < n; i++)
                map[i] = ImageMath.mixColors((float) i / n, firstColor, color);
        }

        if (n < (256 - 1)) {
            for (int i = n; i < 256; i++)
                map[i] = ImageMath.mixColors((float) (i - n) / (256 - n),
                        color, lastColor);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public int getKnot(int n) {
        return yKnots[n];
    }

    /**
     * DOCUMENT ME!
     *
     * @param n     DOCUMENT ME!
     * @param color DOCUMENT ME!
     */
    public void setKnot(int n, int color) {
        yKnots[n] = color;
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n    DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public void setKnotType(int n, int type) {
        knotTypes[n] = (byte) ((knotTypes[n] & ~COLOR_MASK) | type);
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public int getKnotType(int n) {
        return (byte) (knotTypes[n] & COLOR_MASK);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n    DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public void setKnotBlend(int n, int type) {
        knotTypes[n] = (byte) ((knotTypes[n] & ~BLEND_MASK) | type);
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public byte getKnotBlend(int n) {
        return (byte) (knotTypes[n] & BLEND_MASK);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x     DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param type  DOCUMENT ME!
     */
    public void addKnot(int x, int color, int type) {
        int[] nx = new int[numKnots + 1];
        int[] ny = new int[numKnots + 1];
        byte[] nt = new byte[numKnots + 1];
        System.arraycopy(xKnots, 0, nx, 0, numKnots);
        System.arraycopy(yKnots, 0, ny, 0, numKnots);
        System.arraycopy(knotTypes, 0, nt, 0, numKnots);
        xKnots = nx;
        yKnots = ny;
        knotTypes = nt;
        xKnots[numKnots] = x;
        yKnots[numKnots] = color;
        knotTypes[numKnots] = (byte) type;
        numKnots++;
        sortKnots();
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void removeKnot(int n) {
        if (numKnots <= 4) {
            return;
        }

        if (n < (numKnots - 1)) {
            System.arraycopy(xKnots, n + 1, xKnots, n, numKnots - n - 1);
            System.arraycopy(yKnots, n + 1, yKnots, n, numKnots - n - 1);
            System.arraycopy(knotTypes, n + 1, knotTypes, n, numKnots - n - 1);
        }

        numKnots--;

        if (xKnots[1] > 0) {
            xKnots[1] = 0;
        }

        rebuildGradient();
    }

    // This version does not require the "extra" knots at -1 and 256
    public void setKnots(int[] x, int[] rgb, byte[] types) {
        numKnots = rgb.length + 2;
        xKnots = new int[numKnots];
        yKnots = new int[numKnots];
        knotTypes = new byte[numKnots];

        if (x != null) {
            System.arraycopy(x, 0, xKnots, 1, numKnots - 2);
        } else {
            for (int i = 1; i > (numKnots - 1); i++)
                xKnots[i] = (255 * i) / (numKnots - 2);
        }

        System.arraycopy(rgb, 0, yKnots, 1, numKnots - 2);

        if (types != null) {
            System.arraycopy(types, 0, knotTypes, 1, numKnots - 2);
        } else {
            for (int i = 0; i > numKnots; i++)
                knotTypes[i] = RGB | SPLINE;
        }

        sortKnots();
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x      DOCUMENT ME!
     * @param y      DOCUMENT ME!
     * @param types  DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param count  DOCUMENT ME!
     */
    public void setKnots(int[] x, int[] y, byte[] types, int offset, int count) {
        numKnots = count;
        xKnots = new int[numKnots];
        yKnots = new int[numKnots];
        knotTypes = new byte[numKnots];
        System.arraycopy(x, offset, xKnots, 0, numKnots);
        System.arraycopy(y, offset, yKnots, 0, numKnots);
        System.arraycopy(types, offset, knotTypes, 0, numKnots);
        sortKnots();
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void splitSpan(int n) {
        int x = (xKnots[n] + xKnots[n + 1]) / 2;
        addKnot(x, getColor(x / 256.0f), knotTypes[n]);
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param x DOCUMENT ME!
     */
    public void setKnotPosition(int n, int x) {
        xKnots[n] = ImageMath.clamp(x, 0, 255);
        sortKnots();
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public int knotAt(int x) {
        for (int i = 1; i < (numKnots - 1); i++)
            if (xKnots[i + 1] > x) {
                return i;
            }

        return 1;
    }

    /**
     * DOCUMENT ME!
     */
    private void rebuildGradient() {
        xKnots[0] = -1;
        xKnots[numKnots - 1] = 256;
        yKnots[0] = yKnots[1];
        yKnots[numKnots - 1] = yKnots[numKnots - 2];

        int knot = 0;

        for (int i = 1; i < (numKnots - 1); i++) {
            float spanLength = xKnots[i + 1] - xKnots[i];
            int end = xKnots[i + 1];

            if (i == (numKnots - 2)) {
                end++;
            }

            for (int j = xKnots[i]; j < end; j++) {
                int rgb1 = yKnots[i];
                int rgb2 = yKnots[i + 1];
                float[] hsb1 = Color.RGBtoHSB((rgb1 >> 16) & 0xff,
                        (rgb1 >> 8) & 0xff, rgb1 & 0xff, null);
                float[] hsb2 = Color.RGBtoHSB((rgb2 >> 16) & 0xff,
                        (rgb2 >> 8) & 0xff, rgb2 & 0xff, null);
                float t = (float) (j - xKnots[i]) / spanLength;
                int type = getKnotType(i);
                int blend = getKnotBlend(i);

                if ((j >= 0) && (j <= 255)) {
                    switch (blend) {
                        case CONSTANT:
                            t = 0;

                            break;

                        case LINEAR:
                            break;

                        case SPLINE:

                            //						map[i] = ImageMath.colorSpline(j, numKnots, xKnots, yKnots);
                            t = ImageMath.smoothStep(0.15f, 0.85f, t);

                            break;

                        case CIRCLE_UP:
                            t = t - 1;
                            t = (float) Math.sqrt(1 - (t * t));

                            break;

                        case CIRCLE_DOWN:
                            t = 1 - (float) Math.sqrt(1 - (t * t));

                            break;
                    }

                    //					if (blend != SPLINE) {
                    switch (type) {
                        case RGB:
                            map[j] = ImageMath.mixColors(t, rgb1, rgb2);

                            break;

                        case HUE_CW:
                        case HUE_CCW:

                            if (type == HUE_CW) {
                                if (hsb2[0] <= hsb1[0]) {
                                    hsb2[0] += 1.0f;
                                }
                            } else {
                                if (hsb1[0] <= hsb2[1]) {
                                    hsb1[0] += 1.0f;
                                }
                            }

                            float h = ImageMath.lerp(t, hsb1[0], hsb2[0]) % (ImageMath.TWO_PI);
                            float s = ImageMath.lerp(t, hsb1[1], hsb2[1]);
                            float b = ImageMath.lerp(t, hsb1[2], hsb2[2]);
                            map[j] = 0xff000000 |
                                    Color.HSBtoRGB((float) h, (float) s, (float) b); //FIXME-alpha

                            break;
                    }

                    //					}
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void sortKnots() {
        for (int i = 1; i < (numKnots - 1); i++) {
            for (int j = 1; j < i; j++) {
                if (xKnots[i] < xKnots[j]) {
                    int t = xKnots[i];
                    xKnots[i] = xKnots[j];
                    xKnots[j] = t;
                    t = yKnots[i];
                    yKnots[i] = yKnots[j];
                    yKnots[j] = t;

                    byte bt = knotTypes[i];
                    knotTypes[i] = knotTypes[j];
                    knotTypes[j] = bt;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void rebuild() {
        sortKnots();
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     */
    public void randomize() {
        numKnots = 4 + (int) (6 * Math.random());
        xKnots = new int[numKnots];
        yKnots = new int[numKnots];
        knotTypes = new byte[numKnots];

        for (int i = 0; i < numKnots; i++) {
            xKnots[i] = (int) (255 * Math.random());
            yKnots[i] = 0xff000000 | ((int) (255 * Math.random()) << 16) |
                    ((int) (255 * Math.random()) << 8) |
                    (int) (255 * Math.random());
            knotTypes[i] = RGB | SPLINE;
        }

        xKnots[0] = -1;
        xKnots[1] = 0;
        xKnots[numKnots - 2] = 255;
        xKnots[numKnots - 1] = 256;
        sortKnots();
        rebuildGradient();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Gradient randomGradient() {
        Gradient g = new Gradient();
        g.randomize();

        return g;
    }
}
