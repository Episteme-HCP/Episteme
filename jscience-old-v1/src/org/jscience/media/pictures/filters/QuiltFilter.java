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
public class QuiltFilter extends WholeImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    private Random randomGenerator;

    /** DOCUMENT ME! */
    private long seed = 567;

    /** DOCUMENT ME! */
    private int iterations = 25000;

    /** DOCUMENT ME! */
    private float a = -0.59f;

    /** DOCUMENT ME! */
    private float b = 0.2f;

    /** DOCUMENT ME! */
    private float c = 0.1f;

    /** DOCUMENT ME! */
    private float d = 0;

    /** DOCUMENT ME! */
    private int k = 0;

    /** DOCUMENT ME! */
    private Colormap colormap = new LinearColormap();

/**
     * Creates a new QuiltFilter object.
     */
    public QuiltFilter() {
        randomGenerator = new Random();
    }

    /**
     * DOCUMENT ME!
     */
    public void randomize() {
        seed = new Date().getTime();
        randomGenerator.setSeed(seed);
        a = randomGenerator.nextFloat();
        b = randomGenerator.nextFloat();
        c = randomGenerator.nextFloat();
        d = randomGenerator.nextFloat();
        k = (randomGenerator.nextInt() % 20) - 10;
    }

    /**
     * DOCUMENT ME!
     *
     * @param iterations DOCUMENT ME!
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void setA(float a) {
        this.a = a;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getA() {
        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setB(float b) {
        this.b = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getB() {
        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setC(float c) {
        this.c = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getC() {
        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setD(float d) {
        this.d = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getD() {
        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     */
    public void setK(int k) {
        this.k = k;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK() {
        return k;
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

        int i = 0;
        int max = 0;

        float x = 0.1f;
        float y = 0.3f;

        for (int n = 0; n < 20; n++) {
            float mx = ImageMath.PI * x;
            float my = ImageMath.PI * y;
            float smx2 = (float) Math.sin(2 * mx);
            float smy2 = (float) Math.sin(2 * my);
            float x1 = (float) ((a * smx2) + (b * smx2 * Math.cos(2 * my)) +
                (c * Math.sin(4 * mx)) +
                (d * Math.sin(6 * mx) * Math.cos(4 * my)) + (k * x));
            x1 = (x1 >= 0) ? (x1 - (int) x1) : (x1 - (int) x1 + 1);

            float y1 = (float) ((a * smy2) + (b * smy2 * Math.cos(2 * mx)) +
                (c * Math.sin(4 * my)) +
                (d * Math.sin(6 * my) * Math.cos(4 * mx)) + (k * y));
            y1 = (y1 >= 0) ? (y1 - (int) y1) : (y1 - (int) y1 + 1);
            x = x1;
            y = y1;
        }

        for (int n = 0; n < iterations; n++) {
            float mx = ImageMath.PI * x;
            float my = ImageMath.PI * y;
            float x1 = (float) ((a * Math.sin(2 * mx)) +
                (b * Math.sin(2 * mx) * Math.cos(2 * my)) +
                (c * Math.sin(4 * mx)) +
                (d * Math.sin(6 * mx) * Math.cos(4 * my)) + (k * x));
            x1 = (x1 >= 0) ? (x1 - (int) x1) : (x1 - (int) x1 + 1);

            float y1 = (float) ((a * Math.sin(2 * my)) +
                (b * Math.sin(2 * my) * Math.cos(2 * mx)) +
                (c * Math.sin(4 * my)) +
                (d * Math.sin(6 * my) * Math.cos(4 * mx)) + (k * y));
            y1 = (y1 >= 0) ? (y1 - (int) y1) : (y1 - (int) y1 + 1);
            x = x1;
            y = y1;

            int ix = (int) (width * x);
            int iy = (int) (height * y);

            if ((ix >= 0) && (ix < width) && (iy >= 0) && (iy < height)) {
                int t = outPixels[(width * iy) + ix]++;

                if (t > max) {
                    max = t;
                }
            }
        }

        if (colormap != null) {
            int index = 0;

            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    outPixels[index] = colormap.getColor(outPixels[index] / (float) max);
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
        return "Texture/Chaotic Quilt...";
    }
}
