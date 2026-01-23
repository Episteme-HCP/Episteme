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
import org.jscience.util.Named;

/**
 * An abstract representation of a specific task within a psychological experiment.
 * Implementations should define the actual experimental logic in {@link #doTask()}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class Task implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String description;
    private final List<Trial> trials;
    private Subject subject;

    /**
     * Creates a new experimental task.
     *
     * @param name        the identifying name of the task
     * @param description a brief description of the task's purpose
     * @param trials      the list of trials to be executed (must not be empty)
     * @throws IllegalArgumentException if name, description, or trials are invalid
     * @throws NullPointerException     if any argument is null
     */
    protected Task(String name, String description, List<Trial> trials) {
        Objects.requireNonNull(name, "Task name cannot be null");
        Objects.requireNonNull(description, "Task description cannot be null");
        Objects.requireNonNull(trials, "Trials list cannot be null");

        if (name.isEmpty() || description.isEmpty() || trials.isEmpty()) {
            throw new IllegalArgumentException("Arguments cannot be empty");
        }

        this.name = name;
        this.description = description;
        this.trials = new ArrayList<>(trials);
        
        for (Trial trial : this.trials) {
            trial.setTask(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the description of this task.
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the list of trials associated with this task.
     * @return the trials list
     */
    public List<Trial> getTrials() {
        return trials;
    }

    /**
     * Returns the subject performing this task, if assigned.
     * @return the experimental subject
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Sets the subject for this task.
     * @param subject the subject to set
     */
    protected void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * Executes the experimental task logic.
     */
    public abstract void doTask();
}
