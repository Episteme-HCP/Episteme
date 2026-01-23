/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Originally based on code from Mark E. Shoulson <mark@kli.org>
 * http://web.meson.org/calendars/
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

package org.jscience.history.calendars;

/**
 * Implementation of the proleptic Julian calendar.
 * The Julian calendar was introduced by Julius Caesar in 46 BCE and was the predominant
 * calendar in the Roman world and subsequently in the Western world until the
 * Gregorian reform of 1582.
 *
 * <p>Key differences from the Gregorian calendar:</p>
 * <ul>
 *   <li>Leap year rule: every year divisible by 4 (no century exception)</li>
 *   <li>Drifts approximately 1 day per 128 years relative to the tropical year</li>
 *   <li>Uses astronomical year numbering (year 0 = 1 BCE)</li>
 * </ul>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class JulianCalendar extends GregorianCalendar {

    private static final long serialVersionUID = 1L;

    /** The Julian epoch: December 30, 1 BCE (proleptic Gregorian) = January 1, 1 CE (Julian). */
    public static long EPOCH = (new GregorianCalendar(0, 12, 30)).toRD();

    /**
     * Creates a Julian calendar set to the specified Rata Die.
     *
     * @param rataDie the day number
     */
    public JulianCalendar(long rataDie) {
        set(rataDie);
    }

    /**
     * Creates a Julian calendar set to the epoch (January 1, 1 CE).
     */
    public JulianCalendar() {
        this(EPOCH);
    }

    /**
     * Creates a Julian calendar from another calendar's date.
     *
     * @param calendar the source calendar
     */
    public JulianCalendar(AlternateCalendar calendar) {
        this(calendar.toRD());
    }

    /**
     * Creates a Julian calendar set to the specified date.
     *
     * @param year  the year (negative for BCE, using astronomical year numbering)
     * @param month the month (1-12)
     * @param day   the day of month (1-31)
     */
    public JulianCalendar(int year, int month, int day) {
        super(1, 1, 1);
        set(year, month, day);
    }

    /**
     * Recomputes the Rata Die from the current year, month, and day fields.
     */
    @Override
    protected synchronized void recomputeRD() {
        int adjustedYear = year;

        // Adjust for astronomical year numbering (no year 0 in common usage)
        if (adjustedYear < 0) {
            adjustedYear++;
        }

        rd = (EPOCH - 1L) 
            + (365L * (adjustedYear - 1))
            + AlternateCalendar.floorDiv(adjustedYear - 1, 4L)
            + AlternateCalendar.floorDiv((367 * month) - 362, 12L);

        if (month > 2) {
            if (isLeapYear(year)) {
                rd--;
            } else {
                rd -= 2L;
            }
        }

        rd += day;
    }

    /**
     * Recomputes the year, month, and day fields from the current Rata Die.
     */
    @Override
    protected synchronized void recomputeFromRD() {
        year = (int) AlternateCalendar.floorDiv((4L * (toRD() - EPOCH)) + 1464L, 1461L);

        // Adjust for astronomical year numbering
        if (year <= 0) {
            year--;
        }

        JulianCalendar julian = new JulianCalendar(year, 1, 1);
        int priorDays = (int) (toRD() - julian.toRD());
        julian.set(year, 3, 1);

        if (toRD() >= julian.toRD()) {
            if (isLeapYear(year)) {
                priorDays++;
            } else {
                priorDays += 2;
            }
        }

        month = (int) AlternateCalendar.floorDiv((12 * priorDays) + 373, 367L);
        day = (int) (toRD() - (new JulianCalendar(year, month, 1)).toRD()) + 1;
    }

    /**
     * Sets this calendar to the specified Rata Die and recomputes the date fields.
     *
     * @param rataDie the new Rata Die value
     */
    @Override
    public synchronized void set(long rataDie) {
        rd = rataDie;
        recomputeFromRD();
    }

    /**
     * Determines if the specified year is a leap year in the Julian calendar.
     * In the Julian calendar, every year divisible by 4 is a leap year.
     *
     * @param year the year to check (using astronomical year numbering)
     * @return true if it's a leap year
     */
    public static boolean isLeapYear(int year) {
        if (year > 0) {
            return AlternateCalendar.mod(year, 4) == 0;
        }
        // For negative years (BCE), mod 4 == 3 means leap year
        return AlternateCalendar.mod(year, 4) == 3;
    }

    /**
     * Returns the era suffix for display.
     *
     * @return " C.E." for positive years, " B.C.E." for non-positive years
     */
    @Override
    protected String getSuffix() {
        return year > 0 ? " C.E." : " B.C.E.";
    }

    /**
     * Returns a string representation of this date.
     *
     * @return formatted date string
     */
    @Override
    public String toString() {
        try {
            int displayYear = year < 0 ? -year : year;
            return String.format("%d %s %d%s", day, monthName(), displayYear, getSuffix());
        } catch (ArrayIndexOutOfBoundsException e) {
            return String.format("Invalid date: month=%d, day=%d, year=%d", month, day, year);
        }
    }
}
