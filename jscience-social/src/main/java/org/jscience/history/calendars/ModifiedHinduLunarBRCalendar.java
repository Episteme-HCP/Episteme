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

import org.jscience.mathematics.numbers.rationals.Rational;

/**
 * Modified Hindu Lunar calendar using arbitrary-precision arithmetic.
 * This version uses Rational for precise lunar calculations
 * without floating-point errors.
 * * @version 2.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
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

    public ModifiedHinduLunarBRCalendar(long l) {
        super(l);
    }

    public ModifiedHinduLunarBRCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public ModifiedHinduLunarBRCalendar() {
    }

    public ModifiedHinduLunarBRCalendar(int i, boolean flag, int j,
        boolean flag1, int k) {
        set(i, flag, j, flag1, k);
    }

    public synchronized void set(int i, boolean flag, int j, boolean flag1,
        int k) {
        super.month = i;
        super.leap = flag;
        super.day = j;
        leapday = flag1;
        super.year = k;
        recomputeRD();
    }

    public synchronized void recomputeFromRD() {
        Rational bigrational = Rational.of(super.rd - OldHinduSolarCalendar.EPOCH);
        Rational bigrational1 = ModifiedHinduBRCalendar.sunrise(bigrational);
        super.day = ModifiedHinduBRCalendar.lunarDay(bigrational1);
        leapday = super.day == ModifiedHinduBRCalendar.lunarDay(ModifiedHinduBRCalendar.sunrise(
                    bigrational.subtract(Rational.ONE)));

        Rational bigrational2 = ModifiedHinduBRCalendar.newMoon(bigrational1);
        Rational bigrational3 = ModifiedHinduBRCalendar.newMoon(Rational.of(35L).add(bigrational2.floor().toRational()));
        int i = ModifiedHinduBRCalendar.zodiac(bigrational2);
        super.leap = i == ModifiedHinduBRCalendar.zodiac(bigrational3);
        super.month = (int) AlternateCalendar.amod(i + 1, 12L);
        super.year = ModifiedHinduBRCalendar.calYear(bigrational3) - 3044 -
            ((!super.leap || (super.month != 1)) ? 0 : (-1));
    }

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

        long l2 = l1 - 4L;
        long l3 = l1 + 4L;
        long l4 = (l2 + l3) / 2L;

        do {
            l4 = (l2 + l3) / 2L;

            if ((new ModifiedHinduLunarBRCalendar(l4)).precedes(this)) {
                l2 = l4;
            } else {
                l3 = l4;
            }
        } while ((l3 - l2) > 2L);

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

    public boolean getLeapDay() {
        return leapday;
    }

    public String toString() {
        return super.month + "(" + super.leap + ") " + super.day + "(" +
        leapday + ") " + super.year;
    }

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
