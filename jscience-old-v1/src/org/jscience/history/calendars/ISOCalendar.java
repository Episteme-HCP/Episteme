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
public class ISOCalendar extends GregorianCalendar {
    /** DOCUMENT ME! */
    private int week;

    /** DOCUMENT ME! */
    private int day;

    /** DOCUMENT ME! */
    private int year;

/**
     * Creates a new ISOCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public ISOCalendar(long l) {
        set(l);
    }

/**
     * Creates a new ISOCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public ISOCalendar(int i, int j, int k) {
        super(1, 1, 1);
        set(i, j, k);
    }

/**
     * Creates a new ISOCalendar object.
     */
    public ISOCalendar() {
        this(GregorianCalendar.EPOCH);
    }

/**
     * Creates a new ISOCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public ISOCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k) {
        week = i;
        day = j;
        year = k;
        recomputeRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getYear() {
        return year;
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        GregorianCalendar gregorian = new GregorianCalendar(12, 28, year - 1);
        gregorian.nthKDay(week, 0);
        gregorian.add(day);
        super.rd = gregorian.toRD();
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeFromRD() {
        int i = (new GregorianCalendar(super.rd - 3L)).getYear();
        ISOCalendar iso = new ISOCalendar(1, 1, i + 1);

        if (super.rd < iso.toRD()) {
            iso.set(1, 1, i);
        }

        year = iso.getYear();
        week = (int) AlternateCalendar.fldiv(super.rd - iso.toRD(), 7L) + 1;
        day = (int) AlternateCalendar.amod(super.rd, 7L);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWeek() {
        return week;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDay() {
        return day;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        try {
            return SevenDaysWeek.DAYNAMES[AlternateCalendar.mod(getDay(), 7)] +
            " of week " + getWeek() + ", " + getYear();
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return getDay() + " " + getWeek() + " " + getYear();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMonth() {
        return 0;
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

        ISOCalendar iso = new ISOCalendar(gregorian);
        System.out.println(gregorian + ": " + iso);
        iso.set(i, j, k);
        System.out.println("ISO(" + i + "," + j + "," + k + "): " + iso);
    }
}
