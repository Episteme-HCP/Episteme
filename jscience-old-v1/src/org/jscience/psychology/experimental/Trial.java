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

package org.jscience.psychology.experimental;

import java.util.Vector;


/**
 * A class representing a specific event in a task. This should be used
 * when the task has a repeated measure.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Trial extends Object {
    /** DOCUMENT ME! */
    private Vector variables;

    /** DOCUMENT ME! */
    private Task task;

/**
     * Creates a new Trial object.
     *
     * @param variables DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Trial(Vector variables) {
        if ((variables != null) && (variables.size() > 0)) {
            this.variables = variables;
        } else {
            throw new IllegalArgumentException(
                "Variables of a trial must be a non empty vector.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getVariables() {
        return variables;
    }

    //automatically set by the system when added to a task
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Task getTask() {
        return task;
    }

    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     */
    protected void setTask(Task task) {
        this.task = task;
    }

    //automatically set by the system when task added to a subject
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Subject getSubject() {
        return task.getSubject();
    }

    //for example show a black screen then something on the screen for 5 seconds
    /**
     * DOCUMENT ME!
     */
    public abstract void doTrial();
}
