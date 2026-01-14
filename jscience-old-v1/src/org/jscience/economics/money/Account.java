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

package org.jscience.economics.money;

import org.jscience.biology.human.Human;

import org.jscience.economics.Bank;
import org.jscience.economics.Property;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * A class representing a bank account on which you can store money or
 * properties on some things.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//credit card is not a money, it rather is a key to draw money instantly from a bank account
//electronic money is a sort of credit card without the credit card
//you use transactions to change the contents of accounts, or as a bank owner you tweak up the results as you wish
//as an organisation (although it is NOT provided) you store the corresponding receipts to have a trace of what transactions were done
public class Account extends Object implements Property, Named, Identified {
    /** DOCUMENT ME! */
    private Bank bank;

    /** DOCUMENT ME! */
    private Set owners;

    /** DOCUMENT ME! */
    private Identification identification; //the account number

    /** DOCUMENT ME! */
    private String name; //the account name, usually the kind of stuff stored inside

    /** DOCUMENT ME! */
    private Currency currency; //the currency in which to count the current values

    /** DOCUMENT ME! */
    private Amount<Money> amount; //values and quantity if any

    /** DOCUMENT ME! */
    private Map shares; //values and quantity if any

    //to store money only
    /**
     * Creates a new Account object.
     *
     * @param bank DOCUMENT ME!
     * @param owners DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param amount DOCUMENT ME!
     */
    public Account(Bank bank, Set owners, Identification identification,
        String name, Amount<Money> amount) {
        if ((bank != null) && (owners != null) && (identification != null) &&
                (name != null) && (name.length() > 0) && (amount != null)) {
            this.bank = bank;
            setOwners(owners);
            this.identification = identification;
            this.name = name;
            this.amount = amount;
            this.shares = Collections.EMPTY_MAP;
        } else {
            throw new IllegalArgumentException(
                "Account doesn't accept null arguments (and name can't be empty).");
        }
    }

    //to store values
    /**
     * Creates a new Account object.
     *
     * @param bank DOCUMENT ME!
     * @param owners DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param amount DOCUMENT ME!
     * @param shares DOCUMENT ME!
     */
    public Account(Bank bank, Set owners, Identification identification,
        String name, Amount<Money> amount, Map shares) {
        Iterator iterator;
        boolean valid;

        if ((bank != null) && (owners != null) && (identification != null) &&
                (name != null) && (name.length() > 0) && (amount != null) &&
                (shares != null)) {
            iterator = shares.keySet().iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Share;
            }

            if (valid) {
                this.bank = bank;
                setOwners(owners);
                this.identification = identification;
                this.name = name;
                this.amount = amount;
                this.shares = shares;
            } else {
                throw new IllegalArgumentException(
                    "The shares Map must contain only Shares.");
            }
        } else {
            throw new IllegalArgumentException(
                "Account doesn't accept null arguments (and name can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Bank getBank() {
        return bank;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Set getOwners() {
        return owners;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void addOwner(Human owner) {
        if (owner != null) {
            owners.add(owner);
        } else {
            throw new IllegalArgumentException("You can't add a null owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void removeOwner(Human owner) {
        if ((owners.size() > 1)) {
            owners.remove(owner);
        } else {
            throw new IllegalArgumentException("You can't remove last owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owners DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setOwners(Set owners) {
        Iterator iterator;
        boolean valid;

        if ((owners != null) && (owners.size() > 0)) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.owners = owners;
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty owners set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String getName() {
        return name;
    }

    //the money balance, n account is worth what you put inside
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getValue() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Currency getCurrency() {
        return (Currency) amount.getUnit();
    }

    //money is converted AT ZERO COST, not stored as raw coins and paper although your bank might prefer a different implementation
    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public final void addAmount(Amount<Money> amount) {
        this.amount.plus(amount);
    }

    //money is converted AT ZERO COST, not stored as raw coins and paper although your bank might prefer a different implementation
    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public final void subtractAmount(Amount<Money> amount) {
        this.amount.minus(amount);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return (amount.doubleValue(amount.getUnit()) == 0) &&
        (shares.size() == 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Map getShares() {
        return shares;
    }

    /**
     * DOCUMENT ME!
     *
     * @param share DOCUMENT ME!
     * @param quantity DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void addShare(Share share, int quantity) {
        if (share != null) {
            if (quantity > 0) {
                if (shares.containsKey(share)) {
                    shares.put(share,
                        new Integer(((Integer) shares.get(share)).intValue() +
                            quantity));
                } else {
                    shares.put(share, new Integer(quantity));
                }
            } else {
                throw new IllegalArgumentException(
                    "You can only add a positive quantity of shares.");
            }
        } else {
            throw new IllegalArgumentException("You can't add a null share.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param share DOCUMENT ME!
     * @param quantity DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void removeShare(Share share, int quantity) {
        if (share != null) {
            if (quantity > 0) {
                if ((shares.containsKey(share)) &&
                        ((((Integer) shares.get(share)).intValue() - quantity) > 0)) {
                    shares.put(share,
                        new Integer(((Integer) shares.get(share)).intValue() -
                            quantity));
                } else {
                    throw new IllegalArgumentException(
                        "You can't remove more shares than there is.");
                }
            } else {
                throw new IllegalArgumentException(
                    "You can only add a positive quantity of shares.");
            }
        } else {
            throw new IllegalArgumentException("You can't add a null share.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param shares DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setShares(Map shares) {
        Iterator iterator;
        boolean valid;

        if (shares != null) {
            iterator = shares.keySet().iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Share;
            }

            if (valid) {
                this.shares = shares;
            } else {
                throw new IllegalArgumentException(
                    "The shares Map must contain only Shares.");
            }
        } else {
            throw new IllegalArgumentException("You can't set a null Map.");
        }
    }

    //may be we should provide support for toString();
}
