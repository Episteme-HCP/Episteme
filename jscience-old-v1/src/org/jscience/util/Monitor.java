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

package org.jscience.util;


//import java.io.Serializable;
/**
 * Monitor is a convenient class that is useful to communicate between
 * threads by exchanging information through this Monitor. The Monitor
 * contains several generically useful members which can be accessed
 * thread-savely to provide for different needs. There is also the
 * availability of a custom call-back function for several purposes.
 *
 * @author Holger Antelmann
 */
public class Monitor //implements Serializable
 {
    /** initialized as running during instanciation */
    public final Stopwatch timer;

    /** DOCUMENT ME! */
    private Stopwatch[] timers;

    /** DOCUMENT ME! */
    public volatile boolean test = false;

    /** DOCUMENT ME! */
    private Object[] listeners;

    /** DOCUMENT ME! */
    private boolean[] testers;

    /** DOCUMENT ME! */
    private int counter;

    /** DOCUMENT ME! */
    private int[] counters;

    /** DOCUMENT ME! */
    private int max;

    /** DOCUMENT ME! */
    private int min;

    /** DOCUMENT ME! */
    private Object object;

    /** DOCUMENT ME! */
    private Object[] objects;

    /** DOCUMENT ME! */
    private String message;

    /** DOCUMENT ME! */
    private Runnable task;

    /** DOCUMENT ME! */
    private boolean enabled;

    /** DOCUMENT ME! */
    private boolean done = false;

    /** DOCUMENT ME! */
    private DisablerThread killer;

/**
     * Creates a new Monitor object.
     */
    public Monitor() {
        reInitialize(true, 0, null);
        timer = new Stopwatch(true); // initialized as running
    }

/**
     * Creates a new Monitor object.
     *
     * @param size DOCUMENT ME!
     */
    public Monitor(int size) {
        reInitialize(true, size, null);
        timer = new Stopwatch(true); // initialized as running
    }

/**
     * initializes the arrays in this instance with n elements to
     * store/exchange data; task's run() method can be called with runTask()
     *
     * @see #runTask()
     */
    public Monitor(boolean enable, int size, Runnable task) {
        timer = new Stopwatch(true); // initialized as running
        reInitialize(enable, size, task);
    }

    /**
     * all objects and arrays are re-initialized as if newly
     * constructed; only the timer is maintained
     *
     * @param enable DOCUMENT ME!
     * @param size DOCUMENT ME!
     * @param task DOCUMENT ME!
     */
    public synchronized void reInitialize(boolean enable, int size,
        Runnable task) {
        done = false;

        if (killer != null) {
            synchronized (killer) {
                killer.interrupt();
            }

            killer = null;
        }

        this.task = task;
        this.enabled = enable;

        if (size > 0) {
            counters = new int[size];
            objects = new Object[size];
            testers = new boolean[size];
            timers = new Stopwatch[size];

            for (int i = 0; i < size; i++) {
                timers[i] = new Stopwatch(false); // initialized as stopped
                                                  //counters[i] = 0;
            }
        } else {
            counters = null;
            objects = null;
            testers = null;
            timers = null;
        }
    }

    /**
     * This function starts a separate Thread that will disable this
     * Monitor in the given time in milliseconds automatically. If a previous
     * Thread was scheduled to disable the monitor, that previous Thread will
     * be interrupted.
     *
     * @param milliseconds DOCUMENT ME!
     */
    public synchronized void disableLater(long milliseconds) {
        if (killer != null) {
            synchronized (killer) {
                killer.interrupt();
            }
        }

        killer = new DisablerThread(milliseconds);
        killer.start();
        killer.setPriority(Thread.MAX_PRIORITY);
    }

    /**
     * enable() sets the Monitor to be enabled() and also interrupts
     * threads scheduled through disableLater()
     */
    public synchronized void enable() {
        if (killer != null) {
            synchronized (killer) {
                killer.interrupt();
            }

            killer = null;
        }

        enabled = true;
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void disable() {
        enabled = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean enabled() {
        return enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean disabled() {
        return !enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone() {
        return done;
    }

    /**
     * the effect cannot be reversed
     */
    public synchronized void done() {
        done = true;
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void increment() {
        counter++;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public synchronized void increment(int i)
        throws ArrayIndexOutOfBoundsException {
        counters[i]++;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumber() {
        return counter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public int getNumber(int i) throws ArrayIndexOutOfBoundsException {
        return counters[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMin() {
        return min;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMax() {
        return max;
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     */
    public synchronized void setMin(int min) {
        this.min = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param max DOCUMENT ME!
     */
    public synchronized void setMax(int max) {
        this.max = max;
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     */
    public synchronized void setNumber(int number) {
        counter = number;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param number DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public synchronized void setNumber(int i, int number)
        throws ArrayIndexOutOfBoundsException {
        counters[i] = number;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public synchronized void setObject(Object obj) {
        object = obj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param obj DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public synchronized void setObject(int i, Object obj)
        throws ArrayIndexOutOfBoundsException {
        objects[i] = obj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     */
    public synchronized void setMessage(String msg) {
        message = msg;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getObject() {
        return object;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public Object getObject(int i) throws ArrayIndexOutOfBoundsException {
        return objects[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMessage() {
        return message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public Stopwatch getTimer(int i) throws ArrayIndexOutOfBoundsException {
        return timers[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     */
    public synchronized void setTask(Runnable task) {
        this.task = task;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Runnable getTask() {
        return task;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param t DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public synchronized void test(int i, boolean t)
        throws ArrayIndexOutOfBoundsException {
        testers[i] = t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public boolean test(int i) throws ArrayIndexOutOfBoundsException {
        return testers[i];
    }

    /**
     * returns the length of the monitor's arrays initialized by the
     * constructor or reInitialize()
     *
     * @return DOCUMENT ME!
     */
    public int getSize() {
        if (testers == null) {
            return 0;
        } else {
            return testers.length;
        }
    }

    /**
     * lets the Thread that uses the Monitor perform a synchronous
     * custom task that completes before the current thread continues. The
     * task performed is the run() method of the Runnable task given. If the
     * task is null, nothing happens.
     */
    public void runTask() {
        if (task != null) {
            task.run();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class DisablerThread extends Thread {
        /** DOCUMENT ME! */
        long milliseconds;

/**
         * Creates a new DisablerThread object.
         *
         * @param milliseconds DOCUMENT ME!
         */
        DisablerThread(long milliseconds) {
            this.milliseconds = milliseconds;
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            try {
                sleep(milliseconds);
            } catch (InterruptedException e) {
                return;
            }

            Monitor.this.disable();
        }
    }
}
