/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
