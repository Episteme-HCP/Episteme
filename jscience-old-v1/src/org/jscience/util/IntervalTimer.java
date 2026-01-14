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

/**
 * Utility class used to calculate the time passed between two events
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public final class IntervalTimer {
    /** DOCUMENT ME! */
    private static final boolean GC = false;

    /** Start time (in milliseconds since 00:00:00 1 Jan 1970) */
    private long startTime;

    /** Stop time (in milliseconds since 00:00:00 1 Jan 1970) */
    private long endTime;

    /** Flag to indicate whether timer is active. */
    private boolean timing;

    /**
     * Starts the timer.
     */
    public void start() {
        if (!timing) {
            // invoke garbage collector explicitly
            // to minimise possible effects on timing
            if (GC) {
                System.gc();
            }

            startTime = System.currentTimeMillis();
            endTime = 0L;
            timing = true;
        }
    }

    /**
     * Calculates elapsed time.
     *
     * @return current elapsed time in seconds, as a real number
     */
    public double getElapsedTime() {
        if (timing) {
            long now = System.currentTimeMillis();

            return (now - startTime) / 1000.0;
        } else {
            return (endTime - startTime) / 1000.0;
        }
    }

    /**
     * Stops the timer (if it is running).
     *
     * @return total elapsed time in seconds, as a real number
     */
    public double stop() {
        if (timing) {
            endTime = System.currentTimeMillis();
            timing = false;
        }

        return (endTime - startTime) / 1000.0;
    }

    /**
     * Resets the timer.
     */
    public void reset() {
        startTime = endTime = 0L;
        timing = false;
    }

    /**
     * Indicates whether timer is currently active.
     *
     * @return true if the timer is active, false otherwise
     */
    public boolean isTiming() {
        return timing;
    }

    /**
     * Indicates whether timer is currently inactive.
     *
     * @return true if the timer is inactive, false otherwise
     */
    public boolean isStopped() {
        return (!timing);
    }

    /**
     * Creates a String representation of timer status.
     *
     * @return timer status, as a String
     */
    public String toString() {
        if ((startTime == 0L) && (endTime == 0L)) {
            return new String(getClass().getName() + ": unused");
        }

        if (timing) {
            return new String(getClass().getName() + ": started " + startTime);
        } else {
            return new String(getClass().getName() + ": started " + startTime +
                ", stopped " + endTime);
        }
    }
}
