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

//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Moment {
    /** DOCUMENT ME! */
    public static final double JD2000 = (new GregorianCalendar(1, 1, 2000)).toJD() +
        0.5D;

    /** DOCUMENT ME! */
    private static final long JAN1900 = (new GregorianCalendar(1, 1, 1900)).toRD();

    /** DOCUMENT ME! */
    private static final long JAN1810 = (new GregorianCalendar(1, 1, 1810)).toRD();

    /** DOCUMENT ME! */
    private static final double[] LONG_COEFFS = {
            403406D, 195207D, 119433D, 112392D, 3891D, 2819D, 1721D, 660D, 350D,
            334D, 314D, 268D, 242D, 234D, 158D, 132D, 129D, 114D, 99D, 93D, 86D,
            78D, 72D, 68D, 64D, 46D, 38D, 37D, 32D, 29D, 28D, 27D, 27D, 25D, 24D,
            21D, 21D, 20D, 18D, 17D, 14D, 13D, 13D, 13D, 12D, 10D, 10D, 10D, 10D
        };

    /** DOCUMENT ME! */
    private static final double[] LONG_MULTS = {
            0.016210430000000001D, 628.30348067D, 628.30821523999998D,
            628.29634301999999D, 1256.605691D, 1256.6098400000001D,
            628.32476599999995D, 1256.5931D, 575.33849999999995D, -0.33931D,
            7771.3771500000003D, 786.04191000000003D, 0.054120000000000001D,
            393.02098000000001D, -0.34860999999999998D, 1150.67698D, 157.74337D,
            52.966700000000003D, 588.49270000000001D, 52.961100000000002D,
            -39.807000000000002D, 522.37689999999998D, 550.76469999999995D,
            2.6107999999999998D, 157.73849999999999D, 1884.9103D, -77.5655D,
            2.6488999999999998D, 1179.0626999999999D, 550.75750000000005D,
            -79.613900000000001D, 1884.8981000000001D, 21.321899999999999D,
            1097.7103D, 548.68560000000002D, 254.4393D, -557.3143D,
            606.97739999999999D, 21.3279D, 1097.7163D, -77.528199999999998D,
            1884.9191000000001D, 2.0781000000000001D, 294.24630000000002D,
            -0.079899999999999999D, 469.41140000000001D, -0.68289999999999995D,
            214.63249999999999D, 1572.0840000000001D
        };

    /** DOCUMENT ME! */
    private static final double[] LONG_ADDS = {
            4.7219639999999998D, 5.9374580000000003D, 1.1155889999999999D,
            5.7816159999999996D, 5.5473999999999997D, 1.512D,
            4.1897000000000002D, 5.415D, 4.3150000000000004D,
            4.5529999999999999D, 5.1980000000000004D, 5.9889999999999999D,
            2.911D, 1.423D, 0.060999999999999999D, 2.3170000000000002D,
            3.1930000000000001D, 2.8279999999999998D, 0.52000000000000002D,
            4.6500000000000004D, 4.3499999999999996D, 2.75D, 4.5D, 3.23D, 1.22D,
            0.14000000000000001D, 3.4399999999999999D, 4.3700000000000001D,
            1.1399999999999999D, 2.8399999999999999D, 5.96D, 5.0899999999999999D,
            1.72D, 2.5600000000000001D, 1.9199999999999999D,
            0.089999999999999997D, 5.9800000000000004D, 4.0300000000000002D,
            4.4699999999999998D, 0.79000000000000004D, 4.2400000000000002D,
            2.0099999999999998D, 2.6499999999999999D, 4.9800000000000004D,
            0.93000000000000005D, 2.21D, 3.5899999999999999D, 1.5D,
            2.5499999999999998D
        };

    /** DOCUMENT ME! */
    public static final double MEANSYNODICMONTH = 29.530588853000001D;

    /** DOCUMENT ME! */
    private double moment;

/**
     * Creates a new Moment object.
     *
     * @param l DOCUMENT ME!
     */
    public Moment(long l) {
        moment = l;
    }

/**
     * Creates a new Moment object.
     *
     * @param d DOCUMENT ME!
     */
    public Moment(double d) {
        moment = d;
    }

/**
     * Creates a new Moment object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public Moment(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Moment jdCreate(double d) {
        return new Moment(momentFromJD(d));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double sind(double d) {
        return Math.sin((d * 3.1415926535897931D) / 180D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double cosd(double d) {
        return Math.cos((d * 3.1415926535897931D) / 180D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double tand(double d) {
        return Math.tan((d * 3.1415926535897931D) / 180D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double momentFromJD(double d) {
        return d + -1721424.5D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double jdFromMoment(double d) {
        return d - -1721424.5D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double equationOfTime(double d) {
        double d1 = (d - JD2000) / 36525D;
        double d2 = 280.46645000000001D +
            (d1 * (36000.769829999997D + (d1 * 0.00030200000000000002D)));
        double d3 = 357.52910000000003D +
            (d1 * (35999.050300000003D +
            (d1 * (-0.00015589999999999999D + (d1 * -4.7999999999999996E-007D)))));
        double d4 = 23.439291109999999D +
            (d1 * (-0.013004167000000001D +
            (d1 * (-1.6388999999999999E-007D + (d1 * 5.0360000000000004E-007D)))));
        double d5 = 0.016708616999999999D +
            (d1 * (-4.2036999999999997E-005D + (d1 * -1.236E-007D)));
        double d6 = Math.pow(tand(d4 / 2D), 2D);

        return ((((d6 * sind(2D * d2)) - (2D * d5 * sind(d3))) +
        (4D * d5 * d6 * sind(d3) * cosd(2D * d2))) -
        (0.5D * d6 * d6 * sind(4D * d2)) - (1.25D * d5 * d5 * sind(2D * d3))) / 6.2831853071795862D;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double equationOfTime() {
        return equationOfTime(jdFromMoment(moment));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double apparentFromLocal(double d) {
        return d + equationOfTime(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double localFromApparent(double d) {
        return d - equationOfTime(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param ad DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double polynomial(double d, double[] ad) {
        double d1 = 0.0D;

        for (int i = ad.length - 1; i >= 0; i--)
            d1 = (d1 * d) + ad[i];

        return d1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double siderealFromJD(double d) {
        double d1 = (d - JD2000) / 36525D;
        double[] ad = {
                280.46061837000002D, 13185000.770053742D,
                0.00038793299999999997D, 2.5833118057349522E-008D
            };

        return (((polynomial(d1, ad) / 360D) % 1.0D) + 1.0D) % 1.0D;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double siderealFromJD() {
        return siderealFromJD(jdFromMoment(moment));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double ephemerisCorrection(double d) {
        double d1 = (new GregorianCalendar((long) d)).getYear();

        if ((d1 >= 1988D) && (d1 <= 2019D)) {
            return (d1 - 1933D) / 86400D;
        }

        double[] ad = {
                -2.0000000000000002E-005D, 0.00029700000000000001D,
                0.025184000000000002D, -0.18113299999999999D,
                0.55303999999999998D, -0.86193799999999998D,
                0.67706599999999995D, -0.212591D
            };
        double[] ad1 = {
                -9.0000000000000002E-006D, 0.0038440000000000002D,
                0.083562999999999998D, 0.86573599999999995D, 4.8675750000000004D,
                15.845535D, 31.332267000000002D, 38.291998999999997D,
                28.316289000000001D, 11.636203999999999D, 2.0437940000000001D
            };
        double d2 = (double) ((new GregorianCalendar(7, 1, (int) d1)).toRD() -
            JAN1900) / 36525D;

        if ((d1 >= 1900D) && (d1 <= 1987D)) {
            return polynomial(d2, ad);
        }

        if ((d1 >= 1800D) && (d1 <= 1899D)) {
            return polynomial(d2, ad1);
        }

        if ((d1 >= 1620D) && (d1 <= 1799D)) {
            return ((196.58332999999999D -
            (4.0674999999999999D * (d1 - 1600D))) +
            (0.021916700000000001D * Math.pow(d1 - 1600D, 2D))) / 86400D;
        } else {
            double d3 = (0.5D +
                (double) (new GregorianCalendar(1, 1, (int) d1)).toRD()) -
                (double) JAN1810;

            return (((d3 * d3) / 41048480D) - 15D) / 86400D;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double ephemerisCorrection() {
        return ephemerisCorrection(moment);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double ephemerisFromUniversal(double d) {
        return d + ephemerisCorrection(momentFromJD(d));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double universalFromEphemeris(double d) {
        return d - ephemerisCorrection(momentFromJD(d));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double julianCenturies(double d) {
        return (ephemerisFromUniversal(d) - JD2000) / 36525D;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double julianCenturies() {
        return julianCenturies(moment);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double abberation(double d) {
        return (1.7E-006D * cosd(177.63D + (35999.018479999999D * d))) -
        9.7299999999999993E-005D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double nutation(double d) {
        double d1 = polynomial(d,
                new double[] {
                    124.90000000000001D, -1934.134D, 0.0020630000000000002D
                });
        double d2 = polynomial(d,
                new double[] {
                    201.11000000000001D, 72001.537700000001D,
                    0.00056999999999999998D
                });

        return (-8.3399999999999994E-005D * sind(d1)) -
        (6.3999999999999997E-006D * sind(d2));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double solarLongitude(double d) {
        double d1 = julianCenturies(d);
        double d2 = 0.0D;

        for (int i = 0; i < LONG_COEFFS.length; i++)
            d2 += (LONG_COEFFS[i] * Math.sin((LONG_MULTS[i] * d1) +
                LONG_ADDS[i]));

        d2 *= 9.9999999999999995E-008D;
        d2 += (4.9353929000000001D + (d1 * 628.33196167999995D));

        return (((((d2 + abberation(d1) + nutation(d1)) * 180D) / 3.1415926535897931D) % 360D) +
        360D) % 360D;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double solarLongitude() {
        return solarLongitude(jdFromMoment(moment));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double dateNextSolarLongitude(double d, int i) {
        double d4 = ((((double) i * Math.ceil(solarLongitude(d) / (double) i)) % 360D) +
            360D) % 360D;
        double d1 = d;
        double d2 = d + (((double) i / 360D) * 400D);
        double d3;

        for (d3 = (d2 + d1) / 2D; (d2 - d1) > 1.0000000000000001E-005D;
                d3 = (d2 + d1) / 2D)
            if (d4 == 0.0D) {
                if ((double) i >= solarLongitude(d3)) {
                    d2 = d3;
                } else {
                    d1 = d3;
                }
            } else if (solarLongitude(d3) >= d4) {
                d2 = d3;
            } else {
                d1 = d3;
            }

        return d3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double solarMoment(long l, double d, double d1, double d2) {
        double d3 = (double) (new GregorianCalendar(l)).dayNumber() + 0.5D +
            d2 + (d1 / -360D);
        double d4 = (0.98560000000000003D * d3) - 3.2890000000000001D;
        double d5 = d4 + (1.9159999999999999D * sind(d4)) +
            282.63400000000001D + (0.02D * sind(2D * d4));
        d5 = ((d5 % 360D) + 360D) % 360D;

        double d6 = cosd(23.441884000000002D) * tand(d5);
        double d7 = Math.atan(d6);
        d7 *= 57.295779513082323D;

        if (((Math.floor(d5 / 90D) + 1.0D) != 1.0D) &&
                ((Math.floor(d5 / 90D) + 1.0D) != 4D)) {
            d7 += 180D;
        }

        d6 = sind(23.441884000000002D) * sind(d5);

        double d8 = (Math.asin(d6) * 180D) / 3.1415926535897931D;
        double d9 = cosd(90.833332999999996D) - (sind(d8) * sind(d));
        double d10 = Math.acos(d9 / (cosd(d8) * cosd(d)));
        d10 *= 57.295779513082323D;
        d10 *= ((d2 <= 0.0D) ? (-1) : 1);

        double d11 = ((d10 + d7) / 360D) - 0.27592D - (0.00273792D * d3);

        return d11 - Math.floor(d11);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double newMoonAtOrAfter(double d) {
        GregorianCalendar gregorian = new GregorianCalendar((long) Math.floor(
                    momentFromJD(d)));
        double d1 = ((double) gregorian.getYear() +
            ((double) gregorian.dayNumber() / 365.25D)) - 2000D;
        int i;

        for (i = (int) Math.floor(d1 * 12.368499999999999D) - 1;
                newMoonTime(i) < d; i++)
            ;

        return newMoonTime(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double newMoonBefore(double d) {
        return newMoonAtOrAfter(newMoonAtOrAfter(d) - 45D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double newMoonTime(int i) {
        double d = (double) i / 1236.8499999999999D;
        double d1 = polynomial(d,
                new double[] {
                    2451550.0976499999D, 36524.908822833051D, 0.0001337D,
                    -1.4999999999999999E-007D, 7.2999999999999996E-010D
                });
        double d2 = polynomial(d,
                new double[] { 1.0D, -0.002516D, -7.4000000000000003E-006D });
        double d3 = polynomial(d,
                new double[] {
                    2.5533999999999999D, 35998.960422026496D,
                    -2.1800000000000001E-005D, -1.1000000000000001E-007D
                });
        double d4 = polynomial(d,
                new double[] {
                    201.5643D, 477197.67640106793D, 0.0107438D, 1.239E-005D,
                    -5.8000000000000003E-008D
                });
        double d5 = polynomial(d,
                new double[] {
                    160.71080000000001D, 483200.81131396897D,
                    -0.0016341000000000001D, -2.2699999999999999E-006D,
                    1.0999999999999999E-008D
                });
        double d6 = polynomial(d,
                new double[] {
                    124.77460000000001D, -1934.1313612299998D,
                    0.0020690999999999999D, 2.1500000000000002E-006D
                });
        double[] ad = {
                -0.40720000000000001D, 0.17241000000000001D,
                0.016080000000000001D, 0.01039D, 0.0073899999999999999D,
                -0.0051399999999999996D, 0.0020799999999999998D,
                -0.0011100000000000001D, -0.00056999999999999998D,
                0.00055999999999999995D, -0.00042000000000000002D,
                0.00042000000000000002D, 0.00038000000000000002D,
                -0.00024000000000000001D, -6.9999999999999994E-005D,
                4.0000000000000003E-005D, 4.0000000000000003E-005D,
                3.0000000000000001E-005D, 3.0000000000000001E-005D,
                -3.0000000000000001E-005D, 3.0000000000000001E-005D,
                -2.0000000000000002E-005D, -2.0000000000000002E-005D,
                2.0000000000000002E-005D
            };
        byte[] abyte0 = {
                0, 1, 0, 0, 1, 1, 2, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0
            };
        byte[] abyte1 = {
                0, 1, 0, 0, -1, 1, 2, 0, 0, 1, 0, 1, 1, -1, 2, 0, 3, 1, 0, 1, -1,
                -1, 1, 0
            };
        byte[] abyte2 = {
                1, 0, 2, 0, 1, 1, 0, 1, 1, 2, 3, 0, 0, 2, 1, 2, 0, 1, 2, 1, 1, 1,
                3, 4
            };
        byte[] abyte3 = {
                0, 0, 0, 2, 0, 0, 0, -2, 2, 0, 0, 2, -2, 0, 0, -2, 0, -2, 2, 2,
                2, -2, 0, 0
            };
        double[] ad1 = {
                299.76999999999998D, 251.88D, 251.83000000000001D,
                349.42000000000002D, 84.659999999999997D, 141.74000000000001D,
                207.13999999999999D, 154.84D, 34.520000000000003D, 207.19D,
                291.33999999999997D, 161.72D, 239.56D, 331.55000000000001D
            };
        double[] ad2 = {
                0.107408D, 0.016320999999999999D, 26.641886D, 36.412478D,
                18.206239D, 53.303770999999998D, 2.453732D, 7.3068600000000004D,
                27.261239D, 0.121824D, 1.844379D, 24.198153999999999D,
                25.513099D, 3.5925180000000001D
            };
        double[] ad3 = {
                -0.0091730000000000006D, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            };
        double[] ad4 = {
                0.00032499999999999999D, 0.000165D, 0.000164D, 0.000126D,
                0.00011D, 6.2000000000000003E-005D, 6.0000000000000002E-005D,
                5.5999999999999999E-005D, 4.6999999999999997E-005D,
                4.1999999999999998E-005D, 4.0000000000000003E-005D,
                3.6999999999999998E-005D, 3.4999999999999997E-005D, 2.3E-005D
            };
        double d7 = -0.00017000000000000001D * sind(d6);

        for (int j = 0; j < ad.length; j++)
            d7 += (ad[j] * Math.pow(d2, abyte0[j]) * sind(((double) abyte1[j] * d3) +
                ((double) abyte2[j] * d4) + ((double) abyte3[j] * d5)));

        double d8 = 0.0D;
        double d9 = d * d;

        for (int k = 0; k < ad1.length; k++)
            d8 += (ad4[k] * sind(ad1[k] + (ad2[k] * (double) i) +
                (ad3[k] * d9)));

        return universalFromEphemeris(d1 + d7 + d8);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double universalFromLocal(double d, double d1) {
        return d - (d1 / 1440D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double localFromUniversal(double d, double d1) {
        return d + (d1 / 1440D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        GregorianCalendar gregorian = new GregorianCalendar(Integer.parseInt(
                    args[0]), Integer.parseInt(args[1]),
                Integer.parseInt(args[2]));
        double d = gregorian.toJD();
        System.out.println(d);
        System.out.println(1440D * equationOfTime(d));
        System.out.println(solarLongitude(d + 0.5D));

        if (args.length >= 6) {
            d += ((double) (Integer.parseInt(args[5]) +
            (60 * (Integer.parseInt(args[4]) +
            (60 * Integer.parseInt(args[3]))))) / 86400D);
        }

        System.out.println(d);
        System.out.println(siderealFromJD(d));
        System.out.println("JD2000: " + JD2000);
        System.out.println(newMoonAtOrAfter(d));
        System.out.println("Eph: " +
            ephemerisCorrection(Integer.parseInt(args[3])));
        System.out.println(newMoonAtOrAfter(2392807.5D));
    }
}
