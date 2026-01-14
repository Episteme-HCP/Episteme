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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Executor;


/**
 * uses a pool of threads to execute tasks asynchronously and concurrently.
 * ThreadPool was implemented before JDK1.5 and is somewhat simmilar to
 * java.util.concurrent.ThreadPoolExecutor
 *
 * @author Holger Antelmann
 */
public class ThreadPool implements Executor {
    /**
     * DOCUMENT ME!
     */
    ArrayList<ThreadWorker> threads;

    /**
     * DOCUMENT ME!
     */
    Vector<ThreadWorker.Listener> listener;

    /**
     * DOCUMENT ME!
     */
    ThreadGroup threadGroup;

    /**
     * DOCUMENT ME!
     */
    boolean daemon;

    /**
     * Creates a new ThreadPool object.
     *
     * @param numberOfThreads DOCUMENT ME!
     */
    public ThreadPool(int numberOfThreads) {
        this(numberOfThreads, false);
    }

    /**
     * Creates a new ThreadPool object.
     *
     * @param numberOfThreads DOCUMENT ME!
     * @param asDaemon DOCUMENT ME!
     */
    public ThreadPool(int numberOfThreads, boolean asDaemon) {
        if (numberOfThreads < 1) {
            throw new IllegalArgumentException(
                "number of threads must be greater than 0: " + numberOfThreads);
        }

        listener = new Vector<ThreadWorker.Listener>();
        threadGroup = new ThreadGroup("ThreadPool group");
        threads = new ArrayList<ThreadWorker>(numberOfThreads);
        daemon = asDaemon;

        for (int i = 0; i < numberOfThreads; i++) {
            addThreadWorker();
        }
    }

    /**
     * returns true if at least one thread in the pool is currently
     * active
     *
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        for (ThreadWorker tw : threads) {
            if (tw.isAlive()) {
                return true;
            }
        }

        return false;
    }

    /**
     * returns the number of all threads currently in this pool
     *
     * @return DOCUMENT ME!
     */
    public synchronized int getNumberOfThreads() {
        return threads.size();
    }

    /**
     * adds one thread to the pool
     */
    public synchronized void addThreadWorker() {
        ThreadWorker tw = new ThreadWorker(threadGroup,
                "ThreadWorker" + threads.size());
        tw.setDaemon(daemon);
        tw.start();
        tw.addListener(new ThreadWorker.Listener() {
                public void taskPerformed(Runnable task, long timeTaken,
                    Throwable exceptionIfAny) {
                    synchronized (ThreadPool.this) {
                        Iterator it = listener.iterator();

                        while (it.hasNext()) {
                            ((ThreadWorker.Listener) it.next()).taskPerformed(task,
                                timeTaken, exceptionIfAny);
                        }
                    }
                }
            });
        threads.add(tw);
    }

    /**
     * returns the number of threads that are currently idle
     *
     * @return DOCUMENT ME!
     */
    public synchronized int idleThreads() {
        int count = 0;

        for (ThreadWorker tw : threads) {
            if (tw.getQueueSize() == 0) {
                count++;
            }
        }

        return count;
    }

    /**
     * removes all threads that are currently idle. A thread can only
     * be removed if idle.
     */
    public synchronized void removeIdleThreads() {
        Iterator<ThreadWorker> i = threads.iterator();

        while (i.hasNext()) {
            ThreadWorker tw = i.next();

            if (tw.getQueueSize() == 0) {
                tw.endAfterLast();
                i.remove();
            }
        }
    }

    /**
     * attempts to remove a single ThreadWorker from the pool. If all
     * threads but one have already been removed from the pool, nothing
     * happens and false is returned. This method first attempts to remove an
     * idle thread - if found. If all threads are active, the one with the
     * smallest queue size is removed.
     *
     * @return true only if a thread was indeed removed from the pool
     */
    public synchronized boolean removeIdleThread() {
        if (threads.size() < 1) {
            return false;
        }

        Iterator<ThreadWorker> i = threads.iterator();

        while (i.hasNext()) {
            ThreadWorker tw = i.next();

            if (tw.getQueueSize() == 0) {
                tw.endAfterLast();
                i.remove();

                return true;
            }
        }

        return false;
    }

    /**
     * removes all ThreadWorkers, which will all end either after the
     * current or the last queued task - depending on the 'now' parameter.
     *
     * @param now DOCUMENT ME!
     */
    public synchronized void finishAll(boolean now) {
        Iterator<ThreadWorker> i = threads.iterator();

        while (i.hasNext()) {
            if (now) {
                i.next().endAfterCurrent();
            } else {
                i.next().endAfterLast();
            }

            i.remove();
        }
    }

    /**
     * total queue size over all threads
     *
     * @return DOCUMENT ME!
     */
    public synchronized int getQueueSize() {
        int count = 0;

        for (ThreadWorker tw : threads) {
            count += tw.getQueueSize();
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public synchronized void addListener(ThreadWorker.Listener listener) {
        this.listener.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized ThreadWorker.Listener[] getListeners() {
        return listener.toArray(new ThreadWorker.Listener[listener.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public synchronized void removeListener(ThreadWorker.Listener listener) {
        this.listener.remove(listener);
    }

    /**
     * calls <code>runTask(task)</code>; this method enables usability
     * with jdk1.5
     *
     * @param task DOCUMENT ME!
     */
    public void execute(Runnable task) {
        runTask(task);
    }

    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public synchronized void runTask(Runnable task)
        throws IllegalStateException {
        if (threads.size() < 1) {
            throw new IllegalStateException("no threads in pool");
        }

        int min = Integer.MAX_VALUE;
        ThreadWorker t = threads.get(0);

        for (ThreadWorker tw : threads) {
            int n = tw.getQueueSize();

            if (n == 0) {
                tw.runTask(task);

                return;
            }

            if (min > n) {
                min = n;
                t = tw;
            }
        }

        t.runTask(task);
    }
}
