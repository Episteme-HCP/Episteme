package org.jscience.history.time;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Represents a historical period defined by two fuzzy points in time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class FuzzyTimeInterval implements TimeCoordinate, Serializable {

    private static final long serialVersionUID = 2L;

    private final FuzzyTimePoint start;
    private final FuzzyTimePoint end;

    public FuzzyTimeInterval(FuzzyTimePoint start, FuzzyTimePoint end) {
        this.start = Objects.requireNonNull(start, "Start cannot be null");
        this.end = end != null ? end : start;
    }

    public static FuzzyTimeInterval of(FuzzyTimePoint start, FuzzyTimePoint end) {
        return new FuzzyTimeInterval(start, end);
    }

    @Override
    public Instant toInstant() {
        long startMs = start.toInstant().toEpochMilli();
        long endMs = end.toInstant().toEpochMilli();
        return Instant.ofEpochMilli((startMs + endMs) / 2);
    }

    @Override
    public Precision getPrecision() {
        return start.getPrecision();
    }

    @Override
    public boolean isFuzzy() {
        return true;
    }

    public FuzzyTimePoint getStart() {
        return start;
    }

    public FuzzyTimePoint getEnd() {
        return end;
    }

    @Override
    public int compareTo(TimeCoordinate o) {
        return this.start.compareTo(o);
    }

    @Override
    public String toString() {
        if (start.equals(end)) return start.toString();
        return String.format("[%s - %s]", start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuzzyTimeInterval that)) return false;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
