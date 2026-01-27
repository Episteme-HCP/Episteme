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

package org.jscience.history.calendars;

import java.util.Enumeration;

/**
 * Implementation of the Persian (Solar Hijri) calendar.
 * The Persian calendar is a solar calendar used officially in Iran and Afghanistan.
 * It is one of the most accurate calendars in use.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: March 19, 622 CE (Julian) - the Hijra</li>
 *   <li>Uses a 2820-year grand cycle for leap year calculation</li>
 *   <li>First 6 months have 31 days, next 5 have 30, last has 29 (30 in leap years)</li>
 *   <li>Years are marked with "A.P." (Anno Persico)</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PersianCalendar extends MonthDayYear {

    private static final long serialVersionUID = 1L;
    /** The RD (Rata Die) number for the Persian calendar epoch. */
    public static long EPOCH = (new JulianCalendar(3, 19, 622)).toRD();

    /** The RD number for the start of the year 475, used in calculations. */
    private static final long FOUR75 = (new PersianCalendar(1, 1, 475)).toRD();

    /** List of Persian month names. */
    private static final String[] MONTHS = {
            "Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar",
            "Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand"
        };

/**
     * Creates a new PersianCalendar object.
     */
    public PersianCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new PersianCalendar object.
     *
     * @param l the Rata Die number to set.
     */
    public PersianCalendar(long l) {
        set(l);
    }

/**
     * Creates a new PersianCalendar object.
     *
     * @param i the month (1-12).
     * @param j the day (1-31).
     * @param k the year.
     */
    public PersianCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new PersianCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public PersianCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    /**
     * Converts a year to a zero-indexed value relative to year 474.
     *
     * @param i the year to convert.
     * @return the zero-indexed year value.
     */
    private static long yconv(long i) {
        if (i > 0) {
            return i - 474;
        } else {
            return i - 473;
        }
    }

    /**
     * Computes the Rata Die number from the given Persian date.
     *
     * @param year the year.
     * @param month the month.
     * @param day the day.
     * @return the Rata Die number.
     */
    public static long fixedFromPersian(long year, int month, int day) {
        long i = yconv(year);
        long j = AlternateCalendar.mod(i, 2820) + 474;
        return (EPOCH - 1L) +
            (0xfb75fL * AlternateCalendar.floorDiv(i, 2820L)) +
            (365L * (j - 1)) +
            AlternateCalendar.floorDiv((682L * j) - 110L, 2816L) +
            (long) ((month > 7) ? ((30 * (month - 1)) + 6)
                                      : (31 * (month - 1))) +
            (long) day;
    }

    /**
     * Recomputes the Rata Die number from the current month, day, and year.
     */
    protected synchronized void recomputeRD() {
        super.rd = fixedFromPersian(super.year, super.month, super.day);
    }

    /**
     * Recomputes the month, day, and year from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        long l = super.rd - FOUR75;
        int j = AlternateCalendar.mod(l, 0xfb75f);

        if (j == 0xfb75e) {
        } else {
             // Side-effect only? No, k was unused. removed.
        }

        long year = AlternateCalendar.floorDiv((((super.rd - EPOCH) + 266) *
                2820) + 266, 1029983);
        long dayOfYear = (super.rd - EPOCH) + 1 -
            (365 * (year - 1)) -
            AlternateCalendar.floorDiv((8 * (year - 1)) + 21, 33);

        int month;
        if (dayOfYear <= 186) {
            month = (int) AlternateCalendar.floorDiv(dayOfYear - 1, 31) + 1;
        } else {
            month = (int) AlternateCalendar.floorDiv(dayOfYear - 187, 30) + 7;
        }

        long day = (super.rd - fixedFromPersian(year, month, 1)) + 1;

        super.year = (int) year;
        super.month = month;
        super.day = (int) day;
    }

    /**
     * Sets the Rata Die number and recomputes the date.
     *
     * @param l the Rata Die number.
     */
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    /**
     * Checks if the given year is a leap year in the Persian calendar.
     *
     * @param i the year to check.
     * @return true if it is a leap year, false otherwise.
     */
    public static boolean isLeapYear(int i) {
        long j = yconv(i);
        int k = AlternateCalendar.mod(j, 2820) + 474;

        return AlternateCalendar.mod((k + 38) * 682, 2816) < 682;
    }

    /**
     * Returns the name of the current month.
     *
     * @return the month name string.
     */
    protected String monthName() {
        return MONTHS[super.month - 1];
    }

    /**
     * Returns the suffix for Persian years ("A.P.").
     *
     * @return the year suffix.
     */
    protected String getSuffix() {
        return " A.P.";
    }

    /**
     * Returns an enumeration of all month names.
     *
     * @return enumeration of month names.
     */
    @Override
    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    /**
     * Main method for testing the Persian calendar implementation.
     *
     * @param args command line arguments (year, month, day).
     */
    public static void main(String[] args) {
        int i;
        int j;
        int k;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception _ex) {
            i = k = j = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        PersianCalendar persian = new PersianCalendar(gregorian);
        System.out.println(gregorian + ": " + persian);
        persian.set(i, j, k);
        System.out.println("PersianCalendar(" + i + "," + j + "," + k + "): " +
            persian);
        System.out.println(persian.toRD());
    }
}
