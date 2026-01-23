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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.economics.money.Wallet;
import org.jscience.sociology.Role;

/**
 * Represents an individual or entity participating in market activities.
 * Manages resource belongings and a financial wallet.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public class EconomicAgent extends Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<Resource> belongings;
    private Wallet wallet;

    /**
     * Creates a new EconomicAgent.
     * 
     * @param individual the underlying biological or social individual
     * @param situation  the economic context
     */
    public EconomicAgent(Individual individual, EconomicSituation situation) {
        super(individual, "Economic Agent", situation, Role.CLIENT);
        this.belongings = new HashSet<>();
        this.wallet = new Wallet();
    }

    protected EconomicAgent(Individual individual, String name,
            EconomicSituation situation, int kind) {
        super(individual, name, situation, kind);
        this.belongings = new HashSet<>();
        this.wallet = new Wallet();
    }

    /** Returns unmodifiable set of resources owned by this agent. */
    public Set<Resource> getBelongings() {
        return Collections.unmodifiableSet(belongings);
    }

    public void addBelonging(Resource belonging) {
        if (belonging != null) {
            belongings.add(belonging);
        }
    }

    public void removeBelonging(Resource belonging) {
        belongings.remove(belonging);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        if (wallet != null) {
            this.wallet = wallet;
        }
    }

    /** Aggregates the agent's identity and geographical position into a social community. */
    public Community getCommunity() {
        Set<Individual> set = new HashSet<>();
        set.add(getIndividual());
        return new Community(getIndividual().getSpecies(), set,
            getIndividual().getPosition());
    }
}
