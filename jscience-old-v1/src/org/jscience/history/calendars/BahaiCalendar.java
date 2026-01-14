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
public class BahaiCalendar extends SevenDaysWeek {
    /** DOCUMENT ME! */
    private static final String[] DAYS = {
            "Jamal", "Kamal", "Fidal", "`Idal", "Istijlal", "Istiqlal", "Jalal"
        };

    /** DOCUMENT ME! */
    private static final String[] MONTHS = {
            "Baha", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat",
            "Kamal", "Asma'", "`Izzat", "Mashiyyat", "`Ilm", "Qudrat", "Qawl",
            "Masail", "Sharaf", "Sultan", "Mulk", "Ayyam-i-Ha", "`Ala'"
        };

    /** DOCUMENT ME! */
    private static final String[] YEARS = {
            "Alif", "Ba'", "Ab", "Dal", "Bab", "Vav", "Abad", "Jad", "Baha",
            "Hubb", "Bahhaj", "Javab", "Ahad", "Vahhab", "Vidad", "Badi'",
            "Bahi'", "Abha", "Vahid"
        };

    /** DOCUMENT ME! */
    public static final long EPOCH = (new GregorianCalendar(3, 21, 1844)).toRD();

    /** DOCUMENT ME! */
    private static final int EPYEAR = 1844;

    /** DOCUMENT ME! */
    private int major;

    /** DOCUMENT ME! */
    private int cycle;

    /** DOCUMENT ME! */
    private int year;

    /** DOCUMENT ME! */
    private int month;

    /** DOCUMENT ME! */
    private int day;

/**
     * Creates a new BahaiCalendar object.
     */
    public BahaiCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new BahaiCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public BahaiCalendar(long l) {
        set(l);
    }

/**
     * Creates a new BahaiCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public BahaiCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

/**
     * Creates a new BahaiCalendar object.
     *
     * @param i  DOCUMENT ME!
     * @param j  DOCUMENT ME!
     * @param k  DOCUMENT ME!
     * @param l  DOCUMENT ME!
     * @param i1 DOCUMENT ME!
     */
    public BahaiCalendar(int i, int j, int k, int l, int i1) {
        set(i, j, k, l, i1);
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
     * @param l DOCUMENT ME!
     * @param i1 DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k, int l, int i1) {
        major = i;
        cycle = j;
        year = k;
        month = l;
        day = i1;
        recomputeRD();
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeRD() {
        int i = (((361 * (major - 1)) + (19 * (cycle - 1)) + year) - 1) + 1844;
        super.rd = (new GregorianCalendar(3, 20, i)).toRD() +
            (long) (19 * (month - 1));

        if (month == 20) {
            if (GregorianCalendar.isLeapYear(i + 1)) {
                super.rd -= 14L;
            } else {
                super.rd -= 15L;
            }
        }

        super.rd += day;
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void recomputeFromRD() {
        int i = (new GregorianCalendar(super.rd)).getYear();
        int j = i - 1844;

        if (((new GregorianCalendar(1, 1, i)).toRD() <= super.rd) &&
                (super.rd <= (new GregorianCalendar(3, 20, i)).toRD())) {
            j--;
        }

        major = (int) AlternateCalendar.fldiv(j, 361L) + 1;
        cycle = (int) AlternateCalendar.fldiv(AlternateCalendar.mod(j, 361), 19L) +
            1;
        year = AlternateCalendar.mod(j, 19) + 1;

        long l = super.rd -
            (new BahaiCalendar(major, cycle, year, 1, 1)).toRD();
        BahaiCalendar bahai = new BahaiCalendar(major, cycle, year, 20, 1);

        if (super.rd >= bahai.toRD()) {
            month = 20;
        } else {
            month = (int) AlternateCalendar.fldiv(l, 19L) + 1;
        }

        day = (int) ((super.rd + 1L) -
            (new BahaiCalendar(major, cycle, year, month, 1)).toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        int i = weekDay();
        int j = day - 1;

        if (j == 18) {
            j++;
        }

        try {
            return DAYS[i] + " (" + SevenDaysWeek.DAYNAMES[i] +
            ") the day of " + MONTHS[j] + " of the month of " +
            MONTHS[month - 1] + " of the year of " + YEARS[year - 1] +
            " of Vahid " + cycle + " of Kull-i-Shay " + major + "\n(" + major +
            " " + cycle + " " + year + " " + month + " " + day + ")";
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return "Invalid date: " + major + " " + cycle + " " + year + " " +
            month + " " + day;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;

        try {
            if (args.length >= 5) {
                l = Integer.parseInt(args[0]);
                i1 = Integer.parseInt(args[1]);
                j1 = Integer.parseInt(args[2]);
                k1 = Integer.parseInt(args[3]);
                l1 = Integer.parseInt(args[4]);
            } else {
                i = Integer.parseInt(args[0]);
                j = Integer.parseInt(args[1]);
                k = Integer.parseInt(args[2]);
            }
        } catch (Exception _ex) {
            i = k = j = 1;
        }

        if (i != 0) {
            GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
            System.out.println(gregorian.toRD());
            System.out.println(gregorian + "\n");

            BahaiCalendar bahai = new BahaiCalendar(gregorian);
            System.out.println(gregorian + ": " + bahai);

            return;
        } else {
            BahaiCalendar bahai1 = new BahaiCalendar(l, i1, j1, k1, l1);
            System.out.println("BahaiCalendar(" + l + " " + i1 + " " + j1 +
                " " + k1 + " " + l1 + "): " + bahai1);
            System.out.println(bahai1.toRD());

            return;
        }
    }
}
