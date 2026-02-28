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

import org.episteme.social.economics.money.Money;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a commercial exchange between two economic agents.
 * A trade involves transferring sets of resources and a monetary payment.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Trade {

    /** The first participant in the trade. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final EconomicAgent economicAgent1;

    /** Resources offered by the first agent. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Resource> agent1Resources;

    /** Amount paid by agent 1 to agent 2. */
    @Attribute
    private final Money pricePaidBy1To2;

    /** The second participant in the trade. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final EconomicAgent economicAgent2;

    /** Resources offered by the second agent. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Resource> agent2Resources;

    /**
     * Creates a new Trade transaction.
     *
     * @param economicAgent1  the first agent
     * @param agent1Resources resources from agent 1 (must be owned by them)
     * @param pricePaidBy1To2 monetary compensation from 1 to 2
     * @param economicAgent2  the second agent
     * @param agent2Resources resources from agent 2 (must be owned by them)
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if agents do not own the specified resources
     */
    public Trade(EconomicAgent economicAgent1, Set<Resource> agent1Resources,
            Money pricePaidBy1To2, EconomicAgent economicAgent2,
            Set<Resource> agent2Resources) {
        
        this.economicAgent1 = Objects.requireNonNull(economicAgent1, "EconomicAgent1 cannot be null");
        this.agent1Resources = new HashSet<>(Objects.requireNonNull(agent1Resources, "Agent1Resources cannot be null"));
        this.pricePaidBy1To2 = Objects.requireNonNull(pricePaidBy1To2, "Price cannot be null");
        this.economicAgent2 = Objects.requireNonNull(economicAgent2, "EconomicAgent2 cannot be null");
        this.agent2Resources = new HashSet<>(Objects.requireNonNull(agent2Resources, "Agent2Resources cannot be null"));

        // Validate ownership
        if (!economicAgent1.getBelongings().containsAll(this.agent1Resources)) {
            throw new IllegalArgumentException("EconomicAgent1 does not own all specified resources.");
        }
        if (!economicAgent2.getBelongings().containsAll(this.agent2Resources)) {
            throw new IllegalArgumentException("EconomicAgent2 does not own all specified resources.");
        }
    }

    /**
     * Returns the first participant.
     *
     * @return agent 1
     */
    public EconomicAgent getEconomicAgent1() {
        return economicAgent1;
    }

    /**
     * Returns an unmodifiable view of resources from agent 1.
     *
     * @return resources from agent 1
     */
    public Set<Resource> getAgent1Resources() {
        return Collections.unmodifiableSet(agent1Resources);
    }

    /**
     * Returns the monetary compensation from agent 1 to agent 2.
     *
     * @return the price
     */
    public Money getPricePaidBy1To2() {
        return pricePaidBy1To2;
    }

    /**
     * Returns the second participant.
     *
     * @return agent 2
     */
    public EconomicAgent getEconomicAgent2() {
        return economicAgent2;
    }

    /**
     * Returns an unmodifiable view of resources from agent 2.
     *
     * @return resources from agent 2
     */
    public Set<Resource> getAgent2Resources() {
        return Collections.unmodifiableSet(agent2Resources);
    }

    /**
     * Executes the trade, transferring resources and money between agents.
     */
    public void tradeResources() {
        // Transfer resources from 2 to 1
        Set<Resource> belongings2 = new HashSet<>(economicAgent2.getBelongings());
        belongings2.removeAll(agent2Resources);
        economicAgent2.setBelongings(belongings2);

        Set<Resource> belongings1 = new HashSet<>(economicAgent1.getBelongings());
        belongings1.addAll(agent2Resources);
        economicAgent1.setBelongings(belongings1);

        // Transfer resources from 1 to 2
        belongings2 = new HashSet<>(economicAgent2.getBelongings());
        belongings2.addAll(agent1Resources);
        economicAgent2.setBelongings(belongings2);

        belongings1 = new HashSet<>(economicAgent1.getBelongings());
        belongings1.removeAll(agent1Resources);
        economicAgent1.setBelongings(belongings1);

        // Monetary transfer
        economicAgent1.getWallet().removeValue(pricePaidBy1To2);
        economicAgent2.getWallet().addValue(pricePaidBy1To2);
    }
}

