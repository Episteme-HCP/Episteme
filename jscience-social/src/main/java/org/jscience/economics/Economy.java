package org.jscience.economics;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jscience.economics.money.Money;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;

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
public abstract class Economy implements Serializable {

    private static final long serialVersionUID = 2L;

    private final Set<Organization> organizations;
    private final Bank centralBank;
    private Money cachedGdp;
    private Real inflationRate = Real.ZERO;
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

    /**
     * Advances the economic simulation by an incremental time step.
     * To be implemented by specific economic models (e.g., FreeMarket, Planned).
     * 
     * @param dt the time delta in simulation units
     */
    public abstract void step(double dt);

    // --- Macro Indicators ---

    /**
     * @return the current Gross Domestic Product (GDP)
     */
    public Money getGdp() {
        return cachedGdp != null ? cachedGdp : getValue();
    }

    public void setGdp(Money gdp) {
        this.cachedGdp = gdp;
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
