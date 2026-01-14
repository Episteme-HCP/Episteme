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

import org.jscience.util.Named;

import java.util.Iterator;
import java.util.Vector;


/**
 * A class representing a specific task in an experiment. You actually have
 * to subclass it to provide with your actual implementation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Task extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String description;

    /** DOCUMENT ME! */
    private Vector trials;

    /** DOCUMENT ME! */
    private Subject subject;

    //the trials should be a Vector of at least one element
    /**
     * Creates a new Task object.
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param trials DOCUMENT ME!
     */
    public Task(String name, String description, Vector trials) {
        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (description != null) &&
                (description.length() > 0) && (trials != null) &&
                (trials.size() > 0)) {
            iterator = trials.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Trial;
            }

            if (valid) {
                this.name = name;
                this.description = description;
                this.trials = trials;
            } else {
                throw new IllegalArgumentException(
                    "The trials Vector must contain only Trials.");
            }

            for (int i = 0; i < trials.size(); i++) {
                ((Trial) trials.elementAt(i)).setTask(this);
            }

            this.subject = null;
        } else {
            throw new IllegalArgumentException(
                "The Task constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getTrials() {
        return trials;
    }

    //automatically set by the system when task added to a subject
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * DOCUMENT ME!
     *
     * @param subject DOCUMENT ME!
     */
    protected void setSubject(Subject subject) {
        this.subject = subject;
    }

    //normally you launch here each trial one after another
    /**
     * DOCUMENT ME!
     */
    public abstract void doTask();
}
