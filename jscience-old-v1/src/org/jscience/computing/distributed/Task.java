/*
 * Task.java
 *
 * Created on 18 April 2001, 12:49
 */
package org.jscience.computing.distributed;

import java.io.Serializable;


/**
 * A Task represents a single task that takes a paramater when it starts and
 * returns a value when it terminates, both can be vectors..
 *
 * @author Michael Garvie
 */
public interface Task extends Serializable {
    /**
     * Totally customizable method with code to be executed by the the
     * interactive task clients.
     *
     * @param params Generic parameters to start the run method with. The
     *        implemented InteractiveTaskClient will always start the run
     *        method with a null parameter so any configuring of the task must
     *        be done previous to the run method being executed.
     *
     * @return Final result of the run method.  This could be the solution to a
     *         problem for example.
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public Object run(Object params) throws InterruptedException;
}
