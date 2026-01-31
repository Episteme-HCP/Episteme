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
 * Implementation of the Coptic calendar.
 * The Coptic calendar is used by the Coptic Orthodox Church and is
 * based on the ancient Egyptian calendar with leap year rules.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: August 29, 284 CE (Julian) - Era of Martyrs</li>
 *   <li>13 months: 12 of 30 days + Nasie (5 or 6 days)</li>
 *   <li>Leap year every 4 years (without century exception)</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CopticCalendar extends JulianCalendar {

    private static final long serialVersionUID = 1L;
    /** The RD number of the Coptic epoch (August 29, 284 CE Julian). */
    public static long EPOCH = (new JulianCalendar(8, 29, 284)).toRD();

    /** List of Coptic month names. */
    private static final String[] MONTHS = {
            "Tut", "Babah", "Hatur", "Kiyahk", "Tubah", "Amshir", "Baramhat",
            "Baramundah", "Bashans", "Ba'unah", "Abib", "Misra", "al-Nasi"
        };

/**
     * Creates a new CopticCalendar object.
     */
    public CopticCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new CopticCalendar object.
     *
     * @param l the Rata Die number.
     */
    public CopticCalendar(long l) {
        set(l);
    }

/**
     * Creates a new CopticCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public CopticCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new CopticCalendar object.
     *
     * @param i the month (1-13).
     * @param j the day.
     * @param k the year.
     */
    public CopticCalendar(int i, int j, int k) {
        super(1, 1, 1);
        set(i, j, k);
    }

    /**
     * Checks if the given year is a leap year in the Coptic calendar.
     *
     * @param i the year to check.
     * @return true if it is a leap year.
     */
    public static boolean isLeapYear(int i) {
        return AlternateCalendar.mod(i, 4) == 3;
    }

    /**
     * Recomputes the Rata Die number from the current month, day, and year.
     */
    protected synchronized void recomputeRD() {
            @SuppressWarnings("deprecation")
            long q = AlternateCalendar.fldiv(super.year, 4L);
            super.rd = (EPOCH - 1L) + (long) (365 * (super.year - 1)) +
            q +
            (long) (30 * (super.month - 1)) + (long) super.day;
    }

    /**
     * Recomputes the month, day, and year from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        super.year = (int) AlternateCalendar.floorDiv((4L * (super.rd - EPOCH)) +
                1463L, 1461L);
        super.month = (int) AlternateCalendar.floorDiv(super.rd -
                (new CopticCalendar(1, 1, super.year)).toRD(), 30L) + 1;
        super.day = (int) ((super.rd + 1L) -
            (new CopticCalendar(super.month, 1, super.year)).toRD());
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
     * Returns the era suffix for Coptic years (empty).
     *
     * @return an empty string.
     */
    protected String getSuffix() {
        return "";
    }

    /**
     * Returns an enumeration of all Coptic month names.
     *
     * @return enumeration of month names.
     */
    @Override
    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    /**
     * Returns a string representation of the Coptic date.
     *
     * @return the date string.
     */
    public String toString() {
        try {
            return super.day + " " + monthName() + " " + super.year +
            getSuffix();
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return "Invalid date: " + super.month + " " + super.day + " " +
            super.year;
        }
    }

    /**
     * Main method for testing the Coptic calendar implementation.
     *
     * @param args command line arguments.
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

        CopticCalendar coptic = new CopticCalendar(gregorian);
        System.out.println(gregorian + ": " + coptic);
        coptic.set(i, j, k);
        System.out.println("CopticCalendar(" + i + "," + j + "," + k + "): " +
            coptic);
        System.out.println(coptic.toRD());
    }
}

