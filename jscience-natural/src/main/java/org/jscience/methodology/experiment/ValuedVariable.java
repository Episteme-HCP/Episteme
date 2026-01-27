/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
