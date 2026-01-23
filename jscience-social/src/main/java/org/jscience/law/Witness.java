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
package org.jscience.law;

import org.jscience.biology.Individual;
import org.jscience.sociology.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a witness in a lawsuit, who provides testimony based on what they observed.
 * A witness can be associated with specific parties (plaintiffs or defendants) they are assisting.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public class Witness extends Role {

    private Set<Role> parties;

    /**
     * Creates a new Witness role for an individual.
     *
     * @param individual the individual taking on the role
     * @param lawSuitSituation the lawsuit context
     */
    public Witness(Individual individual, LawSuitSituation lawSuitSituation) {
        super(individual, "Witness", lawSuitSituation, Role.SERVER);
        this.parties = new HashSet<>();
    }

    /**
     * Returns the set of parties (Plaintiffs or Defendants) that this witness is testifying for.
     * @return a set of parties
     */
    public Set<Role> getClients() {
        return new HashSet<>(parties);
    }

    /**
     * Sets the parties that this witness is testifying for.
     *
     * @param parties a set of Roll objects representing the parties
     * @throws IllegalArgumentException if parties is null or contains unauthorized roles
     */
    public void setClients(Set<Role> parties) {
        if (parties == null) {
            throw new IllegalArgumentException("The set of parties cannot be null.");
        }
        
        for (Role r : parties) {
            if (!(r instanceof Plaintiff) && !(r instanceof Defendant)) {
                throw new IllegalArgumentException(
                    "The set of parties must contain only Plaintiffs and Defendants.");
            }
        }
        
        this.parties = new HashSet<>(parties);
    }
}
