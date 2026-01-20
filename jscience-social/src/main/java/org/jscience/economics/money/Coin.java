package org.jscience.economics.money;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import java.util.Date;


//import java.util.Currency
//should replace our currency class from org.jscience.economics.money.Currency
/**
 * A class representing a metal or paper form of payment from one person to
 * another.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you have to provide yourself with actual values
//for example 1 euroCent, 2 euroCents, 5 euroCents, 10 euroCents, 20 euroCents, 50 euroCents, 1 euros, 2 euros

//perhaps we should extend Amount<Money> or something
public final class Coin implements Identified {
    /** DOCUMENT ME! */
    private Identification identification; //unique for each coin

    /** DOCUMENT ME! */
    private Date emission;

    /** DOCUMENT ME! */
    private Amount<Money> value;

    //value is the facial value
    /**
     * Creates a new Coin object.
     *
     * @param value DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param emission DOCUMENT ME!
     * @param currency DOCUMENT ME!
     */
    public Coin(double value, Identification identification, Date emission,
        Currency currency) {
        if ((identification != null) && (emission != null)) {
            this.identification = identification;
            this.emission = emission;
            this.value = Amount.valueOf(value, currency);
        } else {
            throw new IllegalArgumentException(
                "Check doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Date getEmission() {
        return emission;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getAmount() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final String toString() {
        return "Coin " + identification + " emitted on " + emission +
        " is worth " + getAmount() + " " + getAmount().getUnit().toString();
    }
}
