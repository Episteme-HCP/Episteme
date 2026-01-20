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
//For its entire recorded history of two to three millennia, decimal time had been used in China
//alongside duodecimal time. The day was divided into both 100 parts called ke (Hanzi: 刻; Pinyin: kè)
// and into twelve double hours called shi
// (Traditional Chinese: 時辰; Simplified Chinese: 时辰; Pinyin: shíchen).
// To make ke compatible with shi, each ke was subdivided into 60 fen.
// Jesuits introduced Western time into China during the 17th century,
// at which time the day was redefined as having 96 ke (as well as 12 shi).
public class ChineseTime extends Time {
    /** DOCUMENT ME! */
    public static final int HOURS_PER_DAY = 100;

    /** DOCUMENT ME! */
    public static final int MINUTES_PER_HOUR = 60;

    /** DOCUMENT ME! */
    public static final int SECONDS_PER_MINUTE = 60; //my own implementation, unspecified.

    /** DOCUMENT ME! */
    public static final int MILLISECONDS_PER_SECOND = 1000;  //my own implementation, unspecified.

    /** The base unit for chinese seconds, which is 0.24 SI seconds. */
    public static final BaseUnit<Duration> CHINESE_SECOND = new BaseUnit<Duration>(
            "s");

    /**
     * A unit of duration equal to <code>1/1000 s</code> (standard name
     * <code>ms</code>).
     */
    public static final Unit<Duration> CHINESE_MILLISECOND = CHINESE_SECOND.divide(MILLISECONDS_PER_SECOND);

    /**
     * A unit of duration equal to <code>60 s</code> (standard name
     * <code>min</code>).
     */
    public static final Unit<Duration> CHINESE_MINUTE = CHINESE_SECOND.times(SECONDS_PER_MINUTE);

    /**
     * A unit of duration equal to <code>60 {@link
     * #CHINESE_MINUTE}</code> (standard name <code>h</code>).
     */
    public static final Unit<Duration> CHINESE_HOUR = CHINESE_MINUTE.times(MINUTES_PER_HOUR);

    /**
     * A unit of duration equal to <code>100 {@link
     * #CHINESE_HOUR}</code> (standard name <code>d</code>).
     */
    public static final Unit<Duration> CHINESE_DAY = CHINESE_HOUR.times(HOURS_PER_DAY);

    /** DOCUMENT ME! */
    public final static Unit<Duration> DAYS_HOURS_MINUTES_SECONDS_MILLIS = CHINESE_DAY.compound(CHINESE_HOUR)
                                                                                   .compound(CHINESE_MINUTE)
                                                                                   .compound(CHINESE_SECOND)
                                                                                   .compound(CHINESE_MILLISECOND);

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_DAY = 100 * 60 * 60 * 1000L;

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_HOUR = 60 * 60 * 1000L;

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_MINUTE = 60 * 1000L;

    /** DOCUMENT ME! */
    private int millis;

    /** DOCUMENT ME! */
    private int seconds;

    /** DOCUMENT ME! */
    private int minutes;

    /** DOCUMENT ME! */
    private int hours;

    /** DOCUMENT ME! */
    private int days;

    //we could also offer constructor using Measure<Duration>
/**
          * Creates a new ChineseTime object.
          *
           */

    //get sure it is positive
    public ChineseTime(double days, double hours, double minutes,
        double seconds, double millis) {
        if ((days >= 0) && (hours >= 0) && (minutes >= 0) && (seconds >= 0) &&
                (millis >= 0)) {
            double time = (days * MILLISECONDS_PER_DAY) +
                (hours * MILLISECONDS_PER_HOUR) +
                (minutes * MILLISECONDS_PER_MINUTE) +
                (seconds * MILLISECONDS_PER_SECOND) + millis;
            this.millis = (int) (time % MILLISECONDS_PER_SECOND);
            time /= MILLISECONDS_PER_SECOND;
            this.seconds = (int) (time % SECONDS_PER_MINUTE);
            time /= SECONDS_PER_MINUTE;
            this.minutes = (int) (time % MINUTES_PER_HOUR);
            time /= MINUTES_PER_HOUR;
            this.hours = (int) (time % HOURS_PER_DAY);
            this.days = (int) (time / HOURS_PER_DAY);
        } else {
            throw new IllegalArgumentException(
                "Days, hours, minutes, seconds and millis must be greater or equal to zero.");
        }
    }

    //in milliseconds
/**
          * Creates a new ChineseTime object.
          *
          * @param millis DOCUMENT ME!
          */

    //get sure it is positive
    public ChineseTime(double millis) {
        if (millis >= 0) {
            this.millis = (int) (millis % MILLISECONDS_PER_SECOND);
            millis /= MILLISECONDS_PER_SECOND;
            this.seconds = (int) (millis % SECONDS_PER_MINUTE);
            millis /= SECONDS_PER_MINUTE;
            this.minutes = (int) (millis % MINUTES_PER_HOUR);
            millis /= MINUTES_PER_HOUR;
            this.hours = (int) (millis % HOURS_PER_DAY);
            this.days = (int) (millis / HOURS_PER_DAY);
        } else {
            throw new IllegalArgumentException(
                "Millis must be greater or equal to zero.");
        }
    }

    //using currenttime
/**
          * Creates a new ChineseClock object.
          */
    public ChineseTime() {
        this(System.currentTimeMillis());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Duration> getTime() {
        return Amount.valueOf(millis, DAYS_HOURS_MINUTES_SECONDS_MILLIS);
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

                if (minutes > MINUTES_PER_HOUR) {
                    minutes = 0;
                    hours += 1;

                    if (hours > HOURS_PER_DAY) {
                        hours = 0;
                        days += 1;
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

            if (minutes > MINUTES_PER_HOUR) {
                minutes = 0;
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
    public void nextMinute() {
        minutes += 1;

        if (minutes > MINUTES_PER_HOUR) {
            minutes = 0;
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
        value.append(minutes);
        value.append(" m ");
        value.append(seconds);
        value.append(" s ");
        value.append(millis);
        value.append(" ms");

        return value.toString();
    }
}
