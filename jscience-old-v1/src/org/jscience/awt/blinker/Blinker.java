// Blinker Data Source
// Written by: Craig A. Lindley
// Last Update: 02/31/99
package org.jscience.awt.blinker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * Blinker is an invisible bean that fires a property change event at a
 * regular specified interval.
 */
public class Blinker implements Runnable {
    /** DOCUMENT ME! */
    private static final int DEFAULTINTERVAL = 100;

    // Private class data
    /** DOCUMENT ME! */
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /** DOCUMENT ME! */
    private int interval;

    /** DOCUMENT ME! */
    private boolean pulse = false;

    /** DOCUMENT ME! */
    private transient Thread runner = null;

/**
     * Class Constructor
     *
     * @param milliSeconds milliSeconds is the period of the blink
     */
    public Blinker(int milliSeconds) {
        // Save incoming period interval
        interval = milliSeconds;

        reset();
    }

/**
     * Class constructor which uses the default interval
     */
    public Blinker() {
        this(DEFAULTINTERVAL);
    }

    /**
     * Create a new thread to run this blinker
     */
    private void reset() {
        runner = new Thread(this);
        runner.start();
    }

    /**
     * Return the blink interval
     *
     * @return int containing the current interval in milliseconds
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Set the current blinker interval
     *
     * @param milliSeconds milliseconds is the new blinker interval
     */
    public void setInterval(int milliSeconds) {
        interval = milliSeconds;

        if (runner != null) {
            runner.interrupt();
        }
    }

    /**
     * Run method runs a infinite loop
     */
    public void run() {
        while (true) {
            try {
                // Sleep for the interval
                Thread.sleep(interval);

                // Toggle pulse stream
                pulse = !pulse;

                // Fire change event
                listeners.firePropertyChange("blink", null,
                    pulse ? Boolean.TRUE : Boolean.FALSE);
            } catch (InterruptedException ignore) {
            }
        }
    }

    /**
     * Register a property change listener
     *
     * @param l l is the listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        listeners.addPropertyChangeListener(l);
    }

    /**
     * Remove a property change listener
     *
     * @param l l is the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.removePropertyChangeListener(l);
    }
}
