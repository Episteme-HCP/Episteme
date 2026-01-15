package org.jscience.history.time;

/**
 * A class representing a way to display and change time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//  http://en.wikipedia.org/wiki/Clock
public class BasicClock extends Clock {
    /**
     * DOCUMENT ME!
     */
    private ModernTime time;

    /**
     * Creates a new BasicClock object.
     *
     * @param timeServer DOCUMENT ME!
     */
    public BasicClock(TimeServer timeServer) {
        super(timeServer);
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
    }
}
