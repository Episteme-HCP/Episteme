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
 * Represents Traditional Chinese Time.
 * Historically, the day was divided into 100 components called 'ke' (刻).
 * Each 'ke' was subdivided into 60 'fen' (分).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 * @see <a href="http://en.wikipedia.org/wiki/Chinese_time">Chinese Time (Wikipedia)</a>
 */
@Persistent
public class ChineseTime extends org.jscience.history.time.Time {

    private static final long serialVersionUID = 1L;

    /** Number of 'ke' per day. */
    public static final int KE_PER_DAY = 100;

    /** Number of 'fen' per 'ke'. */
    public static final int FEN_PER_KE = 60;

    /** Number of seconds per 'fen' (custom implementation). */
    public static final int SECONDS_PER_FEN = 60;

    /** Number of milliseconds per second. */
    public static final int MILLIS_PER_SECOND = 1000;

    /** The base unit for Chinese seconds (approx. 0.24 standard seconds). */
    public static final Unit<Time> CHINESE_SECOND = new StandardUnit<>("cs", "Chinese second", Dimension.TIME);

    /** Unit for one Chinese millisecond. */
    public static final Unit<Time> CHINESE_MILLISECOND = CHINESE_SECOND.divide(MILLIS_PER_SECOND);

    /** Unit for one Chinese 'fen' (60 Chinese seconds). */
    public static final Unit<Time> CHINESE_FEN = CHINESE_SECOND.multiply(SECONDS_PER_FEN);

    /** Unit for one Chinese 'ke' (60 'fen'). */
    public static final Unit<Time> CHINESE_KE = CHINESE_FEN.multiply(FEN_PER_KE);

    /** Unit for one Chinese day (100 'ke'). */
    public static final Unit<Time> CHINESE_DAY = CHINESE_KE.multiply(KE_PER_DAY);

    private static final long MILLIS_PER_DAY = (long) KE_PER_DAY * FEN_PER_KE * SECONDS_PER_FEN * MILLIS_PER_SECOND;
    private static final long MILLIS_PER_KE = (long) FEN_PER_KE * SECONDS_PER_FEN * MILLIS_PER_SECOND;
    private static final long MILLIS_PER_FEN = (long) SECONDS_PER_FEN * MILLIS_PER_SECOND;

    @Attribute
    private int millis;

    @Attribute
    private int seconds;

    @Attribute
    private int fen; // minutes equivalent

    @Attribute
    private int ke; // hours equivalent

    @Attribute
    private int days;

    /**
     * Creates ChineseTime from components.
     * 
     * @param days days
     * @param ke count of 'ke' (0-99)
     * @param fen count of 'fen' (0-59)
     * @param seconds Chinese seconds (0-59)
     * @param millis Chinese milliseconds (0-999)
     * @throws IllegalArgumentException if any value is negative
     */
    public ChineseTime(double days, double ke, double fen, double seconds, double millis) {
        if (days < 0 || ke < 0 || fen < 0 || seconds < 0 || millis < 0) {
            throw new IllegalArgumentException("Time components cannot be negative");
        }
        double total = (days * MILLIS_PER_DAY) + (ke * MILLIS_PER_KE) +
                       (fen * MILLIS_PER_FEN) + (seconds * MILLIS_PER_SECOND) + millis;
        initFromMillis(total);
    }

    /**
     * Creates ChineseTime from raw milliseconds.
     * 
     * @param millis total milliseconds
     * @throws IllegalArgumentException if millis is negative
     */
    public ChineseTime(double millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Milliseconds cannot be negative");
        }
        initFromMillis(millis);
    }

    /**
     * Creates ChineseTime with the current moment.
     */
    public ChineseTime() {
        this((double) System.currentTimeMillis());
    }

    private void initFromMillis(double total) {
        this.millis = (int) (total % MILLIS_PER_SECOND);
        total /= MILLIS_PER_SECOND;
        this.seconds = (int) (total % SECONDS_PER_FEN);
        total /= SECONDS_PER_FEN;
        this.fen = (int) (total % FEN_PER_KE);
        total /= FEN_PER_KE;
        this.ke = (int) (total % KE_PER_DAY);
        total /= KE_PER_DAY;
        this.days = (int) (total);
    }

    @Override
    public Amount<Time> getTime() {
        long totalMillis = (long)days * MILLIS_PER_DAY + (long)ke * MILLIS_PER_KE + 
                          (long)fen * MILLIS_PER_FEN + (long)seconds * MILLIS_PER_SECOND + millis;
        return Amount.valueOf(totalMillis, CHINESE_MILLISECOND);
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
        return fen;
    }

    @Override
    public int getHours() {
        return ke;
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
        if (seconds >= SECONDS_PER_FEN) {
            seconds = 0;
            nextMinute();
        }
    }

    @Override
    public void nextMinute() {
        fen++;
        if (fen >= FEN_PER_KE) {
            fen = 0;
            nextHour();
        }
    }

    @Override
    public void nextHour() {
        ke++;
        if (ke >= KE_PER_DAY) {
            ke = 0;
            days++;
        }
    }

    @Override
    public void nextDay() {
        days++;
    }

    @Override
    public String toString() {
        return String.format("%d d %d ke %d fen %d s %d ms", days, ke, fen, seconds, millis);
    }
}
