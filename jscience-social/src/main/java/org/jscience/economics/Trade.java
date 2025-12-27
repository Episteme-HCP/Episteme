/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.economics;

import java.time.LocalDate;

/**
 * Represents a trade/transaction.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
