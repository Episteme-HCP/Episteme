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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a currency with ISO 4217 code.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Currency implements org.jscience.util.identity.Identifiable<String> {

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

    @Override
    public String getId() {
        return code;
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
    public static final Currency EUR = new Currency("EUR", "Euro", "Ã¢â€šÂ¬", 2);
    public static final Currency GBP = new Currency("GBP", "British Pound", "Ã‚Â£", 2);
    public static final Currency JPY = new Currency("JPY", "Japanese Yen", "Ã‚Â¥", 0);
    public static final Currency CNY = new Currency("CNY", "Chinese Yuan", "Ã‚Â¥", 2);
    public static final Currency CHF = new Currency("CHF", "Swiss Franc", "CHF", 2);

    // Registry for flyweight pattern
    private static final Map<String, Currency> CACHE = new HashMap<>();

    static {
        CACHE.put("USD", USD);
        CACHE.put("EUR", EUR);
        CACHE.put("GBP", GBP);
        CACHE.put("JPY", JPY);
        CACHE.put("CNY", CNY);
        CACHE.put("CHF", CHF);
    }

    public static Currency of(String code) {
        if (code == null)
            return null;
        String upper = code.toUpperCase();
        return CACHE.computeIfAbsent(upper, c -> new Currency(c, c, c, 2));
    }
}


