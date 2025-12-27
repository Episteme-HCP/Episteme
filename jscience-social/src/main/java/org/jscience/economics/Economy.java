/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

/**
 * Represents an economic system/economy.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
    private Real inflationRate; // Annual percentage
    private Real unemploymentRate; // Percentage
    private Real giniCoefficient; // Income inequality (0-1)
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

    public Real getInflationRate() {
        return inflationRate;
    }

    public Real getUnemploymentRate() {
        return unemploymentRate;
    }

    public Real getGiniCoefficient() {
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

    public void setInflationRate(Real rate) {
        this.inflationRate = rate;
    }

    public void setUnemploymentRate(Real rate) {
        this.unemploymentRate = rate;
    }

    public void setGiniCoefficient(Real gini) {
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
        double unemp = unemploymentRate != null ? unemploymentRate.doubleValue() : 0;
        double infl = inflationRate != null ? inflationRate.doubleValue() : 0;

        if (unemp < 5 && infl < 3)
            return "Strong";
        if (unemp < 8 && infl < 5)
            return "Moderate";
        if (unemp < 12 && infl < 10)
            return "Weak";
        return "Critical";
    }

    @Override
    public String toString() {
        return String.format("Economy '%s' (%s): GDP=%s, Unemployment=%s",
                name, type, gdp != null ? gdp.toString() : "N/A",
                unemploymentRate != null ? unemploymentRate.toString() : "N/A");
    }

    // Notable economies
    public static Economy usa() {
        Economy e = new Economy("United States", Type.MARKET, Currency.USD);
        e.setGdp(Real.of(25_000_000_000_000.0)); // $25 trillion
        e.setPopulation(330_000_000);
        e.calculateGdpPerCapita();
        e.setUnemploymentRate(Real.of(3.7));
        e.setInflationRate(Real.of(3.2));
        e.setGiniCoefficient(Real.of(0.39));
        return e;
    }

    public static Economy china() {
        Economy e = new Economy("China", Type.MIXED, Currency.CNY);
        e.setGdp(Real.of(18_000_000_000_000.0)); // $18 trillion
        e.setPopulation(1_400_000_000);
        e.calculateGdpPerCapita();
        e.setUnemploymentRate(Real.of(5.3));
        e.setInflationRate(Real.of(0.2));
        e.setGiniCoefficient(Real.of(0.38));
        return e;
    }
}
