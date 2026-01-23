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

import org.jscience.mathematics.algebraic.numbers.Rational;

import java.util.Enumeration;

/**
 * Implementation of the Old Hindu Solar calendar (Surya Siddhanta).
 * This is the traditional Hindu solar calendar based on the Surya Siddhanta
 * astronomical treatise, with solar months based on solar longitude.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: February 18, 3102 BCE (Julian) - Kali Yuga</li>
 *   <li>Uses sidereal year (star-to-star) length</li>
 *   <li>12 solar months of varying length</li>
 * </ul>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class OldHinduSolarCalendar extends MonthDayYear {

    private static final long serialVersionUID = 1L;
    /** The RD (Rata Die) number for the Old Hindu Solar calendar epoch (Kali Yuga). */
    protected static final long EPOCH = (new JulianCalendar(2, 18, -3102)).toRD();

    /** The length of the sidereal year in days. */
    protected static final Rational SIDEREALYEAR;

    /** The Jovian period (cycle of Jupiter) in days. */
    protected static final Rational JOVIANPERIOD = new Rational(0x5e0d1c3cL,
            0x58ec0L);

    /** The length of a solar month in days. */
    protected static final Rational SOLARMONTH;

    /** List of Hindu solar month names. */
    protected static final String[] MONTHS = {
            "Mesha", "Vrishabha", "Mithuna", "Karka", "Simha", "Kanya", "Tula",
            "Vrischika", "Dhanu", "Makara", "Kumbha", "Mina"
        };

    static {
        SIDEREALYEAR = new Rational(0x5e0d1c3cL, 0x41eb00L);
        SOLARMONTH = SIDEREALYEAR.divide(new Rational(12L));
    }

/**
     * Creates a new OldHinduSolarCalendar object.
     *
     * @param l the Rata Die number to set.
     */
    public OldHinduSolarCalendar(long l) {
        set(l);
    }

/**
     * Creates a new OldHinduSolarCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public OldHinduSolarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new OldHinduSolarCalendar object.
     */
    public OldHinduSolarCalendar() {
    }

/**
     * Creates a new OldHinduSolarCalendar object.
     *
     * @param i the month (1-12).
     * @param j the day.
     * @param k the year.
     */
    public OldHinduSolarCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    /**
     * Returns the number of days elapsed since the epoch.
     *
     * @return the day count.
     */
    public long dayCount() {
        return toRD() - EPOCH;
    }

    /**
     * Returns the year in the 60-year Jovian cycle.
     *
     * @return the Jovian year (1-60).
     */
    public int jovianYear() {
        Rational rational = new Rational((int) dayCount(), 1L);
        int i = (int) rational.divide(JOVIANPERIOD.divide(new Rational(12L)))
                              .floor();
        i = AlternateCalendar.mod(i, 60);

        return ++i;
    }

    /**
     * Recomputes the Rata Die number from the current month, day, and year.
     */
    protected synchronized void recomputeRD() {
        Rational rational = new Rational(EPOCH);
        rational = rational.add(SIDEREALYEAR.multiply(new Rational(super.year)));
        rational = rational.add(SOLARMONTH.multiply(
                    new Rational(super.month - 1)));
        rational = rational.add(new Rational(super.day));
        rational = rational.subtract(new Rational(1L, 4L));
        super.rd = rational.floor();
    }

    /**
     * Recomputes the month, day, and year from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        Rational rational = new Rational(1L, 4L);
        rational = rational.add(new Rational(dayCount()));

        Rational rational1 = rational.divide(SIDEREALYEAR);
        super.year = (int) rational1.floor();

        Rational rational2 = rational.divide(SOLARMONTH);
        super.month = AlternateCalendar.mod(rational2.floor(), 12) + 1;
        super.day = (int) rational.mod(SOLARMONTH).floor() + 1;
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
     * Sets the month, day, and year and recomputes the Rata Die number.
     *
     * @param i the month.
     * @param j the day.
     * @param k the year.
     */
    public synchronized void set(int i, int j, int k) {
        super.month = i;
        super.day = j;
        super.year = k;
        recomputeRD();
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
     * Returns the suffix for Old Hindu years (" K.Y.").
     *
     * @return the year suffix.
     */
    protected String getSuffix() {
        return " K.Y.";
    }

    /**
     * Returns an enumeration of all Hindu solar month names.
     *
     * @return enumeration of month names.
     */
    public Enumeration getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    /**
     * Main method for testing the Old Hindu Solar calendar implementation.
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

        OldHinduSolarCalendar oldhindusolar = new OldHinduSolarCalendar(gregorian);
        System.out.println(gregorian + ": " + oldhindusolar);
        oldhindusolar.set(i, j, k);
        System.out.println("OldHinduSolarCalendar(" + i + "," + j + "," + k +
            "): " + oldhindusolar);
        System.out.println(oldhindusolar.toRD());
        System.out.println("\n" + SOLARMONTH.toString() + "\n" +
            SIDEREALYEAR.toString() + "\n" +
            (new Rational(oldhindusolar.dayCount())).divide(SOLARMONTH)
             .toString());
    }
}
