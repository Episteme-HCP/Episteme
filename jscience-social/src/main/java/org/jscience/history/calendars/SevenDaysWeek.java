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

package org.jscience.history.calendars;

/**
 * Abstract base class for calendar systems that use a seven-day week.
 * Provides methods for weekday calculations and finding specific days of the week
 * relative to a given date.
 *
 * <p>Day numbering follows ISO convention where Sunday = 0 through Saturday = 6.</p>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public abstract class SevenDaysWeek extends AlternateCalendar {

    private static final long serialVersionUID = 1L;

    /** Sunday (day 0 of the week). */
    public static final int SUNDAY = 0;

    /** Monday (day 1 of the week). */
    public static final int MONDAY = 1;

    /** Tuesday (day 2 of the week). */
    public static final int TUESDAY = 2;

    /** Wednesday (day 3 of the week). */
    public static final int WEDNESDAY = 3;

    /** Thursday (day 4 of the week). */
    public static final int THURSDAY = 4;

    /** Friday (day 5 of the week). */
    public static final int FRIDAY = 5;

    /** Saturday (day 6 of the week). */
    public static final int SATURDAY = 6;

    /** English names for days of the week. */
    public static final String[] DAY_NAMES = {
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    /** @deprecated Use {@link #DAY_NAMES} instead. */
    @Deprecated
    public static final String[] DAYNAMES = DAY_NAMES;

    /**
     * Default constructor.
     */
    public SevenDaysWeek() {
        // Default initialization
    }

    /**
     * Returns the day of the week for the current date.
     *
     * @return the weekday (0 = Sunday, 6 = Saturday)
     */
    public int weekDay() {
        return AlternateCalendar.mod(toRD(), 7);
    }

    /**
     * Returns the name of the current weekday.
     *
     * @return the weekday name (e.g., "Monday")
     */
    public String weekDayName() {
        return DAY_NAMES[weekDay()];
    }

    /**
     * Computes the weekday for a specific Rata Die.
     *
     * @param rataDie the day number
     * @return the weekday (0-6)
     */
    private static int weekDay(long rataDie) {
        return AlternateCalendar.mod(rataDie, 7);
    }

    /**
     * Adjusts this date to the specified weekday on or before the current date.
     *
     * @param weekday the target weekday (0-6)
     */
    public void kDayOnOrBefore(int weekday) {
        long currentRD = toRD();
        subtract(weekDay(currentRD - (long) weekday));
    }

    /**
     * Adjusts this date to the specified weekday on or after the current date.
     *
     * @param weekday the target weekday (0-6)
     */
    public void kDayOnOrAfter(int weekday) {
        add(6L);
        kDayOnOrBefore(weekday);
    }

    /**
     * Adjusts this date to the specified weekday nearest to the current date.
     *
     * @param weekday the target weekday (0-6)
     */
    public void kDayNearest(int weekday) {
        add(3L);
        kDayOnOrBefore(weekday);
    }

    /**
     * Adjusts this date to the first occurrence of the specified weekday after the current date.
     *
     * @param weekday the target weekday (0-6)
     */
    public void kDayAfter(int weekday) {
        add(7L);
        kDayOnOrBefore(weekday);
    }

    /**
     * Adjusts this date to the last occurrence of the specified weekday before the current date.
     *
     * @param weekday the target weekday (0-6)
     */
    public void kDayBefore(int weekday) {
        add(1L);
        kDayOnOrBefore(weekday);
    }

    /**
     * Adjusts this date to the nth occurrence of the specified weekday relative to the current date.
     * Positive n means forward, negative n means backward.
     *
     * @param n       the occurrence count (positive = forward, negative = backward)
     * @param weekday the target weekday (0-6)
     */
    public void nthKDay(int n, int weekday) {
        if (n < 0) {
            kDayBefore(weekday);
        } else {
            kDayAfter(weekday);
        }
        add(7L * n);
    }
}
