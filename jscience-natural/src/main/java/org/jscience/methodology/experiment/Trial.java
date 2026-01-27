/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.methodology.experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;

import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

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
        this.id = new org.jscience.util.identity.UUIDIdentification(java.util.UUID.randomUUID());
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
