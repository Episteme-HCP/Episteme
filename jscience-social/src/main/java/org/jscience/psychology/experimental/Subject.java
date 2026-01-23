package org.jscience.psychology.experimental;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;

import java.util.*;


/**
 * A class representing an animal or human subject that takes part in an
 * experiment.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Subject extends Role {
    public final static int READY = -1;
    public final static int INVALID = 0;
    public final static int VALID = 1;

    private int state;
    private String identifier;
    private Vector<Task> tasks;
    private Set<ValuedVariable> valuedVariables;

    public Subject(Individual individual, Experiment experiment, String identifier, Vector<Task> tasks) {
        super(individual, "Subject", experiment, Role.SERVER);
        
        this.identifier = Objects.requireNonNull(identifier, "Identifier cannot be null");
        this.tasks = Objects.requireNonNull(tasks, "Tasks Vector cannot be null");
        
        if (identifier.isEmpty() || tasks.isEmpty()) {
            throw new IllegalArgumentException("Identifier and tasks cannot be empty.");
        }

        this.state = READY;
        this.valuedVariables = new HashSet<>();
        
        for (Task task : tasks) {
            task.setSubject(this);
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (this.state == READY) {
            if ((state == VALID) || (state == INVALID)) {
                this.state = state;
            } else {
                throw new IllegalArgumentException(
                    "Subject state can only be set to VALID or INVALID.");
            }
        } else {
            throw new IllegalArgumentException(
                "setState(int state) can only be called once.");
        }
    }

    public boolean isValid() {
        return state == VALID;
    }

    public Vector<Task> getTasks() {
        return tasks;
    }

    public void addValuedVariable(ValuedVariable valuedVariable) {
        valuedVariables.add(Objects.requireNonNull(valuedVariable));
    }

    public void addValuedVariables(Collection<ValuedVariable> valuedVariables) {
        this.valuedVariables.addAll(Objects.requireNonNull(valuedVariables));
    }

    public Set<ValuedVariable> getAllValuedVariables() {
        if (isValid()) {
            return Collections.unmodifiableSet(valuedVariables);
        } else {
            throw new IllegalStateException(
                "You can get the subjects ValuedVariables only when isValid is true.");
        }
    }
}
}
