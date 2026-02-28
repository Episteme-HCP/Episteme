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

package org.episteme.social.history.time;

import java.time.Instant;
import org.episteme.core.util.Temporal;

/**
 * Interface for a temporal coordinate, representing a position in time.
 * Analogous to {@link org.episteme.natural.earth.coordinates.EarthCoordinate}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TimeCoordinate extends Temporal<TimeCoordinate>, Comparable<TimeCoordinate> {

    /**
     * Returns the exact instant this coordinate represents.
     * If the coordinate is fuzzy or an interval, this returns a representative point.
     * 
     * @return the instant
     */
    Instant toInstant();

    /**
     * Returns the precision of this coordinate.
     * 
     * @return the precision level
     */
    TimePrecision getPrecision();

    /**
     * Returns whether this coordinate represents a range or has uncertainty.
     * 
     * @return true if fuzzy or interval-based
     */
    boolean isFuzzy();

    @Override
    default TimeCoordinate getWhen() {
        return this;
    }
}

