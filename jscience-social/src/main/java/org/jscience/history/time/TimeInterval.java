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
import java.util.Objects;

/**
 * A continuous interval in time.
 * Defined by a start and an end instant.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
