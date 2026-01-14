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

import org.jscience.mathematics.algebraic.numbers.Rational;

import java.util.Enumeration;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class OldHinduSolarCalendar extends MonthDayYear {
    /** DOCUMENT ME! */
    protected static final long EPOCH = (new JulianCalendar(2, 18, -3102)).toRD();

    /** DOCUMENT ME! */
    protected static final Rational SIDEREALYEAR;

    /** DOCUMENT ME! */
    protected static final Rational JOVIANPERIOD = new Rational(0x5e0d1c3cL,
            0x58ec0L);

    /** DOCUMENT ME! */
    protected static final Rational SOLARMONTH;

    /** DOCUMENT ME! */
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
     * @param l DOCUMENT ME!
     */
    public OldHinduSolarCalendar(long l) {
        set(l);
    }

/**
     * Creates a new OldHinduSolarCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
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
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public OldHinduSolarCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long dayCount() {
        return toRD() - EPOCH;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int jovianYear() {
        Rational rational = new Rational((int) dayCount(), 1L);
        int i = (int) rational.divide(JOVIANPERIOD.divide(new Rational(12L)))
                              .floor();
        i = AlternateCalendar.mod(i, 60);

        return ++i;
    }

    /**
     * DOCUMENT ME!
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
     * DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k) {
        super.month = i;
        super.day = j;
        super.year = k;
        recomputeRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String monthName() {
        return MONTHS[super.month - 1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSuffix() {
        return " K.Y.";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getMonths() {
        return new ArrayEnumeration(MONTHS);
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
