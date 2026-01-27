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

package org.jscience.methodology.experiment;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a specific measurement or assignment for a Variable during an experiment.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class ValuedVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Relation
    private final Variable variable;

    @Attribute
    private final Object value;

    @Relation
    private final Trial trial;

    @Relation
    private final Task task;

    @Relation
    private final Subject subject;

    public ValuedVariable(Variable variable, Object value, Trial trial, Task task, Subject subject) {
        this.variable = Objects.requireNonNull(variable);
        this.value = Objects.requireNonNull(value);
        this.trial = Objects.requireNonNull(trial);
        this.task = Objects.requireNonNull(task);
        this.subject = Objects.requireNonNull(subject);
    }

    public Variable getVariable() { return variable; }
    public Object getValue() { return value; }
    public Trial getTrial() { return trial; }
    public Task getTask() { return task; }
    public Subject getSubject() { return subject; }

    @Override
    public String toString() {
        return variable.getName() + " = " + value;
    }
}
