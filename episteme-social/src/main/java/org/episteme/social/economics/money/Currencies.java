/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.economics.money;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry of world currencies based on ISO 4217 standard.
 * <p>
 * This class provides static currency constants and a registry for
 * looking up currencies by code. Currency codes follow the ISO 4217
 * international standard.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://www.iso.org/iso-4217-currency-codes.html">ISO 4217</a>
 */
public final class Currencies {

    private static final Map<String, Currency> REGISTRY = new HashMap<>();

    private Currencies() {
        // Utility class - no instantiation
    }

    // ===== Major World Currencies =====

    /** United States Dollar */
    public static final Currency USD = register(new Currency("US Dollar", "USD", "$", 2));
    
    /** Euro */
    public static final Currency EUR = register(new Currency("Euro", "EUR", "â‚¬", 2));
    
    /** Japanese Yen */
    public static final Currency JPY = register(new Currency("Yen", "JPY", "Â¥", 0));
    
    /** British Pound Sterling */
    public static final Currency GBP = register(new Currency("Pound Sterling", "GBP", "Â£", 2));
    
    /** Swiss Franc */
    public static final Currency CHF = register(new Currency("Swiss Franc", "CHF", "Fr", 2));
    
    /** Canadian Dollar */
    public static final Currency CAD = register(new Currency("Canadian Dollar", "CAD", "CA$", 2));
    
    /** Australian Dollar */
    public static final Currency AUD = register(new Currency("Australian Dollar", "AUD", "A$", 2));
    
    /** Chinese Yuan Renminbi */
    public static final Currency CNY = register(new Currency("Yuan Renminbi", "CNY", "Â¥", 2));
    
    /** Hong Kong Dollar */
    public static final Currency HKD = register(new Currency("Hong Kong Dollar", "HKD", "HK$", 2));
    
    /** New Zealand Dollar */
    public static final Currency NZD = register(new Currency("New Zealand Dollar", "NZD", "NZ$", 2));
    
    /** Singapore Dollar */
    public static final Currency SGD = register(new Currency("Singapore Dollar", "SGD", "S$", 2));
    
    /** South Korean Won */
    public static final Currency KRW = register(new Currency("Won", "KRW", "â‚©", 0));
    
    /** Indian Rupee */
    public static final Currency INR = register(new Currency("Indian Rupee", "INR", "â‚¹", 2));
    
    /** Russian Ruble */
    public static final Currency RUB = register(new Currency("Russian Ruble", "RUB", "â‚½", 2));
    
    /** Brazilian Real */
    public static final Currency BRL = register(new Currency("Brazilian Real", "BRL", "R$", 2));
    
    /** Mexican Peso */
    public static final Currency MXN = register(new Currency("Mexican Peso", "MXN", "MX$", 2));

    // ===== European Currencies =====

    /** Swedish Krona */
    public static final Currency SEK = register(new Currency("Swedish Krona", "SEK", "kr", 2));
    
    /** Norwegian Krone */
    public static final Currency NOK = register(new Currency("Norwegian Krone", "NOK", "kr", 2));
    
    /** Danish Krone */
    public static final Currency DKK = register(new Currency("Danish Krone", "DKK", "kr", 2));
    
    /** Polish Zloty */
    public static final Currency PLN = register(new Currency("Zloty", "PLN", "zÅ‚", 2));
    
    /** Czech Koruna */
    public static final Currency CZK = register(new Currency("Czech Koruna", "CZK", "KÄ", 2));
    
    /** Hungarian Forint */
    public static final Currency HUF = register(new Currency("Forint", "HUF", "Ft", 2));

    // ===== Asian Currencies =====

    /** Thai Baht */
    public static final Currency THB = register(new Currency("Baht", "THB", "à¸¿", 2));
    
    /** Malaysian Ringgit */
    public static final Currency MYR = register(new Currency("Malaysian Ringgit", "MYR", "RM", 2));
    
    /** Indonesian Rupiah */
    public static final Currency IDR = register(new Currency("Rupiah", "IDR", "Rp", 2));
    
    /** Philippine Peso */
    public static final Currency PHP = register(new Currency("Philippine Peso", "PHP", "â‚±", 2));
    
    /** Vietnamese Dong */
    public static final Currency VND = register(new Currency("Dong", "VND", "â‚«", 0));
    
    /** Taiwan Dollar */
    public static final Currency TWD = register(new Currency("New Taiwan Dollar", "TWD", "NT$", 2));

    // ===== Middle East & Africa =====

    /** UAE Dirham */
    public static final Currency AED = register(new Currency("UAE Dirham", "AED", "Ø¯.Ø¥", 2));
    
    /** Saudi Riyal */
    public static final Currency SAR = register(new Currency("Saudi Riyal", "SAR", "ï·¼", 2));
    
    /** Israeli Shekel */
    public static final Currency ILS = register(new Currency("New Israeli Sheqel", "ILS", "â‚ª", 2));
    
    /** Turkish Lira */
    public static final Currency TRY = register(new Currency("Turkish Lira", "TRY", "â‚º", 2));
    
    /** South African Rand */
    public static final Currency ZAR = register(new Currency("Rand", "ZAR", "R", 2));
    
    /** Egyptian Pound */
    public static final Currency EGP = register(new Currency("Egyptian Pound", "EGP", "EÂ£", 2));

    // ===== Americas =====

    /** Argentine Peso */
    public static final Currency ARS = register(new Currency("Argentine Peso", "ARS", "$", 2));
    
    /** Chilean Peso */
    public static final Currency CLP = register(new Currency("Chilean Peso", "CLP", "$", 0));
    
    /** Colombian Peso */
    public static final Currency COP = register(new Currency("Colombian Peso", "COP", "$", 2));
    
    /** Peruvian Sol */
    public static final Currency PEN = register(new Currency("Nuevo Sol", "PEN", "S/", 2));

    // ===== Cryptocurrencies =====

    /** Bitcoin */
    public static final Currency BTC = register(new Currency("Bitcoin", "BTC", "â‚¿", 8));
    
    /** Ethereum */
    public static final Currency ETH = register(new Currency("Ethereum", "ETH", "Îž", 18));

    // ===== Precious Metals =====

    /** Gold (troy ounce) */
    public static final Currency XAU = register(new Currency("Gold", "XAU", "Au", 6));
    
    /** Silver (troy ounce) */
    public static final Currency XAG = register(new Currency("Silver", "XAG", "Ag", 6));
    
    /** Platinum */
    public static final Currency XPT = register(new Currency("Platinum", "XPT", "Pt", 6));
    
    /** Palladium */
    public static final Currency XPD = register(new Currency("Palladium", "XPD", "Pd", 6));

    // ===== Special Currencies =====

    /** SDR (Special Drawing Rights - IMF) */
    public static final Currency XDR = register(new Currency("SDR", "XDR", "SDR", 6));
    
    /** Testing currency */
    public static final Currency XTS = register(new Currency("Test Currency", "XTS", "XTS", 2));
    
    /** No currency */
    public static final Currency XXX = register(new Currency("No Currency", "XXX", "", 0));

    // ===== Convenience Aliases =====

    /** US Dollar alias */
    public static final Currency US_DOLLAR = USD;
    
    /** Euro alias */
    public static final Currency EURO = EUR;
    
    /** Japanese Yen alias */
    public static final Currency YEN = JPY;
    
    /** British Pound alias */
    public static final Currency POUND_STERLING = GBP;

    // ===== Registry Methods =====

    private static Currency register(Currency currency) {
        REGISTRY.put(currency.getCode(), currency);
        return currency;
    }

    /**
     * Returns the currency for the given ISO code.
     *
     * @param code the ISO 4217 currency code
     * @return the currency, or null if not found
     */
    public static Currency forCode(String code) {
        return REGISTRY.get(code);
    }

    /**
     * Returns the currency for the given ISO code, throwing if not found.
     *
     * @param code the ISO 4217 currency code
     * @return the currency
     * @throws IllegalArgumentException if currency not found
     */
    public static Currency requireCode(String code) {
        Currency currency = REGISTRY.get(code);
        if (currency == null) {
            throw new IllegalArgumentException("Unknown currency code: " + code);
        }
        return currency;
    }

    /**
     * Returns all registered currencies.
     * @return unmodifiable collection of currencies
     */
    public static Collection<Currency> all() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    /**
     * Checks if a currency code is registered.
     *
     * @param code the currency code
     * @return true if registered
     */
    public static boolean isKnown(String code) {
        return REGISTRY.containsKey(code);
    }

    /**
     * Returns the number of registered currencies.
     * @return the count
     */
    public static int count() {
        return REGISTRY.size();
    }
}

