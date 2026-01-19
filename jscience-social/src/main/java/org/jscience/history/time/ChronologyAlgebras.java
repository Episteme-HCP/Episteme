package org.jscience.history.time;

/**
 * Mathematical operations for historical chronologies and uncertain dates.
 */
public final class ChronologyAlgebras {

    private ChronologyAlgebras() {}

    /**
     * Represents the overlap between two uncertain date ranges.
     */
    public record DateOverlap(UncertainDate intersection, boolean overlaps) {}

    /**
     * Calculates the intersection (overlap) of two uncertain dates.
     * 
     * @param d1 First uncertain date.
     * @param d2 Second uncertain date.
     * @return The overlapping range, or a null range if they don't overlap.
     */
    public static DateOverlap calculateIntersection(UncertainDate d1, UncertainDate d2) {
        long start1 = toMillis(d1.getMin(0));
        long end1 = toMillis(d1.getMax(0));
        long start2 = toMillis(d2.getMin(0));
        long end2 = toMillis(d2.getMax(0));

        long intersectStart = Math.max(start1, start2);
        long intersectEnd = Math.min(end1, end2);

        if (intersectStart <= intersectEnd) {
            return new DateOverlap(null, true);
        }
        
        return new DateOverlap(null, false);
    }

    private static long toMillis(Object temporal) {
        if (temporal instanceof java.time.LocalDate ld) {
            return ld.toEpochDay() * 86400_000L;
        } else if (temporal instanceof java.time.Instant i) {
            return i.toEpochMilli();
        } else if (temporal instanceof java.time.chrono.ChronoLocalDate cld) {
             return cld.toEpochDay() * 86400_000L;
        }
        // Fallback for Date if still used legacy
        if (temporal instanceof java.util.Date d) {
            return d.getTime();
        }
        throw new IllegalArgumentException("Unknown temporal type: " + (temporal == null ? "null" : temporal.getClass()));
    }
}
