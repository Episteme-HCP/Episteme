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


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jscience.economics.money.Money;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.util.identity.Identification;

import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Functional abstraction for an economic system, encompassing organizations, 
 * central banking, and macro-financial indicators. It provides the framework 
 * for simulating resource flows and systemic events within a specific 
 * economic situation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
@Persistent
public abstract class Economy implements ComprehensiveIdentification {

    private static final long serialVersionUID = 3L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Organization> organizations;
    
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final Bank centralBank;
    
    @Attribute
    private Money cachedGDP;
    @Attribute
    private Real inflationRate = Real.ZERO;
    @Attribute
    private Real unemploymentRate = Real.ZERO;

    /**
     * Factory method creating a snapshot of the USA economy.
     * @return a concrete Economy instance
     */
    public static Economy usa() {
        Bank fed = new Bank("Federal Reserve");
        Set<Organization> orgs = new HashSet<>();
        orgs.add(fed);
        // Add some dummy organizations
        orgs.add(new Organization("US Corp", (org.jscience.earth.Place) null, Money.usd(0)) {}); 
        return new FreeMarketEconomy(orgs, fed);
    }

    /**
     * Initializes a new Economy with its constituent organizations and a 
     * mandatory central bank.
     * 
     * @param orgs the initial set of productive organizations
     * @param centralBank the system's central bank
     * @throws NullPointerException if centralBank or orgs is null
     */
    public Economy(Set<Organization> orgs, Bank centralBank) {
        this(new org.jscience.util.identity.UUIDIdentification(UUID.randomUUID()), orgs, centralBank);
    }

    public Economy(Identification id, Set<Organization> orgs, Bank centralBank) {
        this.id = Objects.requireNonNull(id);
        this.centralBank = Objects.requireNonNull(centralBank, "Central bank cannot be null");
        this.organizations = new HashSet<>(Objects.requireNonNull(orgs, "Organization set cannot be null"));
        this.organizations.remove(centralBank);
    }

    public Set<Organization> getOrganizations() {
        return Collections.unmodifiableSet(organizations);
    }

    public Bank getCentralBank() {
        return centralBank;
    }

    public void addOrganization(Organization org) {
        if (org != null && org != centralBank) {
            organizations.add(org);
        }
    }

    public void removeOrganization(Organization org) {
        organizations.remove(org);
    }

    /** 
     * Aggregates the total quantity of a specific resource across all 
     * organizations in the current economy.
     * 
     * @param resource the prototype of the resource to count
     * @return the total quantity unit of that resource, or null if not found
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Quantity<?> getNumberOfUnits(Resource resource) {
        if (resource == null) return null;
        Quantity total = null;
        for (Organization org : organizations) {
            for (Resource r : org.getResources()) {
                if (r.getName().equalsIgnoreCase(resource.getName())) {
                    total = (total == null) ? r.getAmount() : total.add(r.getAmount());
                }
            }
        }
        return total;
    }

    /** 
     * Calculates the gross value of all organizations in the economy 
     * based on their localized liquid capital.
     * 
     * @return total aggregated value in Money units
     */
    public Money getValue() {
        Money total = Money.usd(Real.ZERO);
        for (Organization org : organizations) {
            total = total.add(org.getCapital());
        }
        return total;
    }

    public abstract void step(Real dt);

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getModelType() {
        return "ECONOMY";
    }

    // --- Macro Indicators ---

    /**
     * @return the current Gross Domestic Product (GDP)
     */
    public Money getGDP() {
        return cachedGDP != null ? cachedGDP : getValue();
    }

    public void setGDP(Money gdp) {
        this.cachedGDP = gdp;
    }

    public Real getInflationRate() {
        return inflationRate;
    }

    public void setInflationRate(Real rate) {
        this.inflationRate = (rate != null) ? rate : Real.ZERO;
    }

    public Real getUnemploymentRate() {
        return unemploymentRate;
    }

    public void setUnemploymentRate(Real rate) {
        this.unemploymentRate = (rate != null) ? rate : Real.ZERO;
    }
}
