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

import org.jscience.social.economics.Organization;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.social.history.time.TimeCoordinate;

import java.util.Objects;

/**
 * Represents a receipt for a completed transaction.
 * <p>
 * A receipt is an immutable record of an exchange of money or shares
 * between a seller and buyer.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public final class Receipt implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Organization seller;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Organization buyer;

    @Attribute
    private final TimeCoordinate date;

    @Id
    private final Identification id;

    @Attribute
    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Attribute
    private final Money amount;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Share share;

    @Attribute
    private final int quantity;

    /**
     * Creates a receipt for a money transaction.
     *
     * @param seller         the selling organization
     * @param buyer          the buying organization
     * @param date           the transaction date
     * @param identification unique receipt ID
     * @param description    transaction description
     * @param amount         the monetary amount
     */
    public Receipt(Organization seller, Organization buyer, TimeCoordinate date,
                   Identification id, String description, Money amount) {
        this.seller = Objects.requireNonNull(seller, "Seller cannot be null");
        this.buyer = Objects.requireNonNull(buyer, "Buyer cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        setName(requireNonEmpty(description, "Description"));
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.share = null;
        this.quantity = 0;
    }

    /**
     * Creates a receipt for a share transaction.
     *
     * @param seller         the selling organization
     * @param buyer          the buying organization
     * @param date           the transaction date
     * @param identification unique receipt ID
     * @param description    transaction description
     * @param share          the share being traded
     * @param quantity       number of shares
     */
    public Receipt(Organization seller, Organization buyer, TimeCoordinate date,
                   Identification id, String description, 
                   Share share, int quantity) {
        this.seller = Objects.requireNonNull(seller, "Seller cannot be null");
        this.buyer = Objects.requireNonNull(buyer, "Buyer cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        setName(requireNonEmpty(description, "Description"));
        this.share = Objects.requireNonNull(share, "Share cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
        this.amount = Money.valueOf(Real.ZERO, Currency.USD);
    }

    private static String requireNonEmpty(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return value;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    // Getters

    public Organization getSeller() {
        return seller;
    }

    public Organization getBuyer() {
        return buyer;
    }

    public TimeCoordinate getDate() {
        return date;
    }

    public Identification getIdentification() {
        return id;
    }

    public String getDescription() {
        return getName();
    }

    public Money getAmount() {
        return amount;
    }

    public Share getShare() {
        return share;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns true if this is a money transaction (not shares).
     * @return true for money transaction
     */
    public boolean isMoneyTransaction() {
        return share == null;
    }

    /**
     * Returns true if this is a share transaction.
     * @return true for share transaction
     */
    public boolean isShareTransaction() {
        return share != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Receipt other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        if (isMoneyTransaction()) {
            return String.format("Receipt %s: %s -> %s, %s, %s",
                id, seller.getName(), buyer.getName(), amount, date);
        } else {
            return String.format("Receipt %s: %s -> %s, %d x %s, %s",
                id, seller.getName(), buyer.getName(), 
                quantity, share.getSymbol(), date);
        }
    }
}

