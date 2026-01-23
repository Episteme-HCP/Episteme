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
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Time;
import java.io.Serializable;
import java.util.Calendar;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Standard representation of time using the Gregorian/Modern system (24h/60m/60s).
 * Provides conversion utilities between chronological components and standardized units of duration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
@Persistent
public class ModernTime extends org.jscience.history.time.Time {

    private static final long serialVersionUID = 1L;

    public static final int HOURS_PER_DAY = 24;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;

    /** Unit for one millisecond. */
    public static final Unit<Time> MODERN_MILLISECOND = Units.MILLISECOND;

    /** Standard unit for duration (second). */
    public static final Unit<Time> MODERN_SECOND = Units.SECOND;

    /** Unit for one minute. */
    public static final Unit<Time> MODERN_MINUTE = Units.MINUTE;

    /** Unit for one hour. */
    public static final Unit<Time> MODERN_HOUR = Units.HOUR;

    /** Unit for one 24-hour day. */
    public static final Unit<Time> MODERN_DAY = Units.DAY;

    private static final long MILLIS_PER_DAY = 24L * 60L * 60L * 1000L;
    private static final long MILLIS_PER_HOUR = 60L * 60L * 1000L;
    private static final long MILLIS_PER_MINUTE = 60L * 1000L;

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

    @Attribute
    private boolean is24 = true;

    /**
     * Creates ModernTime from full components.
     * 
     * @param days count of days
     * @param hours hours (0-23)
     * @param minutes minutes (0-59)
     * @param seconds seconds (0-59)
     * @param millis milliseconds (0-999)
     * @throws IllegalArgumentException if any value is negative
     */
    public ModernTime(double days, double hours, double minutes, double seconds, double millis) {
        if (days < 0 || hours < 0 || minutes < 0 || seconds < 0 || millis < 0) {
            throw new IllegalArgumentException("Time components cannot be negative");
        }
        double totalMillis = (days * MILLIS_PER_DAY) + (hours * MILLIS_PER_HOUR) +
                             (minutes * MILLIS_PER_MINUTE) + (seconds * MILLISECONDS_PER_SECOND) + millis;
        initFromMillis(totalMillis);
    }

    /**
     * Creates ModernTime from total milliseconds.
     * 
     * @param millis total milliseconds
     * @throws IllegalArgumentException if millis is negative
     */
    public ModernTime(double millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Time in milliseconds cannot be negative");
        }
        initFromMillis(millis);
    }

    /**
     * Creates ModernTime representing the current system moment.
     */
    public ModernTime() {
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
        return Amount.valueOf(totalMillis, Units.MILLISECOND);
    }

    /**
     * Calculates the total elapsed seconds.
     * 
     * @return total seconds
     */
    public double getTimeInSeconds() {
        return ((long)days * MILLIS_PER_DAY + (long)hours * MILLIS_PER_HOUR + 
                (long)minutes * MILLIS_PER_MINUTE + (long)seconds * MILLISECONDS_PER_SECOND + millis) / 1000.0;
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
     * Checks if 24-hour format is preferred for display.
     * 
     * @return true for 24h, false for 12h (AM/PM)
     */
    public boolean is24() {
        return is24;
    }

    /**
     * Sets the preferred display format.
     * 
     * @param is24 true for 24h format
     */
    public void set24(boolean is24) {
        this.is24 = is24;
    }

    /**
     * Converts to a standard Java Calendar instance.
     * 
     * @return calendar representation
     */
    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, days, hours, minutes, seconds);
        return calendar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(days).append(" d ");

        int displayHours = hours;
        if (!is24 && hours > 12) {
            displayHours -= 12;
        }
        
        sb.append(displayHours).append(" h ")
          .append(minutes).append(" m ")
          .append(seconds).append(" s ")
          .append(millis).append(" ms");

        if (!is24) {
            sb.append(hours >= 12 ? " PM" : " AM");
        }

        return sb.toString();
    }
}
