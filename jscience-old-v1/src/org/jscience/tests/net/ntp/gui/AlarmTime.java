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

package org.jscience.tests.net.ntp.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class AlarmTime {
    /** DOCUMENT ME! */
    private int hour;

    /** DOCUMENT ME! */
    private int min;

    /** DOCUMENT ME! */
    private int sec;

    /** DOCUMENT ME! */
    private Calendar c = new GregorianCalendar();

/**
     * Creates a new AlarmTime object.
     *
     * @param hour DOCUMENT ME!
     * @param min  DOCUMENT ME!
     * @param sec  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AlarmTime(int hour, int min, int sec)
        throws IllegalArgumentException {
        setHour(hour);
        setMin(min);
        setSec(sec);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHour() {
        return hour;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMin() {
        return min;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSec() {
        return sec;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hour DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLegalHour(int hour) {
        return (hour >= 0) && (hour <= 23);
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLegalMin(int min) {
        return (min >= 0) && (min <= 59);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLegalSec(int sec) {
        return (sec >= 0) && (sec <= 59);
    }

    /**
     * DOCUMENT ME!
     *
     * @param hour DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setHour(int hour) throws IllegalArgumentException {
        try {
            if (isLegalHour(hour)) {
                this.hour = hour;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setMin(int min) throws IllegalArgumentException {
        try {
            if (isLegalMin(min)) {
                this.min = min;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sec DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSec(int sec) throws IllegalArgumentException {
        try {
            if (isLegalSec(sec)) {
                this.sec = sec;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAlarm(Date d) {
        c.setTime(d);

        return (hour == c.get(Calendar.HOUR_OF_DAY)) &&
        (min == c.get(Calendar.MINUTE)) && (sec == c.get(Calendar.SECOND));
    }
}
