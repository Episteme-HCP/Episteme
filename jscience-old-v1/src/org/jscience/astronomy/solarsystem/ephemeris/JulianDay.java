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
public class JulianDay {
    /** DOCUMENT ME! */
    public static final double J2000 = 2451545D;

/**
     * Creates a new JulianDay object.
     */
    public JulianDay() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getDay(int i, int j, double d) {
        checkArgs(j, d);

        return getDay(i, j, d, 0, 0, 0.0D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getDay(int i, int j, double d, int k, int l, double d1) {
        if ((j == 1) || (j == 2)) {
            i--;
            j += 12;
        }

        int i1 = (int) ((double) i / 100D);
        int j1 = (2 - i1) + (i1 / 4);
        int k1 = (int) (365.25D * (double) (i + 4716));
        int l1 = (int) (30.600100000000001D * (double) (j + 1));
        double d2 = ((double) (j1 + k1 + l1) + d) - 1524.5D;
        d2 += (((double) k / 24D) + ((double) l / 1440D) + (d1 / 86400D));

        return d2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double LST(double d, double d1) {
        return Angle.reduce(GMST(d) - d1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double GMST(double d) {
        double d1 = (d - 2451545D) / 36525D;
        double d2 = 280.46061837000002D +
            (360.98564736628998D * (d - 2451545D)) +
            (d1 * d1 * (0.00038793299999999997D - (d1 / 38710000D)));

        return Angle.reduce(Angle.toRadians(d2));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Matrix3D equaPrecessMatrix(double d, double d1) {
        double d2 = (d - 2451545D) / 36525D;
        double d3 = (d1 - d) / 36525D;
        double d4 = (2306.2181D +
            ((1.39656D - (0.00013899999999999999D * d2)) * d2) +
            (((0.30187999999999998D - (0.00034400000000000001D * d2)) +
            (0.017998D * d3)) * d3)) * d3;
        double d5 = (2306.2181D +
            ((1.39656D - (0.00013899999999999999D * d2)) * d2) +
            ((1.0946800000000001D + (6.6000000000000005E-005D * d2) +
            (0.018203D * d3)) * d3)) * d3;
        double d6 = (2004.3108999999999D -
            ((0.85329999999999995D - (0.00021699999999999999D * d2)) * d2) -
            (((0.42664999999999997D + (0.00021699999999999999D * d2)) -
            (0.041833000000000002D * d3)) * d3)) * d3;
        Matrix3D matrix3d = Matrix3D.rotZ((d4 * 3.1415926535897931D) / 648000D);
        Matrix3D matrix3d1 = Matrix3D.rotY((-d6 * 3.1415926535897931D) / 648000D);
        Matrix3D matrix3d2 = Matrix3D.rotZ((d5 * 3.1415926535897931D) / 648000D);

        return Matrix3D.mul(matrix3d2, Matrix3D.mul(matrix3d1, matrix3d));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double deltaT(double d) {
        double d2 = d - 2382148D;
        double d1 = ((d2 * d2) / 41048480D) - 15D;

        return d1 / 86400D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double inDynamicalTime(double d) {
        return d + deltaT(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double now() {
        double d = 86400000D;
        double d1 = 2440587.5D;
        long l = System.currentTimeMillis();

        return 2440587.5D + ((double) l / 86400000D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CalendarDate getGregorian(double d) {
        CalendarDate calendardate = new CalendarDate();
        d += 0.5D;

        int j = (int) d;
        double d1 = d - (double) j;
        int i;

        if (j < 0x231519) {
            i = j;
        } else {
            int k = (int) (((double) j - 1867216.25D) / 36524.25D);
            i = (j + 1 + k) - (k / 4);
        }

        int l = i + 1524;
        int i1 = (int) (((double) l - 122.09999999999999D) / 365.25D);
        int j1 = (int) (365.25D * (double) i1);
        int k1 = (int) ((double) (l - j1) / 30.600100000000001D);
        calendardate.day = (double) (l - j1 -
            (int) (30.600100000000001D * (double) k1)) + d1;

        if (k1 < 14) {
            calendardate.month = k1 - 1;
        } else {
            calendardate.month = k1 - 13;
        }

        if (calendardate.month > 2) {
            calendardate.year = i1 - 4716;
        } else {
            calendardate.year = i1 - 4715;
        }

        return calendardate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    private static void checkArgs(int i, double d) {
        if ((i < 1) || (i > 12)) {
            System.err.println("Bad month when setting Julian_Date object.\n");
        }

        if ((d < 0.0D) || (d > 31D)) {
            System.err.println("Bad day when setting Julian_Date object.\n");
        }
    }
}
