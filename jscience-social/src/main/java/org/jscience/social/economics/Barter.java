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

package org.jscience.social.economics;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a direct exchange of property between two agents without using money.
 * Atomic transaction that validates ownership before execution.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Barter implements Serializable {

    private static final long serialVersionUID = 1L;

    private final EconomicAgent agent1;
    private final Set<Resource> resources1;
    private final EconomicAgent agent2;
    private final Set<Resource> resources2;

    /**
     * Initializes a barter contract.
     * 
     * @param agent1     first participant
     * @param resources1 resources offered by agent1
     * @param agent2     second participant
     * @param resources2 resources offered by agent2
     * @throws IllegalArgumentException if ownership is not verified
     */
    public Barter(EconomicAgent agent1, Set<Resource> resources1,
                  EconomicAgent agent2, Set<Resource> resources2) {
        this.agent1 = Objects.requireNonNull(agent1, "Agent1 cannot be null");
        this.agent2 = Objects.requireNonNull(agent2, "Agent2 cannot be null");
        this.resources1 = new HashSet<>(Objects.requireNonNull(resources1));
        this.resources2 = new HashSet<>(Objects.requireNonNull(resources2));

        if (!agent1.getBelongings().containsAll(this.resources1)) {
            throw new IllegalArgumentException("Agent1 does not own all offered resources");
        }
        if (!agent2.getBelongings().containsAll(this.resources2)) {
            throw new IllegalArgumentException("Agent2 does not own all offered resources");
        }
    }

    public EconomicAgent getAgent1() { return agent1; }
    public Set<Resource> getResources1() { return Collections.unmodifiableSet(resources1); }
    public EconomicAgent getAgent2() { return agent2; }
    public Set<Resource> getResources2() { return Collections.unmodifiableSet(resources2); }

    /**
     * Executes the exchange, updating both agents' belongings.
     */
    public void execute() {
        for (Resource r : resources1) {
            agent1.removeBelonging(r);
            agent2.addBelonging(r);
        }
        for (Resource r : resources2) {
            agent2.removeBelonging(r);
            agent1.addBelonging(r);
        }
    }
}

