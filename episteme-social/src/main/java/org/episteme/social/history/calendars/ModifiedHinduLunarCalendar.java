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
 * Modified Hindu Lunar calendar using floating-point arithmetic.
 * This calendar uses modern astronomical calculations for lunar month
 * determinations with support for leap months and leap days.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ModifiedHinduLunarCalendar extends OldHinduLunarCalendar {

    private static final long serialVersionUID = 1L;
    /** Offset for the lunar era (Vikrama Era). */
    protected static final int LUNARERA = 3044;

    /** Delegate for Hindu astronomical calculations. */
    protected static ModifiedHinduCalendar mh;

    /** True if this is a duplicated (leap) day. */
    protected boolean leapday;

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     *
     * @param l Rata Die number.
     */
    public ModifiedHinduLunarCalendar(long l) {
        super(l);
    }

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public ModifiedHinduLunarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     */
    public ModifiedHinduLunarCalendar() {
    }

/**
     * Creates a new ModifiedHinduLunarCalendar object.
     *
     * @param i the month number.
     * @param flag true if leap month.
     * @param j the day number.
     * @param flag1 true if leap day.
     * @param k the year number.
     */
    public ModifiedHinduLunarCalendar(int i, boolean flag, int j,
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
        double d = super.rd - OldHinduSolarCalendar.EPOCH;
        double d1 = ModifiedHinduCalendar.sunrise(d);
        super.day = ModifiedHinduCalendar.lunarDay(d1);
        leapday = super.day == ModifiedHinduCalendar.lunarDay(ModifiedHinduCalendar.sunrise(d -
                    1.0D));

        double d2 = ModifiedHinduCalendar.newMoon(d1);
        double d3 = ModifiedHinduCalendar.newMoon(Math.floor(d2) + 35D);
        int i = ModifiedHinduCalendar.zodiac(d2);
        super.leap = i == ModifiedHinduCalendar.zodiac(d3);
        super.month = (int) AlternateCalendar.amod(i + 1, 12L);
        super.year = ModifiedHinduCalendar.calYear(d3) - 3044 -
            ((!super.leap || (super.month != 1)) ? 0 : (-1));
    }

    /**
     * Checks if this date precedes another Hindu lunar date.
     *
     * @param modhindulunar the other date.
     * @return true if this date is before the other.
     */
    public boolean precedes(ModifiedHinduLunarCalendar modhindulunar) {
        return (super.year < ((MonthDayYear) (modhindulunar)).year) ||
        ((super.year == ((MonthDayYear) (modhindulunar)).year) &&
        ((super.month < ((MonthDayYear) (modhindulunar)).month) ||
        ((super.month == ((MonthDayYear) (modhindulunar)).month) &&
        ((super.leap && !((OldHinduLunarCalendar) (modhindulunar)).leap) ||
        ((super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
        ((super.day < ((MonthDayYear) (modhindulunar)).day) ||
        ((super.day == ((MonthDayYear) (modhindulunar)).day) && !leapday &&
        modhindulunar.leapday)))))));
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

        if ((new ModifiedHinduLunarCalendar(l + 15L)).precedes(this)) {
            l1 = l + 29L;
        } else if (precedes(new ModifiedHinduLunarCalendar(l - 15L))) {
            l1 = l - 29L - 1L;
        } else {
            l1 = l;
        }

        long l2 = l1 - 4L;
        long l3 = l1 + 4L;
        long l4 = (l2 + l3) / 2L;

        do {
            l4 = (l2 + l3) / 2L;

            if ((new ModifiedHinduLunarCalendar(l4)).precedes(this)) {
                l2 = l4;
            } else {
                l3 = l4;
            }
        } while ((l3 - l2) >= 2L);

        ModifiedHinduLunarCalendar modhindulunar = new ModifiedHinduLunarCalendar(l4);

        if ((super.day == ((MonthDayYear) (modhindulunar)).day) &&
                (leapday == modhindulunar.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunar)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunar)).year)) {
            super.rd = l4;

            return;
        }

        modhindulunar = new ModifiedHinduLunarCalendar(l4 - 1L);

        if ((super.day == ((MonthDayYear) (modhindulunar)).day) &&
                (leapday == modhindulunar.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunar)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunar)).year)) {
            super.rd = l4 - 1L;

            return;
        }

        modhindulunar = new ModifiedHinduLunarCalendar(l4 + 1L);

        if ((super.day == ((MonthDayYear) (modhindulunar)).day) &&
                (leapday == modhindulunar.leapday) &&
                (super.month == ((MonthDayYear) (modhindulunar)).month) &&
                (super.leap == ((OldHinduLunarCalendar) (modhindulunar)).leap) &&
                (super.year == ((MonthDayYear) (modhindulunar)).year)) {
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
        return super.day + (leapday ? "(leap) " : " ") + monthName() + " " +
        super.year + getSuffix();
    }

    /**
     * Returns the era suffix for this calendar.
     *
     * @return the suffix string.
     */
    public String getSuffix() {
        return " V.E.";
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

        ModifiedHinduLunarCalendar modhindulunar = new ModifiedHinduLunarCalendar(gregorian.toRD());
        System.out.println(gregorian + "(" + modhindulunar.toRD() + "): " +
            modhindulunar);
        System.out.println("===============");
        modhindulunar.set(i, flag, j, flag1, k);
        System.out.println(modhindulunar + " => " + modhindulunar.toRD());
    }
}

