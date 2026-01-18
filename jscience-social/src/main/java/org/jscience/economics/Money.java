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

package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.Dimension;
import org.jscience.measure.Quantities;
import org.jscience.measure.UnitConverter;

import java.util.Objects;

/**
 * Represents a quantity of money with currency.
 * Implements Quantity&lt;Money&gt; to allow integration with the measurement system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Money implements Quantity<Money> {
    
    public static final Dimension DIMENSION = Dimension.NONE;

    private final Real amount;
    private final Currency currency;

    public Money(Real amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }

    public Money(Real amount, String currencyCode) {
        this(amount, Currency.of(currencyCode));
    }

    @Override
    public Real getValue() {
        return amount;
    }

    @Override
    public Unit<Money> getUnit() {
        return currency;
    }

    @Override
    public Money to(Unit<Money> targetUnit) {
        if (targetUnit.equals(currency)) {
            return this;
        }
        UnitConverter converter = currency.getConverterTo(targetUnit);
        Real newVal = converter.convert(amount);
        if (targetUnit instanceof Currency) {
            return new Money(newVal, (Currency) targetUnit);
        }
        // If converting to a generic Unit<Money>, we can't maintain Money class contract 
        // unless we wrap it or Money supports generic units.
        // But Money(Real, Currency) enforces Currency.
        // So this might fail if targetUnit is not a Currency.
        // However, for Money, Units ARE Currencies.
        throw new IllegalArgumentException("Target unit must be a Currency");
    }

    @Override
    public Money add(Quantity<Money> other) {
        if (other.getUnit().equals(currency)) {
            return new Money(amount.add(other.getValue()), currency);
        }
        return add(other.to(currency));
    }

    @Override
    public Money subtract(Quantity<Money> other) {
        if (other.getUnit().equals(currency)) {
            return new Money(amount.subtract(other.getValue()), currency);
        }
        return subtract(other.to(currency));
    }

    public Money add(Money other) {
        return add((Quantity<Money>) other);
    }

    public Money subtract(Money other) {
        return subtract((Quantity<Money>) other);
    }

    @Override
    public Money multiply(Real scalar) {
        return new Money(amount.multiply(scalar), currency);
    }
    
    // Covariant method for compatibility with existing code using double
    public Money multiply(double scalar) {
        return multiply(Real.of(scalar));
    }

    @Override
    public <R extends Quantity<R>> Quantity<?> multiply(Quantity<R> other) {
        Real val = amount.multiply(other.getValue());
        Unit<?> unit = currency.multiply(other.getUnit());
        return Quantities.create(val, (Unit<?>) unit);
    }

    @Override
    public Money divide(Real scalar) {
        return new Money(amount.divide(scalar), currency);
    }
    
    public Money divide(double scalar) {
        return divide(Real.of(scalar));
    }

    @Override
    public <R extends Quantity<R>> Quantity<?> divide(Quantity<R> other) {
        Real val = amount.divide(other.getValue());
        Unit<?> unit = currency.divide(other.getUnit());
        return Quantities.create(val, (Unit<?>) unit);
    }

    @Override
    public Money abs() {
        return new Money(amount.abs(), currency);
    }

    @Override
    public Money negate() {
        return new Money(amount.negate(), currency);
    }

    @Override
    public int compareTo(Quantity<Money> other) {
        return amount.compareTo(other.to(currency).getValue());
    }

    @Override
    public boolean equals(Quantity<Money> other, Real tolerance) {
         if (other == null) return false;
         try {
             return this.subtract(other).getValue().abs().compareTo(tolerance) <= 0;
         } catch(Exception e) {
             return false;
         }
    }

    @Override
    public Quantity<?> pow(int exponent) {
        return Quantities.create(amount.pow(exponent).doubleValue(), currency.pow(exponent));
    }

    @Override
    public Quantity<?> sqrt() {
        return Quantities.create(amount.sqrt().doubleValue(), currency.sqrt());
    }

    // --- Legacy / Convenience Methods ---

    public Real getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return String.format("%s%s", currency.getSymbol(), amount.toString());
    }

    public static Money usd(Real amount) {
        return new Money(amount, Currency.USD);
    }

    public static Money usd(double amount) {
        return new Money(Real.of(amount), Currency.USD);
    }

    public static Money eur(Real amount) {
        return new Money(amount, Currency.EUR);
    }

    public static Money eur(double amount) {
        return new Money(Real.of(amount), Currency.EUR);
    }
}
