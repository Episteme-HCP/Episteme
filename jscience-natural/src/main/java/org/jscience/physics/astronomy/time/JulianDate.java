/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.astronomy.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Julian Date (JD) representation and conversions.
 *
 * Julian Date is a continuous count of days since the beginning of the
 * Julian Period (January 1, 4713 BC, Julian calendar).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JulianDate {

    /** Julian Date for J2000.0 epoch (2000-01-01 12:00:00 TT) */
    public static final double J2000 = 2451545.0;

    /** Julian Date for B1950.0 epoch */
    public static final double B1950 = 2433282.4235;

    /** Days per Julian century */
    public static final double DAYS_PER_CENTURY = 36525.0;

    private final double jd;

    public JulianDate(double julianDate) {
        this.jd = julianDate;
    }

    /**
     * Creates a JulianDate from a Gregorian calendar date and time (UTC).
     * 
     * @param year  Year (e.g., 2024)
     * @param month Month (1-12)
     * @param day   Day of month (1-31, can include fractional days)
     * @return JulianDate for the specified date/time
     */
    public static JulianDate fromGregorian(int year, int month, double day) {
        // Algorithm from Meeus, "Astronomical Algorithms"
        int y = year;
        int m = month;

        if (m <= 2) {
            y -= 1;
            m += 12;
        }

        int A = y / 100;
        int B = 2 - A + (A / 4);

        double jd = Math.floor(365.25 * (y + 4716))
                + Math.floor(30.6001 * (m + 1))
                + day + B - 1524.5;

        return new JulianDate(jd);
    }

    /**
     * Creates a JulianDate from Java LocalDateTime (assumes UTC).
     */
    public static JulianDate fromLocalDateTime(LocalDateTime dateTime) {
        double day = dateTime.getDayOfMonth()
                + dateTime.getHour() / 24.0
                + dateTime.getMinute() / 1440.0
                + dateTime.getSecond() / 86400.0
                + dateTime.getNano() / 86400.0e9;
        return fromGregorian(dateTime.getYear(), dateTime.getMonthValue(), day);
    }

    /**
     * Converts back to Gregorian calendar.
     * 
     * @return Array [year, month, day] where day may be fractional
     */
    public double[] toGregorian() {
        double Z = Math.floor(jd + 0.5);
        double F = (jd + 0.5) - Z;

        double A;
        if (Z < 2299161) {
            A = Z;
        } else {
            double alpha = Math.floor((Z - 1867216.25) / 36524.25);
            A = Z + 1 + alpha - Math.floor(alpha / 4);
        }

        double B = A + 1524;
        double C = Math.floor((B - 122.1) / 365.25);
        double D = Math.floor(365.25 * C);
        double E = Math.floor((B - D) / 30.6001);

        double day = B - D - Math.floor(30.6001 * E) + F;

        int month;
        if (E < 14) {
            month = (int) E - 1;
        } else {
            month = (int) E - 13;
        }

        int year;
        if (month > 2) {
            year = (int) C - 4716;
        } else {
            year = (int) C - 4715;
        }

        return new double[] { year, month, day };
    }

    /**
     * Returns Julian centuries since J2000.0.
     * T = (JD - 2451545.0) / 36525
     */
    public double getJulianCenturies() {
        return (jd - J2000) / DAYS_PER_CENTURY;
    }

    /**
     * Returns Modified Julian Date (MJD = JD - 2400000.5).
     * MJD starts at midnight, making it more convenient for modern use.
     */
    public double getModifiedJulianDate() {
        return jd - 2400000.5;
    }

    /**
     * Adds days to this Julian Date.
     */
    public JulianDate addDays(double days) {
        return new JulianDate(jd + days);
    }

    /**
     * Returns the day of week (0 = Monday, 6 = Sunday).
     */
    public int getDayOfWeek() {
        return (int) Math.floor(jd + 0.5) % 7;
    }

    public double getValue() {
        return jd;
    }

    @Override
    public String toString() {
        double[] greg = toGregorian();
        return String.format("JD %.5f (%04d-%02d-%.2f)",
                jd, (int) greg[0], (int) greg[1], greg[2]);
    }

    /**
     * Returns current Julian Date.
     */
    public static JulianDate now() {
        return fromLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));
    }
}


