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

package org.jscience.economics.money;

import org.jscience.economics.Organization;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a financial transaction between two parties.
 * <p>
 * A transaction records the exchange of money or shares between a seller
 * and buyer, and can generate receipts for both parties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public final class Transaction implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    /**
     * Transaction status.
     */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Organization seller;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Organization buyer;

    @Attribute
    private final Instant date;

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

    @Attribute
    private TransactionStatus status;

    /**
     * Creates a money transaction.
     *
     * @param seller         the selling organization
     * @param buyer          the buying organization
     * @param date           the transaction date
     * @param identification unique transaction ID
     * @param description    transaction description
     * @param amount         the monetary amount
     */
    public Transaction(Organization seller, Organization buyer, Instant date,
                       Identification id, String description, Money amount) {
        this.seller = Objects.requireNonNull(seller, "Seller cannot be null");
        this.buyer = Objects.requireNonNull(buyer, "Buyer cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        setName(requireNonEmpty(description, "Description"));
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.share = null;
        this.quantity = 0;
        this.status = TransactionStatus.PENDING;
    }

    /**
     * Creates a share transaction.
     *
     * @param seller         the selling organization
     * @param buyer          the buying organization
     * @param date           the transaction date
     * @param identification unique transaction ID
     * @param description    transaction description
     * @param share          the share being traded
     * @param quantity       number of shares
     */
    public Transaction(Organization seller, Organization buyer, Instant date,
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
        this.status = TransactionStatus.PENDING;
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

    public Instant getDate() {
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

    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * Updates the transaction status.
     * @param status the new status
     */
    public void setStatus(TransactionStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    /**
     * Marks the transaction as completed.
     */
    public void complete() {
        this.status = TransactionStatus.COMPLETED;
    }

    /**
     * Marks the transaction as cancelled.
     */
    public void cancel() {
        this.status = TransactionStatus.CANCELLED;
    }

    /**
     * Returns true if this is a money transaction.
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

    /**
     * Calculates the total value of a share transaction.
     * @return the total value (share price × quantity)
     */
    public Money getTotalValue() {
        if (isMoneyTransaction()) {
            return amount;
        }
        return share.getValue().multiply(Real.of(quantity));
    }

    /**
     * Generates a receipt for this transaction.
     * @return the receipt
     */
    public Receipt getReceipt() {
        if (isMoneyTransaction()) {
            return new Receipt(seller, buyer, date, id, getName(), amount);
        } else {
            return new Receipt(seller, buyer, date, id, getName(), share, quantity);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transaction other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        if (isMoneyTransaction()) {
            return String.format("Transaction %s [%s]: %s -> %s, %s",
                id, status, seller.getName(), buyer.getName(), amount);
        } else {
            return String.format("Transaction %s [%s]: %s -> %s, %d x %s",
                id, status, seller.getName(), buyer.getName(), 
                quantity, share.getSymbol());
        }
    }
}
