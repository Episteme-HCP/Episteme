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

package org.jscience.social.history;

import org.jscience.core.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.*;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Converts historical currencies and adjusts for inflation using purchasing power parity (PPP).
 * Provides mappings for major historical currencies and their estimated modern value.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class HistoricalCurrencyConverter {

    private HistoricalCurrencyConverter() {
        // Prevent instantiation
    }

    /**
     * Represents a historical currency and its period of circulation.
     * 
     * @param name      full name of the currency
     * @param symbol    graphical symbol or abbreviation
     * @param startYear approximate start of circulation (BCE negative)
     * @param endYear   approximate end of circulation (BCE negative)
     */
    @Persistent
    public record Currency(
        @Attribute String name,
        @Attribute String symbol,
        @Attribute int startYear,
        @Attribute int endYear
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public Currency {
            Objects.requireNonNull(name, "Currency name cannot be null");
            Objects.requireNonNull(symbol, "Currency symbol cannot be null");
        }
    }

    /**
     * Historical exchange rate between two currencies.
     * 
     * @param from destination from
     * @param to destination to
     * @param year the year of the rate
     * @param rate the exchange rate factor
     */
    @Persistent
    public record ExchangeRate(
        @Relation(type = Relation.Type.MANY_TO_ONE) Currency from,
        @Relation(type = Relation.Type.MANY_TO_ONE) Currency to,
        @Attribute int year,
        @Attribute double rate
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public ExchangeRate {
            Objects.requireNonNull(from, "Source currency cannot be null");
            Objects.requireNonNull(to, "Target currency cannot be null");
        }
    }

    private static final Map<String, Currency> CURRENCIES = Map.ofEntries(
        Map.entry("denarius", new Currency("Roman Denarius", "HS", -211, 275)),
        Map.entry("solidus", new Currency("Byzantine Solidus", "Î£", 309, 1453)),
        Map.entry("pound_sterling", new Currency("Pound Sterling", "Â£", 775, 2100)),
        Map.entry("livre_tournois", new Currency("Livre Tournois", "â‚¶", 1203, 1795)),
        Map.entry("florin", new Currency("Florentine Florin", "Æ’", 1252, 1533)),
        Map.entry("ducat", new Currency("Venetian Ducat", "D", 1284, 1797)),
        Map.entry("dollar_us", new Currency("US Dollar", "$", 1792, 2100)),
        Map.entry("franc", new Currency("French Franc", "F", 1795, 2002)),
        Map.entry("euro", new Currency("Euro", "â‚¬", 1999, 2100))
    );

    // Simplified purchasing power factors (relative to 2020 USD)
    private static final Map<String, Map<Integer, Double>> PURCHASING_POWER = Map.of(
        "pound_sterling", Map.of(
            1300, 0.0008, 1500, 0.002, 1700, 0.005, 1800, 0.02,
            1900, 0.08, 1950, 0.3, 2000, 0.7, 2020, 0.75
        ),
        "dollar_us", Map.of(
            1800, 0.03, 1850, 0.025, 1900, 0.03, 1950, 0.1,
            1970, 0.16, 1990, 0.5, 2000, 0.7, 2020, 1.0
        )
    );

    /**
     * Converts a monetary amount between two historical currencies at a specific year.
     * 
     * @param amount       the amount to convert
     * @param fromCurrency key of source currency
     * @param toCurrency   key of target currency
     * @param year         the historical year
     * @return converted amount as a {@link Real} number
     * @throws NullPointerException if any string or Real argument is null
     * @throws IllegalArgumentException if currency is unknown or not in circulation
     */
    public static Real convert(Real amount, String fromCurrency, String toCurrency, int year) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(fromCurrency, "Source currency cannot be null");
        Objects.requireNonNull(toCurrency, "Target currency cannot be null");
        
        Currency from = CURRENCIES.get(fromCurrency.toLowerCase().trim());
        Currency to = CURRENCIES.get(toCurrency.toLowerCase().trim());
        
        if (from == null) {
            throw new IllegalArgumentException("Unknown currency: " + fromCurrency);
        }
        if (to == null) {
            throw new IllegalArgumentException("Unknown currency: " + toCurrency);
        }
        
        if (year < from.startYear() || year > from.endYear()) {
            throw new IllegalArgumentException(from.name() + " not in circulation in " + year);
        }
        if (year < to.startYear() || year > to.endYear()) {
            throw new IllegalArgumentException(to.name() + " not in circulation in " + year);
        }
        
        double ppFrom = getPurchasingPower(fromCurrency, year);
        double ppTo = getPurchasingPower(toCurrency, year);
        
        if (ppFrom == 0.0 || ppTo == 0.0) {
            return Real.of(Double.NaN);
        }
        
        double convertedValue = amount.doubleValue() * ppFrom / ppTo;
        return Real.of(convertedValue);
    }

    /**
     * Adjusts a monetary amount for inflation within the same currency.
     * 
     * @param amount   the original amount
     * @param currency currency key
     * @param fromYear starting year
     * @param toYear   target year
     * @return inflation-adjusted amount as a {@link Real} number
     * @throws NullPointerException if amount or currency is null
     */
    public static Real adjustForInflation(Real amount, String currency, 
            int fromYear, int toYear) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        
        double ppFrom = getPurchasingPower(currency, fromYear);
        double ppTo = getPurchasingPower(currency, toYear);
        
        if (ppFrom == 0.0) return amount;
        
        double adjustedValue = amount.doubleValue() * ppTo / ppFrom;
        return Real.of(adjustedValue);
    }

    /**
     * Converts a historical amount to its equivalent 2020 US Dollar value.
     * 
     * @param amount   the historical amount
     * @param currency currency key
     * @param year     historical year
     * @return modern USD equivalent as a {@link Real} number
     * @throws NullPointerException if amount or currency is null
     */
    public static Real toModernUSD(Real amount, String currency, int year) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        double pp = getPurchasingPower(currency, year);
        double modernValue = amount.doubleValue() * pp;
        return Real.of(modernValue);
    }

    /**
     * Estimates the human effort value (days of labor) represented by a monetary amount.
     * 
     * @param amount   the monetary amount
     * @param currency currency key
     * @param year     historical year
     * @return estimated number of days of labor to earn this amount as a {@link Real} number
     * @throws NullPointerException if amount or currency is null
     */
    public static Real toDaysLabor(Real amount, String currency, int year) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        
        double dailyWage = switch (currency.toLowerCase().trim()) {
            case "pound_sterling" -> year < 1500 ? 0.005 : year < 1800 ? 0.02 : 0.5;
            case "dollar_us" -> year < 1900 ? 1.0 : year < 1950 ? 3.0 : 50.0;
            case "denarius" -> 1.0;
            default -> 1.0;
        };
        
        return Real.of(amount.doubleValue() / dailyWage);
    }

    private static double getPurchasingPower(String currency, int year) {
        Map<Integer, Double> pp = PURCHASING_POWER.get(currency.toLowerCase().trim());
        if (pp == null) return 0.01;
        
        Integer lower = null, upper = null;
        for (Integer y : pp.keySet()) {
            if (y <= year && (lower == null || y > lower)) lower = y;
            if (y >= year && (upper == null || y < upper)) upper = y;
        }
        
        if (lower == null && upper == null) return 0.01;
        if (lower == null) return pp.get(upper);
        if (upper == null) return pp.get(lower);
        if (lower.equals(upper)) return pp.get(lower);
        
        double ratio = (double)(year - lower) / (upper - lower);
        return pp.get(lower) + ratio * (pp.get(upper) - pp.get(lower));
    }

    /**
     * Returns a list of currencies known to be in circulation during a specific year.
     * 
     * @param year the year to query
     * @return unmodifiable list of circulating currencies
     */
    public static List<Currency> getAvailableCurrencies(int year) {
        return CURRENCIES.values().stream()
            .filter(c -> year >= c.startYear() && year <= c.endYear())
            .toList();
    }
}

