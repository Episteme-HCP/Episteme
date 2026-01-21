/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import java.util.Set;

/**
 * An entity capable of performing tasks by consuming resources.
 * 
 * @author <a href="mailto:jacob.dixon@jscience.org">Jacob Dixon</a>
 * @version 6.0, July 21, 2014
 */
public interface TaskProcessor {
    
    /**
     * Returns the set of resources available to this processor.
     * 
     * @return the set of available resources.
     */
    Set<Resource> getResources();

    /**
     * Consumes the necessary resources to perform the given task.
     *
     * @param task the task to perform.
     * @return {@code true} if resources were successfully consumed; 
     *         {@code false} otherwise.
     */
    boolean consumeResources(Task task);
}
