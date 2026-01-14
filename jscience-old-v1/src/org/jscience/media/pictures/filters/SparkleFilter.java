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
public class SparkleFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1692413049411710802L;

    /** DOCUMENT ME! */
    private int rays = 50;

    /** DOCUMENT ME! */
    private int radius = 25;

    /** DOCUMENT ME! */
    private int amount = 50;

    /** DOCUMENT ME! */
    private int color = 0xffffffff;

    /** DOCUMENT ME! */
    private int randomness = 25;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private int height;

    /** DOCUMENT ME! */
    private int centreX;

    /** DOCUMENT ME! */
    private int centreY;

    /** DOCUMENT ME! */
    private long seed = 371;

    /** DOCUMENT ME! */
    private float[] rayLengths;

    /** DOCUMENT ME! */
    private Random randomNumbers = new Random();

/**
     * Creates a new SparkleFilter object.
     */
    public SparkleFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param randomness DOCUMENT ME!
     */
    public void setRandomness(int randomness) {
        this.randomness = randomness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRandomness() {
        return randomness;
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
     * @param rays DOCUMENT ME!
     */
    public void setRays(int rays) {
        this.rays = rays;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRays() {
        return rays;
    }

    /**
     * DOCUMENT ME!
     *
     * @param radius DOCUMENT ME!
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRadius() {
        return radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        centreX = width / 2;
        centreY = height / 2;
        super.setDimensions(width, height);
        randomNumbers.setSeed(seed);
        rayLengths = new float[rays];

        for (int i = 0; i < rays; i++)
            rayLengths[i] = radius +
                (randomness / 100.0f * radius * (float) randomNumbers.nextGaussian());
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
        float dx = x - centreX;
        float dy = y - centreY;
        float distance = (dx * dx) + (dy * dy);
        float angle = (float) Math.atan2(dy, dx);
        float d = (angle + ImageMath.PI) / (ImageMath.TWO_PI) * rays;
        int i = (int) d;
        float f = d - i;

        if (radius != 0) {
            float length = ImageMath.lerp(f, rayLengths[i % rays],
                    rayLengths[(i + 1) % rays]);
            float g = (length * length) / (distance + 0.0001f);
            g = (float) Math.pow(g, (100 - amount) / 50.0);
            f -= 0.5f;

            //			f *= amount/50.0f;
            f = 1 - (f * f);
            f *= g;
        }

        f = ImageMath.clamp(f, 0, 1);

        return ImageMath.mixColors(f, rgb, color);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Sparkle...";
    }
}
