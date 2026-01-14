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

//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;

import org.jscience.mathematics.algebraic.numbers.ExactRational;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ModifiedHinduLunarBRCalendar extends OldHinduLunarCalendar {
    /** DOCUMENT ME! */
    protected static final int LUNARERA = 3044;

    /** DOCUMENT ME! */
    protected static ModifiedHinduBRCalendar mh;

    /** DOCUMENT ME! */
    protected boolean leapday;

/**
     * Creates a new ModifiedHinduLunarBRCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public ModifiedHinduLunarBRCalendar(long l) {
        super(l);
    }

/**
     * Creates a new ModifiedHinduLunarBRCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
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
     * @param i     DOCUMENT ME!
     * @param flag  DOCUMENT ME!
     * @param j     DOCUMENT ME!
     * @param flag1 DOCUMENT ME!
     * @param k     DOCUMENT ME!
     */
    public ModifiedHinduLunarBRCalendar(int i, boolean flag, int j,
        boolean flag1, int k) {
        set(i, flag, j, flag1, k);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param flag DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param flag1 DOCUMENT ME!
     * @param k DOCUMENT ME!
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
     * DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param modhindulunarbr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @throws InconsistentDateException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getLeapDay() {
        return leapday;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return super.month + "(" + super.leap + ") " + super.day + "(" +
        leapday + ") " + super.year;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
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
