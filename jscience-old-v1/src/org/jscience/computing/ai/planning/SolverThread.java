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

package org.jscience.computing.ai.planning;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * The thread that invokes Planner to solve a planning problem. The only
 * reason to have another thread to solve problems rather than using the main
 * thread to do so is that, in some platforms, the command line parameters
 * that are supposed to change the stack size work only for the threads other
 * than the main thread.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class SolverThread extends Thread {
    /**
     * <code>true</code> if the thread is to return all the possible
     * plans, <code>false</code> if the thread is to return only the the first
     * plan found.
     */
    private boolean findAll;

    /** The task list to be achieved. */
    private TaskList tl;

/**
     * To initialize this thread.
     *
     * @param tlIn      the task list to be achieved by this thread.
     * @param findAllIn <code>true</code> if the thread is to return all the
     *                  possible plans, <code>false</code> if the function is to return
     *                  only the the first plan found.
     */
    public SolverThread(TaskList tlIn, boolean findAllIn) {
        tl = tlIn;
        findAll = findAllIn;
    }

    /**
     * The function that is called when this thread is invoked.
     */
    public void run() {
        //-- Get the current time.
        long t1 = new GregorianCalendar().getTimeInMillis();

        //-- Solve the planning problem.
        LinkedList p = Planner.findPlans(tl, findAll);

        //-- Get the current time again, to calculate the time used.
        long t2 = new GregorianCalendar().getTimeInMillis();

        System.out.println();
        System.out.println(p.size() + " plan(s) were found:");
        System.out.println();

        //-- Print the plans found.
        Iterator e = p.iterator();
        int i = 0;

        while (e.hasNext()) {
            System.out.println("Plan #" + ++i + ":");
            System.out.println((Plan) e.next());
        }

        System.out.println("Time Used = " + ((t2 - t1) / 1000.0));
        System.out.println();
    }
}
