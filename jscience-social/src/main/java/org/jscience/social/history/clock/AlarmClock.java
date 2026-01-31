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

package org.jscience.social.history.clock;

import java.util.Objects;

/**
 * An abstract alarm clock that triggers an action when a specified target time is reached.
 * Monitors standard {@link ModernTime} updates from a {@link TimeServer}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class AlarmClock extends Clock {

    /** Tolerance in seconds for triggering the alarm. */
    private double toleranceSeconds = 1.0;

    /** The target time for the alarm to trigger. */
    private ModernTime alarmTime;

    /** Current time track. */
    private ModernTime currentTime;

    /** Flag to prevent multiple triggers for the same alarm event. */
    private boolean alarmFired = false;

    /**
     * Creates a new AlarmClock associated with a time server.
     *
     * @param timeServer the time server to monitor
     */
    protected AlarmClock(TimeServer timeServer) {
        super(timeServer);
        this.currentTime = new ModernTime();
    }

    /**
     * Starts the underlying time server updates.
     */
    public void start() {
        getTimeServer().start();
    }

    /**
     * Stops the underlying time server updates.
     */
    public void stop() {
        getTimeServer().stop();
    }

    /**
     * Returns the target alarm time.
     *
     * @return alarm time
     */
    public ModernTime getAlarmTime() {
        return alarmTime;
    }

    /**
     * Sets the target alarm time. Resets the triggered flag.
     *
     * @param alarmTime the target time
     */
    public void setAlarmTime(ModernTime alarmTime) {
        this.alarmTime = alarmTime;
        this.alarmFired = false;
    }

    /**
     * Returns the current time held by the clock.
     * 
     * @return current time
     */
    @Override
    public ModernTime getTime() {
        return currentTime;
    }

    /**
     * Updates the internal clock time.
     *
     * @param time the new time state
     * @throws NullPointerException if time is null
     */
    public void setTime(ModernTime time) {
        this.currentTime = Objects.requireNonNull(time, "Time cannot be null");
    }

    @Override
    public void timeChanged(TimeEvent event) {
        Objects.requireNonNull(event, "TimeEvent cannot be null");
        if (event.getTime() instanceof ModernTime mt) {
            setTime(mt);
            checkAlarm();
        }
    }

    private void checkAlarm() {
        if (alarmTime != null && !alarmFired) {
            double currentSeconds = currentTime.getTimeInSeconds();
            double targetSeconds = alarmTime.getTimeInSeconds();
            
            // Trigger if we've reached or just passed the target within the tolerance
            if (currentSeconds >= targetSeconds && (currentSeconds - targetSeconds) <= toleranceSeconds) {
                alarmFired = true;
                fireAlarm();
            }
        }
    }

    /**
     * Hook method called when the alarm condition is met.
     */
    public abstract void fireAlarm();

    /**
     * Sets the firing tolerance.
     * 
     * @param seconds tolerance in seconds
     */
    public void setTolerance(double seconds) {
        this.toleranceSeconds = seconds;
    }
}

