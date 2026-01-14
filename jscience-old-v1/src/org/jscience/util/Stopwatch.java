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

package org.jscience.util;

import java.io.Serializable;
import java.util.Date;

/**
 * Stopwatch is a convenient implementation to bench just about anything.
 * Todo: provide support for internationalization
 *
 * @author Holger Antelmann
 */
public class Stopwatch implements Serializable {
    static final long serialVersionUID = -6958796021257440232L;

    private long start;
    private boolean paused;
    private long halted;
    private long end;

    /**
     * initializes a running Stopwatch starting now
     */
    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    /**
     * This constructor starts a Stopwatch starting at the current time
     * minus the milliseconds given. I.e. a negative value would mean that
     * the StopWach counts towards zero until zero is reached and then
     * continues to count forward.
     */
    public Stopwatch(long milliseconds) {
        start = System.currentTimeMillis() - milliseconds;
    }

    /**
     * This constructor starts a Stopwatch starting at the given point in time.
     * I.e. a time in the future would mean that the StopWach counts towards
     * zero until zero is reached and then continues to count forward.
     */
    public Stopwatch(Date time) {
        start = time.getTime();
    }

    /**
     * This constructor generates a new Stopwatch synchronized
     * with the given timer
     */
    public Stopwatch(Stopwatch timer) {
        synchronize(timer);
    }

    /**
     * If the boolean parameter is true, this call is equivalent
     * to the default constructor; if the parameter is false,
     * the StopWach will be initialized but is halted with
     * no elapsed time so far.
     */
    public Stopwatch(boolean isRunning) {
        this();
        if (!isRunning) {
            pause();
            reset();
        }
    }

    public Stopwatch(long milliseconds, boolean isRunning) {
        if (isRunning) {
            start = System.currentTimeMillis() - milliseconds;
        } else {
            end = System.currentTimeMillis();
            start = end - milliseconds;
            paused = true;
        }
    }

    /**
     * synchronize() will make this Stopwatch equivalent
     * to the passed timer
     */
    public synchronized void synchronize(Stopwatch timer) {
        synchronized (timer) {
            start = timer.getStartTime();
            paused = timer.isPaused();
            end = timer.getEnd();
            halted = timer.getHalted();
        }
    }

    /**
     * returns the time this Stopwatch was constructed or when any of
     * the reset() or restart() methods were called last
     */
    public long getStartTime() {
        return start;
    }

    /**
     * returns the initial start time of the Stopwatch.
     * This time may have been changed through synchronize() or restart().
     */
    public Date getStartDate() {
        return new Date(start);
    }

    /**
     * returns true if Stopwatch is paused.
     * isPaused() <==> !isRunning()
     */
    public boolean isPaused() {
        return (paused);
    }

    /**
     * returns true if Stopwatch is running.
     * isPaused() <==> !isRunning()
     */
    public boolean isRunning() {
        return (!paused);
    }

    /**
     * pause() stops the timer and maintains the elapsed time;
     * it does nothing if the timer is already paused.
     */
    public synchronized void pause() {
        if (!paused) {
            end = System.currentTimeMillis();
            paused = true;
        }
    }

    /**
     * resume() will reactivate a suspended timer (suspended through
     * either stop() or pause()). The beginning time as well as the
     * suspended time is maintained, so that the elapsed time remains
     * accurate. If the StopWach is currently running, the function doesn't
     * do anything.
     */
    public synchronized void resume() {
        if (paused) {
            halted = halted + (System.currentTimeMillis() - end);
            end = 0;
            paused = false;
        }
    }

    /**
     * start() maintains the beginning time and sets
     * the elapsed time to zero (through halted time);
     * time will be running.
     */
    public synchronized void start() {
        halted = (System.currentTimeMillis() - start);
        paused = false;
        end = 0;
    }

    /**
     * stop() returns the elapsed time and also performs
     * a reset().
     */
    public synchronized long stop() {
        long t = elapsed();
        pause();
        reset();
        return t;
    }

    /**
     * reset() will set the start to the current time and set
     * the halted time to zero. The running status (paused or not)
     * of the timer is maintained.
     */
    public synchronized void reset() {
        start = System.currentTimeMillis();
        if (paused) {
            end = start;
        }
        halted = 0;
    }

    /**
     * sets the given time as elapsed time; run/pause status remains
     * and halted time is reset
     */
    public synchronized void reset(long milliseconds) {
        if (paused) {
            start = System.currentTimeMillis() - milliseconds;
            end = start + milliseconds;
            halted = 0;
        } else {
            restart(milliseconds);
        }
    }

    /**
     * restart() reinitializes the timer equivalent to it
     * just being construced with the default constructor (time is running).
     */
    public synchronized void restart() {
        start = System.currentTimeMillis();
        halted = 0;
        end = 0;
        paused = false;
    }

    /**
     * restart() reinitializes the timer with the passed
     * milliseconds interpreted as already elapsed time.
     */
    public synchronized void restart(long milliseconds) {
        start = System.currentTimeMillis();
        halted = -milliseconds;
        end = 0;
        paused = false;
    }

    /**
     * returns the elapsed time in milliseconds
     */
    public synchronized long elapsed() {
        if (paused) {
            return ((end - start) - halted);
        } else {
            return ((System.currentTimeMillis() - start) - halted);
        }
    }

    /**
     * returns a string representing the elapsed time with
     * - if applicable - days, hours, minutes and seconds
     * (down to the milliseconds).
     */
    public String elapsedAsString() {
        return timeAsString(elapsed());
    }

    /**
     * displays the elapsed time as timeAsStringShort(elapsed())
     *
     * @see #timeAsStringShort(long)
     */
    public String display() {
        return timeAsStringShort(elapsed());
    }

    /**
     * takes milliseconds and converts them into a String
     * using all relevant time measures up to days
     */
    public static String timeAsString(long milliSecs) {
        String s = "";
        int days = (int) (milliSecs / (1000 * 60 * 60 * 24));
        int hours = (int) (milliSecs % (1000 * 60 * 60 * 24)) / 3600000;
        int minutes = (int) (milliSecs % 3600000) / 60000;
        double seconds = (double) (milliSecs % 60000) / 1000;
        if (days != 0) {
            s += days + " days, ";
        }
        if (hours != 0) {
            s += hours + " h, ";
        }
        if (minutes != 0) {
            s += minutes + " min, ";
        }
        s += seconds + " sec";
        return s;
    }

    /**
     * takes milliseconds and converts them into a short String.
     * The format is <pre>h:mm:ss</pre>.
     */
    public static String timeAsStringShort(long milliSecs) {
        int hours = Math.abs((int) milliSecs / 3600000);
        int minutes = Math.abs((int) (milliSecs % 3600000) / 60000);
        int seconds = Math.abs((int) (milliSecs % 60000) / 1000);
        String s = hours + ":";
        s += ((minutes < 10) ? ("0" + minutes) : String.valueOf(minutes));
        s += ":";
        s += ((seconds < 10) ? ("0" + seconds) : String.valueOf(seconds));
        if (milliSecs < 0) s = "-" + s;
        return s;
    }

    /**
     * returns the elapsed time in a convenient format
     * including elapsedAsString() and tells
     * whether the Stopwatch is currently running or not
     */
    public String toString() {
        String s = "elapsed time: ";
        s += elapsedAsString();
        if (paused) {
            s += " (time paused)";
        } else {
            s += " (time running)";
        }
        return (s);
    }

    protected long getHalted() {
        return halted;
    }

    protected long getEnd() {
        return end;
    }
}
