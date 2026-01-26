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

package org.jscience.history.clock;

import java.util.Objects;

/**
 * A chronometer (stopwatch) implementation that measures elapsed time.
 * Calculates duration relative to a start time captured from a {@link TimeServer}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class ChronometerClock extends Clock {

    /** Recorded start time from the server. */
    private ModernTime serverStartTime;

    /** Current elapsed time. */
    private ModernTime elapsedTime;

    /** Tracks if the next server update should be used to capture start time. */
    private boolean pendingStart = true;

    /**
     * Creates a new ChronometerClock associated with a time server.
     *
     * @param timeServer the time server to monitor
     */
    public ChronometerClock(TimeServer timeServer) {
        super(timeServer);
        this.elapsedTime = new ModernTime(0);
    }

    /** Starts the underlying time server. */
    public void start() {
        getTimeServer().start();
    }

    /** Stops the underlying time server. */
    public void stop() {
        getTimeServer().stop();
    }

    /** Resets the elapsed time and schedules a new start time capture. */
    public void reset() {
        pendingStart = true;
        this.elapsedTime = new ModernTime(0);
    }

    /** 
     * Returns the elapsed time duration as a {@link ModernTime} offset.
     * @return elapsed time
     */
    @Override
    public ModernTime getTime() {
        return elapsedTime;
    }

    @Override
    public void timeChanged(TimeEvent event) {
        Objects.requireNonNull(event, "TimeEvent cannot be null");
        if (event.getTime() instanceof ModernTime currentServerTime) {
            if (pendingStart) {
                serverStartTime = currentServerTime;
                pendingStart = false;
            }

            double elapsedSecs = currentServerTime.getTimeInSeconds() - serverStartTime.getTimeInSeconds();
            // Ensure we don't have negative elapsed time due to server jitters
            this.elapsedTime = new ModernTime(Math.max(0, elapsedSecs * 1000.0));
        }
    }
}
