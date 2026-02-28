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

import java.util.Enumeration;

/**
 * Abstract base class for calendar systems that use a month-day-year date representation.
 * Extends {@link SevenDaysWeek} to include weekday calculations.
 *
 * <p>Subclasses must implement:</p>
 * <ul>
 *   <li>{@link #monthName()} - Returns the name of the current month</li>
 *   <li>{@link #getSuffix()} - Returns an era suffix (e.g., "CE", "BCE")</li>
 *   <li>{@link #getMonths()} - Returns an enumeration of all month names</li>
 *   <li>{@link #recomputeRD()} - Computes Rata Die from date fields</li>
 *   <li>{@link #recomputeFromRD()} - Computes date fields from Rata Die</li>
 * </ul>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public abstract class MonthDayYear extends SevenDaysWeek {

    private static final long serialVersionUID = 1L;

    /** The month (1-12 for most calendars). */
    protected int month;

    /** The day of the month (1-31 for most calendars). */
    protected int day;

    /** The year (negative values typically represent BCE years). */
    protected int year;

    /**
     * Default constructor.
     */
    public MonthDayYear() {
        // Default initialization
    }

    /**
     * Returns the day of the month.
     *
     * @return the day (typically 1-31)
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the month.
     *
     * @return the month (typically 1-12)
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the year.
     *
     * @return the year (negative for BCE)
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the calendar to the specified date and recomputes the Rata Die.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day of month
     */
    public synchronized void set(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        recomputeRD();
    }

    /**
     * Returns a string representation of this date.
     *
     * @return formatted date string
     */
    @Override
    public String toString() {
        try {
            return String.format("%d %s %d%s", day, monthName(), year, getSuffix());
        } catch (ArrayIndexOutOfBoundsException e) {
            return String.format("Invalid date: month=%d, day=%d, year=%d", month, day, year);
        }
    }

    /**
     * Returns the name of the current month.
     *
     * @return the month name
     */
    protected abstract String monthName();

    /**
     * Returns the era suffix for display (e.g., "CE", "BCE", "AH").
     *
     * @return the suffix string
     */
    protected abstract String getSuffix();

    /**
     * Returns an enumeration of all month names for this calendar.
     *
     * @return month names enumeration
     */
    public abstract Enumeration<String> getMonths();
}

