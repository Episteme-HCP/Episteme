/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a currency with ISO 4217 code.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Currency {

    private final String code; // ISO 4217 code (e.g., "USD")
    private final String name; // Full name (e.g., "US Dollar")
    private final String symbol; // Symbol (e.g., "$")
    private final int decimals; // Minor unit decimals

    public Currency(String code, String name, String symbol, int decimals) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getDecimals() {
        return decimals;
    }

    @Override
    public String toString() {
        return code;
    }

    // Common currencies
    public static final Currency USD = new Currency("USD", "US Dollar", "$", 2);
    public static final Currency EUR = new Currency("EUR", "Euro", "€", 2);
    public static final Currency GBP = new Currency("GBP", "British Pound", "£", 2);
    public static final Currency JPY = new Currency("JPY", "Japanese Yen", "¥", 0);
    public static final Currency CNY = new Currency("CNY", "Chinese Yuan", "¥", 2);
    public static final Currency CHF = new Currency("CHF", "Swiss Franc", "CHF", 2);
}
