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

import org.jscience.measure.Unit;
import org.jscience.measure.StandardUnit;
import org.jscience.measure.UnitConverter;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a currency with ISO 4217 code.
 * Implements Unit&lt;Money&gt; to allow integration with the measurement system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Currency extends StandardUnit<Money> implements org.jscience.util.identity.Identifiable<String> {

    private final String code; // ISO 4217 code (e.g., "USD")
    private final int decimals; // Minor unit decimals

    public Currency(String code, String name, String symbol, int decimals) {
        super(symbol, name, Money.DIMENSION);
        this.code = code;
        this.decimals = decimals;
    }

    @Override
    public String getId() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public int getDecimals() {
        return decimals;
    }

    @Override
    public String toString() {
        return code;
    }
    
    @Override
    public UnitConverter getConverterTo(Unit<Money> target) {
        if (this.equals(target)) {
            return UnitConverter.identity();
        }
        throw new UnsupportedOperationException("Currency conversion between " + this.code + " and " + target + " requires an explicit Exchange Rate.");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Currency)) return false;
        Currency other = (Currency) obj;
        return code.equals(other.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    // --- Flyweight Registry ---

    // Common currencies
    public static final Currency USD = new Currency("USD", "US Dollar", "$", 2);
    public static final Currency EUR = new Currency("EUR", "Euro", "€", 2);
    public static final Currency GBP = new Currency("GBP", "British Pound", "£", 2);
    public static final Currency JPY = new Currency("JPY", "Japanese Yen", "¥", 0);
    public static final Currency CNY = new Currency("CNY", "Chinese Yuan", "¥", 2);
    public static final Currency CHF = new Currency("CHF", "Swiss Franc", "CHF", 2);

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
