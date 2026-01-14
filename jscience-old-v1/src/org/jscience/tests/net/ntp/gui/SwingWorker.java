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

// Copied from Sun tutorial. VdB.
package org.jscience.tests.net.ntp.gui;

import javax.swing.*;


/**
 * An abstract class that you subclass to perform GUI-related work in a
 * dedicated thread. For instructions on using this class, see
 * http://java.sun.com/products/jfc/swingdoc/threads.html
 */
abstract class SwingWorker {
    /** DOCUMENT ME! */
    private Object value;

    /** DOCUMENT ME! */
    private Thread thread;

/**
     * Start a thread that will call the <code>construct</code> method and then
     * exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
                public void run() {
                    finished();
                }
            };

        Runnable doConstruct = new Runnable() {
                public void run() {
                    synchronized (SwingWorker.this) {
                        value = construct();
                        thread = null;
                    }

                    SwingUtilities.invokeLater(doFinished);
                }
            };

        thread = new Thread(doConstruct);
        thread.start();
    }

    /**
     * Compute the value to be returned by the <code>get</code> method.
     *
     * @return DOCUMENT ME!
     */
    public abstract Object construct();

    /**
     * Called on the event dispatching thread (not on the worker
     * thread) after the <code>construct</code> method has returned.
     */
    public void finished() {
    }

    /**
     * Return the value created by the <code>construct</code> method.
     *
     * @return DOCUMENT ME!
     */
    public Object get() {
        while (true) { // keep trying if we're interrupted

            Thread t;

            synchronized (SwingWorker.this) {
                t = thread;

                if (t == null) {
                    return value;
                }
            }

            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
    }
}
