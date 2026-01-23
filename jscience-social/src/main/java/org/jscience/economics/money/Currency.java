/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics.money;

import org.jscience.measure.Dimension;
import org.jscience.measure.StandardUnit;
import org.jscience.measure.Unit;
import org.jscience.measure.UnitConverter;
import org.jscience.util.identity.Identified;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> This class represents a currency {@link org.jscience.measure.Unit Unit}.
 * Currencies are a special form of unit, conversions
 * between currencies is possible if their respective exchange rates
 * have been set and the conversion factor can be changed dynamically.</p>
 * <p/>
 * <p> Quantities stated in {@link Currency} are usually instances of
 * {@link Money}.</p>
 * <p/>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public class Currency extends StandardUnit<Money> implements Identified<String> {

    /**
     * Holds the Reference currency.
     */
    private static Currency REFERENCE; // Will be set to USD later

    /**
     * Holds the exchanges rate to the reference currency.
     */
    private static final Map<String, Double> TO_REFERENCE = new HashMap<>();

    /**
     * The Australian Dollar currency unit.
     */
    public static final Currency AUD = new Currency("AUD");

    /**
     * The Canadian Dollar currency unit.
     */
    public static final Currency CAD = new Currency("CAD");

    /**
     * The China Yan currency.
     */
    public static final Currency CNY = new Currency("CNY");

    /**
     * The Euro currency.
     */
    public static final Currency EUR = new Currency("EUR");

    /**
     * The British Pound currency.
     */
    public static final Currency GBP = new Currency("GBP");

    /**
     * The Japanese Yen currency.
     */
    public static final Currency JPY = new Currency("JPY");

    /**
     * The Korean Republic Won currency.
     */
    public static final Currency KRW = new Currency("KRW");

    /**
     * The Taiwanese dollar currency.
     */
    public static final Currency TWD = new Currency("TWD");

    /**
     * The United State dollar currency.
     */
    public static final Currency USD = new Currency("USD");

    static {
        REFERENCE = USD;
        // Initialize default exchange rates
        TO_REFERENCE.put(USD.getCode(), 1.0);
    }

    private final String code;

    /**
     * Creates the currency unit for the given currency code.
     *
     * @param code the ISO-4217 code of the currency.
     */
    public Currency(String code) {
        super(code, code, Money.DIMENSION);
        this.code = code;
        // Self-register default exchange rate if referencing itself or USD default
        if (code.equals("USD")) {
             TO_REFERENCE.put(code, 1.0);
        }
    }
    
    public static Currency of(String code) {
        // Simple factory - in a real app, use a cache or look up existing static fields
        if ("USD".equals(code)) return USD;
        if ("EUR".equals(code)) return EUR;
        return new Currency(code);
    }

    /**
     * Returns the currency code for this currency.
     *
     * @return the ISO-4217 code.
     */
    public String getCode() {
        return code;
    }

    @Override
    public String getId() {
        return code;
    }

    /**
     * Sets the reference currency (context-local).
     */
    public static void setReferenceCurrency(Currency currency) {
        REFERENCE = currency;
        // Reset rates relative to new reference? 
        // For simplicity, we assume TO_REFERENCE stores rates relative to the CURRENT reference.
        TO_REFERENCE.clear();
        TO_REFERENCE.put(currency.getCode(), 1.0);
    }

    public static Currency getReferenceCurrency() {
        return REFERENCE;
    }

    public void setExchangeRate(double refAmount) {
        TO_REFERENCE.put(this.getCode(), refAmount);
    }

    public double getExchangeRate() {
        Double rate = TO_REFERENCE.get(this.getCode());
        if (rate == null) throw new IllegalStateException("Exchange rate not set for " + code);
        return rate;
    }

    @Override
    public UnitConverter getConverterTo(Unit<Money> targetUnit) {
        if (targetUnit.equals(this)) return UnitConverter.identity();
        
        if (targetUnit instanceof Currency) {
            Currency target = (Currency) targetUnit;
            double thisRate = this.getExchangeRate(); // Value of 1 unit of this in Reference
            double targetRate = target.getExchangeRate(); // Value of 1 unit of target in Reference
            
            // 1 This = thisRate Reference
            // 1 Target = targetRate Reference => 1 Reference = 1/targetRate Target
            // 1 This = (thisRate / targetRate) Target
            
            return new org.jscience.measure.converters.MultiplyConverter(
                 org.jscience.mathematics.numbers.real.Real.of(thisRate / targetRate));
        }
        
        return super.getConverterTo(targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Currency)) return false;
        Currency that = (Currency) obj;
        return this.code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
