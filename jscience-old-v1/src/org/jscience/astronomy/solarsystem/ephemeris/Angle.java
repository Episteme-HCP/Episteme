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
public class Angle {
/**
     * Creates a new Angle object.
     */
    public Angle() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double hoursToRadians(double d) {
        return (d * 3.1415926535897931D) / 12D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double reduce(double d) {
        for (; d < 0.0D; d += 6.2831853071795862D)
            ;

        for (; d > 6.2831853071795862D; d -= 6.2831853071795862D)
            ;

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHMString(double d) {
        double d1 = Math.abs(toHours(d));
        double d2 = (d1 - Math.floor(d1)) * 60D;
        d1 = Math.floor(d1);

        if (d < 0.0D) {
            d1 = -d1;
        }

        return new String(Math.round(d1) + ":" + d2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toDegrees(double d) {
        return (d * 180D) / 3.1415926535897931D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toDMString(double d) {
        double d1 = Math.abs(toDegrees(d));
        double d2 = (d1 - Math.floor(d1)) * 60D;
        d1 = Math.floor(d1);

        if (d < 0.0D) {
            d1 = -d1;
        }

        return new String(Math.round(d1) + ":" + d2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toRadians(double d) {
        return (d * 3.1415926535897931D) / 180D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toHours(double d) {
        return (d * 12D) / 3.1415926535897931D;
    }
}
