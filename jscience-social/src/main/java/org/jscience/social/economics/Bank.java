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
import java.util.Objects;
import java.util.Set;
import org.jscience.social.economics.money.Account;
import org.jscience.social.economics.money.ChangeSource;
import org.jscience.social.economics.money.Currency;
import org.jscience.social.economics.money.Money;
import org.jscience.social.geography.BusinessPlace;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a banking institution capable of managing accounts and providing 
 * currency conversion services.
 * Extends {@link Organization} and implements {@link ChangeSource}.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Bank extends Organization implements ChangeSource {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Account> clientAccounts;

    /**
     * Initializes a new Bank with official documentation.
     * 
     * @param name           official bank name
     * @param identification legal tax/regulatory ID
     * @param owners         set of owners
     * @param place          physical headquarters
     * @param accounts       initial capital accounts
     */
    public Bank(String name, Identification identification, Set<EconomicAgent> owners,
            BusinessPlace place, Set<Account> accounts) {
        super(name, identification, owners, place, accounts);
        this.clientAccounts = new HashSet<>();
    }

    /**
     * Minimal constructor for the modern API.
     */
    public Bank(String name, org.jscience.natural.earth.Place place, Money initialCapital) {
        super(name, place, initialCapital);
        this.clientAccounts = new HashSet<>();
    }

    public Bank(String name) {
        super(name, null, Money.usd(0));
        this.clientAccounts = new HashSet<>();
    }

    /** Returns unmodifiable set of client accounts. */
    public Set<Account> getClientAccounts() {
        return Collections.unmodifiableSet(clientAccounts);
    }

    /**
     * Registers a new account with this bank.
     * @param account the account to add
     */
    public void addClientAccount(Account account) {
        Objects.requireNonNull(account, "Account cannot be null");
        if (!account.getBank().equals(this)) {
             throw new IllegalArgumentException("Account does not belong to this bank");
        }
        clientAccounts.add(account);
    }

    public void removeClientAccount(Account account) {
        clientAccounts.remove(account);
    }

    @Override
    public Money getConverted(Money amount, Currency target) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(target, "Target currency cannot be null");
        // Standard non-fee conversion
        return amount.to(target);
    }
}

