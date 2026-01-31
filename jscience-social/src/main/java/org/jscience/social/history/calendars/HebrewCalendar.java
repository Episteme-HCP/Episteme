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

import java.util.Enumeration;

/**
 * Implementation of the Hebrew (Jewish) calendar.
 * The Hebrew calendar is a lunisolar calendar used for Jewish religious observances.
 * It incorporates both lunar months and a solar year through a 19-year Metonic cycle.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: October 7, 3761 BCE (Julian) - the traditional date of Creation</li>
 *   <li>Uses a 19-year cycle with 7 leap years (years 3, 6, 8, 11, 14, 17, 19)</li>
 *   <li>Months alternate between 29 and 30 days, with adjustments</li>
 *   <li>Years can be deficient (353/383), regular (354/384), or complete (355/385)</li>
 *   <li>Years are marked with "A.M." (Anno Mundi)</li>
 * </ul>
 *
 * <p>The month Adar becomes Adar I in leap years, with Adar II added after it.</p>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HebrewCalendar extends MonthDayYear {

    private static final long serialVersionUID = 1L;

    /** The Hebrew epoch: October 7, 3761 BCE (Julian calendar). */
    protected static final long EPOCH = (new JulianCalendar(-3761, 10, 7)).toRD();

    /** Month names (Nisan-based numbering, with Adar II for leap years). */
    private static final String[] MONTHS = {
        "Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul",
        "Tishri", "Heshvan", "Kislev", "Tevet", "Shvat", "Adar", "Adar II"
    };

    /**
     * Creates a Hebrew calendar set to the epoch (1 Tishri, 1 AM).
     */
    public HebrewCalendar() {
        this(EPOCH);
    }

    /**
     * Creates a Hebrew calendar set to the specified Rata Die.
     *
     * @param rataDie the day number
     */
    public HebrewCalendar(long rataDie) {
        set(rataDie);
    }

    /**
     * Creates a Hebrew calendar set to the specified date.
     *
     * @param year  the Hebrew year (Anno Mundi)
     * @param month the month (1=Nisan, 7=Tishri)
     * @param day   the day of month (1-30)
     */
    public HebrewCalendar(int year, int month, int day) {
        set(year, month, day);
    }

    /**
     * Creates a Hebrew calendar from another calendar's date.
     *
     * @param calendar the source calendar
     */
    public HebrewCalendar(AlternateCalendar calendar) {
        this(calendar.toRD());
    }

    /**
     * Determines if the specified year is a leap year in the Hebrew calendar.
     * Years 3, 6, 8, 11, 14, 17, and 19 of each 19-year cycle are leap years.
     *
     * @param year the Hebrew year
     * @return true if it's a leap year
     */
    public static boolean isLeapYear(int year) {
        return AlternateCalendar.mod((7 * year) + 1, 19) < 7;
    }

    /**
     * Returns the last month of the specified year (12 or 13 for leap years).
     *
     * @param year the Hebrew year
     * @return 12 for regular years, 13 for leap years
     */
    public static int lastMonth(int year) {
        return isLeapYear(year) ? 13 : 12;
    }

    /**
     * Calculates the number of elapsed days from the epoch to the start of the specified year.
     *
     * @param year the Hebrew year
     * @return days elapsed
     */
    protected static long elapsedDays(int year) {
        long months = AlternateCalendar.floorDiv((235L * year) - 234, 19L);
        long parts = 12084L + (13753L * months);
        long days = (29L * months) + AlternateCalendar.floorDiv(parts, 25920L);

        // Apply ADU postponement rule
        if (AlternateCalendar.mod(3L * (days + 1L), 7) < 3) {
            days++;
        }

        return days;
    }

    /**
     * Calculates the year-type delay adjustment.
     *
     * @param year the Hebrew year
     * @return delay in days (0, 1, or 2)
     */
    protected static int delay(int year) {
        long prevYear = elapsedDays(year - 1);
        long thisYear = elapsedDays(year);
        long nextYear = elapsedDays(year + 1);

        if ((nextYear - thisYear) == 356L) {
            return 2;
        }
        return ((thisYear - prevYear) != 382L) ? 0 : 1;
    }

    /**
     * Returns the number of days in the specified month of the specified year.
     *
     * @param month the month (1-13)
     * @param year  the Hebrew year
     * @return days in month (29 or 30)
     */
    protected static int lastDay(int month, int year) {
        // Months with 29 days: 2 (Iyyar), 4 (Tammuz), 6 (Elul), 10 (Tevet), 13 (Adar II)
        // Month 12 (Adar) has 29 unless it's a leap year
        // Month 8 (Heshvan) has 30 only in long years
        // Month 9 (Kislev) has 29 only in short years
        if ((month != 2) && (month != 4) && (month != 6) && (month != 10) && (month != 13) &&
            ((month != 12) || isLeapYear(year)) &&
            ((month != 8) || longHeshvan(year)) &&
            ((month != 9) || !shortKislev(year))) {
            return 30;
        }
        return 29;
    }

    /**
     * Determines if Heshvan is long (30 days) in the specified year.
     *
     * @param year the Hebrew year
     * @return true if Heshvan has 30 days
     */
    protected static boolean longHeshvan(int year) {
        return AlternateCalendar.mod(daysInYear(year), 10) == 5;
    }

    /**
     * Determines if Kislev is short (29 days) in the specified year.
     *
     * @param year the Hebrew year
     * @return true if Kislev has 29 days
     */
    protected static boolean shortKislev(int year) {
        return AlternateCalendar.mod(daysInYear(year), 10) == 3;
    }

    /**
     * Returns the total number of days in the specified Hebrew year.
     *
     * @param year the Hebrew year
     * @return days in year (353, 354, 355, 383, 384, or 385)
     */
    protected static int daysInYear(int year) {
        return (int) ((new HebrewCalendar(year + 1, 7, 1)).toRD() -
                      (new HebrewCalendar(year, 7, 1)).toRD());
    }

    /**
     * Recomputes the Rata Die from the current year, month, and day fields.
     */
    @Override
    protected synchronized void recomputeRD() {
        rd = EPOCH + elapsedDays(year) + delay(year) + day - 1L;

        if (month < 7) {
            // Add days for months 7-13 (or 7-12)
            int lastM = lastMonth(year);
            for (int m = 7; m <= lastM; m++) {
                rd += lastDay(m, year);
            }
            // Add days for months 1 to (month-1)
            for (int m = 1; m < month; m++) {
                rd += lastDay(m, year);
            }
        } else {
            // Add days for months 7 to (month-1)
            for (int m = 7; m < month; m++) {
                rd += lastDay(m, year);
            }
        }
    }

    /**
     * Recomputes the year, month, and day fields from the current Rata Die.
     */
    @Override
    protected synchronized void recomputeFromRD() {
        // Approximate year
        int approxYear = (int) Math.floor((double) (rd - EPOCH) / 365.25);
        year = approxYear - 1;

        // Refine year
        HebrewCalendar hebrew = new HebrewCalendar(year, 7, 1);
        while (rd >= hebrew.toRD()) {
            year++;
            hebrew.set(year, 7, 1);
        }
        year--;
        hebrew.set(year, 7, 1);

        // Determine starting month
        hebrew.set(year, 1, 1);
        if (rd < hebrew.toRD()) {
            month = 7;
        } else {
            month = 1;
        }

        // Refine month
        int m = month;
        hebrew.set(year, m, lastDay(m, year));
        while (rd > hebrew.toRD()) {
            month++;
            m++;
            hebrew.set(year, m, lastDay(m, year));
        }

        // Calculate day
        hebrew.set(year, month, 1);
        day = (int) ((rd - hebrew.toRD()) + 1L);
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
     * Returns the name of the current month.
     *
     * @return the month name
     */
    @Override
    protected String monthName() {
        if ((month == 12) && isLeapYear(year)) {
            return MONTHS[month - 1] + " I";
        }
        return MONTHS[month - 1];
    }

    /**
     * Returns the era suffix for display.
     *
     * @return " A.M." (Anno Mundi)
     */
    @Override
    protected String getSuffix() {
        return " A.M.";
    }

    /**
     * Returns an enumeration of month names.
     *
     * @return month names enumeration
     */
    @Override
    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }
}

