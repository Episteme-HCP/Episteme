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


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class JulianCalendar extends GregorianCalendar {
    /** DOCUMENT ME! */
    public static long EPOCH = (new GregorianCalendar(12, 30, 0)).toRD();

/**
     * Creates a new JulianCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public JulianCalendar(long l) {
        set(l);
    }

/**
     * Creates a new JulianCalendar object.
     */
    public JulianCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new JulianCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public JulianCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new JulianCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public JulianCalendar(int i, int j, int k) {
        super(1, 1, 1);
        set(i, j, k);
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        int i = super.year;

        if (i < 0) {
            i++;
        }

        super.rd = (EPOCH - 1L) + (long) (365 * (i - 1)) +
            AlternateCalendar.fldiv(i - 1, 4L) +
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
     */
    protected synchronized void recomputeFromRD() {
        super.year = (int) AlternateCalendar.fldiv((4L * (toRD() - EPOCH)) +
                1464L, 1461L);

        if (super.year <= 0) {
            super.year--;
        }

        JulianCalendar julian = new JulianCalendar(1, 1, super.year);
        int i = (int) (toRD() - julian.toRD());
        julian.set(3, 1, super.year);

        if (toRD() >= julian.toRD()) {
            if (isLeapYear(super.year)) {
                i++;
            } else {
                i += 2;
            }
        }

        super.month = (int) AlternateCalendar.fldiv((12 * i) + 373, 367L);
        super.day = (int) (toRD() -
            (new JulianCalendar(super.month, 1, super.year)).toRD()) + 1;
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
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLeapYear(int i) {
        if (i > 0) {
            return AlternateCalendar.mod(i, 4) == 0;
        }

        return AlternateCalendar.mod(i, 4) == 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSuffix() {
        if (super.year > 0) {
            return " C.E.";
        } else {
            return " B.C.E.";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        try {
            return super.day + " " + monthName() + " " +
            ((super.year < 0) ? (-super.year) : super.year) + getSuffix();
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return "Invalid date: " + super.month + " " + super.day + " " +
            super.year;
        }
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

        JulianCalendar julian = new JulianCalendar(gregorian);
        System.out.println(gregorian + ": " + julian);
        julian.set(i, j, k);
        System.out.println("JulianCalendar(" + i + "," + j + "," + k + "): " +
            julian);
        System.out.println(julian.toRD());
    }
}
