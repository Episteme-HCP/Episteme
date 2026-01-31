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

package org.jscience.social.history.calendars;

import org.jscience.core.mathematics.numbers.rationals.Rational;
import java.util.Enumeration;

/**
 * Implementation of the Old Hindu Solar calendar (Surya Siddhanta).
 * * @version 2.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OldHinduSolarCalendar extends MonthDayYear {

    private static final long serialVersionUID = 2L;
    /** The RD (Rata Die) number for the Old Hindu Solar calendar epoch (Kali Yuga). */
    protected static final long EPOCH = (new JulianCalendar(2, 18, -3102)).toRD();

    /** The length of the sidereal year in days. */
    protected static final Rational SIDEREALYEAR;

    /** The Jovian period (cycle of Jupiter) in days. */
    protected static final Rational JOVIANPERIOD = Rational.of(0x5e0d1c3cL, 0x58ec0L);

    /** The length of a solar month in days. */
    protected static final Rational SOLARMONTH;

    /** List of Hindu solar month names. */
    protected static final String[] MONTHS = {
            "Mesha", "Vrishabha", "Mithuna", "Karka", "Simha", "Kanya", "Tula",
            "Vrischika", "Dhanu", "Makara", "Kumbha", "Mina"
        };

    static {
        SIDEREALYEAR = Rational.of(0x5e0d1c3cL, 0x41eb00L);
        SOLARMONTH = SIDEREALYEAR.divide(Rational.of(12L));
    }

    public OldHinduSolarCalendar(long l) {
        set(l);
    }

    public OldHinduSolarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public OldHinduSolarCalendar() {
    }

    public OldHinduSolarCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    public long dayCount() {
        return toRD() - EPOCH;
    }

    public int jovianYear() {
        Rational rational = Rational.of(dayCount());
        int i = rational.divide(JOVIANPERIOD.divide(Rational.of(12L))).floor().intValue();
        i = AlternateCalendar.mod(i, 60);
        return ++i;
    }

    protected synchronized void recomputeRD() {
        Rational rational = Rational.of(EPOCH);
        rational = rational.add(SIDEREALYEAR.multiply(Rational.of(super.year)));
        rational = rational.add(SOLARMONTH.multiply(Rational.of(super.month - 1)));
        rational = rational.add(Rational.of(super.day));
        rational = rational.subtract(Rational.of(1L, 4L));
        super.rd = rational.floor().longValue();
    }

    protected synchronized void recomputeFromRD() {
        Rational rational = Rational.of(1L, 4L);
        rational = rational.add(Rational.of(dayCount()));

        Rational rational1 = rational.divide(SIDEREALYEAR);
        super.year = rational1.floor().intValue();

        Rational rational2 = rational.divide(SOLARMONTH);
        super.month = AlternateCalendar.mod(rational2.floor().longValue(), 12) + 1;
        super.day = rational.mod(SOLARMONTH).floor().intValue() + 1;
    }

    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    public synchronized void set(int i, int j, int k) {
        super.month = i;
        super.day = j;
        super.year = k;
        recomputeRD();
    }

    protected String monthName() {
        return MONTHS[super.month - 1];
    }

    protected String getSuffix() {
        return " K.Y.";
    }

    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
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

        OldHinduSolarCalendar oldhindusolar = new OldHinduSolarCalendar(gregorian);
        System.out.println(gregorian + ": " + oldhindusolar);
        oldhindusolar.set(i, j, k);
        System.out.println("OldHinduSolarCalendar(" + i + "," + j + "," + k +
            "): " + oldhindusolar);
        System.out.println(oldhindusolar.toRD());
        System.out.println("\n" + SOLARMONTH.toString() + "\n" +
            SIDEREALYEAR.toString() + "\n" +
            Rational.of(oldhindusolar.dayCount()).divide(SOLARMONTH).toString());
    }
}

