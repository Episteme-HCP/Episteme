/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.economics;

import org.episteme.natural.biology.Individual;
import org.episteme.social.sociology.Role;

/**
 * Represents a work situation where individuals interact around a common 
 * economic activity, typically at dedicated workplaces.
 * 
 * <p>This class extends {@link EconomicSituation} to model employment
 * relationships and workplace dynamics. Use this class when you need
 * finer control over work-related situations than provided by 
 * {@code org.episteme.social.sociology.Situations.WORKING}.</p>
 * * @see EconomicSituation
 * @see Worker
 * @see Organization
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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

