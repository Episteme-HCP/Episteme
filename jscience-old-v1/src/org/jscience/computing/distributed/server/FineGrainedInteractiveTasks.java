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
 * FineGrainedInteractiveTasks.java
 *
 * Created on 20 April 2001, 23:38
 */
package org.jscience.computing.distributed.server;

import org.jscience.computing.distributed.InteractiveTask;

import java.util.Vector;


/**
 * Provides an abstraction to run many interactive tasks concurrently so that
 * if one stops before others it can be replaced by a new task. It also
 * enables tasks to be replaced at any time by the server.
 * <p/>
 * <p/>
 * This is achieved by the server being informed of which tasks are still alive
 * when it calls the get method and by the server sending out the same or new
 * tasks through the set method. If the same task as the current one is sent
 * then it is ignored. WARNING: Task equality is based on the toString()
 * method.
 * </p>
 * <p/>
 * <p/>
 * If a task is sent that the client has previously completed there is an
 * option to ignore this task, but the current implementation will run this
 * task anyway since in many cases this is not a useless functionality.  To
 * reenable this option see line marked with !!.
 * </p>
 * <p/>
 * <p/>
 * This assumes InteractiveTask implementations will be well behaved and check
 * their currentThread.isInterrupted() method.
 * </p>
 *
 * @author Michael Garvie
 */
public class FineGrainedInteractiveTasks implements InteractiveTask {
    /**
     * Index within the Vector sent to the set method of this task for which
     * task control information is sent.  This information will define if the
     * current task should be continued or a new one started.
     */
    public static final int CONTROL_INDEX = 1;

    /**
     * Index within the Vector sent to the set method of this task for which
     * task data information is sent.  This is data sent to the set method of
     * each subtask
     */
    public static final int DATA_INDEX = 0;

    /**
     * Vector of interactive tasks currently being run
     */
    private Vector tasks;

    /**
     * Vector of threads where these tasks are being run
     */
    private Vector taskThreads = new Vector();

    /**
     * Will store the results return by all tasks through their run method
     */
    private Vector results = new Vector();

    /* Vector of hashCodes of tasks we have run or are running.  Used so that we
     * don't re run finished tasks.
    */

    /**
     * DOCUMENT ME!
     */
    private Vector knownTasks = new Vector();

    /**
     * Creates new FineGrainedInteractiveTasks
     */
    public FineGrainedInteractiveTasks() {
        tasks = new Vector();
    }

    /**
     * Creates new FineGrainedInteractiveTasks
     *
     * @param tasks
     */
    public FineGrainedInteractiveTasks(Vector tasks) {
        this.tasks = tasks;

        for (int vl = 0; vl < tasks.size(); vl++) {
            // reenable following line to avoid running previously ran tasks
            //**!!knownTasks.add( new Integer( tasks.elementAt( vl ).toString().hashCode() ) );
            // this should be moved to where task is finished anyway...
        }
    }

    /**
     * Used to get output from the tasks plus a Boolean saying if they're
     * alive.
     *
     * @param params can be null if this task doesn't need to know WHAT it has
     *               to output
     * @return Vector of (Object, Boolean) pairs (which are Vectors of size 2)
     */
    public Object get(Object params) {
        Vector rv = new Vector();

        for (int tl = 0; tl < tasks.size(); tl++) {
            Vector pair = new Vector();
            Object thisTaskParams = null;

            if (params != null) {
                thisTaskParams = ((Vector) params).elementAt(tl);
            }

            pair.add(DATA_INDEX, getTask(tl).get(thisTaskParams));
            pair.add(CONTROL_INDEX, new Boolean(getThread(tl).isAlive()));
            rv.add(pair);
        }

        return rv;
    }

    /**
     * Used to send input to each of the tasks or to replace these tasks. To
     * replace a task a new InteractiveTask must be sent in the pairs, this
     * method will try and work out if the task has been done before and if so
     * ignore it (currently not functional because repeating tasks is useful).
     * Task equality is based on their toString() method's output.
     *
     * @param paramsAndWhat must be a vector of (Object, InteractiveTask) pairs
     *                      (vector of 2) with what must be sent to each task and a new task
     *                      if necesary.
     */
    public void set(Object paramsAndWhat) {
        for (int tl = 0; tl < tasks.size(); tl++) {
            Vector pair = (Vector) ((Vector) paramsAndWhat).elementAt(tl);
            Object thisTaskParams = pair.elementAt(DATA_INDEX);
            InteractiveTask newTask = (InteractiveTask) pair.elementAt(CONTROL_INDEX);

            if ((newTask == null) ||
                    (newTask.toString().equals(getTask(tl).toString()) &&
                            getThread(tl).isAlive())) {
                // we're handling same old task
                getTask(tl).set(thisTaskParams);
            } else if (knownTasks.contains(
                    new Integer(newTask.toString().hashCode()))) {
                //we're handling old previous task
                // do nothing
            } else {
                // we have a new task and must replace the old one togather with its thread
                Thread oldTaskThread = getThread(tl);

                if (oldTaskThread != null) {
                    oldTaskThread.interrupt();
                }

                tasks.setElementAt(newTask, tl);

                Thread newThread = spawnThread(tl);
                newThread.start();
                taskThreads.setElementAt(newThread, tl);
            }
        }
    }

    /**
     * Starts all tasks in their own thread
     *
     * @param params Vector containing initial parameters to send to each run
     *               method of encapsulated tasks
     * @return Vector containing values returned by all tasks that were run. If
     *         multiple tasks were run in a single index then the last one
     *         replaces previous ones.  This method only returns when all
     *         tasks have completed execution of their own run method.
     * @throws InterruptedException If any task is interruped.
     */
    public Object run(Object params) throws InterruptedException {
        try {
            for (int tl = 0; tl < tasks.size(); tl++) {
                results.add(null);

                Thread t = spawnThread(tl);
                t.start();
                taskThreads.add(t);
            }

            boolean survivors = true;

            while (survivors) {
                Thread.currentThread().sleep(1000 * 10);

                survivors = false;

                for (int tl = 0; tl < taskThreads.size(); tl++) {
                    survivors |= getThread(tl).isAlive();
                }
            }
        } catch (InterruptedException e) {
            System.out.println(
                    "FGIT - Fine Grained IT Interrupted - Interrupting down the tree.");

            for (int tl = 0; tl < taskThreads.size(); tl++) {
                getThread(tl).interrupt();
            }
        }

        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ix DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private Thread spawnThread(int ix) {
        final int ti = ix;
        Thread t = new Thread() {
            public void run() {
                try {
                    results.setElementAt(getTask(ti).run(null), ti);
                } catch (InterruptedException e) {
                    // should be normal..
                }
            }
        };

        return t;
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
            taskThreads.setSize(pos + 1);
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
        return ((Thread) taskThreads.elementAt(pos));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rv = "FineGrainedInteractiveTasks with Tasks = " + tasks;

        return rv;
    }
}
