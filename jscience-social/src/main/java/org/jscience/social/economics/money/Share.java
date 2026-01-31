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

import org.jscience.social.economics.EconomicAgent;
import org.jscience.social.economics.Organization;
import org.jscience.social.economics.Property;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a share (stock) in a company.
 * <p>
 * A share represents ownership in a corporation, entitling the holder
 * to a portion of the company's profits and assets.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Share_(finance)">Share (finance)</a>
 */
@Persistent
public final class Share implements Property, ComprehensiveIdentification {

    @Id
    private final Identification id;

    @Attribute
    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<EconomicAgent> owners;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Organization company;

    @Attribute
    private Money currentValue;

    @Attribute
    private Money dividendPerShare;

    /**
     * Creates a new Share.
     *
     * @param owners       the share owners
     * @param symbol       the ticker symbol
     * @param company      the issuing company
     * @param currentValue the current market value
     */
    public Share(Set<EconomicAgent> owners, String symbol, Organization company, Money currentValue) {
        if (owners == null || owners.isEmpty()) {
            throw new IllegalArgumentException("Owners set cannot be null or empty");
        }
        this.id = new SimpleIdentification(symbol);
        setName(symbol);
        
        this.owners = new HashSet<>(owners);
        this.company = Objects.requireNonNull(company, "Company cannot be null");
        this.currentValue = Objects.requireNonNull(currentValue, "Current value cannot be null");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns an unmodifiable view of the owners.
     * @return the owners
     */
    @Override
    public Set<EconomicAgent> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    /**
     * Adds an owner to this share.
     * @param owner the new owner
     */
    public void addOwner(EconomicAgent owner) {
        owners.add(Objects.requireNonNull(owner, "Owner cannot be null"));
    }

    /**
     * Removes an owner from this share.
     * @param owner the owner to remove
     */
    public void removeOwner(EconomicAgent owner) {
        if (owners.size() <= 1) {
            throw new IllegalArgumentException("Cannot remove last owner");
        }
        owners.remove(owner);
    }

    /**
     * Returns the ticker symbol.
     * @return the symbol
     */
    public String getSymbol() {
        return id.toString();
    }

    /**
     * Returns the issuing company.
     * @return the organization
     */
    public Organization getOrganization() {
        return company;
    }

    /**
     * Returns the issuing company.
     * @return the company
     */
    public Organization getCompany() {
        return company;
    }

    /**
     * Returns the current market value.
     * @return the value
     */
    @Override
    public Money getValue() {
        return currentValue;
    }

    /**
     * Updates the current market value.
     * @param value the new value
     */
    public void setCurrentValue(Money value) {
        this.currentValue = Objects.requireNonNull(value, "Value cannot be null");
    }

    /**
     * Returns the dividend per share.
     * @return the dividend, or null if not set
     */
    public Money getDividendPerShare() {
        return dividendPerShare;
    }

    /**
     * Sets the dividend per share.
     * @param dividend the dividend amount
     */
    public void setDividendPerShare(Money dividend) {
        this.dividendPerShare = dividend;
    }

    /**
     * Calculates the dividend yield.
     * @return the yield as a percentage
     */
    public double getDividendYield() {
        if (dividendPerShare == null || currentValue.getValue().isZero()) {
            return 0.0;
        }
        return dividendPerShare.getValue().doubleValue() / 
               currentValue.getValue().doubleValue() * 100;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Share other)) return false;
        return Objects.equals(id, other.id) &&
               Objects.equals(company, other.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, company);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s", getSymbol(), company.getName(), currentValue);
    }
}

