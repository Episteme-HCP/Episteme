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
 * A Filter which distorts an image by twisting it from the centre out. The
 * twisting is centred at the centre of the image and extends out to the
 * smallest of the width and height. Pixels outside this radius are
 * unaffected.
 */
public class TwirlFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1550445062822803342L;

    /** DOCUMENT ME! */
    private float angle = 0;

    /** DOCUMENT ME! */
    private float centreX = 0;

    /** DOCUMENT ME! */
    private float centreY = 0;

    /** DOCUMENT ME! */
    private float radius = 0;

    /** DOCUMENT ME! */
    private float radius2 = 0;

/**
     * Construct a TwirlFilter with no distortion.
     */
    public TwirlFilter() {
    }

    /**
     * Set the angle of twirl in radians. 0 means no distortion.
     *
     * @param angle the angle of twirl. This is the angle by which pixels at
     *        the nearest edge of the image will move.
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    /**
     * Get the angle of twist.
     *
     * @return the angle in radians.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);
        centreX = width / 2;
        centreY = height / 2;
        radius = Math.min(centreX, centreY);
        radius2 = radius * radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        float dx = x - centreX;
        float dy = y - centreY;
        float distance = (dx * dx) + (dy * dy);

        if (distance > radius2) {
            out[0] = x;
            out[1] = y;
        } else {
            distance = (float) Math.sqrt(distance);

            float a = (float) Math.atan2(dy, dx) +
                ((angle * (radius - distance)) / radius);
            out[0] = centreX + (distance * (float) Math.cos(a));
            out[1] = centreY + (distance * (float) Math.sin(a));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Twirl...";
    }
}
