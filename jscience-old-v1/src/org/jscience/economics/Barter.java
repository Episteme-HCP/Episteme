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

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a transfer of property from one entity to another
 * without the use of money.
 *
 * @author Silvere Martin-Michiellot
 */

//Barter occurs directly between economic agents without the need for organizations
//the exchange only comes when barterResources() is called
public class Barter extends Object {
    /** DOCUMENT ME! */
    private EconomicAgent economicAgent1;

    /** DOCUMENT ME! */
    private Set agent1Resources;

    /** DOCUMENT ME! */
    private EconomicAgent economicAgent2;

    /** DOCUMENT ME! */
    private Set agent2Resources;

/**
     * Creates a new Barter object.
     *
     * @param economicAgent1  DOCUMENT ME!
     * @param agent1Resources DOCUMENT ME!
     * @param economicAgent2  DOCUMENT ME!
     * @param agent2Resources DOCUMENT ME!
     */
    public Barter(EconomicAgent economicAgent1, Set agent1Resources,
        EconomicAgent economicAgent2, Set agent2Resources) {
        Iterator iterator;
        boolean valid;

        if ((economicAgent1 != null) && (agent1Resources != null) &&
                (economicAgent2 != null) && (agent2Resources != null)) {
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
                "The Barter constructor can't have null arguments.");
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
    public void barterResources() {
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
    }
}
