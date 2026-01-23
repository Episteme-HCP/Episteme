/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.history.time;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides chronological operations on uncertain dates.
 * Enables set-theoretic operations like intersection and union, as well as 
 * distance and concomitance calculations for historical analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class ChronologicalOperator {

    private ChronologicalOperator() {
        // Utility class
    }

    /**
     * Calculates the intersection of two uncertain date ranges.
     * Returns the period where both ranges overlap, or empty if they are disjoint.
     *
     * @param a the first uncertain date
     * @param b the second uncertain date
     * @return optional intersection interval
     * @throws NullPointerException if a or b is null
     */
    public static Optional<UncertainDate> intersection(UncertainDate a, UncertainDate b) {
        Objects.requireNonNull(a, "UncertainDate 'a' cannot be null");
        Objects.requireNonNull(b, "UncertainDate 'b' cannot be null");
        
        LocalDate startA = a.getEarliestPossible();
        LocalDate endA = a.getLatestPossible();
        LocalDate startB = b.getEarliestPossible();
        LocalDate endB = b.getLatestPossible();
        
        LocalDate intersectionStart = startA.isAfter(startB) ? startA : startB;
        LocalDate intersectionEnd = endA.isBefore(endB) ? endA : endB;
        
        if (intersectionStart.isAfter(intersectionEnd)) {
            return Optional.empty();
        }
        
        return Optional.of(UncertainDate.between(intersectionStart, intersectionEnd));
    }

    /**
     * Calculates the union of two uncertain date ranges.
     * Returns the smallest range that encompassing both (the bounding interval).
     *
     * @param a the first uncertain date
     * @param b the second uncertain date
     * @return the union interval
     * @throws NullPointerException if a or b is null
     */
    public static UncertainDate union(UncertainDate a, UncertainDate b) {
        Objects.requireNonNull(a, "UncertainDate 'a' cannot be null");
        Objects.requireNonNull(b, "UncertainDate 'b' cannot be null");
        
        LocalDate startA = a.getEarliestPossible();
        LocalDate endA = a.getLatestPossible();
        LocalDate startB = b.getEarliestPossible();
        LocalDate endB = b.getLatestPossible();
        
        LocalDate unionStart = startA.isBefore(startB) ? startA : startB;
        LocalDate unionEnd = endA.isAfter(endB) ? endA : endB;
        
        return UncertainDate.between(unionStart, unionEnd);
    }

    /**
     * Checks if two events or periods were potentially contemporaneous.
     * Two events are contemporaneous if their uncertain date ranges overlap.
     *
     * @param a the first uncertain date
     * @param b the second uncertain date
     * @return true if they overlap
     * @throws NullPointerException if a or b is null
     */
    public static boolean areContemporaneous(UncertainDate a, UncertainDate b) {
        return intersection(a, b).isPresent();
    }

    /**
     * Calculates the minimum possible duration (in days) between two events.
     * Returns 0 if they could potentially overlap or occur in any order relative to each other 
     * within their uncertainty.
     *
     * @param earlier the presumed earlier uncertain date
     * @param later   the presumed later uncertain date
     * @return minimum gap in days
     * @throws NullPointerException if earlier or later is null
     */
    public static long minimumGapDays(UncertainDate earlier, UncertainDate later) {
        Objects.requireNonNull(earlier, "Earlier date cannot be null");
        Objects.requireNonNull(later, "Later date cannot be null");
        
        if (areContemporaneous(earlier, later)) {
            return 0;
        }
        
        LocalDate endEarlier = earlier.getLatestPossible();
        LocalDate startLater = later.getEarliestPossible();
        
        long gap = ChronoUnit.DAYS.between(endEarlier, startLater);
        return Math.max(0, gap);
    }

    /**
     * Calculates the maximum possible duration (in days) between two events.
     *
     * @param earlier the presumed earlier uncertain date
     * @param later   the presumed later uncertain date
     * @return maximum gap in days
     * @throws NullPointerException if earlier or later is null
     */
    public static long maximumGapDays(UncertainDate earlier, UncertainDate later) {
        Objects.requireNonNull(earlier, "Earlier date cannot be null");
        Objects.requireNonNull(later, "Later date cannot be null");
        
        LocalDate startEarlier = earlier.getEarliestPossible();
        LocalDate endLater = later.getLatestPossible();
        
        return ChronoUnit.DAYS.between(startEarlier, endLater);
    }

    /**
     * Returns the potential duration range (minimum and maximum days) between two events.
     *
     * @param from start event uncertain date
     * @param to   end event uncertain date
     * @return array where [0] is min days and [1] is max days
     * @throws NullPointerException if from or to is null
     */
    public static long[] durationRange(UncertainDate from, UncertainDate to) {
        return new long[]{
            minimumGapDays(from, to),
            maximumGapDays(from, to)
        };
    }
}
