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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single execution or measurement event within an experimental task.
 * Typically used for repeated measures studies.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class Trial implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Variable> variables;
    private Task task;

    /**
     * Creates a new experimental trial with associated variables.
     *
     * @param variables the list of variables for this trial
     * @throws IllegalArgumentException if variables list is empty
     * @throws NullPointerException if variables list is null
     */
    protected Trial(List<Variable> variables) {
        Objects.requireNonNull(variables, "Variables list cannot be null");
        if (variables.isEmpty()) {
            throw new IllegalArgumentException("Variables list must not be empty");
        }
        this.variables = new ArrayList<>(variables);
    }

    /**
     * Returns the variables monitored or manipulated during this trial.
     * @return the list of variables
     */
    public List<Variable> getVariables() {
        return variables;
    }

    /**
     * Returns the parent task this trial belongs to.
     * @return the associated task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the parent task for this trial.
     * @param task the parent task
     */
    protected void setTask(Task task) {
        this.task = task;
    }

    /**
     * Returns the experimental subject associated with this trial.
     * @return the subject, or null if no parent task is set
     */
    public Subject getSubject() {
        return (task != null) ? task.getSubject() : null;
    }

    /**
     * Executes the experimental trial logic.
     */
    public abstract void doTrial();
}
