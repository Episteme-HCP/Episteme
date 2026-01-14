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
import javax.measure.unit.BaseUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.naming.OperationNotSupportedException;
import java.util.Calendar;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */

//written after the genuine http://www.swatch.com/internettime/info/unix.php
//http://en.wikipedia.org/wiki/Swatch_Internet_Time
public class InternetTime extends Time {
    /** DOCUMENT ME! */
    public static final int TICKS_PER_DAY = 1000;

   /** DOCUMENT ME! */
    public static final int MILLISECONDS_PER_TICK = 1000;

    /**
     * A unit of duration equal to <code>1/1000 s</code> (standard name
     * <code>ms</code>).
     */
    public static final Unit<Duration> INTERNET_MILLISECOND = SI.SECOND.divide(MILLISECONDS_PER_TICK);

    /**
     * The base unit for duration quantities (<code>t</code>).
     */
    public static final BaseUnit<Duration> INTERNET_TICK =  new BaseUnit<Duration>( "t");;

     /**
     * A unit of duration equal to <code>1000 {@link #INTERNET_TICK}</code>
     * (standard name <code>d</code>).
     */
    public static final Unit<Duration> INTERNET_DAY = INTERNET_TICK.times(TICKS_PER_DAY);

    /** DOCUMENT ME! */
    public final static Unit<Duration> DAYS_TICKS_MILLIS = INTERNET_DAY.compound(INTERNET_TICK)
                                                                                  .compound(INTERNET_MILLISECOND);

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_DAY =  1000 * 1000L;

    /** DOCUMENT ME! */
    private int millis;

    /** DOCUMENT ME! */
    private int ticks;

     /** DOCUMENT ME! */
    private int days;

     //we could also offer constructor using Measure<Duration>
/**
     * Creates a new InternetTime object.
     *
      */

    //get sure it is positive
    public InternetTime(double days, double ticks, double millis) {
        if ((days >= 0) && (ticks >= 0) &&
                (millis >= 0)) {
            double time = (days * MILLISECONDS_PER_DAY) +
                (ticks * MILLISECONDS_PER_TICK) + millis;
            this.millis = (int) (time % MILLISECONDS_PER_TICK);
            time /= MILLISECONDS_PER_TICK;
            this.ticks = (int) (time % TICKS_PER_DAY);
            this.days = (int) (time / TICKS_PER_DAY);
        } else {
            throw new IllegalArgumentException(
                "Days, ticks and millis must be greater or equal to zero.");
        }
    }

    //in milliseconds
/**
     * Creates a new InternetTime object.
     *
     * @param millis DOCUMENT ME!
     */

    //get sure it is positive
    public InternetTime(double millis) {
        if (millis >= 0) {
            this.millis = (int) (millis % MILLISECONDS_PER_TICK);
            millis /= MILLISECONDS_PER_TICK;
            this.ticks = (int) (millis % TICKS_PER_DAY);
            this.days = (int) (millis / TICKS_PER_DAY);
        } else {
            throw new IllegalArgumentException(
                "Millis must be greater or equal to zero.");
        }
    }

    //using currenttime
/**
     * Creates a new InternetTime object.
     */
    public InternetTime() {
        this(getCurrentInternetTime());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Duration> getTime() {
        return Amount.valueOf(millis, DAYS_TICKS_MILLIS);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMilliseconds() {
        return millis;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSeconds() {
        return ticks;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinutes() {
        throw new UnsupportedOperationException();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHours() {
        throw new UnsupportedOperationException();
     }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDays() {
        return days;
    }

    /**
     * DOCUMENT ME!
     */
    public void nextMillisecond() {
        millis += 1;

        if (millis > MILLISECONDS_PER_TICK) {
            millis = 0;
            ticks += 1;

            if (ticks > TICKS_PER_DAY) {
                        ticks = 0;
                        days += 1;
                    }
                }
        }

    /**
     * DOCUMENT ME!
     */
    public void nextSecond() {
        ticks += 1;

if (ticks > TICKS_PER_DAY) {
             ticks = 0;
             days += 1;
         }
     }
    /**
     * DOCUMENT ME!
     */
    public void nextMinute() {
        throw new UnsupportedOperationException();
    }

    /**
     * DOCUMENT ME!
     */
    public void nextHour() {
        throw new UnsupportedOperationException();
    }

    /**
     * DOCUMENT ME!
     */
    public void nextDay() {
        days += 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer value;

        value = new StringBuffer();
        value.append(days);
        value.append(" d ");
        value.append(ticks);
        value.append(" t ");
        value.append(millis);
        value.append(" ms");

        return value.toString();
    }

   /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static long getCurrentDayMillis() {
        long l = System.currentTimeMillis();

        return (l + 0x36ee80L) % 0x5265c00L;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getCurrentInternetTime() {
        int i = (int) (getCurrentDayMillis() / 0x15180L);

        if (i >= 1000) {
            i = 0;
        }

        return i;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getCurrentInternetTimeAsString() {
        int i = getCurrentInternetTime();
        String s = String.valueOf(i);

        if (s.length() == 1) {
            s = "00" + s;
        }

        if (s.length() == 2) {
            s = "0" + s;
        }

        s = "@" + s;

        return s;
    }

}
