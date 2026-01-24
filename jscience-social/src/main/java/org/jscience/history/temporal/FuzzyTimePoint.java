package org.jscience.history.temporal;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import org.jscience.mathematics.numbers.integers.Natural;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a historical or cosmic date with varying degrees of precision and uncertainty.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class FuzzyTimePoint implements TemporalCoordinate, Serializable {

    private static final long serialVersionUID = 2L;

    @Attribute
    private final Natural year;

    @Attribute
    private final Natural month; // 1-12

    @Attribute
    private final Natural day; // 1-31

    @Attribute
    private final Precision precision;

    @Attribute
    private final Era era;

    @Attribute
    private final String qualifier;

    private FuzzyTimePoint(Natural year, Natural month, Natural day,
            Precision precision, Era era, String qualifier) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.precision = Objects.requireNonNull(precision, "Precision cannot be null");
        this.era = era != null ? era : Era.CE;
        this.qualifier = qualifier;
    }

    public static FuzzyTimePoint of(long year, int month, int day) {
        return new FuzzyTimePoint(Natural.of(year), Natural.of(month), Natural.of(day), Precision.DAY, Era.CE, null);
    }

    public static FuzzyTimePoint of(long year, int month) {
        return new FuzzyTimePoint(Natural.of(year), Natural.of(month), null, Precision.MONTH, Era.CE, null);
    }

    public static FuzzyTimePoint of(long year) {
        return new FuzzyTimePoint(Natural.of(year), null, null, Precision.YEAR, Era.CE, null);
    }

    public static FuzzyTimePoint circa(long year) {
        return new FuzzyTimePoint(Natural.of(year), null, null, Precision.APPROXIMATE, Era.CE, null);
    }

    public static FuzzyTimePoint bce(long year) {
        return new FuzzyTimePoint(Natural.of(year), null, null, Precision.YEAR, Era.BCE, null);
    }

    public static FuzzyTimePoint unknown() {
        return new FuzzyTimePoint(null, null, null, Precision.UNKNOWN, null, null);
    }

    @Override
    public Precision getPrecision() {
        return precision;
    }

    @Override
    public boolean isFuzzy() {
        return precision != Precision.EXACT && precision != Precision.DAY;
    }

    @Override
    public java.time.Instant toInstant() {
        if (precision == Precision.UNKNOWN || year == null) {
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
    public int compareTo(TemporalCoordinate other) {
        return this.toInstant().compareTo(other.toInstant());
    }

    @Override
    public String toString() {
        if (precision == Precision.UNKNOWN) return "Unknown date";
        StringBuilder sb = new StringBuilder();
        if (qualifier != null) sb.append(qualifier).append(" ");
        if (precision == Precision.APPROXIMATE) sb.append("c. ");
        if (precision == Precision.CENTURY && year != null) {
            long centuryNum = (Math.abs(year.longValue()) / 100) + 1;
            sb.append(centuryNum).append(" century");
        } else if (precision == Precision.DECADE && year != null) {
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
                precision == other.precision &&
                era == other.era;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, precision, era);
    }
}
