/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Originally based on code from Mark E. Shoulson <mark@kli.org>
 * http://web.meson.org/calendars/
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

package org.jscience.history.calendars;

import java.util.Enumeration;

/**
 * Implementation of the traditional Chinese lunisolar calendar.
 * The Chinese calendar is one of the oldest chronological systems still in use,
 * incorporating both astronomical observations and traditional cultural cycles.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: February 15, 2636 BCE</li>
 *   <li>Uses a 60-year cycle (Sexagenary cycle) combining Heavenly Stems and Earthly Branches</li>
 *   <li>Months are lunar, beginning with the new moon</li>
 *   <li>Uses astronomical calculations for new moons and solar terms</li>
 *   <li>Leap months are intercalated to align with the solar year</li>
 *   <li>Year names combine celestial stem and terrestrial branch (e.g., "Jia-Zi")</li>
 * </ul>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class ChineseCalendar extends MonthDayYear {

    private static final long serialVersionUID = 1L;

    /** List of Chinese solar terms corresponding to months. */
    public static final String[] MONTHS = {
            "Yushui", "Chufen", "Guyu", "Xiaoman", "Xiazhi", "Dashu", "Chushu",
            "Qiufen", "Shuangjiang", "Xiaoxue", "Dongzhi", "Dahan"
        };

    /** The RD number for the Chinese calendar epoch (February 15, 2636 BCE). */
    public static final long EPOCH = (new GregorianCalendar(2, 15, -2636)).toRD();

    /** The year of the epoch relative to CE. */
    public static final int EPOCHYEAR = -2636;

    /** The 60-year cycle number. */
    private int cycle;

    /** Indicates if the current month is a leap month. */
    private boolean leap;

/**
     * Creates a new ChineseCalendar object.
     *
     * @param l the Rata Die number.
     */
    public ChineseCalendar(long l) {
        super.rd = l;
        recomputeFromRD();
    }

/**
     * Creates a new ChineseCalendar object.
     *
     * @param i    the cycle number.
     * @param j    the year in cycle (1-60).
     * @param k    the month (1-12).
     * @param flag true if leap month.
     * @param l    the day (1-30).
     */
    public ChineseCalendar(int i, int j, int k, boolean flag, int l) {
        set(i, j, k, flag, l);
    }

/**
     * Creates a new ChineseCalendar object.
     */
    public ChineseCalendar() {
        this(EPOCH);
    }

    /**
     * Returns the major solar term (Zhongqi) for the given date.
     *
     * @param l the Rata Die number.
     * @return the major solar term (1-12).
     */
    public static int majorSolarTerm(long l) {
        double d = Moment.solarLongitude(Moment.universalFromLocal(
                    Moment.jdFromMoment(l), timeZone(l)));

        return (int) AlternateCalendar.amod(2 + (int) Math.floor(d / 30D), 12L);
    }

    /**
     * Returns the time zone offset in minutes for the given RD.
     * Beijing time (UTC+8) is used since 1929.
     *
     * @param l the Rata Die number.
     * @return the time zone offset in minutes.
     */
    public static double timeZone(long l) {
        return ((new GregorianCalendar(l)).getYear() >= 1929) ? 480D
                                                              : 465.66666666666669D;
    }

    /**
     * Returns the time zone offset for the current RD.
     *
     * @return the time zone offset.
     */
    public double timeZone() {
        return timeZone(super.rd);
    }

    /**
     * Finds the date of the next time the sun reacher a specific longitude.
     *
     * @param d the starting RD.
     * @param i the longitude increment (typically 30).
     * @return the RD of the event.
     */
    public static long dateNextSolarLongitude(double d, int i) {
        double d1 = timeZone((long) d);

        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.dateNextSolarLongitude(Moment.universalFromLocal(
                            Moment.jdFromMoment(d), d1), i), d1)));
    }

    /**
     * Returns the RD of the next major solar term (Zhongqi) on or after the date.
     *
     * @param d the starting RD.
     * @return the RD of the next major solar term.
     */
    public static long majorSolarTermOnOrAfter(double d) {
        return dateNextSolarLongitude(d, 30);
    }

    /**
     * Returns the RD of the next new moon on or after the given RD.
     *
     * @param l the starting RD.
     * @return the RD of the next new moon.
     */
    public static long newMoonOnOrAfter(long l) {
        double d = timeZone(l);

        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.newMoonAtOrAfter(Moment.universalFromLocal(
                            Moment.jdFromMoment(l), d)), d)));
    }

    /**
     * Returns the RD of the last new moon before the given RD.
     *
     * @param l the starting RD.
     * @return the RD of the previous new moon.
     */
    public static long newMoonBefore(long l) {
        double d = timeZone(l);

        return (long) Math.floor(Moment.momentFromJD(Moment.localFromUniversal(
                    Moment.newMoonBefore(Moment.universalFromLocal(
                            Moment.jdFromMoment(l), d)), d)));
    }

    /**
     * Determines if a month (defined by its starting RD) contains no major solar term.
     *
     * @param l the starting RD of the lunar month.
     * @return true if the month has no major solar term.
     */
    public static boolean noMajorSolarTerm(long l) {
        return majorSolarTerm(l) == majorSolarTerm(newMoonOnOrAfter(l + 1L));
    }

    /**
     * Determines if there is a leap month between two dates.
     *
     * @param l the starting RD.
     * @param l1 the ending RD.
     * @return true if a leap month exists in the range.
     */
    public static boolean priorLeapMonth(long l, long l1) {
        return (l1 >= l) &&
        (noMajorSolarTerm(l1) || priorLeapMonth(l, newMoonBefore(l1)));
    }

    /**
     * Recomputes the cycle, year, month, day, and leap status from the current Rata Die.
     */
    public synchronized void recomputeFromRD() {
        int i = (new GregorianCalendar(super.rd)).getYear();
        long l = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i - 1)).toRD());
        long l1 = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i)).toRD());
        long l2;
        long l3;

        if ((l <= super.rd) && (super.rd < l1)) {
            l2 = newMoonOnOrAfter(l + 1L);
            l3 = newMoonBefore(l1 + 1L);
        } else {
            l2 = newMoonOnOrAfter(l1 + 1L);
            l3 = newMoonBefore(majorSolarTermOnOrAfter(
                        (new GregorianCalendar(12, 15, i + 1)).toRD()) + 1L);
        }

        long l4 = newMoonBefore(super.rd + 1L);
        boolean flag = Math.round((double) (l3 - l2) / 29.530588853000001D) == 12L;
        super.month = (int) Math.round((double) (l4 - l2) / 29.530588853000001D);

        if (flag && priorLeapMonth(l2, l4)) {
            super.month--;
        }

        super.month = (int) AlternateCalendar.amod(super.month, 12L);
        leap = flag && noMajorSolarTerm(l4) &&
            !priorLeapMonth(l2, newMoonBefore(l4));

        int j = i - -2636;

        if ((super.month < 11) ||
                (super.rd > (new GregorianCalendar(7, 1, i)).toRD())) {
            j++;
        }

        cycle = (int) Math.floor((double) (j - 1) / 60D) + 1;
        super.year = (int) AlternateCalendar.amod(j, 60L);
        super.day = (int) ((super.rd - l4) + 1L);
    }

    /**
     * Finds the Rata Die of the Chinese New Year for a given Gregorian year.
     *
     * @param i the Gregorian year.
     * @return the RD of the Chinese New Year.
     */
    public static long newYear(int i) {
        long l = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i - 1)).toRD());
        long l1 = majorSolarTermOnOrAfter((new GregorianCalendar(12, 15, i)).toRD());
        long l2 = newMoonOnOrAfter(l + 1L);
        long l3 = newMoonOnOrAfter(l2 + 1L);
        long l4 = newMoonBefore(l1 + 1L);

        if ((Math.round((double) (l4 - l2) / 29.530588853000001D) == 12L) &&
                (noMajorSolarTerm(l2) || noMajorSolarTerm(l3))) {
            return newMoonOnOrAfter(l3 + 1L);
        } else {
            return l3;
        }
    }

    /**
     * Recomputes the Rata Die number from current Chinese date components.
     */
    public synchronized void recomputeRD() {
        int i = ((((cycle - 1) * 60) + super.year) - 1) + -2636;
        long l = newYear(i);
        long l1 = newMoonOnOrAfter(l + (long) ((super.month - 1) * 29));
        ChineseCalendar chinese = new ChineseCalendar(l1);
        long l2;

        if ((super.month == ((MonthDayYear) (chinese)).month) &&
                (leap == chinese.leap)) {
            l2 = l1;
        } else {
            l2 = newMoonOnOrAfter(l1 + 1L);
        }

        super.rd = (l2 + (long) super.day) - 1L;
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
     * Sets the month, day, and year (combined from cycle and year-in-cycle).
     *
     * @param i the month.
     * @param j the day.
     * @param k the year.
     */
    public synchronized void set(int i, int j, int k) {
        set((int) Math.floor((double) (k - 1) / 60D) + 1,
            (int) AlternateCalendar.amod(k, 60L), i, false, j);
    }

    /**
     * Sets the cycle, year-in-cycle, month, leap status, and day.
     *
     * @param i    the cycle.
     * @param j    the year in cycle (1-60).
     * @param k    the month.
     * @param flag true if leap month.
     * @param l    the day.
     */
    public synchronized void set(int i, int j, int k, boolean flag, int l) {
        super.month = k;
        super.day = l;
        cycle = i;
        super.year = j;
        leap = flag;
        recomputeRD();
    }

    /**
     * Returns the leap month status of the current date.
     *
     * @return true if it is a leap month.
     */
    public boolean getLeap() {
        return leap;
    }

    /**
     * Returns the suffix for Chinese years (empty).
     *
     * @return an empty string.
     */
    public String getSuffix() {
        return "";
    }

    /**
     * Returns the name of the current lunar month, including leap status.
     *
     * @return the month name string.
     */
    public String monthName() {
        return MONTHS[super.month - 1] + (leap ? "(leap)" : "");
    }

    /**
     * Returns an enumeration of all Chinese month names.
     *
     * @return enumeration of month names.
     */
    @Override
    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }

    /**
     * Returns the traditional name of the current year (Stem-Branch).
     *
     * @return the year name string.
     */
    private String yearName() {
        String[] as = {
                "", "Jia", "Yi", "Bing", "Ding", "Wu", "Ji", "Geng", "Xin",
                "Ren", "Gui"
            };
        String[] as1 = {
                "", "Zi (Rat)", "Chou (Ox)", "Yin (Tiger)", "Mao (Hare)",
                "Chen (Dragon)", "Si (Snake)", "Wu (Horse)", "Wei (Sheep)",
                "Shen (Monkey)", "You (Fowl)", "Xu (Dog)", "Hai (Pig)"
            };

        return as[(int) AlternateCalendar.amod(super.year, 10L)] + "-" +
        as1[(int) AlternateCalendar.amod(super.year, 12L)];
    }

    /**
     * Returns a string representation of the Chinese date.
     *
     * @return the date string.
     */
    public String toString() {
        return super.day + " " + monthName() + ", Year " + super.year + ": " +
        yearName() + ", cycle " + cycle;
    }

    /**
     * Main method for testing the Chinese calendar implementation.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        int i = 1;
        int j = 1;
        int k = 1;
        int l = 1;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
            l = Integer.parseInt(args[3]);
        } catch (Exception _ex) {
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        ChineseCalendar chinese = new ChineseCalendar(gregorian.toRD());
        System.out.println(chinese);
        chinese.set(l, k, i, false, j);
        System.out.println(chinese + ": " + chinese.toRD());

        try {
            chinese.set(Long.parseLong(args[4]));
        } catch (Exception _ex) {
        }

        System.out.println(chinese.toRD() + ": " + chinese);
    }
}
