package org.jscience.history.time;

/**
 * A class representing a way to display and change time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//  http://en.wikipedia.org/wiki/Clock
public abstract class AlarmClock extends Clock {
    /**
     * DOCUMENT ME!
     */
    private double delta = 1000;

    /**
     * DOCUMENT ME!
     */
    private ModernTime alarmTime;

    /**
     * DOCUMENT ME!
     */
    private ModernTime time;

    /**
     * Creates a new AlarmClock object.
     *
     * @param timeServer DOCUMENT ME!
     */
    public AlarmClock(TimeServer timeServer) {
        super(timeServer);
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
     *
     * @return DOCUMENT ME!
     */
    public ModernTime getAlarmTime() {
        return alarmTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarmTime DOCUMENT ME!
     */
    public void setAlarmTime(ModernTime alarmTime) {
        this.alarmTime = alarmTime;
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
        setTime((ModernTime) event.getTime());

        if ((alarmTime != null) &&
                ((((ModernTime) event.getTime()).getTimeInSeconds() -
                getAlarmTime().getTimeInSeconds()) < delta)) {
            //alarm fires
            fireAlarm();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void fireAlarm();
}
