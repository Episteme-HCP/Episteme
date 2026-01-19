package org.jscience.history.time;

import java.time.LocalDate;
import java.time.chrono.*;
import java.util.*;

/**
 * Synchronizes dates across different historical and religious calendars.
 */
public final class CalendarSynchronizer {

    private CalendarSynchronizer() {}

    /**
     * Converts a Gregorian date to multiple systems.
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
     * Julian to Gregorian offset calculation.
     * Offset = Floor(Y/100) - Floor(Y/400) - 2
     */
    public static int julianToGregorianOffset(int year) {
        return (year / 100) - (year / 400) - 2;
    }

    /**
     * Checks if a year is a leap year in the Julian calendar (every 4 years).
     */
    public static boolean isJulianLeapYear(int year) {
        return year % 4 == 0;
    }

    /**
     * French Republican Calendar (very simplified).
     */
    public static String toFrenchRepublican(LocalDate date) {
        // Base: Sept 22, 1792
        LocalDate base = LocalDate.of(1792, 9, 22);
        long days = date.toEpochDay() - base.toEpochDay();
        if (days < 0) return "Before Republic";
        
        int year = (int) (days / 365) + 1;
        int dayOfYear = (int) (days % 365);
        int month = (dayOfYear / 30) + 1;
        int day = (dayOfYear % 30) + 1;
        
        if (month > 12) return "Year " + year + " complementary day " + (dayOfYear - 360 + 1);
        return "Year " + year + " Month " + month + " Day " + day;
    }
}
