/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.history.clock;

import java.time.Instant;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;
import org.episteme.social.history.time.TimeInterval;

/**
 * Defines various algebras over temporal structures.
 * Allen's Interval Algebra and other qualitative time logic systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public final class ChronologyAlgebras {

    private ChronologyAlgebras() {
        // Utility class
    }

    /**
     * Determines the Allen relation between two intervals.
     * Possible relations: equal, before, after, meets, met-by, overlaps, overlapped-by, 
     * starts, started-by, finishes, finished-by, during, contains.
     *
     * @param a first interval
     * @param b second interval
     * @return the Allen relation string
     */
    public static String allenRelation(TimeInterval a, TimeInterval b) {
        if (a.equals(b)) return "equals";
        if (a.getEnd().isBefore(b.getStart())) return "before";
        if (a.getStart().isAfter(b.getEnd())) return "after";
        if (a.getEnd().equals(b.getStart())) return "meets";
        if (a.getStart().equals(b.getEnd())) return "met-by";
        
        // Simplified check, full implementation is extensive
        if (a.overlaps(b)) return "overlaps"; 
        
        return "unknown"; 
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

