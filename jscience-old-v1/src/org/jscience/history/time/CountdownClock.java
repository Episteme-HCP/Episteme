package org.jscience.history.time;

/**
 * A class representing a way to display and change time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//  http://en.wikipedia.org/wiki/Clock
public class CountdownClock extends Clock {
    /**
     * DOCUMENT ME!
     */
    private ModernTime countdownTime;

    /**
     * DOCUMENT ME!
     */
    private ModernTime time;

    /**
     * Creates a new CountdownClock object.
     *
     * @param timeServer DOCUMENT ME!
     */
    public CountdownClock(TimeServer timeServer) {
        super(timeServer);
        countdownTime = new ModernTime(0);
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        //start the time server
        getTimeServer().start();
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        //stop the time server
        getTimeServer().stop();
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        //reset the countdown to its starting value
        setTime(countdownTime);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Time getCountdownTime() {
        return countdownTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param countdownTime DOCUMENT ME!
     */
    public void setCountdownTime(ModernTime countdownTime) {
        this.countdownTime = countdownTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Time getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void setTime(ModernTime time) {
        this.time = time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void timeChanged(TimeEvent event) {
        setTime(new ModernTime(countdownTime.getTimeInSeconds() -
                ((ModernTime) event.getTime()).getTimeInSeconds()));
    }
}
