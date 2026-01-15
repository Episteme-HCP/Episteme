package org.jscience.economics;

import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;

import java.util.Set;


/**
 * An interface which defines what belongs to whom. Should be implementated by
 * classes corresponding to physical or intellectual property.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//perhaps this interface should only have the first method
public interface Property {
    //a Set of Humans
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOwners();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getValue();
}
