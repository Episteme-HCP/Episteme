package org.jscience.economics.money;

import org.jscience.economics.Bank;

import org.jscience.measure.Amount;

import java.util.Iterator;
import java.util.Vector;


//import java.util.Currency
//should replace our currency class from org.jscience.economics.money.Currency
/**
 * A class representing a wallet (a store of money). It has no value by
 * itself. This can be an electronic wallet. Yet this is only very basic here.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//final modifier used to secure the access
//but perhaps we should allow developers to provide better implementations by subclassing
//perhaps we should get rid of this class and bundle it into EconomicAgent
public final class Wallet extends Object {
    /** DOCUMENT ME! */
    private Vector contents;

/**
     * Creates a new Wallet object.
     */
    public Wallet() {
        contents = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Vector getContents() {
        return contents;
    }

    //the change is made in the bank as argument
    /**
     * DOCUMENT ME!
     *
     * @param bank DOCUMENT ME!
     * @param resultCurrency DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getValue(Bank bank, Currency resultCurrency) {
        Iterator iterator;
        Amount<Money> currentAmount;
        Amount<Money> result;

        if ((bank != null) && (resultCurrency != null)) {
            result = Amount.valueOf(0, resultCurrency);
            iterator = contents.iterator();

            while (iterator.hasNext()) {
                currentAmount = (Amount<Money>) iterator.next();
                result.plus(currentAmount);
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can't get the value for a null Bank or null Currency.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //the current good idea is to add coin after coin here
    public final void addValue(Amount<Money> amount) {
        if (amount != null) {
            contents.add(amount);
        } else {
            throw new IllegalArgumentException("You can't add a null Money.");
        }
    }

    //you can only remove stored values AS they were added
    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public final void removeValue(Amount<Money> amount) {
        contents.remove(amount);
    }

    //perhaps we should provide a method to compact (and transform) contents
    //and another method to remove the exact amount if available
}
