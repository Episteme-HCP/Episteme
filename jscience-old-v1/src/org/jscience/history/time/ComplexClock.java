package org.jscience.history.time;

/**
 * A class representing a way to display and change time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//  http://en.wikipedia.org/wiki/Clock
public class ComplexClock {
    /** DOCUMENT ME! */
    private ChronometerClock chronometer;

    /** DOCUMENT ME! */
    private AlarmClock alarm;

    /** DOCUMENT ME! */
    private CountdownClock countdown;

    /** DOCUMENT ME! */
    private BasicClock clock;

/**
     * Creates a new ComplexClock object.
     *
     * @param clock DOCUMENT ME!
     * @param chronometer DOCUMENT ME!
     * @param alarm DOCUMENT ME!
     * @param countdown DOCUMENT ME!
     */
    public ComplexClock(BasicClock clock, ChronometerClock chronometer,
        AlarmClock alarm, CountdownClock countdown) {
        this.chronometer = chronometer;
        this.alarm = alarm;
        this.countdown = countdown;
        this.clock = clock;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChronometerClock getChronometer() {
        return chronometer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param chronometer DOCUMENT ME!
     */
    public void setChronometer(ChronometerClock chronometer) {
        this.chronometer = chronometer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AlarmClock getAlarm() {
        return alarm;
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarm DOCUMENT ME!
     */
    public void setAlarm(AlarmClock alarm) {
        this.alarm = alarm;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CountdownClock getCountdown() {
        return countdown;
    }

    /**
     * DOCUMENT ME!
     *
     * @param countdown DOCUMENT ME!
     */
    public void setCountdown(CountdownClock countdown) {
        this.countdown = countdown;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BasicClock getClock() {
        return clock;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clock DOCUMENT ME!
     */
    public void setClock(BasicClock clock) {
        this.clock = clock;
    }
}
