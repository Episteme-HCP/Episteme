package org.jscience.history.time;

import java.util.Timer;

import javax.swing.*;
import javax.swing.event.EventListenerList;


/**
 * A class representing a way get time values.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class TimeServer {
    /** A list of event listeners for this component. */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * DOCUMENT ME!
     */
    public abstract void start();

    /**
     * DOCUMENT ME!
     */
    public abstract void stop();

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void dispatchTimeEvent(TimeEvent evt) {
        Object[] list = getTimeListeners();

        for (int i = 0; i < list.length; i++) {
            TimeListener target = (TimeListener) list[i];
            target.timeChanged(evt);
        }
    }

    /**
     * Adds the specified listener to receive events from this clock.
     *
     * @param l the TimeListener
     */
    public void addTimeListener(TimeListener l) {
        listenerList.add(TimeListener.class, l);
    }

    /**
     * Removes the specified TimeListener so that it no longer receives
     * events from this clock.
     *
     * @param l the TimeListener
     */
    public void removeTimeListener(TimeListener l) {
        listenerList.remove(TimeListener.class, l);
    }

    /**
     * Returns an array of all the <code>TimeListener</code>s added to
     * this <code>Clock</code> with <code>addTimeListener</code>.
     *
     * @return all of the <code>TimeListener</code>s added or an empty array if
     *         no listeners have been added
     *
     * @see #addTimeListener
     */
    public TimeListener[] getTimeListeners() {
        return (TimeListener[]) listenerList.getListeners(TimeListener.class);
    }
}
