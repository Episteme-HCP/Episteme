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

/**
 * \ VisualLimitAngularBrightnessData \
 */
package org.jscience.astronomy.solarsystem;

/**
 * A support class for VisualLimit.
 * <p/>
 * <p/>
 * Holds values which vary across the sky
 * </p>
 */
public class VisualLimitAngularBrightnessData {
    /**
     * The zenith angle
     */
    private double zenithAngle;

    /**
     * The lunar angular distance
     */
    private double distMoon;

    /**
     * The solar angular distance
     */
    private double distSun;

    /**
     * Creates a new VisualLimitAngularBrightnessData object.
     */
    public VisualLimitAngularBrightnessData() {
        zenithAngle = 0D;
        distMoon = 0D;
        distSun = 0D;
    }

    /**
     * Creates a new VisualLimitAngularBrightnessData object.
     *
     * @param za DOCUMENT ME!
     * @param dm DOCUMENT ME!
     * @param ds DOCUMENT ME!
     */
    public VisualLimitAngularBrightnessData(double za, double dm, double ds) {
        zenithAngle = za;
        distMoon = dm;
        distSun = ds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZenithAngle() {
        return zenithAngle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zenithAngle DOCUMENT ME!
     */
    public void setZenithAngle(double zenithAngle) {
        this.zenithAngle = zenithAngle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMoonAngularDistance() {
        return distMoon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param distMoon DOCUMENT ME!
     */
    public void setMoonAngularDistance(double distMoon) {
        this.distMoon = distMoon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSunAngularDistance() {
        return distSun;
    }

    /**
     * DOCUMENT ME!
     *
     * @param distSun DOCUMENT ME!
     */
    public void setSunAngularDistance(double distSun) {
        this.distSun = distSun;
    }
}
