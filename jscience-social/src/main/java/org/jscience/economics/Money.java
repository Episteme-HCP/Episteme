/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import java.util.Objects;

/**
 * Represents a monetary amount with currency.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Money {
    private final Real amount;
    private final Currency currency;

    public Money(Real amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }

    public Money(Real amount, String currencyCode) {
        this(amount, Currency.of(currencyCode));
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

    public Money divide(Real divisor) {
        return new Money(amount.divide(divisor), currency);
    }

    private void requireSameCurrency(Money other) {
        if (!currency.getCode().equals(other.currency.getCode())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
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
