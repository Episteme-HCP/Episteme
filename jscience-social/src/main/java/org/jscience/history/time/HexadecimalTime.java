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
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents Hexadecimal Time (16h per day, 16 maximes per hour, 16 minutes per maxime, 16 seconds per minute).
 * <p>
 * This system divides the day into 65,536 hexadecimal seconds (hexsecs).
 * One hexsec is exactly 1.318359375 standard seconds.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 * @see <a href="http://en.wikipedia.org/wiki/Hexadecimal_time">Hexadecimal Time (Wikipedia)</a>
 */
@Persistent
public class HexadecimalTime extends org.jscience.history.time.Time {

    private static final long serialVersionUID = 1L;

    public static final int HOURS_PER_DAY = 16;
    public static final int MAXIMES_PER_HOUR = 16;
    public static final int MINUTES_PER_MAXIME = 16;
    public static final int SECONDS_PER_MINUTE = 16;
    public static final int MILLIS_PER_SECOND = 1000;

    /** Base unit for hexadecimal seconds (1.318359375 SI seconds). */
    public static final Unit<Time> HEX_SECOND = new StandardUnit<>("hs", "hexadecimal second", Dimension.TIME);

    /** Unit for one hexadecimal millisecond. */
    public static final Unit<Time> HEX_MILLISECOND = HEX_SECOND.divide(MILLIS_PER_SECOND);

    /** Unit for one hexadecimal minute (16 hex seconds). */
    public static final Unit<Time> HEX_MINUTE = HEX_SECOND.multiply(SECONDS_PER_MINUTE);

    /** Unit for one hexadecimal maxime (16 hex minutes). */
    public static final Unit<Time> HEX_MAXIME = HEX_MINUTE.multiply(MINUTES_PER_MAXIME);

    /** Unit for one hexadecimal hour (16 hex maximes). */
    public static final Unit<Time> HEX_HOUR = HEX_MAXIME.multiply(MAXIMES_PER_HOUR);

    /** Unit for one hexadecimal day (16 hex hours). */
    public static final Unit<Time> HEX_DAY = HEX_HOUR.multiply(HOURS_PER_DAY);

    private static final long RAW_MILLIS_PER_DAY = (long) HOURS_PER_DAY * MAXIMES_PER_HOUR * MINUTES_PER_MAXIME * SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
    private static final long RAW_MILLIS_PER_HOUR = (long) MAXIMES_PER_HOUR * MINUTES_PER_MAXIME * SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
    private static final long RAW_MILLIS_PER_MAXIME = (long) MINUTES_PER_MAXIME * SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
    private static final long RAW_MILLIS_PER_MINUTE = (long) SECONDS_PER_MINUTE * MILLIS_PER_SECOND;

    @Attribute
    private int millis;

    @Attribute
    private int seconds;

    @Attribute
    private int minutes;

    @Attribute
    private int maximes;

    @Attribute
    private int hours;

    @Attribute
    private int days;

    /**
     * Creates HexadecimalTime from components.
     * 
     * @param days count of days
     * @param hours hex hours (0-15)
     * @param maximes hex maximes (0-15)
     * @param minutes hex minutes (0-15)
     * @param seconds hex seconds (0-15)
     * @param millis hex milliseconds (0-999)
     * @throws IllegalArgumentException if any value is negative
     */
    public HexadecimalTime(double days, double hours, double maximes, double minutes, double seconds, double millis) {
        if (days < 0 || hours < 0 || maximes < 0 || minutes < 0 || seconds < 0 || millis < 0) {
            throw new IllegalArgumentException("Time components cannot be negative");
        }
        double total = (days * RAW_MILLIS_PER_DAY) + (hours * RAW_MILLIS_PER_HOUR) +
                       (maximes * RAW_MILLIS_PER_MAXIME) + (minutes * RAW_MILLIS_PER_MINUTE) +
                       (seconds * MILLIS_PER_SECOND) + millis;
        initFromMillis(total);
    }

    /**
     * Creates HexadecimalTime from raw milliseconds.
     * 
     * @param millis total milliseconds
     * @throws IllegalArgumentException if millis is negative
     */
    public HexadecimalTime(double millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Milliseconds cannot be negative");
        }
        initFromMillis(millis);
    }

    /**
     * Creates HexadecimalTime with current moment.
     */
    public HexadecimalTime() {
        this((double) System.currentTimeMillis());
    }

    private void initFromMillis(double total) {
        this.millis = (int) (total % MILLIS_PER_SECOND);
        total /= MILLIS_PER_SECOND;
        this.seconds = (int) (total % SECONDS_PER_MINUTE);
        total /= SECONDS_PER_MINUTE;
        this.minutes = (int) (total % MINUTES_PER_MAXIME);
        total /= MINUTES_PER_MAXIME;
        this.maximes = (int) (total % MAXIMES_PER_HOUR);
        total /= MAXIMES_PER_HOUR;
        this.hours = (int) (total % HOURS_PER_DAY);
        this.days = (int) (total / HOURS_PER_DAY);
    }

    @Override
    public Amount<Time> getTime() {
        long totalMillis = (long)days * RAW_MILLIS_PER_DAY + (long)hours * RAW_MILLIS_PER_HOUR + 
                          (long)maximes * RAW_MILLIS_PER_MAXIME + (long)minutes * RAW_MILLIS_PER_MINUTE + 
                          (long)seconds * MILLIS_PER_SECOND + millis;
        return Amount.valueOf(totalMillis, HEX_MILLISECOND);
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

    /**
     * Returns the hex maxime component.
     * @return maximes
     */
    public int getMaximes() {
        return maximes;
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
        if (millis >= MILLIS_PER_SECOND) {
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
        if (minutes >= MINUTES_PER_MAXIME) {
            minutes = 0;
            nextMaxime();
        }
    }

    /** Increments the time by one hex maxime. */
    public void nextMaxime() {
        maximes++;
        if (maximes >= MAXIMES_PER_HOUR) {
            maximes = 0;
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

    @Override
    public String toString() {
        return String.format("%d d %X h %X M %X m %X s %d ms", days, hours, maximes, minutes, seconds, millis);
    }
}
