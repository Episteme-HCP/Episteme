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

import java.util.Date;
import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class PlasmaFilter extends WholeImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6491871753122667752L;

    /** DOCUMENT ME! */
    public float turbulence = 1.0f;

    /** DOCUMENT ME! */
    private float scaling = 0.0f;

    /** DOCUMENT ME! */
    private Colormap colormap = new LinearColormap();

    /** DOCUMENT ME! */
    private Random randomGenerator;

    /** DOCUMENT ME! */
    private long seed = 567;

    /** DOCUMENT ME! */
    private boolean useImageColors = false;

/**
     * Creates a new PlasmaFilter object.
     */
    public PlasmaFilter() {
        randomGenerator = new Random();
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
     *
     * @param scaling DOCUMENT ME!
     */
    public void setScaling(float scaling) {
        this.scaling = scaling;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getScaling() {
        return scaling;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colormap DOCUMENT ME!
     */
    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Colormap getColormap() {
        return colormap;
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
     */
    public void randomize() {
        seed = new Date().getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int randomRGB(int x, int y) {
        if (useImageColors) {
            return inPixels[(y * originalSpace.width) + x];
        } else {
            int r = (int) (255 * randomGenerator.nextFloat());
            int g = (int) (255 * randomGenerator.nextFloat());
            int b = (int) (255 * randomGenerator.nextFloat());

            return 0xff000000 | (r << 16) | (g << 8) | b;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rgb DOCUMENT ME!
     * @param amount DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int displace(int rgb, float amount) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = PixelUtils.clamp(r +
                (int) (amount * (randomGenerator.nextFloat() - 0.5)));
        g = PixelUtils.clamp(g +
                (int) (amount * (randomGenerator.nextFloat() - 0.5)));
        b = PixelUtils.clamp(b +
                (int) (amount * (randomGenerator.nextFloat() - 0.5)));

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rgb1 DOCUMENT ME!
     * @param rgb2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int average(int rgb1, int rgb2) {
        return PixelUtils.combinePixels(rgb1, rgb2, PixelUtils.AVERAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param stride DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getPixel(int x, int y, int[] pixels, int stride) {
        return pixels[(y * stride) + x];
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param rgb DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param stride DOCUMENT ME!
     */
    private void putPixel(int x, int y, int rgb, int[] pixels, int stride) {
        pixels[(y * stride) + x] = rgb;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param stride DOCUMENT ME!
     * @param depth DOCUMENT ME!
     * @param scale DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean doPixel(int x1, int y1, int x2, int y2, int[] pixels,
        int stride, int depth, int scale) {
        int mx;
        int my;

        if (depth == 0) {
            int ml;
            int mr;
            int mt;
            int mb;
            int mm;
            int t;

            int tl = getPixel(x1, y1, pixels, stride);
            int bl = getPixel(x1, y2, pixels, stride);
            int tr = getPixel(x2, y1, pixels, stride);
            int br = getPixel(x2, y2, pixels, stride);

            float amount = (256.0f / (2.0f * scale)) * turbulence;

            mx = (x1 + x2) / 2;
            my = (y1 + y2) / 2;

            if ((mx == x1) && (mx == x2) && (my == y1) && (my == y2)) {
                return true;
            }

            if ((mx != x1) || (mx != x2)) {
                ml = average(tl, bl);
                ml = displace(ml, amount);
                putPixel(x1, my, ml, pixels, stride);

                if (x1 != x2) {
                    mr = average(tr, br);
                    mr = displace(mr, amount);
                    putPixel(x2, my, mr, pixels, stride);
                }
            }

            if ((my != y1) || (my != y2)) {
                if ((x1 != mx) || (my != y2)) {
                    mb = average(bl, br);
                    mb = displace(mb, amount);
                    putPixel(mx, y2, mb, pixels, stride);
                }

                if (y1 != y2) {
                    mt = average(tl, tr);
                    mt = displace(mt, amount);
                    putPixel(mx, y1, mt, pixels, stride);
                }
            }

            if ((y1 != y2) || (x1 != x2)) {
                mm = average(tl, br);
                t = average(bl, tr);
                mm = average(mm, t);
                mm = displace(mm, amount);
                putPixel(mx, my, mm, pixels, stride);
            }

            if (((x2 - x1) < 3) && ((y2 - y1) < 3)) {
                return false;
            }

            return true;
        }

        mx = (x1 + x2) / 2;
        my = (y1 + y2) / 2;

        doPixel(x1, y1, mx, my, pixels, stride, depth - 1, scale + 1);
        doPixel(x1, my, mx, y2, pixels, stride, depth - 1, scale + 1);
        doPixel(mx, y1, x2, my, pixels, stride, depth - 1, scale + 1);

        return doPixel(mx, my, x2, y2, pixels, stride, depth - 1, scale + 1);
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
        int[] outPixels = new int[width * height];

        randomGenerator.setSeed(seed);

        int w1 = width - 1;
        int h1 = height - 1;
        putPixel(0, 0, randomRGB(0, 0), outPixels, width);
        putPixel(w1, 0, randomRGB(w1, 0), outPixels, width);
        putPixel(0, h1, randomRGB(0, h1), outPixels, width);
        putPixel(w1, h1, randomRGB(w1, h1), outPixels, width);
        putPixel(w1 / 2, h1 / 2, randomRGB(w1 / 2, h1 / 2), outPixels, width);
        putPixel(0, h1 / 2, randomRGB(0, h1 / 2), outPixels, width);
        putPixel(w1, h1 / 2, randomRGB(w1, h1 / 2), outPixels, width);
        putPixel(w1 / 2, 0, randomRGB(w1 / 2, 0), outPixels, width);
        putPixel(w1 / 2, h1, randomRGB(w1 / 2, h1), outPixels, width);

        int depth = 1;

        while (doPixel(0, 0, width - 1, height - 1, outPixels, width, depth, 0))
            depth++;

        if (colormap != null) {
            int index = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    outPixels[index] = colormap.getColor((outPixels[index] &
                            0xff) / 255.0f);
                    index++;
                }
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
        return "Texture/Plasma...";
    }
}
