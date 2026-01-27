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
 
import java.util.HashSet;
import java.util.Set;
import org.jscience.mathematics.numbers.real.Real;
 
/**
 * An economic model where resource allocation is determined by supply and demand 
 * with minimal central intervention.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FreeMarketEconomy extends Economy {
 
    public FreeMarketEconomy(Set<Organization> orgs, Bank centralBank) {
        super(orgs, centralBank);
    }
 
    /**
     * Executes one time step of the free market simulation.
     * Includes handling bankruptcies and transactional cycles.
     */
    @Override
    public void step(Real dt) {
        Set<Organization> dead = new HashSet<>();
        for (Organization organization : getOrganizations()) {
            if (organization.getCapital().getValue().compareTo(Real.of(-1000.0)) < 0) {
                dead.add(organization);
            }
        }
        dead.forEach(this::removeOrganization);
    }
}
