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

import java.util.Enumeration;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class GregorianCalendar extends MonthDayYear {
    /** DOCUMENT ME! */
    protected static final String[] MONTHS = {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
        };

    /** DOCUMENT ME! */
    protected static long EPOCH = 1L;

    /** DOCUMENT ME! */
    private static final int FOURCENTURY = 0x23ab1;

    /** DOCUMENT ME! */
    private static final int CENTURY = 36524;

    /** DOCUMENT ME! */
    private static final int FOURYEAR = 1461;

    /** DOCUMENT ME! */
    private static final int YEAR = 365;

/**
     * Creates a new GregorianCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public GregorianCalendar(long l) {
        set(l);
    }

/**
     * Creates a new GregorianCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public GregorianCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new GregorianCalendar object.
     */
    public GregorianCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new GregorianCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public GregorianCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLeapYear(int i) {
        if (AlternateCalendar.mod(i, 4) != 0) {
            return false;
        }

        int j = AlternateCalendar.mod(i, 400);

        return (j != 100) && (j != 200) && (j != 300);
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        int i = super.year - 1;
        super.rd = (((EPOCH - 1L) + (long) (365 * i) +
            AlternateCalendar.fldiv(i, 4L)) - AlternateCalendar.fldiv(i, 100L)) +
            AlternateCalendar.fldiv(i, 400L) +
            AlternateCalendar.fldiv((367 * super.month) - 362, 12L);

        if (super.month > 2) {
            if (isLeapYear(super.year)) {
                super.rd--;
            } else {
                super.rd -= 2L;
            }
        }

        super.rd += super.day;
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
     */
    protected synchronized void recomputeFromRD() {
        int[] ai = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        long l = super.rd - EPOCH;
        int i = (int) AlternateCalendar.fldiv(l, 0x23ab1L);
        int j = AlternateCalendar.mod(l, 0x23ab1);
        int k = (int) AlternateCalendar.fldiv(j, 36524L);
        int i1 = AlternateCalendar.mod(j, 36524);
        int j1 = (int) AlternateCalendar.fldiv(i1, 1461L);
        int k1 = AlternateCalendar.mod(i1, 1461);
        int l1 = (int) AlternateCalendar.fldiv(k1, 365L);
        super.year = (400 * i) + (100 * k) + (4 * j1) + l1;

        int i2 = AlternateCalendar.mod(k1, 365) + 1;
        super.month = 1;
        super.day = i2;

        if ((k != 4) && (l1 != 4)) {
            super.year++;
        }

        for (int j2 = 0; j2 <= 12; j2++) {
            if (super.day <= ai[j2]) {
                break;
            }

            if ((j2 == 1) && isLeapYear(super.year)) {
                if (super.day <= 29) {
                    break;
                }

                super.day--;
            }

            super.day -= ai[j2];
            super.month++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int dayNumber() {
        return (int) difference(new GregorianCalendar(12, 31, getYear() - 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int daysLeft() {
        return (int) AlternateCalendar.difference(new GregorianCalendar(12, 31,
                getYear()), this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSuffix() {
        return "";
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
    }
}
