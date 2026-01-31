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

import org.jscience.core.mathematics.numbers.real.Real;

import org.jscience.core.measure.StandardUnit;
import org.jscience.core.measure.Unit;
import org.jscience.core.measure.UnitConverter;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a currency as a unit of monetary measurement.
 * <p>
 * Currencies are special units where conversions depend on exchange rates
 * that can change dynamically. Quantities stated in Currency are typically
 * instances of {@link Money}.
 * * @see <a href="https://www.iso.org/iso-4217-currency-codes.html">ISO 4217</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Currency extends StandardUnit<Money> implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;


    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    /**
     * Holds the Reference currency.
     */
    private static Currency REFERENCE;

    /**
     * Holds the exchanges rate to the reference currency.
     */
    private static final Map<String, Real> TO_REFERENCE = new HashMap<>();

    /**
     * Australian Dollar (A$)
     */
    public static final Currency AUD = new Currency("AUD", "$", 2);

    /**
     * Canadian Dollar (C$)
     */
    public static final Currency CAD = new Currency("CAD", "$", 2);

    /**
     * Chinese Yuan (Â¥)
     */
    public static final Currency CNY = new Currency("CNY", "Â¥", 2);

    /**
     * Euro (â‚¬)
     */
    public static final Currency EUR = new Currency("EUR", "â‚¬", 2);

    /**
     * British Pound (Â£)
     */
    public static final Currency GBP = new Currency("GBP", "Â£", 2);

    /**
     * Hong Kong Dollar (HK$)
     */
    public static final Currency HKD = new Currency("HKD", "$", 2);

    /**
     * Indian Rupee (â‚¹)
     */
    public static final Currency INR = new Currency("INR", "â‚¹", 2);

    /**
     * Japanese Yen (Â¥)
     */
    public static final Currency JPY = new Currency("JPY", "Â¥", 0);

    /**
     * Korean Republic Won (â‚©)
     */
    public static final Currency KRW = new Currency("KRW", "â‚©", 0);

    /**
     * Russian Ruble (â‚½)
     */
    public static final Currency RUB = new Currency("RUB", "â‚½", 2);

    /**
     * Swiss Franc (CHF)
     */
    public static final Currency CHF = new Currency("CHF", "Fr", 2);

    /**
     * Taiwanese dollar (NT$)
     */
    public static final Currency TWD = new Currency("TWD", "$", 2);

    /**
     * United State dollar ($)
     */
    public static final Currency USD = new Currency("USD", "$", 2);

    /**
     * Bitcoin (â‚¿) - Conceptual
     */
    public static final Currency BTC = new Currency("BTC", "â‚¿", 8);

    static {
        REFERENCE = USD;
        TO_REFERENCE.put(USD.getCode(), Real.ONE);
    }

    @Attribute
    private final String symbol;

    @Attribute
    private final int fractionalDigits;

    /**
     * Creates a currency with full details.
     *
     * @param name             the currency name
     * @param code             the ISO-4217 code
     * @param symbol           the currency symbol
     * @param fractionalDigits the number of fractional digits
     */
    public Currency(String name, String code, String symbol, int fractionalDigits) {
        super(code, code, Money.DIMENSION);
        this.id = new SimpleIdentification(code);
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        this.symbol = symbol != null ? symbol : code;
        this.fractionalDigits = fractionalDigits;
        
        if (code.equals("USD") && !TO_REFERENCE.containsKey("USD")) {
             TO_REFERENCE.put(code, Real.ONE);
        }
    }

    /**
     * Creates the currency unit for the given currency code.
     *
     * @param code             the ISO-4217 code of the currency.
     * @param symbol           the currency symbol.
     * @param fractionalDigits the number of fractional digits.
     */
    public Currency(String code, String symbol, int fractionalDigits) {
        this(code, code, symbol, fractionalDigits);
    }

    /**
     * Creates a currency unit with default settings.
     */
    public Currency(String code) {
        this(code, code, 2);
    }

    
    public static Currency of(String code) {
        switch (code) {
            case "USD": return USD;
            case "EUR": return EUR;
            case "GBP": return GBP;
            case "JPY": return JPY;
            case "CNY": return CNY;
            case "RUB": return RUB;
            case "CHF": return CHF;
            case "INR": return INR;
            case "AUD": return AUD;
            case "CAD": return CAD;
            default: return new Currency(code);
        }
    }

    /**
     * Returns the currency code for this currency.
     *
     * @return the ISO-4217 code.
     */
    public String getCode() {
        return getId().toString();
    }

    /**
     * Returns the currency symbol.
     */
    @Override
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the number of fractional digits for this currency.
     */
    public int getFractionalDigits() {
        return fractionalDigits;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Sets the reference currency.
     */
    public static void setReferenceCurrency(Currency currency) {
        Objects.requireNonNull(currency);
        REFERENCE = currency;
        TO_REFERENCE.clear();
        TO_REFERENCE.put(currency.getCode(), Real.ONE);
    }

    public static Currency getReferenceCurrency() {
        return REFERENCE;
    }

    /**
     * Sets the exchange rate of this currency relative to the reference currency.
     * @param refAmount value of 1 unit of this currency in reference currency.
     */
    public void setExchangeRate(Real refAmount) {
        TO_REFERENCE.put(this.getCode(), refAmount);
    }

    public void setExchangeRate(double refAmount) {
        setExchangeRate(Real.of(refAmount));
    }

    /**
     * Returns the exchange rate of this currency.
     */
    public Real getExchangeRate() {
        Real rate = TO_REFERENCE.get(this.getCode());
        if (rate == null) throw new IllegalStateException("Exchange rate not set for " + getCode());
        return rate;
    }

    @Override
    public UnitConverter getConverterTo(Unit<Money> targetUnit) {
        if (targetUnit.equals(this)) return UnitConverter.identity();
        
        if (targetUnit instanceof Currency) {
            Currency target = (Currency) targetUnit;
            Real thisRate = this.getExchangeRate();
            Real targetRate = target.getExchangeRate();
            
            return new org.jscience.core.measure.converters.MultiplyConverter(thisRate.divide(targetRate));
        }
        
        return super.getConverterTo(targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Currency that)) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

