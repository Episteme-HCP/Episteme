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

/**
 * A class representing a psychology variable (whether intergroups or
 * intragroups). A valued variable is an actual measure for a variable in a
 * given trial of a given task for a given subject.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ValuedVariable extends Object {
    /** DOCUMENT ME! */
    private Variable variable;

    /** DOCUMENT ME! */
    private Object value;

    /** DOCUMENT ME! */
    private Trial trial;

    /** DOCUMENT ME! */
    private Task task;

    /** DOCUMENT ME! */
    private Subject subject;

/**
     * Creates a new ValuedVariable object.
     *
     * @param variable DOCUMENT ME!
     * @param value    DOCUMENT ME!
     * @param trial    DOCUMENT ME!
     * @param task     DOCUMENT ME!
     * @param subject  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ValuedVariable(Variable variable, Object value, Trial trial,
        Task task, Subject subject) {
        if ((variable != null) && (value != null) && (trial != null) &&
                (task != null) && (subject != null)) {
            this.variable = variable;
            this.value = value;
            this.trial = trial;
            this.task = task;
            this.subject = subject;
        } else {
            throw new IllegalArgumentException(
                "The ValuedVariable constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Variable getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Trial getTrial() {
        return trial;
    }

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
     * @return DOCUMENT ME!
     */
    public Subject getSubject() {
        return subject;
    }
}
