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

import org.jscience.media.pictures.filters.math.*;

import java.awt.*;


/**
 * A filter which distorts an image by rippling it in the X or Y
 * directions. The amplitude and wavelength of rippling can be specified as
 * well as whether pixels going off the edges are wrapped or not.
 */
public class RippleFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5101667633854087384L;

    /** DOCUMENT ME! */
    public final static int SINE = 0;

    /** DOCUMENT ME! */
    public final static int SAWTOOTH = 1;

    /** DOCUMENT ME! */
    public final static int TRIANGLE = 2;

    /** DOCUMENT ME! */
    public final static int NOISE = 3;

    /** DOCUMENT ME! */
    public float xAmplitude;

    /** DOCUMENT ME! */
    public float yAmplitude;

    /** DOCUMENT ME! */
    public float xWavelength;

    /** DOCUMENT ME! */
    public float yWavelength;

    /** DOCUMENT ME! */
    private int waveType;

/**
     * Construct a RIppleFIlter
     */
    public RippleFilter() {
        xAmplitude = 5.0f;
        yAmplitude = 0.0f;
        xWavelength = yWavelength = 16.0f;
    }

    /**
     * Set the amplitude of ripple in the X direction.
     *
     * @param xAmplitude the amplitude (in pixels).
     */
    public void setXAmplitude(float xAmplitude) {
        this.xAmplitude = xAmplitude;
    }

    /**
     * Get the amplitude of ripple in the X direction.
     *
     * @return the amplitude (in pixels).
     */
    public float getXAmplitude() {
        return xAmplitude;
    }

    /**
     * Set the wavelength of ripple in the X direction.
     *
     * @param xWavelength the wavelength (in pixels).
     */
    public void setXWavelength(float xWavelength) {
        this.xWavelength = xWavelength;
    }

    /**
     * Get the wavelength of ripple in the X direction.
     *
     * @return the wavelength (in pixels).
     */
    public float getXWavelength() {
        return xWavelength;
    }

    /**
     * Set the amplitude of ripple in the Y direction.
     *
     * @param yAmplitude the amplitude (in pixels).
     */
    public void setYAmplitude(float yAmplitude) {
        this.yAmplitude = yAmplitude;
    }

    /**
     * Get the amplitude of ripple in the Y direction.
     *
     * @return the amplitude (in pixels).
     */
    public float getYAmplitude() {
        return yAmplitude;
    }

    /**
     * Set the wavelength of ripple in the Y direction.
     *
     * @param yWavelength the wavelength (in pixels).
     */
    public void setYWavelength(float yWavelength) {
        this.yWavelength = yWavelength;
    }

    /**
     * Get the wavelength of ripple in the Y direction.
     *
     * @return the wavelength (in pixels).
     */
    public float getYWavelength() {
        return yWavelength;
    }

    /**
     * DOCUMENT ME!
     *
     * @param waveType DOCUMENT ME!
     */
    public void setWaveType(int waveType) {
        this.waveType = waveType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWaveType() {
        return waveType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    protected void transformSpace(Rectangle r) {
        if (edgeAction == ZERO) {
            r.x -= (int) xAmplitude;
            r.width += (int) (2 * xAmplitude);
            r.y -= (int) yAmplitude;
            r.height += (int) (2 * yAmplitude);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        float nx = (float) y / xWavelength;
        float ny = (float) x / yWavelength;
        float fx;
        float fy;

        switch (waveType) {
        case SINE:default:
            fx = (float) Math.sin(nx);
            fy = (float) Math.sin(ny);

            break;

        case SAWTOOTH:
            fx = ImageMath.mod(nx, 1);
            fy = ImageMath.mod(ny, 1);

            break;

        case TRIANGLE:
            fx = ImageMath.triangle(nx);
            fy = ImageMath.triangle(ny);

            break;

        case NOISE:
            fx = Noise.noise1(nx);
            fy = Noise.noise1(ny);

            break;
        }

        out[0] = x + (xAmplitude * fx);
        out[1] = y + (yAmplitude * fy);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Ripple...";
    }
}
