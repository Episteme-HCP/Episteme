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
 * Implementation of the proleptic Gregorian calendar.
 * The Gregorian calendar is the internationally accepted civil calendar introduced by
 * Pope Gregory XIII in October 1582. This implementation extends the calendar backwards
 * in time (proleptic) for dates before its actual adoption.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>A year is 365 days, with leap years adding one day</li>
 *   <li>Leap year rule: divisible by 4, except centuries not divisible by 400</li>
 *   <li>12 months with varying lengths (28-31 days)</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GregorianCalendar extends MonthDayYear {

    private static final long serialVersionUID = 1L;

    /** Month names in English. */
    protected static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    /** The epoch: January 1, 1 CE (Rata Die 1). */
    protected static long EPOCH = 1L;

    /** Number of days in 400 years (including leap days): 146097. */
    private static final int FOUR_CENTURY = 146097;

    /** Number of days in 100 years (excluding end-of-century leap logic): 36524. */
    private static final int CENTURY = 36524;

    /** Number of days in 4 years (including one leap day): 1461. */
    private static final int FOUR_YEAR = 1461;

    /** Number of days in a non-leap year: 365. */
    private static final int YEAR = 365;

    /** Days in each month (non-leap year). */
    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * Creates a Gregorian calendar set to the specified Rata Die.
     *
     * @param rataDie the day number
     */
    public GregorianCalendar(long rataDie) {
        set(rataDie);
    }

    /**
     * Creates a Gregorian calendar set to the specified date.
     *
     * @param year  the year (negative for BCE)
     * @param month the month (1-12)
     * @param day   the day of month (1-31)
     */
    public GregorianCalendar(int year, int month, int day) {
        set(year, month, day);
    }

    /**
     * Creates a Gregorian calendar set to the epoch (January 1, 1 CE).
     */
    public GregorianCalendar() {
        this(EPOCH);
    }

    /**
     * Creates a Gregorian calendar from another calendar's date.
     *
     * @param calendar the source calendar
     */
    public GregorianCalendar(AlternateCalendar calendar) {
        this(calendar.toRD());
    }

    /**
     * Determines if the specified year is a leap year in the Gregorian calendar.
     *
     * @param year the year to check
     * @return true if it's a leap year
     */
    public static boolean isLeapYear(int year) {
        if (AlternateCalendar.mod(year, 4) != 0) {
            return false;
        }
        int mod400 = AlternateCalendar.mod(year, 400);
        return (mod400 != 100) && (mod400 != 200) && (mod400 != 300);
    }

    /**
     * Recomputes the Rata Die from the current year, month, and day fields.
     */
    @Override
    protected synchronized void recomputeRD() {
        int priorYear = year - 1;
        rd = (EPOCH - 1L) 
            + (365L * priorYear)
            + AlternateCalendar.floorDiv(priorYear, 4L)
            - AlternateCalendar.floorDiv(priorYear, 100L)
            + AlternateCalendar.floorDiv(priorYear, 400L)
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
     * Recomputes the year, month, and day fields from the current Rata Die.
     */
    @Override
    protected synchronized void recomputeFromRD() {
        long days = rd - EPOCH;
        
        int n400 = (int) AlternateCalendar.floorDiv(days, FOUR_CENTURY);
        int d400 = AlternateCalendar.mod(days, FOUR_CENTURY);
        
        int n100 = (int) AlternateCalendar.floorDiv(d400, CENTURY);
        int d100 = AlternateCalendar.mod(d400, CENTURY);
        
        int n4 = (int) AlternateCalendar.floorDiv(d100, FOUR_YEAR);
        int d4 = AlternateCalendar.mod(d100, FOUR_YEAR);
        
        int n1 = (int) AlternateCalendar.floorDiv(d4, YEAR);
        
        year = (400 * n400) + (100 * n100) + (4 * n4) + n1;

        int dayOfYear = AlternateCalendar.mod(d4, YEAR) + 1;
        month = 1;
        day = dayOfYear;

        // Adjust for last day of 400-year or 4-year cycle
        if ((n100 != 4) && (n1 != 4)) {
            year++;
        }

        // Calculate month and day from day of year
        for (int m = 0; m < 12; m++) {
            int daysInMonth = DAYS_IN_MONTH[m];
            if (m == 1 && isLeapYear(year)) {
                daysInMonth = 29;
            }
            
            if (day <= daysInMonth) {
                break;
            }
            
            day -= daysInMonth;
            month++;
        }
    }

    /**
     * Returns the day number within the year (1-366).
     *
     * @return the day of year
     */
    public int dayNumber() {
        return (int) difference(new GregorianCalendar(getYear() - 1, 12, 31));
    }

    /**
     * Returns the number of days remaining in the year.
     *
     * @return days left until year end
     */
    public int daysLeft() {
        return (int) AlternateCalendar.difference(
            new GregorianCalendar(getYear(), 12, 31), this);
    }

    /**
     * Returns the era suffix (e.g., "CE" or "BCE"). Subclasses may override.
     *
     * @return an empty string for the standard Gregorian calendar
     */
    protected String getSuffix() {
        return "";
    }

    /**
     * Returns the name of the current month.
     *
     * @return the month name
     */
    protected String monthName() {
        return MONTHS[month - 1];
    }

    /**
     * Returns an enumeration of month names.
     *
     * @return month names enumeration
     */
    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    @Override
    public String toString() {
        return String.format("%s %d, %d%s", monthName(), day, year, getSuffix());
    }
}

