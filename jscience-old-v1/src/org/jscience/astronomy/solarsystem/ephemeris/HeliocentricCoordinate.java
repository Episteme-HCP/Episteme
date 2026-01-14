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
public class HeliocentricCoordinate {
    /** DOCUMENT ME! */
    protected double l;

    /** DOCUMENT ME! */
    protected double psi;

    /** DOCUMENT ME! */
    protected double r;

    /** DOCUMENT ME! */
    protected double a;

    /** DOCUMENT ME! */
    protected double i;

    /** DOCUMENT ME! */
    protected double Omega;

    /** DOCUMENT ME! */
    protected double JD;

/**
     * Creates a new HeliocentricCoordinate object.
     */
    public HeliocentricCoordinate() {
        r = 0.0D;
        a = 0.0D;
        i = 0.0D;
    }

/**
     * Creates a new HeliocentricCoordinate object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     * @param d3 DOCUMENT ME!
     * @param d4 DOCUMENT ME!
     * @param d5 DOCUMENT ME!
     * @param d6 DOCUMENT ME!
     */
    public HeliocentricCoordinate(double d, double d1, double d2, double d3,
        double d4, double d5, double d6) {
        l = d;
        psi = d1;
        r = d2;
        a = d3;
        i = d4;
        Omega = d5;
        JD = d6;
    }

/**
     * Creates a new HeliocentricCoordinate object.
     *
     * @param heliocentriccoordinate DOCUMENT ME!
     */
    public HeliocentricCoordinate(HeliocentricCoordinate heliocentriccoordinate) {
        copy(heliocentriccoordinate);
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
     * @param heliocentriccoordinate DOCUMENT ME!
     */
    public void copy(HeliocentricCoordinate heliocentriccoordinate) {
        l = heliocentriccoordinate.l;
        psi = heliocentriccoordinate.psi;
        r = heliocentriccoordinate.r;
        a = heliocentriccoordinate.a;
        i = heliocentriccoordinate.i;
        Omega = heliocentriccoordinate.Omega;
        JD = heliocentriccoordinate.JD;
    }
}
