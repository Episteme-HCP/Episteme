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

package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class GeocentricCoordinate {
    /** DOCUMENT ME! */
    public final static int equatorial = 0;

    /** DOCUMENT ME! */
    public final static int ecliptic = 1;

    /** DOCUMENT ME! */
    private double alpha;

    /** DOCUMENT ME! */
    private double delta;

    /** DOCUMENT ME! */
    private double JD;

/**
     * Creates a new GeocentricCoordinate object.
     */
    public GeocentricCoordinate() {
        alpha = 0.0D;
        delta = 0.0D;
        JD = 0.0D;
    }

/**
     * Creates a new GeocentricCoordinate object.
     *
     * @param geocentriccoordinate DOCUMENT ME!
     */
    public GeocentricCoordinate(GeocentricCoordinate geocentriccoordinate) {
        alpha = 0.0D;
        delta = 0.0D;
        JD = 0.0D;
        copy(geocentriccoordinate);
    }

/**
     * Creates a new GeocentricCoordinate object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     */
    public GeocentricCoordinate(double d, double d1, double d2) {
        alpha = 0.0D;
        delta = 0.0D;
        JD = 0.0D;
        setEquatorial(d, d1, d2);
    }

/**
     * Creates a new GeocentricCoordinate object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     * @param i  DOCUMENT ME!
     */
    public GeocentricCoordinate(double d, double d1, double d2, int i) {
        alpha = 0.0D;
        delta = 0.0D;
        JD = 0.0D;

        if (i == ecliptic) {
            setEcliptic(d, d1, d2);
        } else {
            setEquatorial(d, d1, d2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDay() {
        return JD;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     */
    public void setEcliptic(double d, double d1, double d2) {
        JD = d2;

        double d3 = Ecliptic.obliquity(d2);
        double d4 = (Math.sin(d1) * Math.cos(d3)) -
            (Math.tan(d) * Math.sin(d3));
        double d5 = Math.cos(d1);
        alpha = Angle.reduce(Math.atan2(d4, d5));
        delta = Math.asin((Math.sin(d) * Math.cos(d3)) +
                (Math.cos(d) * Math.sin(d3) * Math.sin(d1)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String("Right Ascension: " + Angle.toHMString(alpha) +
            "  Declination: " + Angle.toDMString(delta));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double rightAscension() {
        return alpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @param geocentriccoordinate DOCUMENT ME!
     */
    public void copy(GeocentricCoordinate geocentriccoordinate) {
        alpha = geocentriccoordinate.alpha;
        delta = geocentriccoordinate.delta;
        JD = geocentriccoordinate.JD;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     */
    public void setEquatorial(double d, double d1, double d2) {
        alpha = d1;
        delta = d;
        JD = d2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double declination() {
        return delta;
    }
}
