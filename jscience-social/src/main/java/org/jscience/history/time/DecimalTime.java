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
import org.jscience.measure.Unit;
import org.jscience.measure.StandardUnit;
import org.jscience.measure.Dimension;
import org.jscience.measure.quantity.Time;
import java.io.Serializable;
import java.util.Calendar;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents time using a decimal system (10h/100m/100s per day).
 * Primarily based on the French Revolutionary Time system.
 * <p>
 * One decimal second is exactly 0.864 standard seconds.
 * One decimal hour is exactly 2.4 standard hours.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 * @see <a href="http://en.wikipedia.org/wiki/Decimal_time">Decimal Time (Wikipedia)</a>
 */
@Persistent
public class DecimalTime extends org.jscience.history.time.Time {

    private static final long serialVersionUID = 1L;

    public static final int HOURS_PER_DAY = 10;
    public static final int MINUTES_PER_HOUR = 100;
    public static final int SECONDS_PER_MINUTE = 100;
    public static final int MILLISECONDS_PER_SECOND = 1000;

    /** The base unit for decimal seconds (0.864 SI seconds). */
    public static final Unit<Time> DECIMAL_SECOND = new StandardUnit<>("ds", "decimal second", Dimension.TIME);

    /** Unit for one decimal millisecond. */
    public static final Unit<Time> DECIMAL_MILLISECOND = DECIMAL_SECOND.divide(MILLISECONDS_PER_SECOND);

    /** Unit for one decimal minute (100 decimal seconds). */
    public static final Unit<Time> DECIMAL_MINUTE = DECIMAL_SECOND.multiply(SECONDS_PER_MINUTE);

    /** Unit for one decimal hour (100 decimal minutes). */
    public static final Unit<Time> DECIMAL_HOUR = DECIMAL_MINUTE.multiply(MINUTES_PER_HOUR);

    /** Unit for one decimal day (10 decimal hours). */
    public static final Unit<Time> DECIMAL_DAY = DECIMAL_HOUR.multiply(HOURS_PER_DAY);

    private static final long MILLIS_PER_DAY = (long) HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    private static final long MILLIS_PER_HOUR = (long) MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    private static final long MILLIS_PER_MINUTE = (long) SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;

    @Attribute
    private int millis;

    @Attribute
    private int seconds;

    @Attribute
    private int minutes;

    @Attribute
    private int hours;

    @Attribute
    private int days;

    /**
     * Creates DecimalTime from components.
     * 
     * @param days days
     * @param hours decimal hours (0-9)
     * @param minutes decimal minutes (0-99)
     * @param seconds decimal seconds (0-99)
     * @param millis decimal milliseconds (0-999)
     * @throws IllegalArgumentException if any value is negative
     */
    public DecimalTime(double days, double hours, double minutes, double seconds, double millis) {
        if (days < 0 || hours < 0 || minutes < 0 || seconds < 0 || millis < 0) {
            throw new IllegalArgumentException("Time components cannot be negative");
        }
        double total = (days * MILLIS_PER_DAY) + (hours * MILLIS_PER_HOUR) +
                       (minutes * MILLIS_PER_MINUTE) + (seconds * MILLISECONDS_PER_SECOND) + millis;
        initFromMillis(total);
    }

    /**
     * Creates DecimalTime from total decimal milliseconds.
     * 
     * @param millis total milliseconds
     * @throws IllegalArgumentException if millis is negative
     */
    public DecimalTime(double millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Decimal milliseconds cannot be negative");
        }
        initFromMillis(millis);
    }

    /**
     * Creates DecimalTime with the current moment.
     */
    public DecimalTime() {
        this((double) System.currentTimeMillis());
    }

    private void initFromMillis(double total) {
        this.millis = (int) (total % MILLISECONDS_PER_SECOND);
        total /= MILLISECONDS_PER_SECOND;
        this.seconds = (int) (total % SECONDS_PER_MINUTE);
        total /= SECONDS_PER_MINUTE;
        this.minutes = (int) (total % MINUTES_PER_HOUR);
        total /= MINUTES_PER_HOUR;
        this.hours = (int) (total % HOURS_PER_DAY);
        this.days = (int) (total / HOURS_PER_DAY);
    }

    @Override
    public Amount<Time> getTime() {
        long totalMillis = (long)days * MILLIS_PER_DAY + (long)hours * MILLIS_PER_HOUR + 
                          (long)minutes * MILLIS_PER_MINUTE + (long)seconds * MILLISECONDS_PER_SECOND + millis;
        return Amount.valueOf(totalMillis, DECIMAL_MILLISECOND);
    }

    @Override
    public int getMilliseconds() {
        return millis;
    }

    @Override
    public int getSeconds() {
        return seconds;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override
    public int getHours() {
        return hours;
    }

    @Override
    public int getDays() {
        return days;
    }

    @Override
    public void nextMillisecond() {
        millis++;
        if (millis >= MILLISECONDS_PER_SECOND) {
            millis = 0;
            nextSecond();
        }
    }

    @Override
    public void nextSecond() {
        seconds++;
        if (seconds >= SECONDS_PER_MINUTE) {
            seconds = 0;
            nextMinute();
        }
    }

    @Override
    public void nextMinute() {
        minutes++;
        if (minutes >= MINUTES_PER_HOUR) {
            minutes = 0;
            nextHour();
        }
    }

    @Override
    public void nextHour() {
        hours++;
        if (hours >= HOURS_PER_DAY) {
            hours = 0;
            days++;
        }
    }

    @Override
    public void nextDay() {
        days++;
    }

    /**
     * Converts to a standard Java Calendar instance.
     * 
     * @return calendar
     */
    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, days, hours, minutes, seconds);
        return calendar;
    }

    @Override
    public String toString() {
        return String.format("%d d %d h %d m %d s %d ms", days, hours, minutes, seconds, millis);
    }
}
