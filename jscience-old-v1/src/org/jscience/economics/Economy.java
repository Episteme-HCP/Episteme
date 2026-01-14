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

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing a society with money (goods are not exchanged from
 * hand to hand but using the medium of money). Especially, it defines the
 * complete flow from raw materials to a product with a unique serial number.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//products have a life cycle
//for example if you buy a plastic bottle of water, the plastic comes from a factory that is extracting or transfoming oil (or may be recycling plastic)
//the water comes from a source in the ground
//once drunk, the bottle is thrown and (normally) recycled by another factory.
//So for this example we have two physical resources (oil and water), that are transformed (oil to plastic (optional), plastic to bottle, water in bottle, used bottle) by at least one factory and one consumer
//(and yes may be the factory consumes its own products)
//we do not generate or remove consumers with this simple class
//subclass it to produce your own model, see http://en.wikipedia.org/wiki/Economic_system
//this class does not extend organization although real markets are organization mostly because this market is the overall "economic market"
//we could use this class as a basis for an auction system
//though this class is rather targetted towards market economy, it can be used for non market economics
public abstract class Economy extends Object {
    /** DOCUMENT ME! */
    private Set organizations;

    /** DOCUMENT ME! */
    private Bank centralBank;

    //describes the relations between consumers
    /**
     * Creates a new Economy object.
     *
     * @param orgs DOCUMENT ME!
     * @param centralBank DOCUMENT ME!
     */
    public Economy(Set orgs, Bank centralBank) {
        Iterator iterator;
        boolean valid;

        if ((orgs != null) && (centralBank != null)) {
            iterator = orgs.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Organization;
            }

            if (valid) {
                this.organizations = orgs;

                if (this.organizations.contains(centralBank)) {
                    this.organizations.remove(centralBank);
                }

                this.centralBank = centralBank;
            } else {
                throw new IllegalArgumentException(
                    "Organizations should be a Set of organizations.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Market constructor can't have null arguments.");
        }
    }

    //returns the organization without the bank
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOrganizations() {
        return organizations;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Bank getCentralBank() {
        return centralBank;
    }

    /**
     * DOCUMENT ME!
     *
     * @param org DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addOrganization(Organization org) {
        if ((org != null) && (org != centralBank)) {
            organizations.add(org);
        } else {
            throw new IllegalArgumentException(
                "Organization can't be null or the Central Bank.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param org DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeOrganization(Organization org) {
        if ((org != null) && (org != centralBank)) {
            organizations.remove(org);
        } else {
            throw new IllegalArgumentException(
                "Organization can't be null or the Central Bank.");
        }
    }

    //if the bank was set as part of the organizations
    /**
     * DOCUMENT ME!
     *
     * @param bank DOCUMENT ME!
     */
    public void setCentralBank(Bank bank) {
        if ((bank != null)) {
            if (this.organizations.contains(bank)) {
                this.organizations.remove(bank);
            }

            this.centralBank = bank;
        } else {
            throw new IllegalArgumentException("Bank can't be null.");
        }
    }

    //get all the prices on the market for resource that match the name and description
    /**
     * DOCUMENT ME!
     *
     * @param resource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Money[] getPrices(Resource resource) {
        Iterator iterator1;
        Iterator iterator2;
        Vector result;
        Set resources;
        Resource currentResource;
        Money[] moneyArray;

        if (resource != null) {
            iterator1 = organizations.iterator();
            result = new Vector();

            while (iterator1.hasNext()) {
                iterator2 = ((Organization) iterator1.next()).getResources()
                             .iterator();

                while (iterator2.hasNext()) {
                    currentResource = (Resource) iterator2.next();

                    if ((currentResource.getName()
                                            .equals(currentResource.getName())) &&
                            (currentResource.getDescription()
                                                .equals(currentResource.getDescription()))) {
                        if (currentResource instanceof HumanResource) {
                            result.add(((HumanResource) currentResource).getValue());
                        } else if (currentResource instanceof MaterialResource) {
                            result.add(((MaterialResource) currentResource).getValue());
                        }
                    }
                }
            }

            moneyArray = new Money[result.size()];

            for (int i = 0; i < result.size(); i++) {
                moneyArray[i] = (Money) result.elementAt(i);
            }

            return moneyArray;
        } else {
            throw new IllegalArgumentException(
                "You can't get the prices for a null resource.");
        }
    }

    //gets the number of units currently existing
    /**
     * DOCUMENT ME!
     *
     * @param resource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount getNumberOfUnits(Resource resource) {
        Iterator iterator1;
        Iterator iterator2;
        Amount result;
        Resource currentResource;

        if (resource != null) {
            iterator1 = organizations.iterator();
            result = Amount.ZERO;

            while (iterator1.hasNext()) {
                iterator2 = ((Organization) iterator1.next()).getResources()
                             .iterator();

                while (iterator2.hasNext()) {
                    currentResource = (Resource) iterator2.next();

                    if ((currentResource.getName()
                                            .equals(currentResource.getName())) &&
                            (currentResource.getDescription()
                                                .equals(currentResource.getDescription()))) {
                        result.plus(currentResource.getAmount());
                    }
                }
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can't get the number of units for a null resource.");
        }
    }

    //describes the available resources (with limitless workforce and energy)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllResources() {
        Iterator iterator;
        HashSet result;

        iterator = organizations.iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.addAll(((Organization) iterator.next()).getResources());
        }

        return result;
    }

    //the sum of all money from all organizations
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getValue() {
        Iterator iterator;
        Amount<Money> result;

        iterator = organizations.iterator();
        result = Amount.valueOf(0, Currency.USD);

        while (iterator.hasNext()) {
            result.plus(((Organization) iterator.next()).getCapital());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param resource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Set getOwners(Resource resource) {
        Iterator iterator;
        Organization organization;
        HashSet result;

        if (resource != null) {
            iterator = organizations.iterator();
            result = new HashSet();

            while (iterator.hasNext()) {
                organization = (Organization) iterator.next();

                if (organization.getResources().contains(resource)) {
                    result.add(organization);
                }
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can't get the owners for a null resource.");
        }
    }

    //we could propose several other methods
    /**
     * DOCUMENT ME!
     *
     * @param dt DOCUMENT ME!
     */
    public abstract void step(double dt);
}
