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

package org.jscience.util;


//import org.jscience.*;
/**
 * TimeSystem allows to use a time measurement that is independent from the
 * system time.
 *
 * @author Holger Antelmann
 */
public interface TimeSystem {
    /**
     * provides a TimeSystem implementation based on the system time.
     *
     * @see java.lang.System#currentTimeMillis()
     */
    public static final TimeSystem systemTime = new TimeSystem() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };

    /**
     * returns the current time in milliseconds as measured by the
     * underlying time system which may be different from the system time.
     *
     * @return the difference, measured in milliseconds, between the current
     *         time and midnight, January 1, 1970 UTC.
     */
    long currentTimeMillis();
}
