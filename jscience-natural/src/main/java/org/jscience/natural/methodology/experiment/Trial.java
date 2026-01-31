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

package org.jscience.natural.methodology.experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.Identified;

import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a single execution or measurement event within an experimental task.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public abstract class Trial implements Identified<Identification>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final List<Variable> variables;

    private Task task;

    protected Trial(List<Variable> variables) {
        this.id = new org.jscience.core.util.identity.UUIDIdentification(java.util.UUID.randomUUID());
        this.variables = new ArrayList<>(Objects.requireNonNull(variables, "Variables list cannot be null"));
    }

    @Override
    public Identification getId() {
        return id;
    }

    public List<Variable> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    public Task getTask() {
        return task;
    }

    protected void setTask(Task task) {
        this.task = task;
    }

    public Subject getSubject() {
        return (task != null) ? task.getSubject() : null;
    }

    /**
     * Executes the trial.
     */
    public abstract void doTrial();
}

