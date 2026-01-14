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
 * A class to emboss an image.
 */
public class EmbossFilter extends WholeImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1923710925649589104L;

    /** DOCUMENT ME! */
    private final static float pixelScale = 255.9f;

    /** DOCUMENT ME! */
    private float azimuth = (135.0f * ImageMath.PI) / 180.0f;

    /** DOCUMENT ME! */
    private float elevation = (30.0f * ImageMath.PI) / 180f;

    /** DOCUMENT ME! */
    private boolean emboss = false;

    /** DOCUMENT ME! */
    private float width45 = 3.0f;

/**
     * Creates a new EmbossFilter object.
     */
    public EmbossFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param azimuth DOCUMENT ME!
     */
    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAzimuth() {
        return azimuth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param elevation DOCUMENT ME!
     */
    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getElevation() {
        return elevation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bumpHeight DOCUMENT ME!
     */
    public void setBumpHeight(float bumpHeight) {
        this.width45 = 3 * bumpHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBumpHeight() {
        return width45 / 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param emboss DOCUMENT ME!
     */
    public void setEmboss(boolean emboss) {
        this.emboss = emboss;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getEmboss() {
        return emboss;
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

        int width = transformedSpace.width;
        int height = transformedSpace.height;
        int index = 0;
        int[] outPixels = new int[width * height];

        int[] bumpPixels;
        int bumpMapWidth;
        int bumpMapHeight;

        bumpMapWidth = width;
        bumpMapHeight = height;
        bumpPixels = new int[bumpMapWidth * bumpMapHeight];

        for (int i = 0; i < inPixels.length; i++)
            bumpPixels[i] = PixelUtils.brightness(inPixels[i]);

        int Nx;
        int Ny;
        int Nz;
        int Lx;
        int Ly;
        int Lz;
        int Nz2;
        int NzLz;
        int NdotL;
        int shade;
        int background;

        Lx = (int) (Math.cos(azimuth) * Math.cos(elevation) * pixelScale);
        Ly = (int) (Math.sin(azimuth) * Math.cos(elevation) * pixelScale);
        Lz = (int) (Math.sin(elevation) * pixelScale);

        Nz = (int) ((6 * 255) / width45);
        Nz2 = Nz * Nz;
        NzLz = Nz * Lz;

        background = Lz;

        int bumpIndex = 0;

        for (int y = 0; y < height; y++, bumpIndex += bumpMapWidth) {
            int s1 = bumpIndex;
            int s2 = s1 + bumpMapWidth;
            int s3 = s2 + bumpMapWidth;

            for (int x = 0; x < width; x++, s1++, s2++, s3++) {
                if ((y != 0) && (y < (height - 2)) && (x != 0) &&
                        (x < (width - 2))) {
                    Nx = (bumpPixels[s1 - 1] + bumpPixels[s2 - 1] +
                        bumpPixels[s3 - 1]) - bumpPixels[s1 + 1] -
                        bumpPixels[s2 + 1] - bumpPixels[s3 + 1];
                    Ny = (bumpPixels[s3 - 1] + bumpPixels[s3] +
                        bumpPixels[s3 + 1]) - bumpPixels[s1 - 1] -
                        bumpPixels[s1] - bumpPixels[s1 + 1];

                    if ((Nx == 0) && (Ny == 0)) {
                        shade = background;
                    } else if ((NdotL = (Nx * Lx) + (Ny * Ly) + NzLz) < 0) {
                        shade = 0;
                    } else {
                        shade = (int) (NdotL / Math.sqrt((Nx * Nx) + (Ny * Ny) +
                                Nz2));
                    }
                } else {
                    shade = background;
                }

                if (emboss) {
                    int rgb = inPixels[index];
                    int a = rgb & 0xff000000;
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    r = (r * shade) >> 8;
                    g = (g * shade) >> 8;
                    b = (b * shade) >> 8;
                    outPixels[index++] = a | (r << 16) | (g << 8) | b;
                } else {
                    outPixels[index++] = 0xff000000 | (shade << 16) |
                        (shade << 8) | shade;
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
        return "Stylize/Emboss...";
    }
}
