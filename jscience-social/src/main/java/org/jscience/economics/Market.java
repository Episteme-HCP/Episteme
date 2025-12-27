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

import java.util.HashMap;
import java.util.Map;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a market where goods are traded.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Market {

    private final String name;
    private final Currency currency;
    private final Map<String, Money> prices;
    private final Map<String, Integer> inventory;

    public Market(String name, Currency currency) {
        this.name = name;
        this.currency = currency;
        this.prices = new HashMap<>();
        this.inventory = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the price for a specific good.
     */
    public void setPrice(String good, Money price) {
        if (!price.getCurrency().equals(currency)) {
            throw new IllegalArgumentException("Price currency mismatch");
        }
        prices.put(good, price);
    }

    /**
     * Gets the price of a good.
     */
    public Money getPrice(String good) {
        return prices.get(good);
    }

    /**
     * Adds stock to the market.
     */
    public void addStock(String good, int quantity) {
        inventory.merge(good, quantity, (a, b) -> a + b);
    }

    /**
     * Checks stock level.
     */
    public int getStock(String good) {
        return inventory.getOrDefault(good, 0);
    }

    /**
     * Buys a good from the market.
     * Decreases inventory.
     * 
     * @param good     to buy
     * @param quantity amount to buy
     * @return Total cost
     * @throws IllegalStateException if insufficient stock
     */
    public Money buy(String good, int quantity) {
        int currentStock = getStock(good);
        if (currentStock < quantity) {
            throw new IllegalStateException("Insufficient stock for " + good);
        }

        Money price = prices.get(good);
        if (price == null) {
            throw new IllegalArgumentException("Good not traded: " + good);
        }

        inventory.put(good, currentStock - quantity);
        return price.multiply(Real.of(quantity));
    }

    /**
     * Sells a good to the market.
     * Increases inventory.
     * 
     * @param good     to sell
     * @param quantity amount to sell
     * @return Total earnings
     */
    public Money sell(String good, int quantity) {
        Money price = prices.get(good);
        if (price == null) {
            throw new IllegalArgumentException("Good not traded: " + good);
        }

        inventory.merge(good, quantity, (a, b) -> a + b);
        return price.multiply(Real.of(quantity));
    }
}
