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

/**
 * Implementation of the ISO 8601 week-date calendar.
 * The ISO week-date system is an international standard for representing
 * dates using week numbers instead of months.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Year-week-day format (e.g., 2024-W01-1)</li>
 *   <li>Weeks start on Monday (day 1) and end on Sunday (day 7)</li>
 *   <li>Week 1 contains January 4th (or the first Thursday of January)</li>
 *   <li>Years have either 52 or 53 weeks</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ISOCalendar extends GregorianCalendar {

    private static final long serialVersionUID = 1L;
    /** The ISO week number (1-53). */
    private int week;

    /** The ISO day number (1-7, Monday to Sunday). */
    private int day;

    /** The ISO year. */
    private int year;

/**
     * Creates a new ISOCalendar object.
     *
     * @param l the Rata Die number.
     */
    public ISOCalendar(long l) {
        set(l);
    }

/**
     * Creates a new ISOCalendar object.
     *
     * @param i the week number.
     * @param j the day of week.
     * @param k the year.
     */
    public ISOCalendar(int i, int j, int k) {
        super(1, 1, 1);
        set(i, j, k);
    }

/**
     * Creates a new ISOCalendar object.
     */
    public ISOCalendar() {
        this(GregorianCalendar.EPOCH);
    }

/**
     * Creates a new ISOCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public ISOCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    /**
     * Sets the ISO week components and recomputes the date.
     *
     * @param i the week.
     * @param j the day.
     * @param k the year.
     */
    public synchronized void set(int i, int j, int k) {
        week = i;
        day = j;
        year = k;
        recomputeRD();
    }

    /**
     * Returns the ISO year.
     *
     * @return the year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Recomputes the Rata Die number from current week components.
     */
    protected synchronized void recomputeRD() {
        GregorianCalendar gregorian = new GregorianCalendar(12, 28, year - 1);
        gregorian.nthKDay(week, 0);
        gregorian.add(day);
        super.rd = gregorian.toRD();
    }

    /**
     * Recomputes the ISO week components from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        int i = (new GregorianCalendar(super.rd - 3L)).getYear();
        ISOCalendar iso = new ISOCalendar(1, 1, i + 1);

        if (super.rd < iso.toRD()) {
            iso.set(1, 1, i);
        }

        year = iso.getYear();
        week = (int) AlternateCalendar.floorDiv(super.rd - iso.toRD(), 7L) + 1;
        day = (int) AlternateCalendar.amod(super.rd, 7L);
    }

    /**
     * Returns the ISO week number.
     *
     * @return the week number (1-53).
     */
    public int getWeek() {
        return week;
    }

    /**
     * Returns the ISO day number.
     *
     * @return the day number (1-7).
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns a string representation of the ISO week-date.
     *
     * @return the date string.
     */
    public String toString() {
        try {
            return SevenDaysWeek.DAY_NAMES[AlternateCalendar.mod(getDay(), 7)] +
            " of week " + getWeek() + ", " + getYear();
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return getDay() + " " + getWeek() + " " + getYear();
        }
    }

    /**
     * Returns 0 as ISO weeks are not defined within standard months.
     *
     * @return 0.
     */
    public int getMonth() {
        return 0;
    }

    /**
     * Main method for testing the ISOCalendar implementation.
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

        ISOCalendar iso = new ISOCalendar(gregorian);
        System.out.println(gregorian + ": " + iso);
        iso.set(i, j, k);
        System.out.println("ISO(" + i + "," + j + "," + k + "): " + iso);
    }
}
