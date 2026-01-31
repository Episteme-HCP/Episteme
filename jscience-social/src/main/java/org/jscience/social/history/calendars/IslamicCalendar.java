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

import java.util.Enumeration;

/**
 * Implementation of the Islamic (Hijri) calendar using the tabular arithmetic method.
 * The Islamic calendar is a purely lunar calendar with 12 months alternating between
 * 29 and 30 days, resulting in either 354 or 355 days per year.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: July 16, 622 CE (Julian) - the Hijra of Prophet Muhammad</li>
 *   <li>Uses a 30-year cycle with 11 leap years (years 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)</li>
 *   <li>Months alternate between 29 and 30 days</li>
 *   <li>Years are marked with "A.H." (Anno Hegirae)</li>
 * </ul>
 *
 * <p>Note: This is the tabular/arithmetic Islamic calendar. The actual religious calendar
 * may differ as it is based on lunar observation.</p>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class IslamicCalendar extends MonthDayYear {

    private static final long serialVersionUID = 1L;

    /** The Islamic epoch: July 16, 622 CE (Julian calendar). */
    public static long EPOCH = (new JulianCalendar(622, 7, 16)).toRD();

    /** Month names in Arabic (transliterated). */
    private static final String[] MONTHS = {
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Ula", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qa'da", "Dhu al-Hijja"
    };

    /** Number of days in one lunar year (non-leap). */
    private static final int LUNAR_YEAR = 354;

    /** Average month length (for calculations). */
    private static final double MONTH_LENGTH = 29.5;

    /**
     * Creates an Islamic calendar set to the epoch (1 Muharram, 1 AH).
     */
    public IslamicCalendar() {
        this(EPOCH);
    }

    /**
     * Creates an Islamic calendar set to the specified Rata Die.
     *
     * @param rataDie the day number
     */
    public IslamicCalendar(long rataDie) {
        set(rataDie);
    }

    /**
     * Creates an Islamic calendar set to the specified date.
     *
     * @param year  the Hijri year
     * @param month the month (1-12)
     * @param day   the day of month (1-30)
     */
    public IslamicCalendar(int year, int month, int day) {
        set(year, month, day);
    }

    /**
     * Creates an Islamic calendar from another calendar's date.
     *
     * @param calendar the source calendar
     */
    public IslamicCalendar(AlternateCalendar calendar) {
        set(calendar.toRD());
    }

    /**
     * Recomputes the Rata Die from the current year, month, and day fields.
     */
    @Override
    protected synchronized void recomputeRD() {
        rd = (long) day
            + (long) Math.ceil(MONTH_LENGTH * (month - 1))
            + (long) ((year - 1) * LUNAR_YEAR)
            + AlternateCalendar.floorDiv(3 + (11 * year), 30L)
            + EPOCH - 1L;
    }

    /**
     * Recomputes the year, month, and day fields from the current Rata Die.
     */
    @Override
    protected synchronized void recomputeFromRD() {
        year = (int) AlternateCalendar.floorDiv((30L * (rd - EPOCH)) + 10646L, 10631L);

        int priorDays = (int) Math.ceil((rd - 29L - (new IslamicCalendar(year, 1, 1)).toRD()) / MONTH_LENGTH) + 1;
        month = Math.min(12, priorDays);
        day = (int) (rd - (new IslamicCalendar(year, month, 1)).toRD()) + 1;
    }

    /**
     * Sets this calendar to the specified Rata Die and recomputes the date fields.
     *
     * @param rataDie the new Rata Die value
     */
    @Override
    public synchronized void set(long rataDie) {
        rd = rataDie;
        recomputeFromRD();
    }

    /**
     * Determines if the specified year is a leap year in the Islamic calendar.
     * Leap years in the 30-year cycle are: 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29.
     *
     * @param year the Hijri year
     * @return true if it's a leap year
     */
    public static boolean isLeapYear(int year) {
        int mod30 = AlternateCalendar.mod(year, 30);
        return mod30 == 2 || mod30 == 5 || mod30 == 7 || mod30 == 10 ||
               mod30 == 13 || mod30 == 16 || mod30 == 18 || mod30 == 21 ||
               mod30 == 24 || mod30 == 26 || mod30 == 29;
    }

    /**
     * Returns the name of the current month.
     *
     * @return the month name
     */
    @Override
    protected String monthName() {
        return MONTHS[month - 1];
    }

    /**
     * Returns the era suffix for display.
     *
     * @return " A.H." (Anno Hegirae)
     */
    @Override
    protected String getSuffix() {
        return " A.H.";
    }

    /**
     * Returns an enumeration of month names.
     *
     * @return month names enumeration
     */
    @Override
    public Enumeration<String> getMonths() {
        return new ArrayEnumeration<>(MONTHS);
    }
}

