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
public class OldHinduLunarCalendar extends OldHinduSolarCalendar {
    /** DOCUMENT ME! */
    protected static final String[] MONTHS = {
            "Chaitra", "Vaisakha", "Jyaishtha", "Ashadha", "Sravana",
            "Bhadrapada", "Asvina", "Karttika", "Margasira", "Pausha", "Magha",
            "Phalguna"
        };

    /** DOCUMENT ME! */
    protected static final Rational LUNARMONTH;

    /** DOCUMENT ME! */
    protected static final Rational LUNARDAY;

    /** DOCUMENT ME! */
    private static final Rational LCONST;

    /** DOCUMENT ME! */
    private static final Rational MONTHDIFF;

    static {
        LUNARMONTH = new Rational(0x5e0d1c3cL, 0x32f53f8L);
        LUNARDAY = LUNARMONTH.divide(new Rational(30L));
        LCONST = LUNARMONTH.subtract(OldHinduSolarCalendar.SIDEREALYEAR.mod(
                    LUNARMONTH));
        MONTHDIFF = OldHinduSolarCalendar.SOLARMONTH.subtract(LUNARMONTH);
    }

    /** DOCUMENT ME! */
    protected boolean leap;

/**
     * Creates a new OldHinduLunarCalendar object.
     */
    public OldHinduLunarCalendar() {
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public OldHinduLunarCalendar(long l) {
        super(l);
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public OldHinduLunarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public OldHinduLunarCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new OldHinduLunarCalendar object.
     *
     * @param i    DOCUMENT ME!
     * @param flag DOCUMENT ME!
     * @param j    DOCUMENT ME!
     * @param k    DOCUMENT ME!
     */
    public OldHinduLunarCalendar(int i, boolean flag, int j, int k) {
        set(i, flag, j, k);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static boolean isLeapYear(int i) {
        Rational rational = OldHinduSolarCalendar.SIDEREALYEAR.multiply(new Rational(
                    i));
        rational = rational.subtract(OldHinduSolarCalendar.SOLARMONTH);
        rational = rational.mod(LUNARMONTH);

        return gte(rational, LCONST);
    }

    /**
     * DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @throws InconsistentDateException DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k) {
        set(i, false, j, k);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param flag DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public synchronized void set(int i, boolean flag, int j, int k) {
        super.month = i;
        leap = flag;
        super.day = j;
        super.year = k;
        recomputeRD();
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
     * @return DOCUMENT ME!
     */
    protected String monthName() {
        return MONTHS[super.month - 1] + (leap ? " (leap)" : "");
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
     * @return DOCUMENT ME!
     */
    public boolean getLeap() {
        return leap;
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
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean gt(Rational rational1, Rational rational2) {
        return rational2.subtract(rational1).signum() < 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean lte(Rational rational1, Rational rational2) {
        return !gt(rational1, rational2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean lt(Rational rational1, Rational rational2) {
        return gt(rational2, rational1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean gte(Rational rational1, Rational rational2) {
        return !lt(rational1, rational2);
    }
}
