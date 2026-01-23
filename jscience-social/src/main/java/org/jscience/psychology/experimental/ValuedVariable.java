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
import java.util.Objects;

/**
 * Represents a specific measurement or assignment for a {@link Variable} in a given experimental context.
 * Binds a variable definition to a value, trial, task, and subject.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class ValuedVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Variable variable;
    private final Object value;
    private final Trial trial;
    private final Task task;
    private final Subject subject;

    /**
     * Creates a new measurement result.
     *
     * @param variable the variable being measured
     * @param value    the measured or assigned value
     * @param trial    the specific trial this measurement belongs to
     * @param task     the task this measurement belongs to
     * @param subject  the subject performance the task
     * @throws NullPointerException if any argument is null
     */
    public ValuedVariable(Variable variable, Object value, Trial trial, Task task, Subject subject) {
        this.variable = Objects.requireNonNull(variable, "Variable cannot be null");
        this.value = Objects.requireNonNull(value, "Value cannot be null");
        this.trial = Objects.requireNonNull(trial, "Trial cannot be null");
        this.task = Objects.requireNonNull(task, "Task cannot be null");
        this.subject = Objects.requireNonNull(subject, "Subject cannot be null");
    }

    /**
     * Returns the variable definition.
     * @return the associated variable
     */
    public Variable getVariable() {
        return variable;
    }

    /**
     * Returns the measured value.
     * @return the measurement value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the trial where the measurement was taken.
     * @return the associated trial
     */
    public Trial getTrial() {
        return trial;
    }

    /**
     * Returns the task where the measurement was taken.
     * @return the associated task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Returns the subject who performed the task.
     * @return the associated subject
     */
    public Subject getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return variable.getName() + " = " + value;
    }
}
