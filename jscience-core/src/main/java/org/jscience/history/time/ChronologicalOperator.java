package org.jscience.history.time;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Chronological operations on uncertain dates.
 * Enables intersection, union, and concomitance calculations.
 */
public final class ChronologicalOperator {

    private ChronologicalOperator() {}

    /**
     * Calculates the intersection of two uncertain date ranges.
     * Returns the period where both ranges overlap.
     */
    public static Optional<UncertainDate> intersection(UncertainDate a, UncertainDate b) {
        LocalDate startA = a.getEarliestPossible();
        LocalDate endA = a.getLatestPossible();
        LocalDate startB = b.getEarliestPossible();
        LocalDate endB = b.getLatestPossible();
        
        if (startA == null || endA == null || startB == null || endB == null) {
            return Optional.empty();
        }
        
        LocalDate intersectionStart = startA.isAfter(startB) ? startA : startB;
        LocalDate intersectionEnd = endA.isBefore(endB) ? endA : endB;
        
        if (intersectionStart.isAfter(intersectionEnd)) {
            return Optional.empty(); // No overlap
        }
        
        return Optional.of(UncertainDate.between(intersectionStart, intersectionEnd));
    }

    /**
     * Calculates the union of two uncertain date ranges.
     * Returns the smallest range that encompasses both.
     */
    public static UncertainDate union(UncertainDate a, UncertainDate b) {
        LocalDate startA = a.getEarliestPossible();
        LocalDate endA = a.getLatestPossible();
        LocalDate startB = b.getEarliestPossible();
        LocalDate endB = b.getLatestPossible();
        
        LocalDate unionStart = (startA != null && startB != null) 
            ? (startA.isBefore(startB) ? startA : startB)
            : (startA != null ? startA : startB);
        LocalDate unionEnd = (endA != null && endB != null)
            ? (endA.isAfter(endB) ? endA : endB)
            : (endA != null ? endA : endB);
        
        return UncertainDate.between(unionStart, unionEnd);
    }

    /**
     * Checks if two events/reigns were potentially contemporaneous.
     */
    public static boolean areContemporaneous(UncertainDate a, UncertainDate b) {
        return intersection(a, b).isPresent();
    }

    /**
     * Calculates the minimum possible duration between two events.
     * Returns 0 if they could overlap.
     */
    public static long minimumGapDays(UncertainDate earlier, UncertainDate later) {
        Optional<UncertainDate> overlap = intersection(earlier, later);
        if (overlap.isPresent()) return 0;
        
        LocalDate endEarlier = earlier.getLatestPossible();
        LocalDate startLater = later.getEarliestPossible();
        
        if (endEarlier == null || startLater == null) return 0;
        
        long gap = java.time.temporal.ChronoUnit.DAYS.between(endEarlier, startLater);
        return Math.max(0, gap);
    }

    /**
     * Calculates the maximum possible duration between two events.
     */
    public static long maximumGapDays(UncertainDate earlier, UncertainDate later) {
        LocalDate startEarlier = earlier.getEarliestPossible();
        LocalDate endLater = later.getLatestPossible();
        
        if (startEarlier == null || endLater == null) return Long.MAX_VALUE;
        
        return java.time.temporal.ChronoUnit.DAYS.between(startEarlier, endLater);
    }

    /**
     * Calculates the probable duration range (min-max) between two uncertain events.
     */
    public static long[] durationRange(UncertainDate from, UncertainDate to) {
        return new long[]{
            minimumGapDays(from, to),
            maximumGapDays(from, to)
        };
    }
}
