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

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class WaterFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 8789236343162990941L;

    /** DOCUMENT ME! */
    private float wavelength = 16;

    /** DOCUMENT ME! */
    private float amplitude = 10;

    /** DOCUMENT ME! */
    private float phase = 0;

    /** DOCUMENT ME! */
    private boolean antialias = true;

/**
     * Creates a new WaterFilter object.
     */
    public WaterFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param wavelength DOCUMENT ME!
     */
    public void setWavelength(float wavelength) {
        this.wavelength = wavelength;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWavelength() {
        return wavelength;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amplitude DOCUMENT ME!
     */
    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAmplitude() {
        return amplitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @param phase DOCUMENT ME!
     */
    public void setPhase(float phase) {
        this.phase = phase;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPhase() {
        return phase;
    }

    /**
     * DOCUMENT ME!
     *
     * @param antialias DOCUMENT ME!
     */
    public void setAntialias(boolean antialias) {
        this.antialias = antialias;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getAntialias() {
        return antialias;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean inside(int v, int a, int b) {
        return (a <= v) && (v <= b);
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
        int index = 0;
        int centreX;
        int centreY;

        centreX = width / 2;
        centreY = height / 2;

        int[] outPixels = new int[width * height];
        int[] a = new int[4];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb;
                float dx = x - centreX;
                float dy = y - centreY;
                float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
                float amount = amplitude * (float) Math.sin((distance / wavelength * ImageMath.TWO_PI) +
                        phase);
                float tx = centreX + dx + amount;
                float ty = centreY + dy + amount;

                if (true) {
                    tx = ImageMath.clamp(tx, 0, width - 1);
                    ty = ImageMath.clamp(ty, 0, height - 1);
                }

                if (antialias) {
                    int nx = (int) tx;
                    int ny = (int) ty;
                    boolean xl = inside(nx, 0, width - 1);
                    boolean yt = inside(ny, 0, height - 1);
                    boolean xr = inside(nx, 0, width - 2);
                    boolean yb = inside(ny, 0, height - 2);
                    int i = (ny * width) + nx;

                    if (xl && yt) {
                        a[0] = inPixels[i];
                    } else {
                        a[0] = 0xff000000;
                    }

                    if (xr && yt) {
                        a[1] = inPixels[i + 1];
                    } else {
                        a[1] = 0xff000000;
                    }

                    if (xl && yb) {
                        a[2] = inPixels[i + width];
                    } else {
                        a[2] = 0xff000000;
                    }

                    if (xr && yb) {
                        a[3] = inPixels[i + width + 1];
                    } else {
                        a[3] = 0xff000000;
                    }

                    tx = ImageMath.mod(tx, 1.0f);
                    ty = ImageMath.mod(ty, 1.0f);
                    rgb = ImageMath.bilinearInterpolate(tx, ty, a);
                } else {
                    int nx = ImageMath.clamp((int) (tx + 0.5f), 0, width - 1);
                    int ny = ImageMath.clamp((int) (ty + 0.5f), 0, height - 1);
                    rgb = inPixels[(ny * width) + nx];
                }

                outPixels[index++] = rgb;
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
        return "Distort/Water Ripples...";
    }
}
