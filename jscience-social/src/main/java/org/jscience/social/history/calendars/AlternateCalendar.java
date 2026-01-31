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

import java.io.Serializable;

/**
 * Abstract base class for alternate (non-Gregorian) calendar systems.
 * Provides common operations for calendar date arithmetic, including
 * conversion to/from Rata Die (RD) and Julian Day (JD) representations.
 *
 * <p>The Rata Die is a day-counting system where day 1 corresponds to
 * January 1, 1 CE in the proleptic Gregorian calendar.</p>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public abstract class AlternateCalendar implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The epoch day (Rata Die) for this calendar system's origin. */
    public static long EPOCH;

    /** Flag to control Unicode output in toString methods. */
    public static boolean unicode;

    /** 
     * The Julian Day epoch offset. JD 0 = November 24, 4714 BCE (proleptic Gregorian).
     * This constant relates RD to JD: JD = RD - JD_EPOCH
     */
    public static final double JD_EPOCH = -1721424.5D;

    /** The current Rata Die (day number) for this calendar instance. */
    protected long rd;

    /**
     * Creates a new AlternateCalendar set to the epoch date.
     */
    public AlternateCalendar() {
        rd = EPOCH;
    }

    /**
     * Creates a new AlternateCalendar set to the specified Rata Die.
     *
     * @param rataDie the Rata Die day number
     */
    public AlternateCalendar(long rataDie) {
        rd = rataDie;
    }

    /**
     * Computes the mathematical modulo (always positive) of two long values.
     * Unlike Java's % operator, this always returns a non-negative result.
     *
     * @param dividend the dividend
     * @param divisor  the divisor
     * @return the non-negative remainder
     */
    public static long mod(long dividend, long divisor) {
        return dividend - (divisor * floorDiv(dividend, divisor));
    }

    /**
     * Computes the mathematical modulo (always positive) of a long and an int.
     *
     * @param dividend the dividend
     * @param divisor  the divisor
     * @return the non-negative remainder
     */
    public static int mod(long dividend, int divisor) {
        return (int) mod(dividend, (long) divisor);
    }

    /**
     * Computes the adjusted modulo (1-based instead of 0-based).
     * Returns values in range [1, divisor] instead of [0, divisor-1].
     *
     * @param dividend the dividend
     * @param divisor  the divisor
     * @return the 1-based remainder
     */
    public static long amod(long dividend, long divisor) {
        return 1L + mod(dividend - 1L, divisor);
    }

    /**
     * Computes the floor division (rounds toward negative infinity).
     *
     * @param dividend the dividend
     * @param divisor  the divisor
     * @return the floor of the quotient
     */
    public static long floorDiv(long dividend, long divisor) {
        long quotient = dividend / divisor;
        if ((quotient * divisor) > dividend) {
            return quotient - 1L;
        }
        return quotient;
    }

    /**
     * @deprecated Use {@link #floorDiv(long, long)} instead.
     */
    @Deprecated
    public static long fldiv(long dividend, long divisor) {
        return floorDiv(dividend, divisor);
    }

    /**
     * Sets this calendar to the specified Rata Die and recomputes the calendar fields.
     *
     * @param rataDie the new Rata Die value
     */
    public abstract void set(long rataDie);

    /**
     * Returns the Rata Die (day number) for this calendar date.
     *
     * @return the current Rata Die
     */
    public long toRD() {
        return rd;
    }

    /**
     * Returns the Julian Day number for this calendar date.
     *
     * @return the Julian Day
     */
    public double toJD() {
        return toJD(toRD());
    }

    /**
     * Converts a Rata Die to a Julian Day number.
     *
     * @param rataDie the Rata Die value
     * @return the corresponding Julian Day
     */
    public static double toJD(long rataDie) {
        return (double) rataDie - JD_EPOCH;
    }

    /**
     * Converts a Julian Day number to a Rata Die.
     *
     * @param julianDay the Julian Day value
     * @return the corresponding Rata Die
     */
    public static long fromJD(double julianDay) {
        return (long) Math.floor(julianDay + JD_EPOCH);
    }

    /**
     * Checks if this calendar date is before another calendar date.
     *
     * @param other the calendar to compare against
     * @return true if this date is before the other date
     */
    public boolean isBefore(AlternateCalendar other) {
        return toRD() < other.toRD();
    }

    /**
     * Checks if this calendar date is after another calendar date.
     *
     * @param other the calendar to compare against
     * @return true if this date is after the other date
     */
    public boolean isAfter(AlternateCalendar other) {
        return toRD() > other.toRD();
    }

    /**
     * @deprecated Use {@link #isBefore(AlternateCalendar)} instead.
     * Note: Original implementation had inverted logic.
     */
    @Deprecated
    public boolean before(AlternateCalendar other) {
        return toRD() > other.toRD(); // Preserving original (incorrect) behavior
    }

    /**
     * @deprecated Use {@link #isAfter(AlternateCalendar)} instead.
     * Note: Original implementation had inverted logic.
     */
    @Deprecated
    public boolean after(AlternateCalendar other) {
        return toRD() < other.toRD(); // Preserving original (incorrect) behavior
    }

    /**
     * Calculates the difference in days between two calendar dates.
     *
     * @param cal1 the first calendar
     * @param cal2 the second calendar
     * @return the number of days (cal1 - cal2)
     */
    public static long difference(AlternateCalendar cal1, AlternateCalendar cal2) {
        return cal1.toRD() - cal2.toRD();
    }

    /**
     * Calculates the difference in days between this calendar and another.
     *
     * @param other the calendar to compare against
     * @return the number of days (this - other)
     */
    public long difference(AlternateCalendar other) {
        return difference(this, other);
    }

    /**
     * Adds a number of days to this calendar date.
     *
     * @param days the number of days to add (can be negative)
     */
    public void add(long days) {
        set(rd + days);
    }

    /**
     * Subtracts a number of days from this calendar date.
     *
     * @param days the number of days to subtract
     */
    public void subtract(long days) {
        set(rd - days);
    }

    /**
     * Recomputes the calendar-specific fields from the current Rata Die.
     * Called after setting the RD value.
     */
    protected abstract void recomputeFromRD();

    /**
     * Recomputes the Rata Die from the calendar-specific fields.
     * Called after setting individual calendar fields.
     */
    protected abstract void recomputeRD();

    /**
     * Returns a string representation of this calendar date.
     *
     * @return a formatted date string
     */
    @Override
    public abstract String toString();
}

