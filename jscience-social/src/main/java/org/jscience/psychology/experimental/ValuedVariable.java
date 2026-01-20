package org.jscience.psychology.experimental;

/**
 * A class representing a psychology variable (whether intergroups or
 * intragroups). A valued variable is an actual measure for a variable in a
 * given trial of a given task for a given subject.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ValuedVariable extends Object {
    /** DOCUMENT ME! */
    private Variable variable;

    /** DOCUMENT ME! */
    private Object value;

    /** DOCUMENT ME! */
    private Trial trial;

    /** DOCUMENT ME! */
    private Task task;

    /** DOCUMENT ME! */
    private Subject subject;

/**
     * Creates a new ValuedVariable object.
     *
     * @param variable DOCUMENT ME!
     * @param value    DOCUMENT ME!
     * @param trial    DOCUMENT ME!
     * @param task     DOCUMENT ME!
     * @param subject  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ValuedVariable(Variable variable, Object value, Trial trial,
        Task task, Subject subject) {
        if ((variable != null) && (value != null) && (trial != null) &&
                (task != null) && (subject != null)) {
            this.variable = variable;
            this.value = value;
            this.trial = trial;
            this.task = task;
            this.subject = subject;
        } else {
            throw new IllegalArgumentException(
                "The ValuedVariable constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Variable getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Trial getTrial() {
        return trial;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Task getTask() {
        return task;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Subject getSubject() {
        return subject;
    }
}
