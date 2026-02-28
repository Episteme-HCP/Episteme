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

package org.episteme.social.history.time;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import org.episteme.core.mathematics.numbers.integers.Natural;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

/**
 * Represents a historical or cosmic date with varying degrees of precision and uncertainty.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class FuzzyTimePoint implements TimeCoordinate, Serializable {

    private static final long serialVersionUID = 2L;

    @Attribute
    private final Natural year;

    @Attribute
    private final Natural month; // 1-12

    @Attribute
    private final Natural day; // 1-31

    @Attribute
    private final TimePrecision precision;

    @Attribute
    private final Era era;

    @Attribute
    private final String qualifier;

    private FuzzyTimePoint(Natural year, Natural month, Natural day,
            TimePrecision precision, Era era, String qualifier) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.precision = Objects.requireNonNull(precision, "Precision cannot be null");
        this.era = era != null ? era : Era.CE;
        this.qualifier = qualifier;
    }

    public static FuzzyTimePoint of(long year, int month, int day) {
        return new FuzzyTimePoint(Natural.of(year), Natural.of(month), Natural.of(day), TimePrecision.DAY, Era.CE, null);
    }

    public static FuzzyTimePoint of(long year, int month) {
        return new FuzzyTimePoint(Natural.of(year), Natural.of(month), null, TimePrecision.MONTH, Era.CE, null);
    }

    public static FuzzyTimePoint of(long year) {
        return new FuzzyTimePoint(Natural.of(year), null, null, TimePrecision.YEAR, Era.CE, null);
    }

    public static FuzzyTimePoint circa(long year) {
        return new FuzzyTimePoint(Natural.of(year), null, null, TimePrecision.APPROXIMATE, Era.CE, null);
    }

    public static FuzzyTimePoint bce(long year) {
        return new FuzzyTimePoint(Natural.of(year), null, null, TimePrecision.YEAR, Era.BCE, null);
    }

    public static FuzzyTimePoint circaBce(long year) {
        return new FuzzyTimePoint(Natural.of(year), null, null, TimePrecision.APPROXIMATE, Era.BCE, null);
    }

    public static FuzzyTimePoint unknown() {
        return new FuzzyTimePoint(null, null, null, TimePrecision.UNKNOWN, null, null);
    }

    @Override
    public TimePrecision getPrecision() {
        return precision;
    }

    public Natural getYear() {
        return year;
    }

    public Era getEra() {
        return era;
    }

    @Override
    public boolean isFuzzy() {
        return !Objects.equals(precision, TimePrecision.EXACT) && !Objects.equals(precision, TimePrecision.DAY);
    }

    @Override
    public java.time.Instant toInstant() {
        if (precision == TimePrecision.UNKNOWN || year == null) {
            return java.time.Instant.MIN;
        }
        try {
            long y = (era == Era.BCE) ? -year.longValue() + 1 : year.longValue();
            int m = month != null ? (int) month.longValue() : 1;
            int d = day != null ? (int) day.longValue() : 1;
            return LocalDate.of((int)y, m, d).atStartOfDay(ZoneOffset.UTC).toInstant();
        } catch (Exception e) {
            return java.time.Instant.MIN;
        }
    }

    @Override
    public int compareTo(TimeCoordinate other) {
        return this.toInstant().compareTo(other.toInstant());
    }

    @Override
    public String toString() {
        if (precision == TimePrecision.UNKNOWN) return "Unknown date";
        StringBuilder sb = new StringBuilder();
        if (qualifier != null) sb.append(qualifier).append(" ");
        if (precision == TimePrecision.APPROXIMATE) sb.append("c. ");
        if (precision == TimePrecision.CENTURY && year != null) {
            long centuryNum = (Math.abs(year.longValue()) / 100) + 1;
            sb.append(centuryNum).append(" century");
        } else if (precision == TimePrecision.DECADE && year != null) {
            sb.append((year.longValue() / 10) * 10).append("s");
        } else {
            if (day != null) sb.append(day).append(" ");
            if (month != null) sb.append(month).append(" ");
            if (year != null) sb.append(year);
        }
        if (era == Era.BCE && year != null) sb.append(" BCE");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuzzyTimePoint other)) return false;
        return Objects.equals(year, other.year) &&
                Objects.equals(month, other.month) &&
                Objects.equals(day, other.day) &&
                Objects.equals(precision, other.precision) &&
                Objects.equals(era, other.era);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, precision, era);
    }
}

