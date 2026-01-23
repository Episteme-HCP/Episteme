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

package org.jscience.history;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.io.Serializable;
import java.util.Objects;
import org.jscience.util.Temporal;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a specific point in time.
 * Wrapper around {@link java.time.Instant} for JScience integration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class TimePoint implements Comparable<TimePoint>, Serializable, Temporal {

    private static final long serialVersionUID = 1L;

    /** The underlying instant in time. */
    @Attribute
    private final Instant instant;

    private TimePoint(Instant instant) {
        this.instant = Objects.requireNonNull(instant, "Instant cannot be null");
    }

    /**
     * Creates a TimePoint for the current moment.
     * 
     * @return current time
     */
    public static TimePoint now() {
        return new TimePoint(Instant.now());
    }

    /**
     * Creates a TimePoint from an Instant.
     * 
     * @param instant the instant
     * @return a new TimePoint
     * @throws NullPointerException if instant is null
     */
    public static TimePoint of(Instant instant) {
        return new TimePoint(Objects.requireNonNull(instant, "Instant cannot be null"));
    }

    /**
     * Creates a TimePoint from a LocalDateTime using system default timezone.
     * 
     * @param ldt the date-time
     * @return a new TimePoint
     * @throws NullPointerException if ldt is null
     */
    public static TimePoint of(LocalDateTime ldt) {
        return new TimePoint(Objects.requireNonNull(ldt, "LocalDateTime cannot be null")
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Creates a TimePoint from a LocalDate at start of day in system default timezone.
     * 
     * @param ld the local date
     * @return a new TimePoint
     * @throws NullPointerException if ld is null
     */
    public static TimePoint of(LocalDate ld) {
        return new TimePoint(Objects.requireNonNull(ld, "LocalDate cannot be null")
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Returns the underlying Instant.
     *
     * @return the instant
     */
    public Instant toInstant() {
        return instant;
    }

    @Override
    public Instant getTimestamp() {
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
        if (this == obj) return true;
        if (obj instanceof TimePoint other) {
            return instant.equals(other.instant);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return instant.hashCode();
    }
}
