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

import java.io.Serializable;
import java.time.Instant;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;
import java.util.Optional;
import org.jscience.history.temporal.TemporalInterval;

/**
 * Algebraic operations and helper types for historical chronologies.
 * Provides specialized result types for chronological computations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class ChronologyAlgebras {

    private ChronologyAlgebras() {
        // Utility class
    }

    /**
     * Represents the result of an intersection operation between two temporal intervals.
     * 
     * @param intersection the resulting overlapping interval, or null if none
     * @param overlaps     true if the intervals overlap
     */
    public record DateOverlap(TemporalInterval intersection, boolean overlaps) implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    /**
     * Calculates the intersection (overlap) of two temporal intervals.
     * 
     * @param d1 the first interval
     * @param d2 the second interval
     * @return a {@link DateOverlap} detailing the result
     * @throws NullPointerException if d1 or d2 is null
     */
    public static DateOverlap calculateIntersection(TemporalInterval d1, TemporalInterval d2) {
        Optional<TemporalInterval> result = ChronologicalOperator.intersection(d1, d2);
        return new DateOverlap(result.orElse(null), result.isPresent());
    }

    /**
     * Converts various temporal objects to epoch milliseconds.
     * 
     * @param temporal the temporal object (LocalDate, Instant, Date, etc.)
     * @return milliseconds since Unix Epoch
     * @throws NullPointerException if temporal is null
     * @throws IllegalArgumentException if the type is not supported
     */
    public static long toMillis(Object temporal) {
        Objects.requireNonNull(temporal, "Temporal object cannot be null");
        
        if (temporal instanceof Instant i) {
            return i.toEpochMilli();
        } 
        if (temporal instanceof ChronoLocalDate cld) {
            return cld.toEpochDay() * 86400_000L;
        }
        if (temporal instanceof java.util.Date d) {
            return d.getTime();
        }
        
        throw new IllegalArgumentException("Unsupported temporal type: " + temporal.getClass().getName());
    }
}
