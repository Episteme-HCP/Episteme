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

import java.util.Calendar;

import javax.measure.quantity.Duration;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.Unit;


/**
 * A class representing a way to compute time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//  http://en.wikipedia.org/wiki/Clock
//http://en.wikipedia.org/wiki/Hexadecimal_time
//1 day  =  1 . 0 0 0 0  hexsec  =  6 5 . 5 3 6  hexsec  =  24 h
//1 hexadecimal hour  =    1 0 0 0  hexsec  =   4 . 0 9 6  hexsec  =  1 h 30 min
//1 hexadecimal maxime  =     1 0 0  hexsec  =     2 5 6  hexsec  =  5 min 37,5 sec
//1 hexadecimal minute  =      1 0  hexsec  =      1 6  hexsec  ≈  21,09 sec
//1 hexadecimal second  =       1  hexsec  =       1  hexsec  ≈  1,32 sec            
public class HexadecimalTime extends Time {
    /** DOCUMENT ME! */
    public static final int HOURS_PER_DAY = 16;

    /** DOCUMENT ME! */
    public static final int MAXIMES_PER_HOUR = 16;

    /** DOCUMENT ME! */
    public static final int MINUTES_PER_MAXIME = 16;

    /** DOCUMENT ME! */
    public static final int SECONDS_PER_MINUTE = 16;

    /** DOCUMENT ME! */
    public static final int MILLISECONDS_PER_SECOND = 1000;

    /**
     * The base unit for decimal seconds, which is about 1,318359375 SI
     * seconds.
     */
    public static final BaseUnit<Duration> HEXADECIMAL_SECOND = new BaseUnit<Duration>(
            "s");

    /**
     * A unit of duration equal to <code>1/1000 s</code> (standard name
     * <code>ms</code>).
     */
    public static final Unit<Duration> HEXADECIMAL_MILLISECOND = HEXADECIMAL_SECOND.divide(MILLISECONDS_PER_SECOND);

    /**
     * A unit of duration equal to <code>16 s</code> (standard name
     * <code>min</code>).
     */
    public static final Unit<Duration> HEXADECIMAL_MINUTE = HEXADECIMAL_SECOND.times(SECONDS_PER_MINUTE);

    /**
     * A unit of duration equal to <code>16 {@link
     * #HEXADECIMAL_MINUTE}</code> (standard name <code>M</code>).
     */
    public static final Unit<Duration> HEXADECIMAL_MAXIME = HEXADECIMAL_MINUTE.times(MINUTES_PER_MAXIME);

    /**
     * A unit of duration equal to <code>10 {@link
     * #HEXADECIMAL_MINUTE}</code> (standard name <code>h</code>).
     */
    public static final Unit<Duration> HEXADECIMAL_HOUR = HEXADECIMAL_MAXIME.times(MAXIMES_PER_HOUR);

    /**
     * A unit of duration equal to <code>16 {@link
     * #HEXADECIMAL_HOUR}</code> (standard name <code>d</code>).
     */
    public static final Unit<Duration> HEXADECIMAL_DAY = HEXADECIMAL_HOUR.times(HOURS_PER_DAY);

    /** DOCUMENT ME! */
    public final static Unit<Duration> DAYS_HOURS_MAXIMES_MINUTES_SECONDS_MILLIS = HEXADECIMAL_DAY.compound(HEXADECIMAL_HOUR)
                                                                                              .compound(HEXADECIMAL_MAXIME)
                                                                                              .compound(HEXADECIMAL_MINUTE)
                                                                                              .compound(HEXADECIMAL_SECOND)
                                                                                              .compound(HEXADECIMAL_MILLISECOND);

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_DAY = 16 * 16 * 16 * 16 * 1000L;

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_HOUR = 16 * 16 * 16 * 1000L;

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_MAXIME = 16 * 16 * 1000L;

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_MINUTE = 16 * 1000L;

    /** DOCUMENT ME! */
    private int millis;

    /** DOCUMENT ME! */
    private int seconds;

    /** DOCUMENT ME! */
    private int minutes;

    /** DOCUMENT ME! */
    private int maximes;

    /** DOCUMENT ME! */
    private int hours;

    /** DOCUMENT ME! */
    private int days;

    //we could also offer constructor using Measure<Duration>
/**
          * Creates a new HexadecimalTime object.
          *
           */

    //get sure it is positive
    public HexadecimalTime(double days, double hours, double maximes,
        double minutes, double seconds, double millis) {
        if ((days >= 0) && (hours >= 0) && (minutes >= 0) && (maximes >= 0) &&
                (seconds >= 0) && (millis >= 0)) {
            double time = (days * MILLISECONDS_PER_DAY) +
                (hours * MILLISECONDS_PER_HOUR) +
                (maximes * MILLISECONDS_PER_MAXIME) +
                (minutes * MILLISECONDS_PER_MINUTE) +
                (seconds * MILLISECONDS_PER_SECOND) + millis;
            this.millis = (int) (time % MILLISECONDS_PER_SECOND);
            time /= MILLISECONDS_PER_SECOND;
            this.seconds = (int) (time % SECONDS_PER_MINUTE);
            time /= SECONDS_PER_MINUTE;
            this.minutes = (int) (time % MINUTES_PER_MAXIME);
            time /= MINUTES_PER_MAXIME;
            this.maximes = (int) (time % MAXIMES_PER_HOUR);
            time /= MAXIMES_PER_HOUR;
            this.hours = (int) (time % HOURS_PER_DAY);
            this.days = (int) (time / HOURS_PER_DAY);
        } else {
            throw new IllegalArgumentException(
                "Days, hours, maximes, minutes, seconds and millis must be greater or equal to zero.");
        }
    }

    //in milliseconds
/**
          * Creates a new HexadecimalTime object.
          *
          * @param millis DOCUMENT ME!
          */

    //get sure it is positive
    public HexadecimalTime(double millis) {
        if (millis >= 0) {
            this.millis = (int) (millis % MILLISECONDS_PER_SECOND);
            millis /= MILLISECONDS_PER_SECOND;
            this.seconds = (int) (millis % SECONDS_PER_MINUTE);
            millis /= SECONDS_PER_MINUTE;
            this.minutes = (int) (millis % MINUTES_PER_MAXIME);
            millis /= MINUTES_PER_MAXIME;
            this.maximes = (int) (millis % MAXIMES_PER_HOUR);
            millis /= MAXIMES_PER_HOUR;
            this.hours = (int) (millis % HOURS_PER_DAY);
            this.days = (int) (millis / HOURS_PER_DAY);
        } else {
            throw new IllegalArgumentException(
                "Millis must be greater or equal to zero.");
        }
    }

    //using currenttime
/**
          * Creates a new HexadecimalTime object.
          */
    public HexadecimalTime() {
        this(System.currentTimeMillis());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Duration> getTime() {
        return Amount.valueOf(millis, DAYS_HOURS_MAXIMES_MINUTES_SECONDS_MILLIS);
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
        return seconds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaximes() {
        return maximes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHours() {
        return hours;
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

        if (millis > MILLISECONDS_PER_SECOND) {
            millis = 0;
            seconds += 1;

            if (seconds > SECONDS_PER_MINUTE) {
                seconds = 0;
                minutes += 1;

                if (minutes > MINUTES_PER_MAXIME) {
                    minutes = 0;
                    hours += 1;

                    if (maximes > MAXIMES_PER_HOUR) {
                        maximes = 0;
                        hours += 1;

                        if (hours > HOURS_PER_DAY) {
                            hours = 0;
                            days += 1;
                        }
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void nextSecond() {
        seconds += 1;

        if (seconds > SECONDS_PER_MINUTE) {
            seconds = 0;
            minutes += 1;

            if (seconds > SECONDS_PER_MINUTE) {
                seconds = 0;
                minutes += 1;

                if (minutes > MINUTES_PER_MAXIME) {
                    minutes = 0;
                    hours += 1;

                    if (maximes > MAXIMES_PER_HOUR) {
                        maximes = 0;
                        hours += 1;

                        if (hours > HOURS_PER_DAY) {
                            hours = 0;
                            days += 1;
                        }
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void nextMinute() {
        minutes += 1;

        if (minutes > MINUTES_PER_MAXIME) {
            minutes = 0;
            hours += 1;

            if (maximes > MAXIMES_PER_HOUR) {
                maximes = 0;
                hours += 1;

                if (hours > HOURS_PER_DAY) {
                    hours = 0;
                    days += 1;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void nextMaxime() {
        maximes += 1;

        if (maximes > MAXIMES_PER_HOUR) {
            maximes = 0;
            hours += 1;

            if (hours > HOURS_PER_DAY) {
                hours = 0;
                days += 1;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void nextHour() {
        hours += 1;

        if (hours > HOURS_PER_DAY) {
            hours = 0;
            days += 1;
        }
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
        value.append(hours);
        value.append(" h ");
        value.append(maximes);
        value.append(" M ");
        value.append(minutes);
        value.append(" m ");
        value.append(seconds);
        value.append(" s ");
        value.append(millis);
        value.append(" ms");

        return value.toString();
    }
}
