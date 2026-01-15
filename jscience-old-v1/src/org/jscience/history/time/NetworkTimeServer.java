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
public class NetworkTimeServer extends TimeServer {
    /**
     * Creates a new NetworkTimeServer object.
     */
    public NetworkTimeServer() {
        //connect to NTP server at regular interval (using Timer thread) and fill a Time object then generate a TimeEvent
        Timer timer = new Timer();
        ModernTime time = new ModernTime();
        TimeEvent evt = new TimeEvent(this, time, TimeEvent.TIME_CHANGED);
        dispatchTimeEvent(evt);
    }
}
