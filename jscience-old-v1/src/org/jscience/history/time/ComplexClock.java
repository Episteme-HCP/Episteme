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

/**
 * A class representing a way to display and change time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//  http://en.wikipedia.org/wiki/Clock
public class ComplexClock {
    /** DOCUMENT ME! */
    private ChronometerClock chronometer;

    /** DOCUMENT ME! */
    private AlarmClock alarm;

    /** DOCUMENT ME! */
    private CountdownClock countdown;

    /** DOCUMENT ME! */
    private BasicClock clock;

/**
     * Creates a new ComplexClock object.
     *
     * @param clock DOCUMENT ME!
     * @param chronometer DOCUMENT ME!
     * @param alarm DOCUMENT ME!
     * @param countdown DOCUMENT ME!
     */
    public ComplexClock(BasicClock clock, ChronometerClock chronometer,
        AlarmClock alarm, CountdownClock countdown) {
        this.chronometer = chronometer;
        this.alarm = alarm;
        this.countdown = countdown;
        this.clock = clock;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChronometerClock getChronometer() {
        return chronometer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param chronometer DOCUMENT ME!
     */
    public void setChronometer(ChronometerClock chronometer) {
        this.chronometer = chronometer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AlarmClock getAlarm() {
        return alarm;
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarm DOCUMENT ME!
     */
    public void setAlarm(AlarmClock alarm) {
        this.alarm = alarm;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CountdownClock getCountdown() {
        return countdown;
    }

    /**
     * DOCUMENT ME!
     *
     * @param countdown DOCUMENT ME!
     */
    public void setCountdown(CountdownClock countdown) {
        this.countdown = countdown;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BasicClock getClock() {
        return clock;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clock DOCUMENT ME!
     */
    public void setClock(BasicClock clock) {
        this.clock = clock;
    }
}
