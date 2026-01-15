package org.jscience.economics;

import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a transfer of property from one entity to another
 *
 * @author Silvere Martin-Michiellot
 */

//Trade occurs directly between economic agents without the need for organizations
//the exchange only comes when tradeResources() is called
//a Zero Money Trade is a Barter (although the Barter class is kept for convenience)
public class Trade extends Object {
    /** DOCUMENT ME! */
    private EconomicAgent economicAgent1;

    /** DOCUMENT ME! */
    private Set agent1Resources;

    /** DOCUMENT ME! */
    private Amount<Money> pricePaidBy1To2;

    /** DOCUMENT ME! */
    private EconomicAgent economicAgent2;

    /** DOCUMENT ME! */
    private Set agent2Resources;

/**
     * Creates a new Trade object.
     *
     * @param economicAgent1  DOCUMENT ME!
     * @param agent1Resources DOCUMENT ME!
     * @param pricePaidBy1To2 DOCUMENT ME!
     * @param economicAgent2  DOCUMENT ME!
     * @param agent2Resources DOCUMENT ME!
     */
    public Trade(EconomicAgent economicAgent1, Set agent1Resources,
        Amount<Money> pricePaidBy1To2, EconomicAgent economicAgent2,
        Set agent2Resources) {
        Iterator iterator;
        boolean valid;

        if ((economicAgent1 != null) && (agent1Resources != null) &&
                (pricePaidBy1To2 != null) && (economicAgent2 != null) &&
                (agent2Resources != null)) {
            iterator = agent1Resources.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Resource;
            }

            if (valid) {
                iterator = agent1Resources.iterator();

                while (iterator.hasNext() && valid) {
                    valid = economicAgent1.getBelongings()
                                          .contains(iterator.next());
                }

                if (valid) {
                    iterator = agent2Resources.iterator();

                    while (iterator.hasNext() && valid) {
                        valid = iterator.next() instanceof Resource;
                    }

                    if (valid) {
                        iterator = agent2Resources.iterator();

                        while (iterator.hasNext() && valid) {
                            valid = economicAgent2.getBelongings()
                                                  .contains(iterator.next());
                        }

                        if (valid) {
                            this.economicAgent1 = economicAgent1;
                            this.agent1Resources = agent1Resources;
                            this.pricePaidBy1To2 = pricePaidBy1To2;
                            this.economicAgent2 = economicAgent2;
                            this.agent2Resources = agent2Resources;
                        } else {
                            throw new IllegalArgumentException(
                                "All agent2Resources should be owned by economicAgent2.");
                        }
                    } else {
                        throw new IllegalArgumentException(
                            "agent2Resources should be a Set of Resources.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "All agent1Resources should be owned by economicAgent1.");
                }
            } else {
                throw new IllegalArgumentException(
                    "agent1Resources should be a Set of Resources.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Trade constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EconomicAgent getEconomicAgent1() {
        return economicAgent1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAgent1Resources() {
        return agent1Resources;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getPricePaidBy1To2() {
        return pricePaidBy1To2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EconomicAgent getEconomicAgent2() {
        return economicAgent2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAgent2Resources() {
        return agent2Resources;
    }

    /**
     * DOCUMENT ME!
     */
    public void tradeResources() {
        Set resources;

        resources = economicAgent2.getBelongings();
        resources.removeAll(agent2Resources);
        economicAgent2.setBelongings(resources);
        resources = economicAgent1.getBelongings();
        resources.addAll(agent2Resources);
        economicAgent1.setBelongings(resources);
        resources = economicAgent2.getBelongings();
        resources.addAll(agent1Resources);
        economicAgent2.setBelongings(resources);
        resources = economicAgent1.getBelongings();
        resources.removeAll(agent1Resources);
        economicAgent1.setBelongings(resources);
        economicAgent1.getWallet().removeValue(pricePaidBy1To2);
        economicAgent2.getWallet().addValue(pricePaidBy1To2);
    }
}
