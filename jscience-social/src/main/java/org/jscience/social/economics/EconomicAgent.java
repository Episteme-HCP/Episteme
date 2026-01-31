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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jscience.natural.biology.Individual;
import org.jscience.social.economics.money.Wallet;
import org.jscience.social.sociology.Role;
import org.jscience.social.sociology.RoleKind;

/**
 * Represents an autonomous actor in an economic system, which can be an 
 * individual, a household, or a firm. Economic agents are defined by their 
 * ability to own resources, accumulate capital in a wallet, and interact 
 * within markets.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EconomicAgent extends Role {

    private static final long serialVersionUID = 2L;

    private Set<Resource> belongings;
    private Wallet wallet;

    /**
     * Initializes a standard economic agent associated with an individual.
     * 
     * @param individual the biological or social entity acting as the agent
     * @param situation the economic environment/context of action
     */
    public EconomicAgent(Individual individual, EconomicSituation situation) {
        super(individual, "Economic Agent", situation, Role.CLIENT);
        this.belongings = new HashSet<>();
        this.wallet = new Wallet();
    }

    /**
     * Protected constructor for specialized sub-types of agents (e.g., Banks, Factories).
     */
    protected EconomicAgent(Individual individual, String name,
            EconomicSituation situation, RoleKind kind) {
        super(individual, name, situation, kind);
        this.belongings = new HashSet<>();
        this.wallet = new Wallet();
    }

    @Deprecated
    protected EconomicAgent(Individual individual, String name,
            EconomicSituation situation, int kind) {
        super(individual, name, situation, kind);
        this.belongings = new HashSet<>();
        this.wallet = new Wallet();
    }

    /**
     * Returns the set of resources currently owned by this agent.
     * @return unmodifiable set of owned resources
     */
    public Set<Resource> getBelongings() {
        return Collections.unmodifiableSet(belongings);
    }

    /**
     * Transfers ownership of a resource to this agent.
     * @param belonging the resource to acquire
     */
    public void addBelonging(Resource belonging) {
        if (belonging != null) {
            belongings.add(belonging);
        }
    }

    /**
     * Relinquishes ownership of a resource.
     * @param belonging the resource to remove
     */
    public void removeBelonging(Resource belonging) {
        belongings.remove(belonging);
    }

    /**
     * Sets the resources owned by this agent.
     * @param belongings the set of resources
     */
    public void setBelongings(Set<Resource> belongings) {
        this.belongings = belongings != null ? belongings : new HashSet<>();
    }

    /**
     * @return the agent's financial wallet containing currency holdings
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Replaces the agent's wallet.
     * @param wallet the new wallet instance
     */
    public void setWallet(Wallet wallet) {
        if (wallet != null) {
            this.wallet = wallet;
        }
    }

    /**
     * Aggregates the agent's identity into a localized social community 
     * based on their current geographic position.
     * 
     * @return a community containing this agent
     */
    public Community getCommunity() {
        Set<Individual> set = new HashSet<>();
        set.add(getIndividual());
        return new Community(getIndividual().getSpecies(), set,
            getIndividual().getPosition());
    }
}

