/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.Objects;

/**
 * Represents a monetary amount with currency.
 */
public class Money {
    private final Real amount;
    private final Currency currency;

    public Money(Real amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }

    public Money(double amount, Currency currency) {
        this(Real.of(amount), currency);
    }

    public Real getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(Real factor) {
        return new Money(amount.multiply(factor), currency);
    }

    private void requireSameCurrency(Money other) {
        if (!currency.getCode().equals(other.currency.getCode())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }

    @Override
    public String toString() {
        return String.format("%s%.2f", currency.getSymbol(), amount.doubleValue());
    }

    public static Money usd(double amount) {
        return new Money(amount, Currency.USD);
    }

    public static Money eur(double amount) {
        return new Money(amount, Currency.EUR);
    }
}
