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
 * A clock that counts down from a target duration.
 * Shows the remaining time relative to a deadline capture from a {@link TimeServer}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class CountdownClock extends Clock {

    /** The initial countdown duration or target deadline reference. */
    private ModernTime startingTime;

    /** Time server reference recorded when countdown started. */
    private ModernTime serverStartTime;

    /** Current remaining time. */
    private ModernTime currentTime;

    /** Tracks if the start reference needs to be captured. */
    private boolean pendingStart = true;

    /**
     * Creates a new CountdownClock.
     *
     * @param timeServer the time server to monitor
     * @param initialTime the starting countdown duration
     * @throws NullPointerException if timeServer or initialTime is null
     */
    public CountdownClock(TimeServer timeServer, ModernTime initialTime) {
        super(timeServer);
        this.startingTime = Objects.requireNonNull(initialTime, "Initial time cannot be null");
        this.currentTime = initialTime;
    }

    /** Starts the underlying time server. */
    public void start() {
        getTimeServer().start();
    }

    /** Stops the underlying time server. */
    public void stop() {
        getTimeServer().stop();
    }

    /** Resets the countdown to its initial duration. */
    public void reset() {
        pendingStart = true;
        this.currentTime = startingTime;
    }

    /**
     * Returns the initial countdown duration.
     * @return starting time
     */
    public ModernTime getStartingTime() {
        return startingTime;
    }

    /**
     * Sets the initial countdown duration.
     * @param startingTime the new starting duration
     */
    public void setStartingTime(ModernTime startingTime) {
        this.startingTime = Objects.requireNonNull(startingTime, "Starting time cannot be null");
        reset();
    }

    @Override
    public ModernTime getTime() {
        return currentTime;
    }

    @Override
    public void timeChanged(TimeEvent event) {
        Objects.requireNonNull(event, "TimeEvent cannot be null");
        if (event.getTime() instanceof ModernTime serverTime) {
            if (pendingStart) {
                serverStartTime = serverTime;
                pendingStart = false;
            }

            double elapsedSecs = serverTime.getTimeInSeconds() - serverStartTime.getTimeInSeconds();
            double remainingSecs = startingTime.getTimeInSeconds() - elapsedSecs;

            this.currentTime = new ModernTime(Math.max(0, remainingSecs * 1000.0));
        }
    }
}
