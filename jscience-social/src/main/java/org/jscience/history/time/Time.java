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

import org.jscience.measure.Amount;
import javax.measure.quantity.Duration;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;

/**
 * An abstract base class for different time representation systems (Gregorian, Chinese, Internet time, etc.).
 * Defines standard methods for retrieving chronological components and advancing time state.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public abstract class Time implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Returns the total elapsed time as a duration measurement.
     *
     * @return the amount of time
     */
    public abstract Amount<Duration> getTime();

    /**
     * Returns the millisecond component of the current time.
     *
     * @return milliseconds (typically 0-999)
     */
    public abstract int getMilliseconds();

    /**
     * Returns the second component of the current time.
     *
     * @return seconds (typically 0-59)
     */
    public abstract int getSeconds();

    /**
     * Returns the minute component of the current time.
     *
     * @return minutes (typically 0-59)
     */
    public abstract int getMinutes();

    /**
     * Returns the hour component of the current time.
     *
     * @return hours (typically 0-23)
     */
    public abstract int getHours();

    /**
     * Returns the number of days elapsed.
     *
     * @return count of days
     */
    public abstract int getDays();

    /**
     * Advances the internal clock by one millisecond.
     */
    public abstract void nextMillisecond();

    /**
     * Advances the internal clock by one second.
     */
    public abstract void nextSecond();

    /**
     * Advances the internal clock by one minute.
     */
    public abstract void nextMinute();

    /**
     * Advances the internal clock by one hour.
     */
    public abstract void nextHour();

    /**
     * Advances the internal clock by one day.
     */
    public abstract void nextDay();

    @Override
    public abstract String toString();
}
