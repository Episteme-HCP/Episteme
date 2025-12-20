/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.time.LocalDate;

/**
 * Represents a trade/transaction.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Trade {

    public enum Type {
        BUY, SELL, EXCHANGE, BARTER, LEASE, LICENSE
    }

    private final String id;
    private final Type type;
    private final String buyer;
    private final String seller;
    private final String item;
    private final Money amount;
    private final LocalDate date;
    private String description;
    private boolean completed;

    public Trade(String id, Type type, String buyer, String seller, String item, Money amount) {
        this.id = id;
        this.type = type;
        this.buyer = buyer;
        this.seller = seller;
        this.item = item;
        this.amount = amount;
        this.date = LocalDate.now();
    }

    // Getters
    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public String getItem() {
        return item;
    }

    public Money getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Completes the trade.
     */
    public void complete() {
        this.completed = true;
    }

    @Override
    public String toString() {
        return String.format("Trade[%s] %s: %s -> %s for %s",
                id, type, seller, buyer, amount);
    }
}
