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

import org.jscience.biology.Individual;

import org.jscience.economics.money.Wallet;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a consumer basic facts.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//has private personal belongings and a virtual wallet (which for most species should be empty)
//you should use this class for humans only.
//if you need to consider an animal in the wild, define a new role different from this one or simply do the following
//   HashSet set = new HashSet(); set.add(individual); Community community = new Community(individual.getSpecies(), set, individual.getPosition());
//then work with that animal seen as a "resource consuming community"
public class EconomicAgent extends Role {
    /** DOCUMENT ME! */
    private Set belongings; //including homes, Set of Resource

    /** DOCUMENT ME! */
    private Wallet wallet;

/**
     * Creates a new EconomicAgent object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public EconomicAgent(Individual individual, EconomicSituation situation) {
        super(individual, "Economic Agent", situation, Role.CLIENT);
        this.belongings = Collections.EMPTY_SET;
        this.wallet = new Wallet();
    }

/**
     * Creates a new EconomicAgent object.
     *
     * @param individual DOCUMENT ME!
     * @param name       DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     * @param kind       DOCUMENT ME!
     */
    protected EconomicAgent(Individual individual, String name,
        EconomicSituation situation, int kind) {
        super(individual, name, situation, kind);
        this.belongings = Collections.EMPTY_SET;
        this.wallet = new Wallet();
    }

    //returns the current belongings
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getBelongings() {
        return belongings;
    }

    /**
     * DOCUMENT ME!
     *
     * @param belonging DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addBelonging(Resource belonging) {
        if (belonging != null) {
            belongings.add(belonging);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null belonging.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param belonging DOCUMENT ME!
     */
    public void removeBelonging(Resource belonging) {
        belongings.remove(belonging);
    }

    /**
     * DOCUMENT ME!
     *
     * @param belongings DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setBelongings(Set belongings) {
        Iterator iterator;
        boolean valid;

        if (belongings != null) {
            iterator = belongings.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Resource;
            }

            if (valid) {
                this.belongings = belongings;
            } else {
                throw new IllegalArgumentException(
                    "The Set of belongings should contain only Resource.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of belongings shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Wallet getWallet() {
        return wallet;
    }

    //this is a virtual wallet
    /**
     * DOCUMENT ME!
     *
     * @param wallet DOCUMENT ME!
     */
    public void setWallet(Wallet wallet) {
        if (wallet != null) {
            this.wallet = wallet;
        } else {
            throw new IllegalArgumentException("You can't set a null Wallet.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Community getCommunity() {
        HashSet set;

        set = new HashSet();
        set.add(getIndividual());

        return new Community(getIndividual().getSpecies(), set,
            getIndividual().getPosition());

        //you then manually add resources from your belongings
    }
}
