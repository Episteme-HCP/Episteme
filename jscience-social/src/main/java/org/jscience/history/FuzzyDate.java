/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.history;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a date with varying degrees of precision and uncertainty.
 * <p>
 * Historical dates often have incomplete or uncertain information.
 * FuzzyDate supports:
 * <ul>
 * <li>Full dates (year, month, day)</li>
 * <li>Year-month only</li>
 * <li>Year only</li>
 * <li>Approximate dates ("circa")</li>
 * <li>BCE/CE era support</li>
 * <li>Century/decade references</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FuzzyDate implements Comparable<FuzzyDate> {

    /**
     * Precision levels for historical dates.
     */
    public enum Precision {
        EXACT, // Known to the day
        APPROXIMATE, // "Circa" - approximately
        MONTH, // Known to month/year
        YEAR, // Year only
        DECADE, // e.g., "1920s"
        CENTURY, // e.g., "5th century"
        MILLENNIUM, // e.g., "2nd millennium BCE"
        UNKNOWN // Unknown date
    }

    /**
     * Historical era.
     */
    public enum Era {
        BCE("Before Common Era"),
        CE("Common Era"),
        BC("Before Christ"),
        AD("Anno Domini");

        private final String description;

        Era(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final Integer year;
    private final Integer month; // 1-12
    private final Integer day; // 1-31
    private final Precision precision;
    private final Era era;
    private final String qualifier; // e.g., "early", "late", "mid"

    private FuzzyDate(Integer year, Integer month, Integer day,
            Precision precision, Era era, String qualifier) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.precision = precision;
        this.era = era != null ? era : Era.CE;
        this.qualifier = qualifier;
    }

    // ========== Factory Methods ==========

    /**
     * Creates an exact date.
     */
    public static FuzzyDate of(int year, int month, int day) {
        return new FuzzyDate(year, month, day, Precision.EXACT, Era.CE, null);
    }

    /**
     * Creates a year-month date.
     */
    public static FuzzyDate of(int year, int month) {
        return new FuzzyDate(year, month, null, Precision.MONTH, Era.CE, null);
    }

    /**
     * Creates a year-only date.
     */
    public static FuzzyDate of(int year) {
        return new FuzzyDate(year, null, null, Precision.YEAR, Era.CE, null);
    }

    /**
     * Creates an approximate ("circa") date.
     */
    public static FuzzyDate circa(int year) {
        return new FuzzyDate(year, null, null, Precision.APPROXIMATE, Era.CE, null);
    }

    /**
     * Creates a BCE date.
     */
    public static FuzzyDate bce(int year) {
        return new FuzzyDate(year, null, null, Precision.YEAR, Era.BCE, null);
    }

    /**
     * Creates an approximate BCE date.
     */
    public static FuzzyDate circaBce(int year) {
        return new FuzzyDate(year, null, null, Precision.APPROXIMATE, Era.BCE, null);
    }

    /**
     * Creates a century reference.
     *
     * @param century the century number (e.g., 5 for "5th century")
     * @param era     the era
     */
    public static FuzzyDate century(int century, Era era) {
        int year = (century - 1) * 100 + 50; // middle of century
        return new FuzzyDate(year, null, null, Precision.CENTURY, era, null);
    }

    /**
     * Creates a decade reference.
     *
     * @param decade the decade start year (e.g., 1920 for "1920s")
     */
    public static FuzzyDate decade(int decade) {
        return new FuzzyDate(decade + 5, null, null, Precision.DECADE, Era.CE, null);
    }

    /**
     * Creates an unknown date.
     */
    public static FuzzyDate unknown() {
        return new FuzzyDate(null, null, null, Precision.UNKNOWN, null, null);
    }

    /**
     * Creates from a LocalDate.
     */
    public static FuzzyDate from(LocalDate date) {
        return of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    // ========== Getters ==========

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public Precision getPrecision() {
        return precision;
    }

    public Era getEra() {
        return era;
    }

    public String getQualifier() {
        return qualifier;
    }

    public boolean isKnown() {
        return precision != Precision.UNKNOWN;
    }

    public boolean isBce() {
        return era == Era.BCE || era == Era.BC;
    }

    /**
     * Converts to LocalDate if precision allows.
     *
     * @return LocalDate representation
     * @throws IllegalStateException if date is too imprecise or BCE
     */
    public LocalDate toLocalDate() {
        if (precision == Precision.UNKNOWN || isBce()) {
            throw new IllegalStateException("Cannot convert to LocalDate: " + this);
        }
        int y = year != null ? year : 1;
        int m = month != null ? month : 1;
        int d = day != null ? day : 1;
        return LocalDate.of(y, m, d);
    }

    // ========== Comparison ==========

    @Override
    public int compareTo(FuzzyDate other) {
        if (this.precision == Precision.UNKNOWN)
            return other.precision == Precision.UNKNOWN ? 0 : -1;
        if (other.precision == Precision.UNKNOWN)
            return 1;

        // Convert to comparable year (negative for BCE)
        int thisYear = this.year != null ? (this.isBce() ? -this.year : this.year) : 0;
        int otherYear = other.year != null ? (other.isBce() ? -other.year : other.year) : 0;

        int cmp = Integer.compare(thisYear, otherYear);
        if (cmp != 0)
            return cmp;

        cmp = Integer.compare(this.month != null ? this.month : 0,
                other.month != null ? other.month : 0);
        if (cmp != 0)
            return cmp;

        return Integer.compare(this.day != null ? this.day : 0,
                other.day != null ? other.day : 0);
    }

    @Override
    public String toString() {
        if (precision == Precision.UNKNOWN) {
            return "Unknown date";
        }

        StringBuilder sb = new StringBuilder();

        if (qualifier != null) {
            sb.append(qualifier).append(" ");
        }

        if (precision == Precision.APPROXIMATE) {
            sb.append("c. ");
        }

        if (precision == Precision.CENTURY && year != null) {
            int century = (Math.abs(year) / 100) + 1;
            sb.append(ordinal(century)).append(" century");
        } else if (precision == Precision.DECADE && year != null) {
            int decade = (year / 10) * 10;
            sb.append(decade).append("s");
        } else {
            if (day != null) {
                sb.append(day).append(" ");
            }
            if (month != null) {
                sb.append(monthName(month)).append(" ");
            }
            if (year != null) {
                sb.append(Math.abs(year));
            }
        }

        if (isBce() && year != null) {
            sb.append(" BCE");
        }

        return sb.toString();
    }

    private static String ordinal(int n) {
        if (n >= 11 && n <= 13)
            return n + "th";
        return switch (n % 10) {
            case 1 -> n + "st";
            case 2 -> n + "nd";
            case 3 -> n + "rd";
            default -> n + "th";
        };
    }

    private static String monthName(int month) {
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FuzzyDate other))
            return false;
        return Objects.equals(year, other.year) &&
                Objects.equals(month, other.month) &&
                Objects.equals(day, other.day) &&
                precision == other.precision &&
                era == other.era;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, precision, era);
    }
}
