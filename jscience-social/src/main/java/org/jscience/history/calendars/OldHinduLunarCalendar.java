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
 * Implementation of the Old Hindu Lunar calendar (Surya Siddhanta).
 * This is the traditional Hindu lunar calendar with months starting at new moon.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: Kali Yuga (3102 BCE)</li>
 *   <li>Lunar months (amānta - new moon to new moon)</li>
 *   <li>Leap months (adhika māsa) to synchronize with solar year</li>
 *   <li>Lunar days (tithis) may be skipped or doubled</li>
 * </ul>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class OldHinduLunarCalendar extends OldHinduSolarCalendar {

    private static final long serialVersionUID = 1L;
    /** List of Hindu lunar month names. */
    protected static final String[] MONTHS = {
            "Chaitra", "Vaisakha", "Jyaishtha", "Ashadha", "Sravana",
            "Bhadrapada", "Asvina", "Karttika", "Margasira", "Pausha", "Magha",
            "Phalguna"
        };

    /** The length of a synodic month (new moon to new moon) in days. */
    protected static final Rational LUNARMONTH;

    /** The length of a tithi (lunar day) in days. */
    protected static final Rational LUNARDAY;

    /** A constant used in leap month calculations. */
    private static final Rational LCONST;

    /** The difference between a solar month and a lunar month. */
    private static final Rational MONTHDIFF;

    static {
        LUNARMONTH = new Rational(0x5e0d1c3cL, 0x32f53f8L);
        LUNARDAY = LUNARMONTH.divide(new Rational(30L));
        LCONST = LUNARMONTH.subtract(OldHinduSolarCalendar.SIDEREALYEAR.mod(
                    LUNARMONTH));
        MONTHDIFF = OldHinduSolarCalendar.SOLARMONTH.subtract(LUNARMONTH);
    }

    /** True if the current month is a leap month (adhika māsa). */
    protected boolean leap;

/**
     * Creates a new OldHinduLunarCalendar object.
     */
    public OldHinduLunarCalendar() {
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param l the Rata Die number to set.
     */
    public OldHinduLunarCalendar(long l) {
        super(l);
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public OldHinduLunarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param i the month (1-12).
     * @param j the day (1-30).
     * @param k the year.
     */
    public OldHinduLunarCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param i    the month (1-12).
     * @param flag true if it is a leap month.
     * @param j    the day (1-30).
     * @param k    the year.
     */
    public OldHinduLunarCalendar(int i, boolean flag, int j, int k) {
        set(i, flag, j, k);
    }

    /**
     * Checks if the given year is a leap year (contains a leap month).
     *
     * @param i the year to check.
     * @return true if it is a leap year, false otherwise.
     */
    protected static boolean isLeapYear(int i) {
        Rational rational = OldHinduSolarCalendar.SIDEREALYEAR.multiply(new Rational(
                    i));
        rational = rational.subtract(OldHinduSolarCalendar.SOLARMONTH);
        rational = rational.mod(LUNARMONTH);

        return gte(rational, LCONST);
    }

    /**
     * Recomputes the month, day, year, and leap month status from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        Rational rational = (new Rational(1L, 4L)).add(new Rational(dayCount()));
        Rational rational1 = rational.subtract(rational.mod(LUNARMONTH));
        Rational rational2 = rational1.mod(OldHinduSolarCalendar.SOLARMONTH);
        leap = gte(MONTHDIFF, rational2) && gt(rational2, new Rational(0L));
        super.month = AlternateCalendar.mod(rational1.divide(
                    OldHinduSolarCalendar.SOLARMONTH).ceil(), 12) + 1;
        super.day = AlternateCalendar.mod(rational.divide(LUNARDAY).floor(), 30) +
            1;
        super.year = (int) rational1.add(OldHinduSolarCalendar.SOLARMONTH)
                                    .divide(OldHinduSolarCalendar.SIDEREALYEAR)
                                    .ceil() - 1;
    }

    /**
     * Recomputes the Rata Die number from the current month, day, year, and leap month status.
     *
     * @throws InconsistentDateException if the date is invalid or skipped.
     */
    protected synchronized void recomputeRD() {
        Rational rational = OldHinduSolarCalendar.SOLARMONTH.multiply(new Rational((12 * super.year) -
                    1));
        Rational rational1 = LUNARMONTH.multiply(new Rational(rational.divide(
                        new Rational(LUNARMONTH)).floor() + 1L));
        long l;

        if (!leap &&
                (rational1.subtract(rational).divide(MONTHDIFF).ceil() <= (long) super.month)) {
            l = super.month;
        } else {
            l = super.month - 1;
        }

        Rational rational2 = rational1.add(LUNARMONTH.multiply(new Rational(l)));
        rational2 = rational2.add(LUNARDAY.multiply(new Rational(super.day - 1)));
        rational2 = rational2.add(new Rational(3L, 4L));
        super.rd = rational2.floor() + OldHinduSolarCalendar.EPOCH;

        if (lt(rational2.fractionalPart().add(LUNARDAY), new Rational(1L))) {
            throw new InconsistentDateException("Lost lunar day");
        }

        if (leap) {
            Rational rational3 = rational1.mod(OldHinduSolarCalendar.SOLARMONTH);
            rational3 = rational3.subtract(MONTHDIFF.multiply(
                        new Rational(super.month - 1)));

            if (!lte(rational3.mod(OldHinduSolarCalendar.SOLARMONTH), MONTHDIFF)) {
                throw new InconsistentDateException("Month not leap that year");
            }
        }
    }

    /**
     * Sets the month, day, and year, assuming it's not a leap month.
     *
     * @param i the month.
     * @param j the day.
     * @param k the year.
     */
    public synchronized void set(int i, int j, int k) {
        set(i, false, j, k);
    }

    /**
     * Sets the month, leap status, day, and year.
     *
     * @param i    the month.
     * @param flag true if leap month.
     * @param j    the day.
     * @param k    the year.
     */
    public synchronized void set(int i, boolean flag, int j, int k) {
        super.month = i;
        leap = flag;
        super.day = j;
        super.year = k;
        recomputeRD();
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
     * Returns the name of the current month, indicating if it is a leap month.
     *
     * @return the month name string.
     */
    protected String monthName() {
        return MONTHS[super.month - 1] + (leap ? " (leap)" : "");
    }

    /**
     * Returns the suffix for Old Hindu lunar years (" K.Y.").
     *
     * @return the year suffix.
     */
    protected String getSuffix() {
        return " K.Y.";
    }

    /**
     * Returns an enumeration of all Hindu lunar month names.
     *
     * @return enumeration of month names.
     */
    public Enumeration getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    /**
     * Returns the leap month status of the current date.
     *
     * @return true if it is a leap month.
     */
    public boolean getLeap() {
        return leap;
    }

    /**
     * Main method for testing the Old Hindu Lunar calendar implementation.
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

        OldHinduLunarCalendar oldhindulunar = new OldHinduLunarCalendar(gregorian);
        System.out.println(gregorian + ": " + oldhindulunar);
        oldhindulunar.set(i, false, j, k);
        System.out.println("OldHinduLunar(" + i + "," + false + "," + j + "," +
            k + "): " + oldhindulunar);
        System.out.println(oldhindulunar.toRD());
        oldhindulunar.set(i, true, j, k);
        System.out.println("OldHinduLunar(" + i + "," + true + "," + j + "," +
            k + "): " + oldhindulunar);
        System.out.println(oldhindulunar.toRD());
    }

    /**
     * Performs a "greater than" comparison between two Rational numbers.
     *
     * @param rational1 the first number.
     * @param rational2 the second number.
     * @return true if rational1 > rational2.
     */
    private static boolean gt(Rational rational1, Rational rational2) {
        return rational2.subtract(rational1).signum() < 0;
    }

    /**
     * Performs a "less than or equal to" comparison between two Rational numbers.
     *
     * @param rational1 the first number.
     * @param rational2 the second number.
     * @return true if rational1 <= rational2.
     */
    private static boolean lte(Rational rational1, Rational rational2) {
        return !gt(rational1, rational2);
    }

    /**
     * Performs a "less than" comparison between two Rational numbers.
     *
     * @param rational1 the first number.
     * @param rational2 the second number.
     * @return true if rational1 < rational2.
     */
    private static boolean lt(Rational rational1, Rational rational2) {
        return gt(rational2, rational1);
    }

    /**
     * Performs a "greater than or equal to" comparison between two Rational numbers.
     *
     * @param rational1 the first number.
     * @param rational2 the second number.
     * @return true if rational1 >= rational2.
     */
    private static boolean gte(Rational rational1, Rational rational2) {
        return !lt(rational1, rational2);
    }
}
