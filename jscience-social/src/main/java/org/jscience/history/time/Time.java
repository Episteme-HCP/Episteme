package org.jscience.history.time;

import org.jscience.measure.Amount;

import javax.measure.quantity.Duration;


/**
 * A class representing a way to compute time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Time {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Amount<Duration> getTime();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getMilliseconds();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getSeconds();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getMinutes();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getHours();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getDays();

    /**
     * DOCUMENT ME!
     */
    public abstract void nextMillisecond();

    /**
     * DOCUMENT ME!
     */
    public abstract void nextSecond();

    /**
     * DOCUMENT ME!
     */
    public abstract void nextMinute();

    /**
     * DOCUMENT ME!
     */
    public abstract void nextHour();

    /**
     * DOCUMENT ME!
     */
    public abstract void nextDay();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String toString();
}
