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
//http://en.wikipedia.org/wiki/10-hour_clock
//Decimal to Standard

// One decimal second is 86,400/100,000 = 0.864 standard seconds.
// One decimal minute is 1,440/1,000 = 1.44 standard minutes, or 1 standard minute and 26.4 standard seconds.
// One decimal hour is 24/10 = 2.4 standard hours, or 2 standard hours and 24 standard minutes.
//One hundredth of a day is 14 standard minutes 24 standard seconds, or approximately 15 minutes.

// Standard to Decimal

// One standard second = 1.15740 decimal seconds
// One standard minute = 69.44 decimal seconds (or .69 decimal minutes)
// One standard hour = 4,166.67 decimal seconds (or 41 decimal minutes and 67 decimal seconds)
public class DecimalTime extends Time {
    /** DOCUMENT ME! */
    public static final int HOURS_PER_DAY = 10;

    /** DOCUMENT ME! */
    public static final int MINUTES_PER_HOUR = 100;

    /** DOCUMENT ME! */
    public static final int SECONDS_PER_MINUTE = 100;

    /** DOCUMENT ME! */
    public static final int MILLISECONDS_PER_SECOND = 1000;

    /** The base unit for decimal seconds, which is 0.864 SI seconds. */
    public static final BaseUnit<Duration> DECIMAL_SECOND = new BaseUnit<Duration>(
            "s");

    /**
     * A unit of duration equal to <code>1/1000 s</code> (standard name
     * <code>ms</code>).
     */
    public static final Unit<Duration> DECIMAL_MILLISECOND = DECIMAL_SECOND.divide(MILLISECONDS_PER_SECOND);

    /**
     * A unit of duration equal to <code>100 s</code> (standard name
     * <code>min</code>).
     */
    public static final Unit<Duration> DECIMAL_MINUTE = DECIMAL_SECOND.times(SECONDS_PER_MINUTE);

    /**
     * A unit of duration equal to <code>100 {@link
     * #DECIMAL_MINUTE}</code> (standard name <code>h</code>).
     */
    public static final Unit<Duration> DECIMAL_HOUR = DECIMAL_MINUTE.times(MINUTES_PER_HOUR);

    /**
     * A unit of duration equal to <code>10 {@link
     * #DECIMAL_HOUR}</code> (standard name <code>d</code>).
     */
    public static final Unit<Duration> DECIMAL_DAY = DECIMAL_HOUR.times(HOURS_PER_DAY);

    /** DOCUMENT ME! */
    public final static Unit<Duration> DAYS_HOURS_MINUTES_SECONDS_MILLIS = DECIMAL_DAY.compound(DECIMAL_HOUR)
                                                                                   .compound(DECIMAL_MINUTE)
                                                                                   .compound(DECIMAL_SECOND)
                                                                                   .compound(DECIMAL_MILLISECOND);

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_DAY = 10 * 100 * 100 * 1000L;

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_HOUR = 100 * 100 * 1000L;

    /** DOCUMENT ME! */
    private static final long MILLISECONDS_PER_MINUTE = 100 * 1000L;

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
          * Creates a new DecimalTime object.
          *
           */

    //get sure it is positive
    public DecimalTime(double days, double hours, double minutes,
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
          * Creates a new DecimalTime object.
          *
          * @param millis DOCUMENT ME!
          */

    //get sure it is positive
    public DecimalTime(double millis) {
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
          * Creates a new DecimalTime object.
          */
    public DecimalTime() {
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
    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, days, hours, minutes, seconds);

        return calendar;
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
