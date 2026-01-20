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
public class LocalTimeServer extends TimeServer {
    /**
     * Creates a new LocalTimeServer object.
     */
    public LocalTimeServer() {
        //use Java local time at regular interval (using Timer thread) and fill a Time object then generate a TimeEvent
        ModernTime time = new ModernTime();
        TimeEvent evt = new TimeEvent(this, time, TimeEvent.TIME_CHANGED);
        dispatchTimeEvent(evt);
    }
}
