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
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
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
    private static int yconv(int i) {
        if (i > 0) {
            return i - 474;
        } else {
            return i - 473;
        }
    }

    /**
     * Recomputes the Rata Die number from the current month, day, and year.
     */
    protected synchronized void recomputeRD() {
        int i = yconv(super.year);
        int j = AlternateCalendar.mod(i, 2820) + 474;
        super.rd = (EPOCH - 1L) +
            (0xfb75fL * AlternateCalendar.fldiv(i, 2820L)) +
            (long) (365 * (j - 1)) +
            AlternateCalendar.fldiv((682 * j) - 110, 2816L) +
            (long) ((super.month > 7) ? ((30 * (super.month - 1)) + 6)
                                      : (31 * (super.month - 1))) +
            (long) super.day;
    }

    /**
     * Recomputes the month, day, and year from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        long l = super.rd - FOUR75;
        int i = (int) AlternateCalendar.fldiv(l, 0xfb75fL);
        int j = AlternateCalendar.mod(l, 0xfb75f);
        int k;

        if (j == 0xfb75e) {
            k = 2820;
        } else {
            k = (int) AlternateCalendar.fldiv((2816L * (long) j) + 0xfbca9L,
                    0xfb1aaL);
        }

        super.year = 474 + (2820 * i) + k;

        if (super.year <= 0) {
            super.year--;
        }

        int i1 = (int) ((super.rd -
            (new PersianCalendar(1, 1, super.year)).toRD()) + 1L);

        if (i1 <= 186) {
            super.month = (int) Math.ceil((double) i1 / 31D);
        } else {
            super.month = (int) Math.ceil((double) (i1 - 6) / 30D);
        }

        super.day = (int) ((super.rd -
            (new PersianCalendar(super.month, 1, super.year)).toRD()) + 1L);
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
        int j = yconv(i);
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
