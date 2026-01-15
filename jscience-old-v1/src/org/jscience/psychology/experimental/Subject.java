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
    /** DOCUMENT ME! */
    public final static int READY = -1; //the subject has been built in the design, he is ready for the experimental tasks

    /** DOCUMENT ME! */
    public final static int INVALID = 0; //the subject could not complete the whole group of tasks and has been tagged as invalid

    /** DOCUMENT ME! */
    public final static int VALID = 1; //the subject has completed the experiment and we can analyze his data

    /** DOCUMENT ME! */
    private int state;

    /** DOCUMENT ME! */
    private String identifier;

    /** DOCUMENT ME! */
    private Vector tasks;

    /** DOCUMENT ME! */
    private Set valuedVariables;

    //identifier should be unique, (for example calling hashCode());
    /**
     * Creates a new Subject object.
     *
     * @param individual DOCUMENT ME!
     * @param experiment DOCUMENT ME!
     * @param tasks DOCUMENT ME!
     */
    public Subject(Individual individual, Experiment experiment, Vector tasks) {
        super(individual, "Subject", experiment, Role.SERVER);

        Iterator iterator;
        boolean valid;

        if ((identifier != null) && (identifier.length() > 0) &&
                (tasks != null) && (tasks.size() > 0)) {
            iterator = tasks.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Task;
            }

            if (valid) {
                this.identifier = identifier;
                state = READY;
                this.tasks = tasks;
            } else {
                throw new IllegalArgumentException(
                    "The tasks Vector must contain only Tasks.");
            }

            for (int i = 0; i < tasks.size(); i++) {
                ((Task) tasks.elementAt(i)).setSubject(this);
            }

            valuedVariables = new HashSet();
        } else {
            throw new IllegalArgumentException(
                "The Subject constructor can't have null or empty arguments.");
        }
    }

    //each subject should be identified by this human readable identifier
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getState() {
        return state;
    }

    //you should call this once, when the experiment has ended for the subject
    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
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

    //sometimes the subject has to be taken out of the experiment
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isValid() {
        return state == VALID;
    }

    //each task in the vector is ordered from the first to the last one
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getTasks() {
        return tasks;
    }

    /**
     * DOCUMENT ME!
     *
     * @param valuedVariable DOCUMENT ME!
     */
    public void addValuedVariable(ValuedVariable valuedVariable) {
        valuedVariables.add(valuedVariable);
    }

    //the vector is actually made of ValuedVariables
    /**
     * DOCUMENT ME!
     *
     * @param valuedVariables DOCUMENT ME!
     */
    public void addValuedVariables(Vector valuedVariables) {
        Enumeration enumeration;
        boolean valid;

        enumeration = valuedVariables.elements();
        valid = true;

        while (enumeration.hasMoreElements() && valid) {
            valid = enumeration.nextElement() instanceof ValuedVariable;
        }

        if (valid) {
            this.valuedVariables.addAll(valuedVariables);
        } else {
            throw new IllegalArgumentException(
                "A Vector of valued variables shoud contain only ValuedVariables.");
        }
    }

    //you should call this method only when the subject isValid()
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllValuedVariables() {
        if (isValid()) {
            return valuedVariables;
        } else {
            throw new IllegalArgumentException(
                "You can get the subjects ValuedVaribles only when isValid it true.");
        }
    }
}
