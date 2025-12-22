package org.jscience.history;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * Represents a specific point in time.
 * Wrapper around java.time types for JScience integration.
 */
public class TimePoint implements Comparable<TimePoint>, Serializable {

    private final Instant instant;

    private TimePoint(Instant instant) {
        this.instant = instant;
    }

    public static TimePoint now() {
        return new TimePoint(Instant.now());
    }

    public static TimePoint of(Instant instant) {
        return new TimePoint(instant);
    }

    public static TimePoint of(LocalDateTime ldt) {
        return new TimePoint(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static TimePoint of(LocalDate ld) {
        return new TimePoint(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Instant toInstant() {
        return instant;
    }

    @Override
    public int compareTo(TimePoint o) {
        return instant.compareTo(o.instant);
    }

    @Override
    public String toString() {
        return instant.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimePoint) {
            return instant.equals(((TimePoint) obj).instant);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return instant.hashCode();
    }
}
