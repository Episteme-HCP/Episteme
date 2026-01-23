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

/**
 * Constants useful for historical and geological calculations.
 * All time-based constants are expressed in seconds relative to the Unix Epoch (1970-01-01T00:00:00Z).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class HistoryConstants {

    /** Number of seconds in a Julian Year (365.25 days). */
    public static final double JULIAN_YEAR_SECONDS = 365.25 * 86400.0;
    
    /** Timestamp of the Unix Epoch. */
    public static final double UNIXTIME = 0.0; 

    /** Estimated time of the Big Bang. */
    public static final double BIGBANG = UNIXTIME - (1.38e10 * JULIAN_YEAR_SECONDS);

    /** Estimated age of the Solar System. */
    public static final double SOLAR_SYSTEM_AGE = UNIXTIME - (5.0e9 * JULIAN_YEAR_SECONDS);

    /** Estimated age of the Earth. */
    public static final double EARTH_AGE = UNIXTIME - (4.6e9 * JULIAN_YEAR_SECONDS);

    private HistoryConstants() {
        // Prevent instantiation of utility class
    }
}
