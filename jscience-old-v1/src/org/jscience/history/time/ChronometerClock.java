package org.jscience.history.time;

/**
 * A class representing a way to display and change time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ChronometerClock extends Clock {
    /**
     * DOCUMENT ME!
     */
    private ModernTime startTime;

    /**
     * DOCUMENT ME!
     */
    private ModernTime time;

    /**
     * DOCUMENT ME!
     */
    private boolean firstTime;

    /**
     * Creates a new ChronometerClock object.
     *
     * @param timeServer DOCUMENT ME!
     */
    public ChronometerClock(TimeServer timeServer) {
        super(timeServer);
        firstTime = true;
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

    //restarts now
    /**
     * DOCUMENT ME!
     */
    public void reset() {
        firstTime = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ModernTime getTime() {
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
        if (firstTime) {
            startTime = (ModernTime) event.getTime();
            firstTime = false;
        }

        setTime(new ModernTime(((ModernTime) event.getTime()).getTimeInSeconds() -
                startTime.getTimeInSeconds()));
    }
}
