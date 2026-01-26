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

package org.jscience.history.clock;

import java.time.LocalDate;
import java.time.chrono.*;
import java.util.*;

/**
 * Utility for synchronizing dates across various historical, religious, and political calendars.
 * Provides conversions between standard ISO-8601 (Gregorian) and systems like Hijri, 
 * French Republican, and Julian.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class CalendarSynchronizer {

    private CalendarSynchronizer() {
        // Utility class
    }

    /**
     * Converts a Gregorian date to multiple chronological systems.
     *
     * @param year  the Gregorian year
     * @param month the Gregorian month (1-12)
     * @param day   the Gregorian day of month
     * @return a map of system names to formatted date strings
     * @throws java.time.DateTimeException if date components are invalid
     */
    public static Map<String, String> convertGregorian(int year, int month, int day) {
        LocalDate iso = LocalDate.of(year, month, day);
        Map<String, String> results = new LinkedHashMap<>();
        
        results.put("Gregorian", iso.toString());
        results.put("Hijri", HijrahDate.from(iso).toString());
        results.put("Japanese", JapaneseDate.from(iso).toString());
        results.put("Minguo", MinguoDate.from(iso).toString());
        results.put("ThaiBuddhist", ThaiBuddhistDate.from(iso).toString());
        
        return results;
    }

    /**
     * Calculates the offset in days between Julian and Gregorian calendars for a given year.
     * <p>
     * Formula: {@code Floor(Y/100) - Floor(Y/400) - 2}
     * </p>
     * 
     * @param year the year to calculate for
     * @return day offset (positive means Gregorian is ahead)
     */
    public static int julianToGregorianOffset(int year) {
        return (year / 100) - (year / 400) - 2;
    }

    /**
     * Determines if a year is a leap year in the Julian calendar.
     * In the Julian system, every year divisible by 4 is a leap year.
     *
     * @param year the year to check
     * @return true if leap year
     */
    public static boolean isJulianLeapYear(int year) {
        return year % 4 == 0;
    }

    /**
     * Converts a modern date to the French Republican Calendar (simplified model).
     * <p>
     * Uses September 22, 1792 (Autumnal Equinox) as the Epoch (Year I).
     * This implementation uses a fixed 365-day year model.
     * </p>
     *
     * @param date the date to convert
     * @return formatted republican date string
     * @throws NullPointerException if date is null
     */
    public static String toFrenchRepublican(LocalDate date) {
        Objects.requireNonNull(date, "Date cannot be null");
        
        // Base: Sept 22, 1792
        LocalDate base = LocalDate.of(1792, 9, 22);
        long days = date.toEpochDay() - base.toEpochDay();
        if (days < 0) return "Before Republic";
        
        int year = (int) (days / 365) + 1;
        int dayOfYear = (int) (days % 365);
        int month = (dayOfYear / 30) + 1;
        int day = (dayOfYear % 30) + 1;
        
        if (month > 12) {
            return String.format("Year %d, Complementary Day %d", year, (dayOfYear - 360 + 1));
        }
        return String.format("Year %d, Month %d, Day %d", year, month, day);
    }
}
