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
 * A composite clock container that aggregates multiple specialized clock functionalities.
 * Groups a standard clock, chronometer, alarm, and countdown into a single management unit.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ComplexClock {

    /** The chronometer function. */
    private ChronometerClock chronometer;

    /** The alarm clock function. */
    private AlarmClock alarm;

    /** The countdown function. */
    private CountdownClock countdown;

    /** The primary basic clock. */
    private BasicClock clock;

    /**
     * Creates a new ComplexClock with initial clock components.
     *
     * @param clock       the primary clock
     * @param chronometer the chronometer
     * @param alarm       the alarm clock
     * @param countdown   the countdown clock
     * @throws NullPointerException if any component is null
     */
    public ComplexClock(BasicClock clock, ChronometerClock chronometer, AlarmClock alarm, CountdownClock countdown) {
        this.clock = Objects.requireNonNull(clock, "BasicClock cannot be null");
        this.chronometer = Objects.requireNonNull(chronometer, "ChronometerClock cannot be null");
        this.alarm = Objects.requireNonNull(alarm, "AlarmClock cannot be null");
        this.countdown = Objects.requireNonNull(countdown, "CountdownClock cannot be null");
    }

    /**
     * Returns the chronometer component.
     * @return chronometer
     */
    public ChronometerClock getChronometer() {
        return chronometer;
    }

    /**
     * Sets the chronometer component.
     * @param chronometer the chronometer
     */
    public void setChronometer(ChronometerClock chronometer) {
        this.chronometer = Objects.requireNonNull(chronometer, "ChronometerClock cannot be null");
    }

    /**
     * Returns the alarm component.
     * @return alarm
     */
    public AlarmClock getAlarm() {
        return alarm;
    }

    /**
     * Sets the alarm component.
     * @param alarm the alarm clock
     */
    public void setAlarm(AlarmClock alarm) {
        this.alarm = Objects.requireNonNull(alarm, "AlarmClock cannot be null");
    }

    /**
     * Returns the countdown component.
     * @return countdown
     */
    public CountdownClock getCountdown() {
        return countdown;
    }

    /**
     * Sets the countdown component.
     * @param countdown the countdown clock
     */
    public void setCountdown(CountdownClock countdown) {
        this.countdown = Objects.requireNonNull(countdown, "CountdownClock cannot be null");
    }

    /**
     * Returns the primary basic clock.
     * @return basic clock
     */
    public BasicClock getClock() {
        return clock;
    }

    /**
     * Sets the primary basic clock.
     * @param clock the basic clock
     */
    public void setClock(BasicClock clock) {
        this.clock = Objects.requireNonNull(clock, "BasicClock cannot be null");
    }
}
