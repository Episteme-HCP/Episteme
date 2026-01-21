/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import org.jscience.biology.Individual;
import org.jscience.sociology.Role;

/**
 * Represents a work situation where individuals interact around a common 
 * economic activity, typically at dedicated workplaces.
 * 
 * <p>This class extends {@link EconomicSituation} to model employment
 * relationships and workplace dynamics. Use this class when you need
 * finer control over work-related situations than provided by 
 * {@code org.jscience.sociology.Situations.WORKING}.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @version 6.0, July 21, 2014
 * @see EconomicSituation
 * @see Worker
 * @see Organization
 */
public class WorkSituation extends EconomicSituation {

    /**
     * Creates a new work situation.
     * 
     * <p>Use the organization name (or a part of it for large organizations)
     * as the situation name for clarity.</p>
     *
     * @param name the name of the work situation, typically the organization name.
     * @param comments additional comments or description of this work situation.
     */
    public WorkSituation(String name, String comments) {
        super(name, comments);
    }

    /**
     * Adds an individual as a worker to this work situation.
     * 
     * <p>This method creates a new {@link Worker} role for the individual
     * and associates them with the specified function and organization.</p>
     *
     * @param individual the individual to add as a worker.
     * @param function the job function or role title of the worker.
     * @param organization the organization employing the worker.
     */
    public void addWorker(Individual individual, String function,
            Organization organization) {
        addRole(new Worker(individual, this, function, organization));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRole(Role role) {
        super.addRole(role);
    }
}
