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

import org.jscience.mathematics.algebraic.numbers.ExactRational;

/**
 * Modified Hindu Lunar calendar using arbitrary-precision arithmetic.
 * This version uses ExactRational for precise lunar calculations
 * without floating-point errors.
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class ModifiedHinduLunarBRCalendar extends OldHinduLunarCalendar {

    private static final long serialVersionUID = 1L;
    /** Offset for the lunar era (Vikrama Era). */
    protected static final int LUNARERA = 3044;

    /** Delegate for Hindu astronomical calculations. */
    protected static ModifiedHinduBRCalendar mh;

    /** True if this is a duplicated (leap) day. */
    protected boolean leapday;

/**
     * Creates a new ModifiedHinduLunarBRCalendar object.
     *
     * @param l Rata Die number.
     */
    public ModifiedHinduLunarBRCalendar(long l) {
        super(l);
    }

/**
     * Creates a new ModifiedHinduLunarBRCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public ModifiedHinduLunarBRCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new ModifiedHinduLunarBRCalendar object.
     */
    public ModifiedHinduLunarBRCalendar() {
    }

/**
     * Creates a new ModifiedHinduLunarBRCalendar object.
     *
     * @param i the month number.
     * @param flag true if leap month.
     * @param j the day number.
     * @param flag1 true if leap day.
     * @param k the year number.
     */
    public ModifiedHinduLunarBRCalendar(int i, boolean flag, int j,
        boolean flag1, int k) {
        set(i, flag, j, flag1, k);
    }

    /**
     * Sets the Hindu lunar date components.
     *
     * @param i the month number.
     * @param flag true if leap month.
     * @param j the day number.
     * @param flag1 true if leap day.
     * @param k the year number.
     */
    public synchronized void set(int i, boolean flag, int j, boolean flag1,
        int k) {
        super.month = i;
        super.leap = flag;
        super.day = j;
        leapday = flag1;
        super.year = k;
        recomputeRD();
    }

    /**
     * Recomputes the Hindu lunar date components from the current Rata Die number.
     */
    public synchronized void recomputeFromRD() {
        ExactRational bigrational = new ExactRational(super.rd -
                OldHinduSolarCalendar.EPOCH);
        ExactRational bigrational1 = ModifiedHinduBRCalendar.sunrise(bigrational);
        super.day = ModifiedHinduBRCalendar.lunarDay(bigrational1);
        leapday = super.day == ModifiedHinduBRCalendar.lunarDay(ModifiedHinduBRCalendar.sunrise(
                    bigrational.subtract(ExactRational.ONE)));

        ExactRational bigrational2 = ModifiedHinduBRCalendar.newMoon(bigrational1);
        ExactRational bigrational3 = ModifiedHinduBRCalendar.newMoon((new ExactRational(
                    35L)).add(bigrational2.floor()));
        int i = ModifiedHinduBRCalendar.zodiac(bigrational2);
        super.leap = i == ModifiedHinduBRCalendar.zodiac(bigrational3);
        super.month = (int) AlternateCalendar.amod(i + 1, 12L);
        super.year = ModifiedHinduBRCalendar.calYear(bigrational3) - 3044 -
            ((!super.leap || (super.month != 1)) ? 0 : (-1));
    }

    /**
     * Checks if this date precedes another Hindu lunar date.
     *
     * @param modhindulunarbr the other date.
     * @return true if this date is before the other.
     */
    public boolean precedes(ModifiedHinduLunarBRCalendar modhindulunarbr) {
        return (super.year < ((MonthDayYear) (modhindulunarbr)).year) ||
        ((super.year == ((MonthDayYear) (modhindulunarbr)).year) &&
        ((super.month < ((MonthDayYear) (modhindulunarbr)).month) ||
        ((super.month == ((MonthDayYear) (modhindulunarbr)).month) &&
        ((super.leap && !((OldHinduLunarCalendar) (modhindulunarbr)).leap) ||
        ((super.leap == ((OldHinduLunarCalendar) (modhindulunarbr)).leap) &&
        ((super.day < ((MonthDayYear) (modhindulunarbr)).day) ||
        ((super.day == ((MonthDayYear) (modhindulunarbr)).day) && !leapday &&
        modhindulunarbr.leapday)))))));
    }

    /**
     * Recomputes the Rata Die number from the current Hindu lunar date components.
     *
     * @throws InconsistentDateException if the date is invalid.
     */
    public synchronized void recomputeRD() {
        int i = super.year + 3044;
        OldHinduLunarCalendar oldhindulunar = new OldHinduLunarCalendar(1L);

        try {
            oldhindulunar.set(super.month, super.leap, super.day, i);
        } catch (InconsistentDateException _ex) {
        }

        long l = oldhindulunar.toRD();
        long l1;

        if ((new ModifiedHinduLunarBRCalendar(l + 15L)).precedes(this)) {
            l1 = l + ModifiedHinduBRCalendar.SYNODICMONTH.longValue();
        } else if (precedes(new ModifiedHinduLunarBRCalendar(l - 15L))) {
            l1 = l - ModifiedHinduBRCalendar.SYNODICMONTH.longValue() - 1L;
        } else {
            l1 = l;
        }

        System.err.println("approx: " + l1);

        long l2 = l1 - 4L;
        long l3 = l1 + 4L;
        long l4 = (l2 + l3) / 2L;

        do {
            l4 = (l2 + l3) / 2L;
            System.err.println("current trie: " + l4);

            if ((new ModifiedHinduLunarBRCalendar(l4)).precedes(this)) {
                l2 = l4;
            } else {
                l3 = l4;
            }
        } while ((l3 - l2) > 2L);

        System.err.println("trie: " + l4);

        ModifiedHinduLunarBRCalendar modhindulunarbr = new ModifiedHinduLunarBRCalendar(l4);

        if ((super.day == ((MonthDayYear) (modhindulunarbr)).day) &&
                (leapday == modhindulunarbr.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunarbr)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunarbr)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunarbr)).year)) {
            super.rd = l4;

            return;
        }

        modhindulunarbr = new ModifiedHinduLunarBRCalendar(l4 - 1L);

        if ((super.day == ((MonthDayYear) (modhindulunarbr)).day) &&
                (leapday == modhindulunarbr.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunarbr)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunarbr)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunarbr)).year)) {
            super.rd = l4 - 1L;

            return;
        }

        modhindulunarbr = new ModifiedHinduLunarBRCalendar(l4 + 1L);

        if ((super.day == ((MonthDayYear) (modhindulunarbr)).day) &&
                (leapday == modhindulunarbr.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunarbr)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunarbr)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunarbr)).year)) {
            super.rd = l4 + 1L;

            return;
        } else {
            super.rd = l4;
            throw new InconsistentDateException(
                "Invalid Hindu Lunar Date (RD: " + super.rd + "?)");
        }
    }

    /**
     * Returns true if the current day is a leap day.
     *
     * @return true if leap day.
     */
    public boolean getLeapDay() {
        return leapday;
    }

    /**
     * Returns a string representation of the Hindu lunar date.
     *
     * @return string representation.
     */
    public String toString() {
        return super.month + "(" + super.leap + ") " + super.day + "(" +
        leapday + ") " + super.year;
    }

    /**
     * Main method for testing Hindu lunar calendar calculations.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        int i;
        int j;
        int k;
        boolean flag;
        boolean flag1;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
            flag = args[3].equals("l");
            flag1 = args[4].equals("ld");
        } catch (Exception _ex) {
            i = k = j = 1;
            flag = flag1 = false;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        ModifiedHinduLunarBRCalendar modhindulunarbr = new ModifiedHinduLunarBRCalendar(gregorian.toRD());
        System.out.println(gregorian + "(" + modhindulunarbr.toRD() + "): " +
            modhindulunarbr);
        System.out.println("===============");
        modhindulunarbr.set(i, flag, j, flag1, k);
        System.out.println(modhindulunarbr + " => " + modhindulunarbr.toRD());
    }
}
