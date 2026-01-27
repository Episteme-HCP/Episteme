/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.methodology.experiment;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jscience.biology.Individual;
import org.jscience.earth.Place;
import org.jscience.util.Named;
import org.jscience.util.Positioned;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A class representing an animal or human subject that takes part in an experiment.
 * <p>
 * In a scientific experiment, the subject is the individual being observed or tested.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Subject implements Named, Identified<Identification>, Positioned<Place>, Serializable {

    private static final long serialVersionUID = 1L;

    public enum State {
        READY, INVALID, VALID
    }

    @Id
    private final Identification id;

    @Attribute
    private String name;

    @Relation
    private final Individual individual;

    @Attribute
    private State state;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Task> tasks;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<ValuedVariable> valuedVariables;

    @Attribute
    private final Instant startTime;

    /**
     * Creates a new Subject for the given individual.
     * @param individual The individual participating in the experiment.
     */
    public Subject(Individual individual) {
        this.id = new org.jscience.util.identity.UUIDIdentification(java.util.UUID.randomUUID());
        this.individual = Objects.requireNonNull(individual, "Individual cannot be null");
        this.name = individual.toString();
        this.tasks = new ArrayList<>();
        this.state = State.READY;
        this.valuedVariables = new HashSet<>();
        this.startTime = Instant.now();
    }

    /**
     * Creates a new Subject for the given individual and tasks.
     * @param individual The individual participating in the experiment.
     * @param tasks The tasks assigned to this subject.
     */
    public Subject(Individual individual, List<Task> tasks) {
        this(individual);
        if (tasks != null) {
            this.tasks.addAll(tasks);
            for (Task task : tasks) {
                task.setSubject(this);
            }
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

    public void setName(String name) {
        this.name = name;
    }

    public Individual getIndividual() {
        return individual;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isValid() {
        return state == State.VALID;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(Task task) {
        if (task != null) {
            tasks.add(task);
            task.setSubject(this);
        }
    }

    public void addValuedVariable(ValuedVariable valuedVariable) {
        valuedVariables.add(Objects.requireNonNull(valuedVariable));
    }

    public void addValuedVariables(Collection<ValuedVariable> variables) {
        this.valuedVariables.addAll(Objects.requireNonNull(variables));
    }

    public Set<ValuedVariable> getAllValuedVariables() {
        return Collections.unmodifiableSet(valuedVariables);
    }

    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public Place getPosition() {
        return individual != null ? individual.getPosition() : null;
    }

    @Override
    public String toString() {
        return String.format("Subject{name='%s', state=%s}", name, state);
    }
}
