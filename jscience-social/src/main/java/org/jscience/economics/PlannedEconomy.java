package org.jscience.economics;
 
import java.util.Set;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.economics.money.Money;
 
/**
 * A class representing a concrete implementation of an Economy for planned economics.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class PlannedEconomy extends Economy {
 
    public PlannedEconomy(Set<Organization> orgs, Bank centralBank) {
        super(orgs, centralBank);
    }
 
    @Override
    public void step(Real dt) {
        // Implement planned economy logic:
        // 1. Bail out failing organizations
        for (Organization organization : getOrganizations()) {
            if (organization.getCapital().getValue().compareTo(Real.ZERO) < 0) {
                // Bailout: Reset capital to positive
                organization.setCapital(Money.usd(1000.0));
            }
        }
    }
}
