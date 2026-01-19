package org.jscience.history;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Converts historical currencies with inflation adjustment.
 */
public final class HistoricalCurrencyConverter {

    private HistoricalCurrencyConverter() {}

    public record Currency(String name, String symbol, int startYear, int endYear) {}

    public record ExchangeRate(Currency from, Currency to, int year, double rate) {}

    private static final Map<String, Currency> CURRENCIES = Map.of(
        "denarius", new Currency("Roman Denarius", "HS", -211, 275),
        "solidus", new Currency("Byzantine Solidus", "Σ", 309, 1453),
        "pound_sterling", new Currency("Pound Sterling", "£", 775, 2100),
        "livre_tournois", new Currency("Livre Tournois", "₶", 1203, 1795),
        "florin", new Currency("Florentine Florin", "ƒ", 1252, 1533),
        "ducat", new Currency("Venetian Ducat", "D", 1284, 1797),
        "dollar_us", new Currency("US Dollar", "$", 1792, 2100),
        "franc", new Currency("French Franc", "F", 1795, 2002),
        "euro", new Currency("Euro", "€", 1999, 2100)
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
     * Converts an amount between historical currencies.
     */
    public static Real convert(Real amount, String fromCurrency, String toCurrency, int year) {
        Currency from = CURRENCIES.get(fromCurrency.toLowerCase());
        Currency to = CURRENCIES.get(toCurrency.toLowerCase());
        
        if (from == null || to == null) {
            throw new IllegalArgumentException("Unknown currency");
        }
        
        if (year < from.startYear() || year > from.endYear()) {
            throw new IllegalArgumentException(from.name() + " not in circulation in " + year);
        }
        if (year < to.startYear() || year > to.endYear()) {
            throw new IllegalArgumentException(to.name() + " not in circulation in " + year);
        }
        
        // Simplified conversion via purchasing power parity
        double ppFrom = getPurchasingPower(fromCurrency, year);
        double ppTo = getPurchasingPower(toCurrency, year);
        
        if (ppFrom == 0 || ppTo == 0) {
            return Real.of(Double.NaN);
        }
        
        double converted = amount.doubleValue() * ppFrom / ppTo;
        return Real.of(converted);
    }

    /**
     * Adjusts an amount for inflation to a target year.
     */
    public static Real adjustForInflation(Real amount, String currency, 
            int fromYear, int toYear) {
        
        double ppFrom = getPurchasingPower(currency, fromYear);
        double ppTo = getPurchasingPower(currency, toYear);
        
        if (ppFrom == 0) return amount;
        
        double adjusted = amount.doubleValue() * ppTo / ppFrom;
        return Real.of(adjusted);
    }

    /**
     * Converts to 2020 USD equivalent (purchasing power).
     */
    public static Real toModernUSD(Real amount, String currency, int year) {
        double pp = getPurchasingPower(currency, year);
        double modernValue = amount.doubleValue() * pp;
        return Real.of(modernValue);
    }

    /**
     * Estimates the wage equivalent (how many days' labor to earn this amount).
     */
    public static Real toDaysLabor(Real amount, String currency, int year) {
        // Rough daily wage estimates in various currencies
        double dailyWage = switch (currency.toLowerCase()) {
            case "pound_sterling" -> year < 1500 ? 0.005 : year < 1800 ? 0.02 : 0.5;
            case "dollar_us" -> year < 1900 ? 1.0 : year < 1950 ? 3.0 : 50.0;
            case "denarius" -> 1.0; // One denarius was roughly a day's wage
            default -> 1.0;
        };
        
        return Real.of(amount.doubleValue() / dailyWage);
    }

    private static double getPurchasingPower(String currency, int year) {
        Map<Integer, Double> pp = PURCHASING_POWER.get(currency.toLowerCase());
        if (pp == null) return 0.01; // Default low value for unknown currencies
        
        // Find closest years and interpolate
        Integer lower = null, upper = null;
        for (Integer y : pp.keySet()) {
            if (y <= year && (lower == null || y > lower)) lower = y;
            if (y >= year && (upper == null || y < upper)) upper = y;
        }
        
        if (lower == null && upper == null) return 0.01;
        if (lower == null) return pp.get(upper);
        if (upper == null) return pp.get(lower);
        if (lower.equals(upper)) return pp.get(lower);
        
        // Linear interpolation
        double ratio = (double)(year - lower) / (upper - lower);
        return pp.get(lower) + ratio * (pp.get(upper) - pp.get(lower));
    }

    /**
     * Gets available currencies for a year.
     */
    public static List<Currency> getAvailableCurrencies(int year) {
        return CURRENCIES.values().stream()
            .filter(c -> year >= c.startYear() && year <= c.endYear())
            .toList();
    }
}
