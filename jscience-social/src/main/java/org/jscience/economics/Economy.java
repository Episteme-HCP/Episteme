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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;

/**
 * Higher-level model for a macro or micro economy.
 * Manages the flow of resources between organizations and the role of the central bank.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class Economy implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<Organization> organizations;
    private Bank centralBank;
    private Money cachedGdp;
    private Real inflationRate = Real.ZERO;
    private Real unemploymentRate = Real.ZERO;

    /**
     * Initializes an economy with a central bank.
     */
    public Economy(Set<Organization> orgs, Bank centralBank) {
        this.centralBank = Objects.requireNonNull(centralBank, "Central bank cannot be null");
        this.organizations = new HashSet<>(Objects.requireNonNull(orgs, "Organization set cannot be null"));
        this.organizations.remove(centralBank);
    }

    public Set<Organization> getOrganizations() {
        return Collections.unmodifiableSet(organizations);
    }

    public Bank getCentralBank() { return centralBank; }

    public void addOrganization(Organization org) {
        if (org != null && org != centralBank) organizations.add(org);
    }

    public void removeOrganization(Organization org) {
        organizations.remove(org);
    }

    /** 
     * Scans the economy for the total quantity of a specific resource.
     */
    public Quantity<?> getNumberOfUnits(Resource resource) {
        if (resource == null) return null;
        Quantity<?> total = null;
        for (Organization org : organizations) {
            for (Resource r : org.getResources()) {
                if (r.getName().equals(resource.getName())) {
                    total = (total == null) ? r.getAmount() : (Quantity) total.add(r.getAmount());
                }
            }
        }
        return total;
    }

    /** Calculates the sum of capital of all organizations in the economy. */
    public Money getValue() {
        Money total = Money.usd(Real.ZERO);
        for (Organization org : organizations) {
            total = total.add(org.getCapital());
        }
        return total;
    }

    public abstract void step(double dt);

    // --- Macro Indicators ---

    public Money getGdp() {
        return cachedGdp != null ? cachedGdp : getValue();
    }

    public void setGdp(Money gdp) { this.cachedGdp = gdp; }

    public Real getInflationRate() { return inflationRate; }
    public void setInflationRate(Real rate) { this.inflationRate = rate; }

    public Real getUnemploymentRate() { return unemploymentRate; }
    public void setUnemploymentRate(Real rate) { this.unemploymentRate = rate; }
}
