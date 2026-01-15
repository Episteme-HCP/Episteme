/*
 * Java Robotics Library (JRL) Copyright (c) 2004, Levent Bayindir, All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution. Neither the name of the odejava nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
