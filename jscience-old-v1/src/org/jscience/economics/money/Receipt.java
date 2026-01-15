package org.jscience.economics.money;

import org.jscience.economics.Organization;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import java.util.Date;


/**
 * A class representing a receipt for the person who pays or a bill for the
 * person who provides the goods or services.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is the same class as transaction except it is provided to get sure you don't reuse the same transaction twice
//final modifier used to secure the access
public final class Receipt extends Object implements Identified {
    /** DOCUMENT ME! */
    private Organization seller;

    /** DOCUMENT ME! */
    private Organization buyer;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Identification identification;

    /** DOCUMENT ME! */
    private String description;

    /** DOCUMENT ME! */
    private Amount<Money> amount;

    /** DOCUMENT ME! */
    private Share share;

    /** DOCUMENT ME! */
    private int quantity;

    //money is transfered from the buyer to the seller
    /**
     * Creates a new Receipt object.
     *
     * @param seller DOCUMENT ME!
     * @param buyer DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param money DOCUMENT ME!
     */
    public Receipt(Organization seller, Organization buyer, Date date,
        Identification identification, String description, Amount<Money> money) {
        if ((seller != null) && (buyer != null) && (date != null) &&
                (identification != null) && (description != null) &&
                (description.length() > 0) && (money != null)) {
            this.seller = seller;
            this.buyer = buyer;
            this.date = date;
            this.identification = identification;
            this.description = description;
            this.amount = amount;
            this.share = null;
            this.quantity = 0;
        } else {
            throw new IllegalArgumentException(
                "Receipt doesn't accept null arguments and description can't be empty.");
        }
    }

    //money is transfered from the buyer to the seller
    /**
     * Creates a new Receipt object.
     *
     * @param seller DOCUMENT ME!
     * @param buyer DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param share DOCUMENT ME!
     * @param quantity DOCUMENT ME!
     */
    public Receipt(Organization seller, Organization buyer, Date date,
        Identification identification, String description, Share share,
        int quantity) {
        if ((seller != null) && (buyer != null) && (date != null) &&
                (identification != null) && (description != null) &&
                (description.length() > 0) && (share != null)) {
            if (quantity > 0) {
                this.seller = seller;
                this.buyer = buyer;
                this.date = date;
                this.identification = identification;
                this.description = description;
                this.amount = Amount.valueOf(0, Currency.USD);
                this.share = share;
                this.quantity = quantity;
            } else {
                throw new IllegalArgumentException(
                    "The quantity must be positive.");
            }
        } else {
            throw new IllegalArgumentException(
                "Receipt doesn't accept null arguments and description can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Organization getSeller() {
        return seller;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Organization getBuyer() {
        return buyer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Date getDate() {
        return date;
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
    public final String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Amount<Money> getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Share getShare() {
        return share;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int getQuantity() {
        return quantity;
    }

    //otherwise you traded shares
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMoneyTransaction() {
        return quantity == 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Receipt getReceipt() {
        if (isMoneyTransaction()) {
            return new Receipt(seller, buyer, date, identification,
                description, amount);
        } else {
            return new Receipt(seller, buyer, date, identification,
                description, share, quantity);
        }
    }
}
