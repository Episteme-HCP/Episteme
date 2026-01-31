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

package org.jscience.social.economics.money;

import org.jscience.social.economics.Bank;
import org.jscience.social.economics.EconomicAgent;
import org.jscience.social.economics.Property;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a bank account for storing money and financial assets.
 * <p>
 * An account can hold monetary balances and financial shares, with support
 * for multiple owners and bank affiliation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Account implements Property, ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Bank bank;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<EconomicAgent> owners;
    
    @Id
    private final Identification id;
    
    @Attribute
    private final Map<String, Object> traits = new HashMap<>();
    
    @Attribute
    private Money amount;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Map<Share, Integer> shares;

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Creates a new Account with a name and currency.
     *
     * @param name     the account name
     * @param currency the currency
     */
    public Account(String name, Currency currency) {
        this(null, new HashSet<>(), null, name, Money.valueOf(Real.ZERO, currency));
    }

    /**
     * Creates a new Account with full details.
     *
     * @param bank           the bank holding this account
     * @param owners         the owners of this account
     * @param identification the account ID
     * @param name           the account name
     * @param amount         initial balance
     */
    public Account(Bank bank, Set<EconomicAgent> owners, Identification identification,
                   String name, Money amount) {
        this(bank, owners, identification, name, amount, new HashMap<>());
    }

    /**
     * Creates a new Account with shares.
     *
     * @param bank           the bank holding this account
     * @param owners         the owners of this account
     * @param identification the account ID
     * @param name           the account name
     * @param amount         initial balance
     * @param shares         initial share holdings
     */
    public Account(Bank bank, Set<EconomicAgent> owners, Identification id,
                   String name, Money amount, Map<Share, Integer> shares) {
        this.bank = bank;
        this.id = id != null ? id : new org.jscience.core.util.identity.SimpleIdentification(name);
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isEmpty()) throw new IllegalArgumentException("Account name cannot be empty");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        
        this.owners = owners != null ? new HashSet<>(owners) : new HashSet<>();
        this.shares = shares != null ? new HashMap<>(shares) : new HashMap<>();
    }

    /**
     * Returns the bank holding this account.
     * @return the bank, or null for cash collections
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Returns an unmodifiable set of account owners.
     * @return the owners
     */
    @Override
    public Set<EconomicAgent> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    /**
     * Adds an owner to the account.
     * @param owner the owner to add
     */
    public void addOwner(EconomicAgent owner) {
        owners.add(Objects.requireNonNull(owner, "Owner cannot be null"));
    }

    /**
     * Removes an owner from the account.
     * @param owner the owner to remove
     * @throws IllegalArgumentException if trying to remove the last owner
     */
    public void removeOwner(EconomicAgent owner) {
        if (owners.size() <= 1) {
            throw new IllegalArgumentException("Cannot remove last owner");
        }
        owners.remove(owner);
    }

    /**
     * Replaces all owners.
     * @param owners the new set of owners
     */
    public void setOwners(Set<EconomicAgent> owners) {
        this.owners = new HashSet<>(Objects.requireNonNull(owners, "Owners set cannot be null"));
        if (this.owners.isEmpty()) throw new IllegalArgumentException("Owners set cannot be empty");
    }

    public Identification getIdentification() {
        return id;
    }

    // getName() and setName() are provided by ComprehensiveIdentification default methods

    /**
     * Returns the current account balance.
     * @return the balance
     */
    public Money getValue() {
        return amount;
    }

    /**
     * Returns the account currency.
     * @return the currency
     */
    public Currency getCurrency() {
        return amount.getCurrency();
    }

    /**
     * Deposits an amount into the account.
     * @param amount the amount to add
     */
    public void deposit(Money amount) {
        this.amount = this.amount.add(amount);
    }

    /**
     * Withdraws an amount from the account.
     * @param amount the amount to subtract
     */
    public void withdraw(Money amount) {
        this.amount = this.amount.subtract(amount);
    }

    /**
     * Checks if the account is empty (no money and no shares).
     * @return true if empty
     */
    public boolean isEmpty() {
        return amount.getValue().isZero() && shares.isEmpty();
    }

    /**
     * Returns an unmodifiable map of share holdings.
     * @return the shares
     */
    public Map<Share, Integer> getShares() {
        return Collections.unmodifiableMap(shares);
    }

    /**
     * Adds shares to the account.
     * @param share    the share type
     * @param quantity the number of shares
     */
    public void addShare(Share share, int quantity) {
        Objects.requireNonNull(share, "Share cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        shares.merge(share, quantity, (v1, v2) -> v1 + v2);
    }

    /**
     * Removes shares from the account.
     * @param share    the share type
     * @param quantity the number of shares to remove
     */
    public void removeShare(Share share, int quantity) {
        Objects.requireNonNull(share, "Share cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        
        int current = shares.getOrDefault(share, 0);
        if (current < quantity) {
            throw new IllegalArgumentException("Not enough shares to remove");
        }
        if (current == quantity) {
            shares.remove(share);
        } else {
            shares.put(share, current - quantity);
        }
    }

    /**
     * Replaces all share holdings.
     * @param shares the new shares map
     */
    public void setShares(Map<Share, Integer> shares) {
        this.shares = new HashMap<>(Objects.requireNonNull(shares, "Shares map cannot be null"));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Account[%s, balance=%s, shares=%d types]", 
            getName(), amount, shares.size());
    }
}

