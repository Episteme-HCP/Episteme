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
 * Implementation of the Old Hindu Lunar calendar (Surya Siddhanta).
 * * @version 2.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OldHinduLunarCalendar extends OldHinduSolarCalendar {

    private static final long serialVersionUID = 2L;
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
        LUNARMONTH = Rational.of(0x5e0d1c3cL, 0x32f53f8L);
        LUNARDAY = LUNARMONTH.divide(Rational.of(30L));
        LCONST = LUNARMONTH.subtract(OldHinduSolarCalendar.SIDEREALYEAR.mod(LUNARMONTH));
        MONTHDIFF = OldHinduSolarCalendar.SOLARMONTH.subtract(LUNARMONTH);
    }

    /** True if the current month is a leap month (adhika mÄsa). */
    protected boolean leap;

    public OldHinduLunarCalendar() {
    }

    public OldHinduLunarCalendar(long l) {
        super(l);
    }

    public OldHinduLunarCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    public OldHinduLunarCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    public OldHinduLunarCalendar(int i, boolean flag, int j, int k) {
        set(i, flag, j, k);
    }

    protected static boolean isLeapYear(int i) {
        Rational rational = OldHinduSolarCalendar.SIDEREALYEAR.multiply(Rational.of(i));
        rational = rational.subtract(OldHinduSolarCalendar.SOLARMONTH);
        rational = rational.mod(LUNARMONTH);
        return gte(rational, LCONST);
    }

    protected synchronized void recomputeFromRD() {
        Rational rational = Rational.of(1L, 4L).add(Rational.of(dayCount()));
        Rational rational1 = rational.subtract(rational.mod(LUNARMONTH));
        Rational rational2 = rational1.mod(OldHinduSolarCalendar.SOLARMONTH);
        leap = gte(MONTHDIFF, rational2) && gt(rational2, Rational.ZERO);
        super.month = (int) AlternateCalendar.mod(rational1.divide(OldHinduSolarCalendar.SOLARMONTH).ceil().longValue(), 12) + 1;
        super.day = (int) AlternateCalendar.mod(rational.divide(LUNARDAY).floor().longValue(), 30) + 1;
        super.year = (int) rational1.add(OldHinduSolarCalendar.SOLARMONTH)
                                     .divide(OldHinduSolarCalendar.SIDEREALYEAR)
                                     .ceil().longValue() - 1;
    }

    protected synchronized void recomputeRD() {
        Rational rational = OldHinduSolarCalendar.SOLARMONTH.multiply(Rational.of((12 * super.year) - 1));
        Rational rational1 = LUNARMONTH.multiply(Rational.of(rational.divide(LUNARMONTH).floor().longValue() + 1L));
        long l;

        if (!leap && (rational1.subtract(rational).divide(MONTHDIFF).ceil().longValue() <= (long) super.month)) {
            l = super.month;
        } else {
            l = super.month - 1;
        }

        Rational rational2 = rational1.add(LUNARMONTH.multiply(Rational.of(l)));
        rational2 = rational2.add(LUNARDAY.multiply(Rational.of(super.day - 1)));
        rational2 = rational2.add(Rational.of(3L, 4L));
        super.rd = rational2.floor().longValue() + OldHinduSolarCalendar.EPOCH;

        if (lt(rational2.fractionalPart().add(LUNARDAY), Rational.ONE)) {
            throw new InconsistentDateException("Lost lunar day");
        }

        if (leap) {
            Rational rational3 = rational1.mod(OldHinduSolarCalendar.SOLARMONTH);
            rational3 = rational3.subtract(MONTHDIFF.multiply(Rational.of(super.month - 1)));

            if (!lte(rational3.mod(OldHinduSolarCalendar.SOLARMONTH), MONTHDIFF)) {
                throw new InconsistentDateException("Month not leap that year");
            }
        }
    }

    public synchronized void set(int i, int j, int k) {
        set(i, false, j, k);
    }

    public synchronized void set(int i, boolean flag, int j, int k) {
        super.month = i;
        leap = flag;
        super.day = j;
        super.year = k;
        recomputeRD();
    }

    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    protected String monthName() {
        return MONTHS[super.month - 1] + (leap ? " (leap)" : "");
    }

    protected String getSuffix() {
        return " K.Y.";
    }

    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    public boolean getLeap() {
        return leap;
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

    private static boolean gt(Rational rational1, Rational rational2) {
        return rational1.compareTo(rational2) > 0;
    }

    private static boolean lte(Rational rational1, Rational rational2) {
        return rational1.compareTo(rational2) <= 0;
    }

    private static boolean lt(Rational rational1, Rational rational2) {
        return rational1.compareTo(rational2) < 0;
    }

    private static boolean gte(Rational rational1, Rational rational2) {
        return rational1.compareTo(rational2) >= 0;
    }
}

