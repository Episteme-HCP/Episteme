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

package org.jscience.social.history.calendars;

/**
 * Abstract base class for modified Hindu calendars using floating-point arithmetic.
 * Provides astronomical calculations for solar and lunar positions based
 * on the Surya Siddhanta with modern refinements.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
abstract class ModifiedHinduCalendar {
    /** Sidereal year length in days. */
    public static final double SIDEREALYEAR = 365.2587564814815D;

    /** Sidereal month length in days. */
    public static final double SIDEREALMONTH = 27.321674162683866D;

    /** Synodic month length in days. */
    public static final double SYNODICMONTH = 29.530587946071719D;

    /** Days from creation to epoch. */
    public static final double CREATION = 714402296627D;

    /** Anomalistic year length in days. */
    public static final double ANOMYEAR = 365.25878920258134D;

    /** Anomalistic month length in days. */
    public static final double ANOMMONTH = 27.554597974680476D;

/**
     * Creates a new ModifiedHinduCalendar object.
     */
    ModifiedHinduCalendar() {
    }

    /**
     * Returns the sign of the given value.
     *
     * @param d the value.
     * @return 1 if positive, -1 if negative, 0 if zero.
     */
    public static int signum(double d) {
        if (d > 0.0D) {
            return 1;
        }

        return (d >= 0.0D) ? 0 : (-1);
    }

    /**
     * Returns the Hindu sine table value for the given index.
     *
     * @param l the index.
     * @return the sine table value in minutes.
     */
    public static int hindSineTable(long l) {
        double d = 3438D * Math.sin(((((double) l * 225D) / 60D) * 3.1415926535897931D) / 180D);
        double d1 = 0.215D * (double) signum(d) * (double) signum(Math.abs(d) -
                1716D);

        return (int) Math.round(d + d1);
    }

    /**
     * Returns the Hindu sine for the given arc in minutes.
     *
     * @param d the arc in minutes.
     * @return the Hindu sine.
     */
    public static double hindSine(double d) {
        double d1 = d / 225D;
        double d2 = d1 - Math.floor(d1);

        return (d2 * (double) hindSineTable((long) Math.ceil(d1))) +
        ((1.0D - d2) * (double) hindSineTable((long) Math.floor(d1)));
    }

    /**
     * Returns the Hindu arc sine for the given sine value.
     *
     * @param d the sine value.
     * @return the arc in minutes.
     */
    public static double hindArcSine(double d) {
        if (d < 0.0D) {
            return -hindArcSine(-d);
        }

        int i = 0;

        for (int k = 0; d > (double) hindSineTable(k); k++)
            i++;

        int j = hindSineTable(i - 1);

        return 225D * ((double) (i - 1) +
        ((d - (double) j) / (double) (hindSineTable(i) - j)));
    }

    /**
     * Returns the mean position of a celestial body.
     *
     * @param d days since creation.
     * @param d1 orbital period.
     * @return the mean position in minutes.
     */
    public static double meanPosition(double d, double d1) {
        double d2 = d / d1;

        return 21600D * (d2 - Math.floor(d2));
    }

    /**
     * Returns the true position of a celestial body.
     *
     * @param d days since creation.
     * @param d1 orbital period.
     * @param d2 eccentricity.
     * @param d3 anomalistic period.
     * @param d4 epicycle correction.
     * @return true position in minutes.
     */
    public static double truePosition(double d, double d1, double d2,
        double d3, double d4) {
        double d5 = meanPosition(d, d1);
        double d6 = d + 714402296627D;
        double d7 = hindSine(meanPosition(d6, d3));
        double d8 = (Math.abs(d7) * d4 * d2) / 3438D;
        double d9 = hindArcSine(d7 * (d2 - d8));

        return (((d5 - d9) % 21600D) + 21600D) % 21600D;
    }

    /**
     * Returns the solar longitude in minutes.
     *
     * @param d days since creation.
     * @return solar longitude.
     */
    public static double solarLongitude(double d) {
        return truePosition(d, 365.2587564814815D, 0.03888888888888889D,
            365.25878920258134D, 0.023809523809523808D);
    }

    /**
     * Returns the zodiac sign index (1-12) for the given day.
     *
     * @param d the day.
     * @return the zodiac sign index.
     */
    public static int zodiac(double d) {
        return (int) Math.floor(solarLongitude(d) / 1800D) + 1;
    }

    /**
     * Returns the lunar longitude in minutes.
     *
     * @param d days since creation.
     * @return lunar longitude.
     */
    public static double lunarLongitude(double d) {
        return truePosition(d, 27.321674162683866D, 0.088888888888888892D,
            27.554597974680476D, 0.023809523809523808D);
    }

    /**
     * Calculates the lunar phase for a given day.
     *
     * @param d the day.
     * @return the lunar phase in minutes.
     */
    public static double lunarPhase(double d) {
        return (((lunarLongitude(d) - solarLongitude(d)) % 21600D) + 21600D) % 21600D;
    }

    /**
     * Returns the lunar day (tithi) for the given day.
     *
     * @param d the day.
     * @return the lunar day number (1-30).
     */
    public static int lunarDay(double d) {
        return (int) Math.floor(lunarPhase(d) / 720D) + 1;
    }

    /**
     * Returns the time of the next new moon after the given day.
     *
     * @param d the day.
     * @return the time of the new moon.
     */
    public static double newMoon(double d) {
        double d1 = d + 1.0D;
        double d2 = d1 -
            (((d1 % 29.530587946071719D) + 29.530587946071719D) % 29.530587946071719D);
        double d3 = d2 - 0.66666666666666663D;
        double d4 = d2 + 0.66666666666666663D;
        double d5;

        for (d5 = (d3 + d4) / 2D;
                (d >= d3) && ((d4 > d) || (zodiac(d3) != zodiac(d4)));
                d5 = (d3 + d4) / 2D)
            if (lunarPhase(d5) < 10800D) {
                d4 = d5;
            } else {
                d3 = d5;
            }

        if (d5 > d) {
            return newMoon(Math.floor(d) - 20D);
        } else {
            return d5;
        }
    }

    /**
     * Returns the Hindu calendar year for the given day.
     *
     * @param d the day.
     * @return the calendar year.
     */
    public static int calYear(double d) {
        double d1 = d / 365.2587564814815D;
        double d2 = (d1 - Math.floor(d1)) * 21600D;
        double d3 = solarLongitude(d);
        int i = (int) Math.floor(d1);

        if ((d3 > 20000D) && (d2 < 1000D)) {
            return i - 1;
        }

        if ((d2 > 20000D) && (d3 < 1000D)) {
            return i + 1;
        } else {
            return i;
        }
    }

    /**
     * Returns the ascensional difference for the given day and latitude.
     *
     * @param d the day.
     * @param d1 the latitude in minutes.
     * @return the ascensional difference.
     */
    public static double ascDiff(double d, double d1) {
        double d2 = 0.40634089586969169D * hindSine(tropLongitude(d));
        double d3 = hindSine(5400D - hindArcSine(d2));
        double d4 = hindSine(d1) / hindSine(d1 + 5400D);
        double d5 = d2 * d4;

        return hindArcSine((d5 / d3) * -3438D);
    }

    /**
     * Returns the tropical longitude in minutes.
     *
     * @param d the day.
     * @return the tropical longitude.
     */
    public static double tropLongitude(double d) {
        long l = (long) Math.floor(d);
        double d1 = 0.002464006636472327D;
        d1 *= l;
        d1 %= 6480D;
        d1 = (d1 + 6480D) % 6480D;
        d1 += 1620D;
        d1 = 3240D - d1;
        d1 = 1620D - Math.abs(d1);

        return (((solarLongitude(d) - d1) % 21600D) + 21600D) % 21600D;
    }

    /**
     * Returns the difference between solar and sidereal time.
     *
     * @param d the day.
     * @return the time difference.
     */
    public static double solarSiderealDifference(double d) {
        return (dailyMotion(d) * (double) risingSign(d)) / 1800D;
    }

    /**
     * Returns the daily motion of the sun.
     *
     * @param d the day.
     * @return the daily motion in minutes.
     */
    public static double dailyMotion(double d) {
        double d1 = 59.136159275335849D;
        double d2 = meanPosition(714402296627D + d, 365.25878920258134D);
        double d3 = 0.03888888888888889D - (Math.abs(hindSine(d2)) / 3713040D);
        int i = (int) Math.floor(d2 / 225D);
        int j = hindSineTable(i + 1) - hindSineTable(i);
        double d4 = (-d3 / 225D) * (double) j;

        return d1 * (d4 + 1.0D);
    }

    /**
     * Returns the rising sign correction.
     *
     * @param d the day.
     * @return the rising sign value.
     */
    public static int risingSign(double d) {
        int[] ai = { 1670, 1795, 1935, 1935, 1795, 1670 };

        return ai[(int) Math.floor(tropLongitude(d) / 1800D) % 6];
    }

    /**
     * Returns the equation of time correction.
     *
     * @param d the day.
     * @return the equation of time value.
     */
    public static double equationOfTime(double d) {
        double d1 = hindSine(meanPosition(714402296627D + d, 365.25878920258134D));
        double d2 = d1 * ((Math.abs(d1) / 3713040D) - 0.03888888888888889D);

        return (dailyMotion(d) * d2 * 365.2587564814815D) / 21600D / 21600D;
    }

    /**
     * Returns the time of sunrise for the given day.
     *
     * @param d the day.
     * @return the sunrise time.
     */
    public static double sunrise(double d) {
        return d + 0.25D + equationOfTime(d) +
        (4.6169893048655071E-005D * (ascDiff(d, 1389D) +
        (solarSiderealDifference(d) / 4D)));
    }

    /**
     * Main method for testing Hindu calendar calculations.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        System.out.println(sunrise(Integer.parseInt(args[0])));
    }
}

