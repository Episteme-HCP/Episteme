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

package org.jscience.social.history.clock;

import java.util.Calendar;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Time;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Standard representation of time using the Gregorian/Modern system (24h/60m/60s).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class ModernTime extends org.jscience.social.history.time.Time {

    private static final long serialVersionUID = 2L;

    public static final int HOURS_PER_DAY = 24;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;

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

    public ModernTime(double days, double hours, double minutes, double seconds, double millis) {
        if (days < 0 || hours < 0 || minutes < 0 || seconds < 0 || millis < 0) {
            throw new IllegalArgumentException("Time components cannot be negative");
        }
        double totalMillis = (days * MILLIS_PER_DAY) + (hours * MILLIS_PER_HOUR) +
                             (minutes * MILLIS_PER_MINUTE) + (seconds * MILLISECONDS_PER_SECOND) + millis;
        initFromMillis(totalMillis);
    }

    public ModernTime(double totalMillis) {
        if (totalMillis < 0) {
            throw new IllegalArgumentException("Time in milliseconds cannot be negative");
        }
        initFromMillis(totalMillis);
    }

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
    public Quantity<Time> getTime() {
        long totalMillis = (long)days * MILLIS_PER_DAY + (long)hours * MILLIS_PER_HOUR + 
                          (long)minutes * MILLIS_PER_MINUTE + (long)seconds * MILLISECONDS_PER_SECOND + millis;
        return Quantities.create(org.jscience.core.mathematics.numbers.real.Real.of(totalMillis), Units.MILLISECOND);
    }

    @Override
    public int getMilliseconds() { return millis; }
    @Override
    public int getSeconds() { return seconds; }
    @Override
    public int getMinutes() { return minutes; }
    @Override
    public int getHours() { return hours; }
    @Override
    public int getDays() { return days; }

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
    public void nextDay() { days++; }

    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, days, hours, minutes, seconds);
        return calendar;
    }

    public double getTimeInSeconds() {
        long totalMillis = (long)days * MILLIS_PER_DAY + (long)hours * MILLIS_PER_HOUR + 
                          (long)minutes * MILLIS_PER_MINUTE + (long)seconds * MILLISECONDS_PER_SECOND + millis;
        return totalMillis / 1000.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(days).append(" d ");
        int displayHours = hours;
        if (!is24 && hours > 12) displayHours -= 12;
        sb.append(displayHours).append(" h ").append(minutes).append(" m ").append(seconds).append(" s ").append(millis).append(" ms");
        if (!is24) sb.append(hours >= 12 ? " PM" : " AM");
        return sb.toString();
    }
}

