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

import org.episteme.core.mathematics.numbers.rationals.Rational;

/**
 * Modified Hindu Solar calendar using arbitrary-precision arithmetic.
 * This version uses Rational for precise calculations without
 * floating-point errors.
 * * @version 2.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ModifiedHinduSolarBRCalendar extends OldHinduSolarCalendar {

    private static final long serialVersionUID = 1L;
    /** Offset for the solar era (Saka Era). */
    protected static final int SOLARERA = 3179;

    /** Delegate for Hindu astronomical calculations. */
    protected static ModifiedHinduBRCalendar mh;

    public ModifiedHinduSolarBRCalendar(long l) {
        super(l);
    }

    public ModifiedHinduSolarBRCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public ModifiedHinduSolarBRCalendar() {
    }

    public ModifiedHinduSolarBRCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    public synchronized void recomputeFromRD() {
        Rational bigrational = Rational.of(dayCount());
        Rational bigrational1 = ModifiedHinduBRCalendar.sunrise(bigrational);
        super.month = ModifiedHinduBRCalendar.zodiac(bigrational1);
        super.year = ModifiedHinduBRCalendar.calYear(bigrational1) - 3179;

        Rational bigrational2;

        for (bigrational2 = bigrational.subtract(Rational.of(3L))
                                       .subtract(ModifiedHinduBRCalendar.solarLongitude(bigrational1)
                                                .mod(Rational.of(1800L))
                                                .divide(Rational.of(60L))
                                                .floor().toRational());
                ModifiedHinduBRCalendar.zodiac(ModifiedHinduBRCalendar.sunrise(bigrational2)) != super.month;
                bigrational2 = bigrational2.add(Rational.ONE))
            ;

        super.day = bigrational.subtract(bigrational2).intValue() + 1;
    }

    public boolean precedes(ModifiedHinduSolarBRCalendar modhindusolarbr) {
        return (super.year < ((MonthDayYear) (modhindusolarbr)).year) ||
        ((super.year == ((MonthDayYear) (modhindusolarbr)).year) &&
        ((super.month < ((MonthDayYear) (modhindusolarbr)).month) ||
        ((super.month == ((MonthDayYear) (modhindusolarbr)).month) &&
        (super.day < ((MonthDayYear) (modhindusolarbr)).day))));
    }

    public synchronized void recomputeRD() {
        Rational bigrational = Rational.of(super.month - 1, 12L);
        bigrational = bigrational.add(Rational.of(super.year + 3179))
                                 .multiply(ModifiedHinduBRCalendar.SIDEREALYEAR);

        long l = bigrational.floor().longValue();
        l += ((OldHinduSolarCalendar.EPOCH + (long) super.day) - 9L);

        ModifiedHinduSolarBRCalendar modhindusolarbr = new ModifiedHinduSolarBRCalendar(l);

        if ((((MonthDayYear) (modhindusolarbr)).month == super.month) &&
                (((MonthDayYear) (modhindusolarbr)).year == super.year)) {
            l += (super.day - ((MonthDayYear) (modhindusolarbr)).day - 1);
        }

        for (; modhindusolarbr.precedes(this); )
            modhindusolarbr = new ModifiedHinduSolarBRCalendar(++l);

        super.rd = l;

        if ((((MonthDayYear) (modhindusolarbr)).day != super.day) ||
                (((MonthDayYear) (modhindusolarbr)).month != super.month) ||
                (((MonthDayYear) (modhindusolarbr)).year != super.year)) {
            throw new InconsistentDateException("Illegal Hindu Solar Date");
        }
    }

    protected String getSuffix() {
        return " S.E.";
    }

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

        ModifiedHinduSolarBRCalendar modhindusolarbr = new ModifiedHinduSolarBRCalendar(gregorian);
        System.out.println(gregorian + "(" + modhindusolarbr.toRD() + "): " +
            modhindusolarbr);
        System.out.println("===============");
        modhindusolarbr = new ModifiedHinduSolarBRCalendar(i, j, k);
        System.out.println("HinduCalendar " + i + " " + j + " " + k + ": " +
            modhindusolarbr.toRD());
    }
}

