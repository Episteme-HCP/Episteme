/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
 * @author Gemini AI (Google DeepMind)
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
