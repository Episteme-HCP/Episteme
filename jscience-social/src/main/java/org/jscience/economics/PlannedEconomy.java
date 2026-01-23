package org.jscience.economics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class representing a concrete implementation of an Economy for planned economics.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//it is highly expected that you provide alternate implementations to this class
public class PlannedEconomy extends Economy {

    public PlannedEconomy(Set<Organization> orgs, Bank centralBank) {
        super(orgs, centralBank);
    }

    //scheduler
    //you have to build up the scheduler yourselves as you have to decide what your society needs

    //here is what is done:
    //don't remove all organizations with capital well under 0 as they are supported by the central plan
    //central bank creates money
    //organizations are given away "capital" by the central plan thinkers
    //organizations "buy" resources or they are simply given to them by the central plan thinkers
    //resources are transformed into products according to work that is done
    //organizations sell to consumers or give away to them according to the plan
    //"capital" is given back to banks or not as central plans thinkers also own the banks
    //may be new organizations are added to the system
    //
    //this is really the raw mechanism from which considerable variation may be observed in real cases
    //
    //Our basic implementation is not really different from the FreeMarket system (except we don't remove bankrupt organizations).
    //You should consider an additional mechanism:
    //a more correct way to think about a planned economy may be to imagine a single StateOrganization
    //with branches in every industry, therefore exchanging resources internally within itself
    //(thus with no money or so called "sales" even if resources have a value)
    //
    //although we step from dt, we don't use that value
    //all calls are therefore meant to be atomic

    @Override
    public void step(double dt) {
        // Implement planned economy logic:
        // 1. Bail out failing organizations
        for (Organization organization : getOrganizations()) {
            if (organization.getCapital().getValue().doubleValue() < 0) {
                // Bailout: Reset capital to positive
                organization.setCapital(org.jscience.economics.money.Money.usd(1000.0));
            }
        }

        // 2. Production Simulation
        for (Organization organization : getOrganizations()) {
            if (organization instanceof Factory) {
                // Simple production: factories generate value
                Factory factory = (Factory) organization;
                // Assume factory produces value equivalent to 1% of capital per step
                org.jscience.economics.money.Money production = organization.getCapital().multiply(org.jscience.mathematics.numbers.real.Real.of(0.01));
                organization.setCapital(organization.getCapital().add(production));
            }
        }

        // 3. Central Bank Growth (Inflation/Money Supply)
        // Add random new organizations rarely
        if (Math.random() < 0.05) {
             // Create a new factory occasionally
             // Factory newFactory = new Factory("State Factory " + System.currentTimeMillis());
             // getOrganizations().add(newFactory);
             // getCentralBank().createMoney(newFactory, 10000);
        }
    }

}
