/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an economic system/economy.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Economy {

    public enum Type {
        MARKET, PLANNED, MIXED, TRADITIONAL, TRANSITIONAL
    }

    public enum Sector {
        PRIMARY, // Agriculture, mining
        SECONDARY, // Manufacturing
        TERTIARY, // Services
        QUATERNARY // Information/knowledge
    }

    private final String name;
    private final Type type;
    private Currency primaryCurrency;
    private Real gdp; // Gross Domestic Product
    private Real gdpPerCapita;
    private double inflationRate; // Annual percentage
    private double unemploymentRate; // Percentage
    private double giniCoefficient; // Income inequality (0-1)
    private long population;

    public Economy(String name, Type type, Currency primaryCurrency) {
        this.name = name;
        this.type = type;
        this.primaryCurrency = primaryCurrency;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Currency getPrimaryCurrency() {
        return primaryCurrency;
    }

    public Real getGdp() {
        return gdp;
    }

    public Real getGdpPerCapita() {
        return gdpPerCapita;
    }

    public double getInflationRate() {
        return inflationRate;
    }

    public double getUnemploymentRate() {
        return unemploymentRate;
    }

    public double getGiniCoefficient() {
        return giniCoefficient;
    }

    public long getPopulation() {
        return population;
    }

    // Setters
    public void setPrimaryCurrency(Currency currency) {
        this.primaryCurrency = currency;
    }

    public void setGdp(Real gdp) {
        this.gdp = gdp;
    }

    public void setGdpPerCapita(Real gdpPerCapita) {
        this.gdpPerCapita = gdpPerCapita;
    }

    public void setInflationRate(double rate) {
        this.inflationRate = rate;
    }

    public void setUnemploymentRate(double rate) {
        this.unemploymentRate = rate;
    }

    public void setGiniCoefficient(double gini) {
        this.giniCoefficient = gini;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    /**
     * Calculates GDP per capita from GDP and population.
     */
    public void calculateGdpPerCapita() {
        if (gdp != null && population > 0) {
            gdpPerCapita = gdp.divide(Real.of(population));
        }
    }

    /**
     * Returns economic health indicator (simplified).
     * Based on unemployment, inflation, and growth.
     */
    public String getHealthIndicator() {
        if (unemploymentRate < 5 && inflationRate < 3)
            return "Strong";
        if (unemploymentRate < 8 && inflationRate < 5)
            return "Moderate";
        if (unemploymentRate < 12 && inflationRate < 10)
            return "Weak";
        return "Critical";
    }

    @Override
    public String toString() {
        return String.format("Economy '%s' (%s): GDP=%s, Unemployment=%.1f%%",
                name, type, gdp != null ? gdp.toString() : "N/A", unemploymentRate);
    }

    // Notable economies
    public static Economy usa() {
        Economy e = new Economy("United States", Type.MARKET, Currency.USD);
        e.setGdp(Real.of(25_000_000_000_000.0)); // $25 trillion
        e.setPopulation(330_000_000);
        e.calculateGdpPerCapita();
        e.setUnemploymentRate(3.7);
        e.setInflationRate(3.2);
        e.setGiniCoefficient(0.39);
        return e;
    }

    public static Economy china() {
        Economy e = new Economy("China", Type.MIXED, Currency.CNY);
        e.setGdp(Real.of(18_000_000_000_000.0)); // $18 trillion
        e.setPopulation(1_400_000_000);
        e.calculateGdpPerCapita();
        e.setUnemploymentRate(5.3);
        e.setInflationRate(0.2);
        e.setGiniCoefficient(0.38);
        return e;
    }
}
