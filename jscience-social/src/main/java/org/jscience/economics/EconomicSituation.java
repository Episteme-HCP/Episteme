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
import org.jscience.sociology.Situation;

/**
 * Represents a social situation involving the interaction of people around 
 * economic resources and activities.
 * 
 * <p>This class extends {@link Situation} to provide a base for modeling
 * economic contexts where individuals participate as {@link EconomicAgent}s.
 * Subclasses like {@link WorkSituation} provide more specialized behavior.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @version 6.0, July 21, 2014
 * @see Situation
 * @see EconomicAgent
 * @see WorkSituation
 */
public class EconomicSituation extends Situation {

    /**
     * Creates a new economic situation.
     *
     * @param name the name of this economic situation.
     * @param comments additional description or comments about the situation.
     */
    public EconomicSituation(String name, String comments) {
        super(name, comments);
    }

    /**
     * Adds an individual as an economic agent participating in this situation.
     * 
     * <p>This method creates a new {@link EconomicAgent} role for the 
     * individual and associates them with this economic context.</p>
     *
     * @param individual the individual to add as an economic agent.
     */
    public void addEconomicAgent(Individual individual) {
        addRole(new EconomicAgent(individual, this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRole(Role role) {
        super.addRole(role);
    }
}
