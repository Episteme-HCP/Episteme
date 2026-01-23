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

import org.jscience.biology.Human;
import org.jscience.biology.taxonomy.HomoSapiens;
import org.jscience.economics.money.Account;
import org.jscience.economics.money.Money;
import org.jscience.geography.BusinessPlace;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a formal social and economic entity, such as a company, 
 * institution, or cooperative.
 * 
 * <p>An organization can own assets, employ workers (via an {@link Organigram}), 
 * manage financial {@link Account}s, and interact with other organizations 
 * as providers or clients.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */
@Persistent
public class Organization extends Community implements Property, Identified<String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;
    
    @Id
    private Identification identification;
    
    /** The estimated total value of the organization. */
    @Attribute
    private Money value;

    /** The economic agents (individuals or other organizations) that own this entity. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<EconomicAgent> owners;

    /** The internal organizational structure and worker hierarchy. */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Organigram organigram;

    /** The financial accounts belonging to the organization. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Account> accounts;

    /** The current working capital of the organization. */
    @Attribute
    private Money capital;

    /** The set of organizations that supply goods or services. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Organization> providers;

    /** The set of organizations that consume goods or services. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Organization> clients;

    @Override
    public String getId() {
        return identification != null ? identification.getId() : name;
    }

    /**
     * Creates a new organization.
     *
     * @param name           the name of the organization.
     * @param identification the legal identification of the organization.
     * @param owners         the initial set of owners.
     * @param place          the business location.
     * @param accounts       the initial set of financial accounts.
     */
    public Organization(String name, Identification identification, Set<EconomicAgent> owners,
                        BusinessPlace place, Set<Account> accounts) {
        super(HomoSapiens.SPECIES, place);
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        
        this.identification = Objects.requireNonNull(identification, "Identification cannot be null");
        this.owners = Objects.requireNonNull(owners, "Owners set cannot be null");
        this.accounts = Objects.requireNonNull(accounts, "Accounts set cannot be null");
        if (accounts.isEmpty()) throw new IllegalArgumentException("Accounts cannot be empty");

        this.value = Money.usd(0);
        this.organigram = new Organigram(name, new HashSet<>());
        this.capital = Money.usd(0);
        this.providers = new HashSet<>();
        this.clients = new HashSet<>();
    }

    /**
     * Convenience constructor for modern API.
     */
    public Organization(String name, org.jscience.geography.Place place, Money initialCapital) {
        super(HomoSapiens.SPECIES, place);
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");

        this.identification = new SimpleIdentification(name);
        this.value = Money.usd(0);
        this.owners = new HashSet<>();
        this.accounts = new HashSet<>();
        this.accounts.add(new Account(null, owners, identification, "Main Account", initialCapital));
        this.organigram = new Organigram(name, new HashSet<>());
        this.capital = initialCapital;
        this.providers = new HashSet<>();
        this.clients = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = Objects.requireNonNull(identification, "Identification cannot be null");
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    @Override
    public Set<EconomicAgent> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    /**
     * Adds an owner to the organization.
     * @param owner the new owner
     */
    public void addOwner(EconomicAgent owner) {
        owners.add(Objects.requireNonNull(owner, "Owner cannot be null"));
    }

    /**
     * Removes an owner from the organization.
     * @param owner the owner to remove
     */
    public void removeOwner(EconomicAgent owner) {
        owners.remove(owner);
    }

    /**
     * Sets the set of owners for the organization.
     * @param owners the new set of owners
     */
    public void setOwners(Set<EconomicAgent> owners) {
        this.owners = new HashSet<>(Objects.requireNonNull(owners, "Owners set cannot be null"));
    }

    public Organigram getOrganigram() {
        return organigram;
    }

    public void setOrganigram(Organigram organigram) {
        this.organigram = organigram;
    }

    public Set<Organization> getProviders() {
        return Collections.unmodifiableSet(providers);
    }

    public void addProvider(Organization organization) {
        providers.add(Objects.requireNonNull(organization, "Provider cannot be null"));
    }

    public void removeProvider(Organization organization) {
        providers.remove(organization);
    }

    public Set<Organization> getClients() {
        return Collections.unmodifiableSet(clients);
    }

    public void addClient(Organization organization) {
        clients.add(Objects.requireNonNull(organization, "Client cannot be null"));
    }

    public void removeClient(Organization organization) {
        clients.remove(organization);
    }

    public void buyResources(Money value, Organization otherParty, Set<Resource> wantedResources) {
        Objects.requireNonNull(value, "Value cannot be null");
        Objects.requireNonNull(otherParty, "Other party cannot be null");
        Objects.requireNonNull(wantedResources, "Wanted resources cannot be null");

        if (!otherParty.getResources().containsAll(wantedResources)) {
            throw new IllegalArgumentException("Other party does not own all wanted resources");
        }

        // Transfer resources
        Set<Resource> currentResources = new HashSet<>(otherParty.getResources());
        currentResources.removeAll(wantedResources);
        otherParty.setResources(currentResources);

        currentResources = new HashSet<>(getResources());
        currentResources.addAll(wantedResources);
        setResources(currentResources);

        // Transfer money
        otherParty.setCapital(otherParty.getCapital().add(value));
        this.setCapital(this.getCapital().subtract(value));
    }

    public Money getCapital() {
        return capital;
    }

    public void setCapital(Money value) {
        this.capital = Objects.requireNonNull(value, "Capital cannot be null");
    }

    public Set<Account> getAccounts() {
        return Collections.unmodifiableSet(accounts);
    }

    public void addAccount(Account account) {
        accounts.add(Objects.requireNonNull(account, "Account cannot be null"));
    }

    public void removeAccount(Account account) {
        if (accounts.size() <= 1) {
             throw new IllegalStateException("Cannot remove the last account");
        }
        accounts.remove(account);
    }

    public void setAccounts(Set<Account> accounts) {
        Objects.requireNonNull(accounts, "Accounts set cannot be null");
        if (accounts.isEmpty()) {
            throw new IllegalArgumentException("Accounts set cannot be empty");
        }
        this.accounts = new HashSet<>(accounts);
    }

    public Set<Worker> getWorkers() {
        return organigram != null ? organigram.getAllWorkers() : Collections.emptySet();
    }

    //perhaps we should also provide a setter/getter for country in which this organization is built
}
