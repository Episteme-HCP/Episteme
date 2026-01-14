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

import org.jscience.astronomy.AstronomyConstants;


/**
 * A class representing history useful constants.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class HistoryConstants extends Object {
    /** DOCUMENT ME! */
    public static final double UNIXTIME = 0; //our reference

    /** DOCUMENT ME! */
    public static final double BIGBANG = UNIXTIME -
        (1.38e10 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY);

    /** DOCUMENT ME! */
    public static final double SOLAR_SYSTEM_AGE = UNIXTIME -
        (5.0e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY);

    /** DOCUMENT ME! */
    public static final double EARTH_AGE = UNIXTIME -
        (4.6e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY);
}
