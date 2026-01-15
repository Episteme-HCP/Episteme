package org.jscience.awt;

import javax.swing.event.EventListenerList;


/**
 * The AbstractGraphModel class handles the dispatching of GraphDataEvents
 * to interested listeners.
 *
 * @author Mark Hale
 * @version 1.2
 */
public abstract class AbstractGraphModel extends Object {
    /** DOCUMENT ME! */
    private final EventListenerList listenerList = new EventListenerList();

    /** DOCUMENT ME! */
    private final GraphDataEvent dataChangedEvent = new GraphDataEvent(this);

    /**
     * Notifies all listeners that the graph data may have changed.
     */
    protected final void fireGraphDataChanged() {
        fireGraphChanged(dataChangedEvent);
    }

    /**
     * Notifies all listeners that a series may have changed.
     *
     * @param series DOCUMENT ME!
     */
    protected final void fireGraphSeriesUpdated(int series) {
        final GraphDataEvent event = new GraphDataEvent(this, series);
        fireGraphChanged(event);
    }

    /**
     * Notifies all listeners of a given graph data event.
     *
     * @param event DOCUMENT ME!
     */
    protected final void fireGraphChanged(GraphDataEvent event) {
        final Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == GraphDataListener.class) {
                ((GraphDataListener) listeners[i + 1]).dataChanged(event);
            }
        }
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the graph data occurs.
     *
     * @param l DOCUMENT ME!
     */
    public final void addGraphDataListener(GraphDataListener l) {
        listenerList.add(GraphDataListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the graph data occurs.
     *
     * @param l DOCUMENT ME!
     */
    public final void removeGraphDataListener(GraphDataListener l) {
        listenerList.remove(GraphDataListener.class, l);
    }
}
