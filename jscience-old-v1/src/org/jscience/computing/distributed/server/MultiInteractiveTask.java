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

/*
 * MultiInteractiveTask.java
 *
 * Created on 20 April 2001, 00:06
 */
package org.jscience.computing.distributed.server;

import org.jscience.computing.distributed.InteractiveTask;

import java.util.Vector;


/**
 * This allows many InteractiveTasks to be bundled togather as one
 * InteractiveTask.
 * <p/>
 * <p/>
 * All tasks will be started simultaneously and the task as a whole will only
 * finish when all tasks have completed.
 * </p>
 * <p/>
 * <p/>
 * For more flexibility in managing tasks dynamically use
 * FineGrainedInteractiveTasks.
 * </p>
 *
 * @author Michael Garvie
 */
public class MultiInteractiveTask implements InteractiveTask {
    /**
     * DOCUMENT ME!
     */
    private Vector tasks;

    /**
     * DOCUMENT ME!
     */
    private Vector results = new Vector();

    /**
     * DOCUMENT ME!
     */
    private Vector threads = new Vector();

    /**
     * Creates new MultiInteractiveTask
     *
     * @param tasks Vector containing list of tasks to run simultaneously.
     */
    public MultiInteractiveTask(Vector tasks) {
        this.tasks = tasks;
    }

    /**
     * Used to get output from the task
     *
     * @param params If not null, then must be a Vector with what must be sent
     *               to each task.
     * @return a Vector holding what each task's get method returned.
     */
    public Object get(Object params) {
        Vector rv = new Vector();

        for (int tl = 0; tl < tasks.size(); tl++) {
            Object thisTaskParams = null;

            if (params != null) {
                thisTaskParams = ((Vector) params).elementAt(tl);
            }

            rv.add(((InteractiveTask) tasks.elementAt(tl)).get(thisTaskParams));
        }

        return rv;
    }

    /**
     * Used to send input to the task
     *
     * @param paramsAndWhat if not null, must be a Vector with what must be
     *                      sent to each task
     */
    public void set(Object paramsAndWhat) {
        for (int tl = 0; tl < tasks.size(); tl++) {
            Object thisTaskParams = null;

            if (paramsAndWhat != null) {
                thisTaskParams = ((Vector) paramsAndWhat).elementAt(tl);
            }

            ((InteractiveTask) tasks.elementAt(tl)).set(thisTaskParams);
        }
    }

    /**
     * Starts all tasks in their own thread
     *
     * @param params DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws InterruptedException DOCUMENT ME!
     */
    public Object run(Object params) throws InterruptedException {
        for (int tl = 0; tl < tasks.size(); tl++) {
            results.add(null);

            final int ti = tl;
            Thread t = new Thread() {
                public void run() {
                    try {
                        results.add(ti,
                                ((InteractiveTask) tasks.elementAt(ti)).run(
                                        null));
                    } catch (InterruptedException e) {
                        // should be normal..
                    }
                }
            };

            t.start();
            threads.add(t);
        }

        for (int tl = 0; tl < tasks.size(); tl++) {
            if (Thread.currentThread().isInterrupted()) {
                getThread(tl).interrupt();
            } else {
                try {
                    System.out.println("Waiting for task " + tl);
                    ((Thread) threads.elementAt(tl)).join();
                } catch (InterruptedException e) {
                    getThread(tl).interrupt();
                }
            }
        }

        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private InteractiveTask getTask(int pos) {
        if (tasks.size() <= pos) {
            tasks.setSize(pos + 1);
            threads.setSize(pos + 1);
            results.setSize(pos + 1);
        }

        return ((InteractiveTask) tasks.elementAt(pos));
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private Thread getThread(int pos) {
        return ((Thread) threads.elementAt(pos));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rv = "";

        for (int i = 0; i < tasks.size(); i++) {
            rv += ("\nTask " + i + ":\n" + tasks.elementAt(i) + "\n");
        }

        return rv;
    }
}
