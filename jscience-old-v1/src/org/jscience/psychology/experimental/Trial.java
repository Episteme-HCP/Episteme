package org.jscience.psychology.experimental;

import java.util.Vector;


/**
 * A class representing a specific event in a task. This should be used
 * when the task has a repeated measure.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Trial extends Object {
    /** DOCUMENT ME! */
    private Vector variables;

    /** DOCUMENT ME! */
    private Task task;

/**
     * Creates a new Trial object.
     *
     * @param variables DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Trial(Vector variables) {
        if ((variables != null) && (variables.size() > 0)) {
            this.variables = variables;
        } else {
            throw new IllegalArgumentException(
                "Variables of a trial must be a non empty vector.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getVariables() {
        return variables;
    }

    //automatically set by the system when added to a task
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
     * @param task DOCUMENT ME!
     */
    protected void setTask(Task task) {
        this.task = task;
    }

    //automatically set by the system when task added to a subject
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Subject getSubject() {
        return task.getSubject();
    }

    //for example show a black screen then something on the screen for 5 seconds
    /**
     * DOCUMENT ME!
     */
    public abstract void doTrial();
}
