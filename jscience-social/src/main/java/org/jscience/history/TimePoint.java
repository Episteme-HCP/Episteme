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

/**
 * Represents a specific point in time.
 * Wrapper around java.time types for JScience integration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


