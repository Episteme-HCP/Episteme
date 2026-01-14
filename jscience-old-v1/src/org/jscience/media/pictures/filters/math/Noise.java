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

package org.jscience.media.pictures.filters.math;

import java.util.Random;


/**
 * Perlin Noise functions
 */
public class Noise implements Function1D, Function2D, Function3D {
    /** DOCUMENT ME! */
    private static Random randomGenerator = new Random();

    /** DOCUMENT ME! */
    private final static int B = 0x100;

    /** DOCUMENT ME! */
    private final static int BM = 0xff;

    /** DOCUMENT ME! */
    private final static int N = 0x1000;

    /** DOCUMENT ME! */
    static int[] p = new int[B + B + 2];

    /** DOCUMENT ME! */
    static float[][] g3 = new float[B + B + 2][3];

    /** DOCUMENT ME! */
    static float[][] g2 = new float[B + B + 2][2];

    /** DOCUMENT ME! */
    static float[] g1 = new float[B + B + 2];

    /** DOCUMENT ME! */
    static boolean start = true;

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float x) {
        return noise1(x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float x, float y) {
        return noise2(x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float x, float y, float z) {
        return noise3(x, y, z);
    }

    /**
     * Compute turbulence using Perlin noise.
     *
     * @param x the x value
     * @param y the y value
     * @param octaves number of octaves of turbulence
     *
     * @return turbulence value at (x,y)
     */
    public static float turbulence2(float x, float y, float octaves) {
        float t = 0.0f;

        for (float f = 1.0f; f <= octaves; f *= 2)
            t += (Math.abs(noise2(f * x, f * y)) / f);

        return t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static float sCurve(float t) {
        return t * t * (3.0f - (2.0f * t));
    }

    /**
     * Compute 1-dimensional Perlin noise.
     *
     * @param x the x value
     *
     * @return noise value at x in the range -1..1
     */
    public static float noise1(float x) {
        int bx0;
        int bx1;
        float rx0;
        float rx1;
        float sx;
        float t;
        float u;
        float v;

        if (start) {
            start = false;
            init();
        }

        t = x + N;
        bx0 = ((int) t) & BM;
        bx1 = (bx0 + 1) & BM;
        rx0 = t - (int) t;
        rx1 = rx0 - 1.0f;

        sx = sCurve(rx0);

        u = rx0 * g1[p[bx0]];
        v = rx1 * g1[p[bx1]];

        return 2.3f * lerp(sx, u, v);
    }

    /**
     * Compute 2-dimensional Perlin noise.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     *
     * @return noise value at (x,y)
     */
    public static float noise2(float x, float y) {
        int bx0;
        int bx1;
        int by0;
        int by1;
        int b00;
        int b10;
        int b01;
        int b11;
        float rx0;
        float rx1;
        float ry0;
        float ry1;
        float[] q;
        float sx;
        float sy;
        float a;
        float b;
        float t;
        float u;
        float v;
        int i;
        int j;

        if (start) {
            start = false;
            init();
        }

        t = x + N;
        bx0 = ((int) t) & BM;
        bx1 = (bx0 + 1) & BM;
        rx0 = t - (int) t;
        rx1 = rx0 - 1.0f;

        t = y + N;
        by0 = ((int) t) & BM;
        by1 = (by0 + 1) & BM;
        ry0 = t - (int) t;
        ry1 = ry0 - 1.0f;

        i = p[bx0];
        j = p[bx1];

        b00 = p[i + by0];
        b10 = p[j + by0];
        b01 = p[i + by1];
        b11 = p[j + by1];

        sx = sCurve(rx0);
        sy = sCurve(ry0);

        q = g2[b00];
        u = (rx0 * q[0]) + (ry0 * q[1]);
        q = g2[b10];
        v = (rx1 * q[0]) + (ry0 * q[1]);
        a = lerp(sx, u, v);

        q = g2[b01];
        u = (rx0 * q[0]) + (ry1 * q[1]);
        q = g2[b11];
        v = (rx1 * q[0]) + (ry1 * q[1]);
        b = lerp(sx, u, v);

        return 1.5f * lerp(sy, a, b); //FIXME
    }

    /**
     * Compute 3-dimensional Perlin noise.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param y the y coordinate
     *
     * @return noise value at (x,y,z)
     */
    public static float noise3(float x, float y, float z) {
        int bx0;
        int bx1;
        int by0;
        int by1;
        int bz0;
        int bz1;
        int b00;
        int b10;
        int b01;
        int b11;
        float rx0;
        float rx1;
        float ry0;
        float ry1;
        float rz0;
        float rz1;
        float[] q;
        float sy;
        float sz;
        float a;
        float b;
        float c;
        float d;
        float t;
        float u;
        float v;
        int i;
        int j;

        if (start) {
            start = false;
            init();
        }

        t = x + N;
        bx0 = ((int) t) & BM;
        bx1 = (bx0 + 1) & BM;
        rx0 = t - (int) t;
        rx1 = rx0 - 1.0f;

        t = y + N;
        by0 = ((int) t) & BM;
        by1 = (by0 + 1) & BM;
        ry0 = t - (int) t;
        ry1 = ry0 - 1.0f;

        t = z + N;
        bz0 = ((int) t) & BM;
        bz1 = (bz0 + 1) & BM;
        rz0 = t - (int) t;
        rz1 = rz0 - 1.0f;

        i = p[bx0];
        j = p[bx1];

        b00 = p[i + by0];
        b10 = p[j + by0];
        b01 = p[i + by1];
        b11 = p[j + by1];

        t = sCurve(rx0);
        sy = sCurve(ry0);
        sz = sCurve(rz0);

        q = g3[b00 + bz0];
        u = (rx0 * q[0]) + (ry0 * q[1]) + (rz0 * q[2]);
        q = g3[b10 + bz0];
        v = (rx1 * q[0]) + (ry0 * q[1]) + (rz0 * q[2]);
        a = lerp(t, u, v);

        q = g3[b01 + bz0];
        u = (rx0 * q[0]) + (ry1 * q[1]) + (rz0 * q[2]);
        q = g3[b11 + bz0];
        v = (rx1 * q[0]) + (ry1 * q[1]) + (rz0 * q[2]);
        b = lerp(t, u, v);

        c = lerp(sy, a, b);

        q = g3[b00 + bz1];
        u = (rx0 * q[0]) + (ry0 * q[1]) + (rz1 * q[2]);
        q = g3[b10 + bz1];
        v = (rx1 * q[0]) + (ry0 * q[1]) + (rz1 * q[2]);
        a = lerp(t, u, v);

        q = g3[b01 + bz1];
        u = (rx0 * q[0]) + (ry1 * q[1]) + (rz1 * q[2]);
        q = g3[b11 + bz1];
        v = (rx1 * q[0]) + (ry1 * q[1]) + (rz1 * q[2]);
        b = lerp(t, u, v);

        d = lerp(sy, a, b);

        return 1.5f * lerp(sz, c, d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float lerp(float t, float a, float b) {
        return a + (t * (b - a));
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    private static void normalize2(float[] v) {
        float s = (float) Math.sqrt((v[0] * v[0]) + (v[1] * v[1]));
        v[0] = v[0] / s;
        v[1] = v[1] / s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    static void normalize3(float[] v) {
        float s = (float) Math.sqrt((v[0] * v[0]) + (v[1] * v[1]) +
                (v[2] * v[2]));
        v[0] = v[0] / s;
        v[1] = v[1] / s;
        v[2] = v[2] / s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int random() {
        return randomGenerator.nextInt() & 0x7fffffff;
    }

    /**
     * DOCUMENT ME!
     */
    private static void init() {
        int i;
        int j;
        int k;

        for (i = 0; i < B; i++) {
            p[i] = i;

            g1[i] = (float) ((random() % (B + B)) - B) / B;

            for (j = 0; j < 2; j++)
                g2[i][j] = (float) ((random() % (B + B)) - B) / B;

            normalize2(g2[i]);

            for (j = 0; j < 3; j++)
                g3[i][j] = (float) ((random() % (B + B)) - B) / B;

            normalize3(g3[i]);
        }

        for (i = B - 1; i >= 0; i--) {
            k = p[i];
            p[i] = p[j = random() % B];
            p[j] = k;
        }

        for (i = 0; i < (B + 2); i++) {
            p[B + i] = p[i];
            g1[B + i] = g1[i];

            for (j = 0; j < 2; j++)
                g2[B + i][j] = g2[i][j];

            for (j = 0; j < 3; j++)
                g3[B + i][j] = g3[i][j];
        }
    }

    /**
     * Returns the minimum and maximum of a number of random values of
     * the given function. This is useful for making some stab at normalising
     * the function.
     *
     * @param f DOCUMENT ME!
     * @param minmax DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float[] findRange(Function1D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }

        float min = 0;
        float max = 0;

        // Some random numbers here...
        for (float x = -100; x < 100; x += 1.27139) {
            float n = f.evaluate(x);
            min = Math.min(min, n);
            max = Math.max(max, n);
        }

        minmax[0] = min;
        minmax[1] = max;

        return minmax;
    }

    /**
     * Returns the minimum and maximum of a number of random values of
     * the given function. This is useful for making some stab at normalising
     * the function.
     *
     * @param f DOCUMENT ME!
     * @param minmax DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float[] findRange(Function2D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }

        float min = 0;
        float max = 0;

        // Some random numbers here...
        for (float y = -100; y < 100; y += 10.35173) {
            for (float x = -100; x < 100; x += 10.77139) {
                float n = f.evaluate(x, y);
                min = Math.min(min, n);
                max = Math.max(max, n);
            }
        }

        minmax[0] = min;
        minmax[1] = max;

        return minmax;
    }
}
