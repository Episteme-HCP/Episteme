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

package org.jscience.history.clock;

import java.util.Objects;
import java.util.Optional;
import org.jscience.history.time.TimeInterval;

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
     * Calculates the intersection of two temporal intervals.
     * Returns the period where both ranges overlap, or empty if they are disjoint.
     *
     * @param a the first interval
     * @param b the second interval
     * @return optional intersection interval
     * @throws NullPointerException if a or b is null
     */
    public static java.util.Optional<TimeInterval> intersection(TimeInterval a, TimeInterval b) {
        Objects.requireNonNull(a, "Interval 'a' cannot be null");
        Objects.requireNonNull(b, "Interval 'b' cannot be null");
        
        if (!a.overlaps(b)) {
            return Optional.empty();
        }
        
        java.time.Instant start = a.getStart().isAfter(b.getStart()) ? a.getStart() : b.getStart();
        java.time.Instant end = a.getEnd().isBefore(b.getEnd()) ? a.getEnd() : b.getEnd();
        
        return java.util.Optional.of(new TimeInterval(start, end));
    }

    /**
     * Calculates the union of two temporal intervals.
     * Returns the smallest range that encompassing both (the bounding interval).
     *
     * @param a the first interval
     * @param b the second interval
     * @return the union interval
     * @throws NullPointerException if a or b is null
     */
    public static TimeInterval union(TimeInterval a, TimeInterval b) {
        Objects.requireNonNull(a, "Interval 'a' cannot be null");
        Objects.requireNonNull(b, "Interval 'b' cannot be null");
        
        java.time.Instant start = a.getStart().isBefore(b.getStart()) ? a.getStart() : b.getStart();
        java.time.Instant end = a.getEnd().isAfter(b.getEnd()) ? a.getEnd() : b.getEnd();
        
        return new TimeInterval(start, end);
    }

    /**
     * Checks if two events or periods were potentially contemporaneous.
     * Two events are contemporaneous if their time intervals overlap.
     *
     * @param a the first interval
     * @param b the second interval
     * @return true if they overlap
     * @throws NullPointerException if a or b is null
     */
    public static boolean areContemporaneous(TimeInterval a, TimeInterval b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.overlaps(b);
    }

    /**
     * Calculates the minimum possible duration (in milliseconds) between two events.
     * Returns 0 if they could potentially overlap or occur in any order relative to each other 
     * within their uncertainty.
     *
     * @param earlier the presumed earlier interval
     * @param later   the presumed later interval
     * @return minimum gap in milliseconds
     * @throws NullPointerException if earlier or later is null
     */
    public static long minimumGapMillis(TimeInterval earlier, TimeInterval later) {
        Objects.requireNonNull(earlier, "Earlier interval cannot be null");
        Objects.requireNonNull(later, "Later interval cannot be null");
        
        if (areContemporaneous(earlier, later)) {
            return 0;
        }
        
        // Gap is between end of earlier and start of later
        // If earlier is actually after later, we might need to handle that, but assuming param names roughly hold intent or we verify relative order.
        // Actually, logic: distance( [A_s, A_e], [B_s, B_e] )
        // If A_e < B_s, gap is B_s - A_e.
        // If B_e < A_s, gap is A_s - B_e.
        
        if (earlier.getEnd().isBefore(later.getStart())) {
            return java.time.Duration.between(earlier.getEnd(), later.getStart()).toMillis();
        } else if (later.getEnd().isBefore(earlier.getStart())) {
            return java.time.Duration.between(later.getEnd(), earlier.getStart()).toMillis();
        }
        
        return 0; 
    }

    /**
     * Calculates the maximum possible duration (in milliseconds) between two events.
     * Defined as distance between earliest start and latest end minus lengths? 
     * Usually distance between A and B max is (Latest point of B - Earliest point of A) if A is before B.
     *
     * @param earliest possible start of first event
     * @param latest   possible end of second event
     * @return maximum gap in milliseconds
     * @throws NullPointerException if earliest or latest is null
     */
    public static long maximumGapMillis(TimeInterval earliest, TimeInterval latest) {
        Objects.requireNonNull(earliest, "First interval cannot be null");
        Objects.requireNonNull(latest, "Second interval cannot be null");
        
        // Assuming we want max distance between the two disjoint sets of points.
        // Usually max(abs(a - b)) for a in A, b in B.
        // This is max( |A.start - B.end|, |A.end - B.start| )
        
        long d1 = Math.abs(java.time.Duration.between(earliest.getStart(), latest.getEnd()).toMillis());
        long d2 = Math.abs(java.time.Duration.between(earliest.getEnd(), latest.getStart()).toMillis());
        
        return Math.max(d1, d2);
    }

    /**
     * Returns the potential duration range (minimum and maximum milliseconds) between two events.
     *
     * @param from start event interval
     * @param to   end event interval
     * @return array where [0] is min millis and [1] is max millis
     * @throws NullPointerException if from or to is null
     */
    public static long[] durationRange(TimeInterval from, TimeInterval to) {
        return new long[]{
            minimumGapMillis(from, to),
            maximumGapMillis(from, to)
        };
    }
}
