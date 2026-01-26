package org.jscience.history.time;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A precise point in time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class TimePoint implements TimeCoordinate, Serializable {

    private static final long serialVersionUID = 2L;

    private final Instant instant;

    public TimePoint(Instant instant) {
        this.instant = Objects.requireNonNull(instant, "Instant cannot be null");
    }

    public static TimePoint of(Instant instant) {
        return new TimePoint(instant);
    }

    public static TimePoint now() {
        return new TimePoint(Instant.now());
    }

    @Override
    public Instant toInstant() {
        return instant;
    }

    @Override
    public Precision getPrecision() {
        return Precision.EXACT;
    }

    @Override
    public boolean isFuzzy() {
        return false;
    }

    @Override
    public int compareTo(TimeCoordinate o) {
        return this.instant.compareTo(o.toInstant());
    }

    @Override
    public String toString() {
        return instant.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimePoint that)) return false;
        return Objects.equals(instant, that.instant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instant);
    }
}
