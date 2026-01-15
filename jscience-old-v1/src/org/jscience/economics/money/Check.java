package org.jscience.economics.money;

import org.jscience.economics.Organization;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import java.util.Date;


/**
 * A class representing a paper form of payment from one person to another.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this class does not tell if the check was actually paid or not
//look at accounts transactions or receipts

//perhaps we should extend Amount<Money> or something
public final class Check implements Identified {
    /** DOCUMENT ME! */
    private Identification identification; //unique for each check

    /** DOCUMENT ME! */
    private Account emitter;

    /** DOCUMENT ME! */
    private Organization receiver;

    /** DOCUMENT ME! */
    private Date emission;

    /** DOCUMENT ME! */
    private Amount<Money> value;

    //we don't store the place where the check was made because it is not really a trusted value
    /**
     * Creates a new Check object.
     *
     * @param identification DOCUMENT ME!
     * @param emitter DOCUMENT ME!
     * @param receiver DOCUMENT ME!
     * @param emission DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param currency DOCUMENT ME!
     */
    public Check(Identification identification, Account emitter,
        Organization receiver, Date emission, double value, Currency currency) {
        if ((identification != null) && (emitter != null) &&
                (receiver != null) && (emission != null)) {
            this.identification = identification;
            this.emitter = emitter;
            this.receiver = receiver;
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
    public final Account getEmitter() {
        return emitter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Organization getReceiver() {
        return receiver;
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
        return "Check " + identification + " emitted by " +
        emitter.getIdentification().toString() + " to " + receiver.getName() +
        " on " + emission + " is worth " + getAmount() + " " +
        getAmount().getUnit().toString();
    }
}
