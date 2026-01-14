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
public abstract class AlarmClock extends Clock {
    /**
     * DOCUMENT ME!
     */
    private double delta = 1000;

    /**
     * DOCUMENT ME!
     */
    private ModernTime alarmTime;

    /**
     * DOCUMENT ME!
     */
    private ModernTime time;

    /**
     * Creates a new AlarmClock object.
     *
     * @param timeServer DOCUMENT ME!
     */
    public AlarmClock(TimeServer timeServer) {
        super(timeServer);
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        //start the time server
        getTimeServer().start();
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        //stop the time server
        getTimeServer().stop();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ModernTime getAlarmTime() {
        return alarmTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarmTime DOCUMENT ME!
     */
    public void setAlarmTime(ModernTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ModernTime getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void setTime(ModernTime time) {
        this.time = time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void timeChanged(TimeEvent event) {
        setTime((ModernTime) event.getTime());

        if ((alarmTime != null) &&
                ((((ModernTime) event.getTime()).getTimeInSeconds() -
                getAlarmTime().getTimeInSeconds()) < delta)) {
            //alarm fires
            fireAlarm();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void fireAlarm();
}
