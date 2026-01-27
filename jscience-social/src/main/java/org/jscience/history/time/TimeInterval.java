package org.jscience.history.time;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A continuous interval in time.
 * Defined by a start and an end instant.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class TimeInterval implements TimeCoordinate, Serializable {

    private static final long serialVersionUID = 2L;

    private final Instant start;
    private final Instant end;

    public TimeInterval(Instant start, Instant end) {
        this.start = Objects.requireNonNull(start, "Start cannot be null");
        this.end = end != null ? end : start;
        if (this.start.isAfter(this.end)) {
            throw new IllegalArgumentException("Start cannot be after end");
        }
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    @Override
    public Instant toInstant() {
        // Return midpoint as representative point
        return Instant.ofEpochMilli((start.toEpochMilli() + end.toEpochMilli()) / 2);
    }

    @Override
    public TimePrecision getPrecision() {
        return TimePrecision.EXACT;
    }

    @Override
    public boolean isFuzzy() {
        return !start.equals(end);
    }

    public boolean contains(Instant instant) {
        return !instant.isBefore(start) && !instant.isAfter(end);
    }

    public boolean overlaps(TimeInterval other) {
        return !this.end.isBefore(other.start) && !this.start.isAfter(other.end);
    }

    @Override
    public int compareTo(TimeCoordinate o) {
        return this.start.compareTo(o.toInstant());
    }

    @Override
    public String toString() {
        if (start.equals(end)) return start.toString();
        return "[" + start + " - " + end + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeInterval that)) return false;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
