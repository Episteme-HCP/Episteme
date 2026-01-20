package org.jscience.history.time;

import javax.swing.*;
import javax.swing.event.EventListenerList;


/**
 * A class representing a way to display and change time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//  http://en.wikipedia.org/wiki/Clock
public abstract class Clock implements TimeListener {
    /**
     * DOCUMENT ME!
     */
    private TimeServer timeServer;

    /**
     * Creates a new Clock object.
     *
     * @param timeServer DOCUMENT ME!
     */
    public Clock(TimeServer timeServer) {
        this.timeServer = timeServer;
        timeServer.addTimeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TimeServer getTimeServer() {
        return timeServer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Time getTime();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return getTime().toString();
    }
}
