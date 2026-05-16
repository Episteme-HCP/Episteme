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

/**
 * Implementation of the BahÃ¡'Ã­ (BadÃ­') calendar.
 * The BahÃ¡'Ã­ calendar is a solar calendar used by the BahÃ¡'Ã­ Faith, 
 * consisting of 19 months of 19 days, plus intercalary days.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: March 21, 1844 CE</li>
 *   <li>19 months of 19 days each (361 days)</li>
 *   <li>4-5 intercalary days (AyyÃ¡m-i-HÃ¡)</li>
 *   <li>19-year cycles (VÃ¡hid) and 361-year major cycles (Kull-i-Shay)</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BahaiCalendar extends SevenDaysWeek {

    private static final long serialVersionUID = 1L;
    /** Day names of the BahÃ¡'Ã­ week. */
    private static final String[] DAYS = {
            "Jamal", "Kamal", "Fidal", "`Idal", "Istijlal", "Istiqlal", "Jalal"
        };

    /** Month names of the BahÃ¡'Ã­ year. */
    private static final String[] MONTHS = {
            "Baha", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat",
            "Kamal", "Asma'", "`Izzat", "Mashiyyat", "`Ilm", "Qudrat", "Qawl",
            "Masail", "Sharaf", "Sultan", "Mulk", "Ayyam-i-Ha", "`Ala'"
        };

    /** Year names within the 19-year VÃ¡hid cycle. */
    private static final String[] YEARS = {
            "Alif", "Ba'", "Ab", "Dal", "Bab", "Vav", "Abad", "Jad", "Baha",
            "Hubb", "Bahhaj", "Javab", "Ahad", "Vahhab", "Vidad", "Badi'",
            "Bahi'", "Abha", "Vahid"
        };

    /** The RD number of the BahÃ¡'Ã­ epoch (March 21, 1844). */
    public static final long EPOCH = (new GregorianCalendar(3, 21, 1844)).toRD();

    /** The Gregorian year of the BahÃ¡'Ã­ epoch. */

    /** The major cycle number (Kull-i-Shay). */
    private int major;

    /** The 19-year cycle number (VÃ¡hid). */
    private int cycle;

    /** The year within the cycle (1-19). */
    private int year;

    /** The month of the year (1-20, with 19 as intercalary days). */
    private int month;

    /** The day of the month (1-19). */
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
     * @param l the Rata Die number.
     */
    public BahaiCalendar(long l) {
        set(l);
    }

/**
     * Creates a new BahaiCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public BahaiCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

/**
     * Creates a new BahaiCalendar object.
     *
     * @param i the Kull-i-Shay (major cycle).
     * @param j the VÃ¡hid (cycle).
     * @param k the year within VÃ¡hid.
     * @param l the month.
     * @param i1 the day.
     */
    public BahaiCalendar(int i, int j, int k, int l, int i1) {
        set(i, j, k, l, i1);
    }

    /**
     * Sets the Rata Die number and recomputes the date fields.
     *
     * @param l the Rata Die number.
     */
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    /**
     * Sets the BahÃ¡'Ã­ cycle components and recomputes the Rata Die date.
     *
     * @param i the Kull-i-Shay.
     * @param j the VÃ¡hid.
     * @param k the year.
     * @param l the month.
     * @param i1 the day.
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
     * Recomputes the Rata Die number from the BahÃ¡'Ã­ date fields.
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
     * Recomputes the BahÃ¡'Ã­ date fields from the current Rata Die number.
     */
    public synchronized void recomputeFromRD() {
        int i = (new GregorianCalendar(super.rd)).getYear();
        int j = i - 1844;

        if (((new GregorianCalendar(1, 1, i)).toRD() <= super.rd) &&
                (super.rd <= (new GregorianCalendar(3, 20, i)).toRD())) {
            j--;
        }

        major = (int) Math.floorDiv(j, 361L) + 1;
        cycle = (int) Math.floorDiv(AlternateCalendar.mod(j, 361), 19L) +
            1;
        year = AlternateCalendar.mod(j, 19) + 1;

        long l = super.rd -
            (new BahaiCalendar(major, cycle, year, 1, 1)).toRD();
        BahaiCalendar bahai = new BahaiCalendar(major, cycle, year, 20, 1);

        if (super.rd >= bahai.toRD()) {
            month = 20;
        } else {
            month = (int) AlternateCalendar.floorDiv(l, 19L) + 1;
        }

        day = (int) ((super.rd + 1L) -
            (new BahaiCalendar(major, cycle, year, month, 1)).toRD());
    }

    /**
     * Returns a string representation of the BahÃ¡'Ã­ date.
     *
     * @return the date string.
     */
    public String toString() {
        int i = weekDay();
        int j = day - 1;

        if (j == 18) {
            j++;
        }

        try {
            return DAYS[i] + " (" + SevenDaysWeek.DAY_NAMES[i] +
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
     * Main method for testing the BahÃ¡'Ã­ calendar implementation.
     *
     * @param args command line arguments.
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

