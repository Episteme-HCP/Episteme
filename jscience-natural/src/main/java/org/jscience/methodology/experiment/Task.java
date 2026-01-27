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
import org.jscience.util.Named;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * An abstract representation of a specific task within an experimental study.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public abstract class Task implements Named, Identified<Identification>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final String name;

    @Attribute
    private final String description;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Trial> trials;

    private Subject subject;

    protected Task(String name, String description, List<Trial> trials) {
        this.id = new org.jscience.util.identity.UUIDIdentification(java.util.UUID.randomUUID());
        this.name = Objects.requireNonNull(name, "Task name cannot be null");
        this.description = Objects.requireNonNull(description, "Task description cannot be null");
        this.trials = new ArrayList<>(Objects.requireNonNull(trials, "Trials list cannot be null"));
        
        for (Trial trial : this.trials) {
            trial.setTask(this);
        }
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Trial> getTrials() {
        return Collections.unmodifiableList(trials);
    }

    public Subject getSubject() {
        return subject;
    }

    protected void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * Executes the task.
     */
    public abstract void doTask();
}
