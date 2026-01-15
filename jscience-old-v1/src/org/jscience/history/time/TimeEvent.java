package org.jscience.history.time;

import java.util.EventObject;


/**
 * A class representing a way to have time related events.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class TimeEvent extends EventObject {
    /**
     * DOCUMENT ME!
     */
    public final static int TIME_CHANGED = 1;

    /**
     * DOCUMENT ME!
     */
    private Time time;

    /**
     * DOCUMENT ME!
     */
    private int id;

/**
     * Constructs an <code>TimeEvent</code> object.
     * @param source the <code>Time</code> object that originated the event
     * @param id     an integer indicating the type of event
     */
    public TimeEvent(TimeServer source, Time time, int id) {
        super(source);
        this.time = time;
        this.id = id;
    }

    /**
     * Returns a parameter string identifying this event. This method
     * is useful for event logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String typeStr;

        switch (getID()) {
        case TIME_CHANGED:
            typeStr = "TIME_CHANGED";

            break;

        default:
            typeStr = "unknown type";
        }

        return typeStr;
    }

    /**
     * Returns the originator of the event.
     *
     * @return the <code>TimeServer</code> object that originated the event
     *
     * @since 1.3
     */
    public TimeServer getTimeServer() {
        return (source instanceof TimeServer) ? (TimeServer) source : null;
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
     * @return DOCUMENT ME!
     */
    public int getID() {
        return id;
    }
}
