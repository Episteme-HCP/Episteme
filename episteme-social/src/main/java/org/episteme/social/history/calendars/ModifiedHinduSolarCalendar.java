/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.history.calendars;


/**
 * Modified Hindu Solar calendar using floating-point arithmetic.
 * This calendar uses modern astronomical calculations for more accurate
 * solar month determinations.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ModifiedHinduSolarCalendar extends OldHinduSolarCalendar {

    private static final long serialVersionUID = 1L;
    /** Offset for the solar era (Saka Era). */
    protected static final int SOLARERA = 3179;

    /** Delegate for Hindu astronomical calculations. */
    protected static ModifiedHinduCalendar mh;

/**
     * Creates a new ModifiedHinduSolarCalendar object.
     *
     * @param l Rata Die number.
     */
    public ModifiedHinduSolarCalendar(long l) {
        super(l);
    }

/**
     * Creates a new ModifiedHinduSolarCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public ModifiedHinduSolarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new ModifiedHinduSolarCalendar object.
     */
    public ModifiedHinduSolarCalendar() {
    }

/**
     * Creates a new ModifiedHinduSolarCalendar object.
     *
     * @param i the month.
     * @param j the day.
     * @param k the year.
     */
    public ModifiedHinduSolarCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    /**
     * Recomputes the Hindu solar date components from the current Rata Die number.
     */
    public synchronized void recomputeFromRD() {
        long l = dayCount();
        double d = ModifiedHinduCalendar.sunrise(l);
        super.month = ModifiedHinduCalendar.zodiac(d);
        super.year = ModifiedHinduCalendar.calYear(d) - 3179;

        long l1;

        for (l1 = l - 3L -
                (long) Math.floor(
                    (((ModifiedHinduCalendar.solarLongitude(d) % 1800D) +
                    1800D) % 1800D) / 60D);
                ModifiedHinduCalendar.zodiac(ModifiedHinduCalendar.sunrise(l1)) != super.month;
                l1++)
            ;

        super.day = (int) ((l - l1) + 1L);
    }

    /**
     * Checks if this date precedes another Hindu solar date.
     *
     * @param modhindusolar the other date.
     * @return true if this date is before the other.
     */
    public boolean precedes(ModifiedHinduSolarCalendar modhindusolar) {
        return (super.year < ((MonthDayYear) (modhindusolar)).year) ||
        ((super.year == ((MonthDayYear) (modhindusolar)).year) &&
        ((super.month < ((MonthDayYear) (modhindusolar)).month) ||
        ((super.month == ((MonthDayYear) (modhindusolar)).month) &&
        (super.day < ((MonthDayYear) (modhindusolar)).day))));
    }

    /**
     * Recomputes the Rata Die number from the current Hindu solar date components.
     *
     * @throws InconsistentDateException if the date is invalid.
     */
    public synchronized void recomputeRD() {
        long l = (long) Math.floor(((((double) (super.year + 3179) +
                ((double) (super.month - 1) / 12D)) * OldHinduSolarCalendar.SIDEREALYEAR.doubleValue()) +
                (double) OldHinduSolarCalendar.EPOCH + (double) super.day) -
                9D);
        ModifiedHinduSolarCalendar modhindusolar;

        for (modhindusolar = new ModifiedHinduSolarCalendar(l);
                modhindusolar.precedes(this);
                modhindusolar = new ModifiedHinduSolarCalendar(++l))
            ;

        super.rd = l;

        if ((((MonthDayYear) (modhindusolar)).day != super.day) ||
                (((MonthDayYear) (modhindusolar)).month != super.month) ||
                (((MonthDayYear) (modhindusolar)).year != super.year)) {
            throw new InconsistentDateException("Illegal Hindu Solar Date");
        } else {
            return;
        }
    }

    /**
     * Returns the era suffix for this calendar.
     *
     * @return the suffix string.
     */
    protected String getSuffix() {
        return " S.E.";
    }

    /**
     * Main method for testing Hindu solar calendar calculations.
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

        ModifiedHinduSolarCalendar modhindusolar = new ModifiedHinduSolarCalendar(gregorian);
        System.out.println(gregorian + "(" + modhindusolar.toRD() + "): " +
            modhindusolar);
        System.out.println("===============");
        modhindusolar = new ModifiedHinduSolarCalendar(i, j, k);
        System.out.println("HinduCalendar " + i + " " + j + " " + k + ": " +
            modhindusolar.toRD());
    }
}

