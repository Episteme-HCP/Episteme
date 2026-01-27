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
 * Implementation of the Ethiopian calendar.
 * The Ethiopian calendar is based on the Coptic calendar and is the official
 * calendar of Ethiopia. It has 13 months - 12 of 30 days and one of 5-6 days.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: August 29, 8 CE (Julian)</li>
 *   <li>13 months: 12 months of 30 days + Paguemen (5 or 6 days)</li>
 *   <li>7-8 years behind the Gregorian calendar</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EthiopicCalendar extends CopticCalendar {

    private static final long serialVersionUID = 1L;
    /** The RD number of the Ethiopian epoch (August 29, 8 CE Julian). */
    public static long EPOCH = (new JulianCalendar(8, 29, 8)).toRD();

    /** List of Ethiopian month names. */
    private static final String[] MONTHS = {
            "Maskaram", "Teqemt", "Khedar", "Takhsas", "Ter", "Yakatit",
            "Magabit", "Miyazya", "Genbot", "Sane", "Hamle", "Nahase",
            "Paguemen"
        };

/**
     * Creates a new EthiopicCalendar object.
     */
    public EthiopicCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new EthiopicCalendar object.
     *
     * @param l the Rata Die number.
     */
    public EthiopicCalendar(long l) {
        set(l);
    }

/**
     * Creates a new EthiopicCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public EthiopicCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new EthiopicCalendar object.
     *
     * @param i the month (1-13).
     * @param j the day.
     * @param k the year.
     */
    public EthiopicCalendar(int i, int j, int k) {
        super(1, 1, 1);
        set(i, j, k);
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
        @SuppressWarnings("deprecation")
        long y = AlternateCalendar.fldiv((4L * (super.rd - EPOCH)) + 1463L, 1461L);
        super.year = (int)y;
        @SuppressWarnings("deprecation")
        long m = AlternateCalendar.fldiv(super.rd -
                (new EthiopicCalendar(1, 1, super.year)).toRD(), 30L);
        super.month = (int)m + 1;
        super.day = (int) ((super.rd + 1L) -
            (new EthiopicCalendar(super.month, 1, super.year)).toRD());
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
     * Returns an enumeration of all Ethiopian month names.
     *
     * @return enumeration of month names.
     */
    @Override
    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    /**
     * Main method for testing the Ethiopian calendar implementation.
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

        EthiopicCalendar ethiopic = new EthiopicCalendar(gregorian);
        System.out.println(gregorian + ": " + ethiopic);
        ethiopic.set(i, j, k);
        System.out.println("EthiopicCalendar(" + i + "," + j + "," + k + "): " +
            ethiopic);
        System.out.println(ethiopic.toRD());
    }
}
