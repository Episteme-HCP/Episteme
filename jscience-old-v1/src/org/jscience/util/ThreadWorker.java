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
import java.util.EventListener;
import java.util.Iterator;
import java.util.concurrent.Executor;


/**
 * ThreadWorker runs tasks in a separate thread, where the tasks are queued
 * up and performed one by one. Java 1.5 makes this class somewhat obsolete,
 * but it's still there as it has been used elsewhere in this framework. Since
 * the JDK 1.5 FCS, this implementation is adopted to implement Executor.
 *
 * @author Holger Antelmann
 *
 * @since 04/28/2004
 */
public class ThreadWorker extends Thread implements Executor {
    /**
     * DOCUMENT ME!
     */
/**
     * DOCUMENT ME!
     */
    ArrayList<Runnable> tasks = new ArrayList<Runnable>();

    /**
     * DOCUMENT ME!
     */
/**
     * DOCUMENT ME!
     */
    ArrayList<Listener> listeners = new ArrayList<Listener>();

    /** DOCUMENT ME! */
    boolean enabled = true;

    /** DOCUMENT ME! */
    boolean endNow = false;

    /** DOCUMENT ME! */
    int count = 0;

    /** DOCUMENT ME! */
    Stopwatch totalRunningTime = new Stopwatch(false);

    /** DOCUMENT ME! */
    Stopwatch currentTaskTime = new Stopwatch(-1, false);

    /** DOCUMENT ME! */
    boolean once = false;

/**
     * Creates a new ThreadWorker object.
     */
    public ThreadWorker() {
        super();
    }

/**
     * Creates a new ThreadWorker object.
     *
     * @param name DOCUMENT ME!
     */
    public ThreadWorker(String name) {
        super(name);
    }

/**
     * Creates a new ThreadWorker object.
     *
     * @param group DOCUMENT ME!
     * @param name  DOCUMENT ME!
     */
    public ThreadWorker(ThreadGroup group, String name) {
        super(group, name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean addListener(Listener listener) {
        return listeners.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean removeListener(Listener listener) {
        return listeners.remove(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized Listener[] getListeners() {
        return listeners.toArray(new Listener[listeners.size()]);
    }

    /**
     * returns the Runnable object currently executed by this
     * ThreadWorker
     *
     * @return DOCUMENT ME!
     */
    public synchronized Runnable getCurrentTask() {
        return (tasks.isEmpty()) ? null : (Runnable) tasks.get(0);
    }

    /**
     * returns the number of Runnable objects waiting to be executed
     *
     * @return DOCUMENT ME!
     */
    public synchronized int getQueueSize() {
        return tasks.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isIdle() {
        return tasks.isEmpty();
    }

    /**
     * returns all tasks currently in the queue (including the current
     * one)
     *
     * @return DOCUMENT ME!
     */
    public synchronized Runnable[] getTasks() {
        return tasks.toArray(new Runnable[tasks.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException if the given task is currently running
     */
    public synchronized boolean removeTask(Runnable task)
        throws IllegalStateException {
        Runnable r = getCurrentTask();

        if (r == null) {
            return false;
        }

        if (r.equals(task)) {
            throw new IllegalStateException("given task is currently active");
        }

        return tasks.remove(task);
    }

    /**
     * returns the number of tasks that have been executed (including
     * the current one)
     *
     * @return DOCUMENT ME!
     */
    public int getTaskCount() {
        return count;
    }

    /**
     * excludes time where this thread is waiting for requests
     *
     * @return DOCUMENT ME!
     */
    public long getTotalRunningTime() {
        return totalRunningTime.elapsed();
    }

    /**
     * if no task is currently running, -1 is returned.
     *
     * @return DOCUMENT ME!
     */
    public long getCurrentTaskRunningTime() {
        return currentTaskTime.elapsed();
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
     * runs the given task immediately after all previous tasks have
     * finished - once the ThreadWorker has been started.
     *
     * @param task DOCUMENT ME!
     *
     * @return the number of tasks currently in the queue (including the given
     *         one)
     *
     * @throws IllegalStateException if the task has no chance anymore of being
     *         run
     */
    public synchronized int runTask(Runnable task) throws IllegalStateException {
        if (endNow) {
            throw new IllegalStateException(
                "ThreadWorker has been ended after current");
        }

        if (once && !isAlive()) {
            throw new IllegalStateException("ThreadWorker already finished");
        }

        if (tasks.isEmpty()) {
            notifyAll();
        }

        tasks.add(task);

        return tasks.size();
    }

    /**
     * should be called only once through <code>start()</code>
     *
     * @throws IllegalThreadStateException if the method is called through
     *         anything but <code>start()</code> once
     */
    public void run() throws IllegalThreadStateException {
        synchronized (this) {
            if (!isAlive()) {
                throw new IllegalThreadStateException();
            }

            if (once) {
                throw new IllegalThreadStateException();
            }

            once = true;
        }

        totalRunningTime.restart();
loop: 
        while (true) {
            if (endNow) {
                break;
            }

            Runnable currentTask = null;

            synchronized (this) {
                currentTaskTime = new Stopwatch(-1, false);

                if (tasks.isEmpty()) {
                    if (!enabled) {
                        break loop;
                    }

                    try {
                        totalRunningTime.pause();
                        wait();
                    } catch (InterruptedException ex) {
                        totalRunningTime.resume();
                    }
                } else {
                    currentTask = (Runnable) tasks.get(0);
                }
            }

            if (currentTask == null) {
                continue;
            }

            Throwable throwable = null;
            count++;
            currentTaskTime.restart();

            try {
                currentTask.run();
            } catch (Throwable th) {
                throwable = th;
            } finally {
                currentTaskTime.pause();

                synchronized (this) {
                    Iterator i = listeners.iterator();

                    while (i.hasNext()) {
                        ((Listener) i.next()).taskPerformed(currentTask,
                            currentTaskTime.elapsed(), throwable);
                    }

                    currentTaskTime.reset(-1);
                    tasks.remove(0);
                }
            }
        }
    }

    /**
     * ends the thread after the last queued task ended; the effect is
     * irreversible
     */
    public synchronized void endAfterLast() {
        enabled = false;

        if (isIdle()) {
            notifyAll();
        }
    }

    /**
     * ends the thread after the current task ended; the effect is
     * irreversible
     */
    public synchronized void endAfterCurrent() {
        endNow = true;

        if (isIdle()) {
            notifyAll();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean willEndAfterCurrent() {
        return endNow;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean willEndAfterLast() {
        return !enabled;
    }

/**
     * listens for tasks run by a ThreadWorker to be finished
     */
    public static interface Listener extends EventListener {
        /**
         * called after ThreadWorker has finished running the task.
         * The current task is not yet removed from the queue when this method
         * is called. The running thread has ownership over the ThreadWorker
         * when this method is called.
         *
         * @param task the task that just finished running
         * @param timeTaken the time (in milliseconds) it took to run the task
         * @param exceptionIfAny if an Exception was thrown during task
         *        execution, it will be held by this parameter. This should be
         *        null if the task ended normally.
         */
        void taskPerformed(Runnable task, long timeTaken,
            Throwable exceptionIfAny);
    }
}
