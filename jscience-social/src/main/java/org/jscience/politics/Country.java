/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.politics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a country/nation with standard codes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Country extends org.jscience.geography.Place implements org.jscience.util.identity.Identifiable<String> {

    private String name;
    private String alpha2;
    private String alpha3;
    private int numericCode;
    private String capital;
    private String continent;
    private long population;
    private Real areaSqKm;

    public Country(String name, String alpha2) {
        super(name, Type.COUNTRY);
        this.name = name;
        this.alpha2 = alpha2;
        this.areaSqKm = Real.ZERO;
    }

    public Country(String name, String alpha2, String alpha3, int numericCode,
            String capital, String continent, long population, Real areaSqKm) {
        this(name, alpha2);
        this.alpha3 = alpha3;
        this.numericCode = numericCode;
        this.capital = capital;
        this.continent = continent;
        this.population = population;
        this.areaSqKm = areaSqKm;

        // Update Place fields as possible
        this.setRegion(continent);
        this.setCountry(name); // It is the country itself
    }

    public Country(String name, String alpha2, String alpha3, int numericCode,
            String capital, String continent, long population, double areaSqKm) {
        this(name, alpha2, alpha3, numericCode, capital, continent, population, Real.of(areaSqKm));
    }

    @Override
    public String getId() {
        return alpha3 != null ? alpha3 : alpha2;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public String getAlpha3() {
        return alpha3;
    }

    public int getNumericCode() {
        return numericCode;
    }

    public String getCapital() {
        return capital;
    }

    public String getContinent() {
        return continent;
    }

    @Override
    public int getPopulation() {
        return (int) population; // Place uses int, Country uses long. Potential overflow for China/India but
                                 // Place should be updated eventually.
    }

    public long getPopulationLong() {
        return population;
    }

    public Real getAreaSqKm() {
        return areaSqKm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }

    public void setNumericCode(int numericCode) {
        this.numericCode = numericCode;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public void setAreaSqKm(Real areaSqKm) {
        this.areaSqKm = areaSqKm;
    }

    public void setAreaSqKm(double areaSqKm) {
        this.areaSqKm = Real.of(areaSqKm);
    }

    public Real getPopulationDensity() {
        return areaSqKm.doubleValue() > 0 ? Real.of(population).divide(areaSqKm) : Real.ZERO;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, alpha2);
    }

    // Major countries
    public static final Country USA = new Country("United States", "US", "USA", 840,
            "Washington D.C.", "North America", 331_000_000L, 9_833_520.0);
    public static final Country CHINA = new Country("China", "CN", "CHN", 156,
            "Beijing", "Asia", 1_412_000_000L, 9_596_960.0);
    public static final Country INDIA = new Country("India", "IN", "IND", 356,
            "New Delhi", "Asia", 1_380_000_000L, 3_287_263.0);
    public static final Country FRANCE = new Country("France", "FR", "FRA", 250,
            "Paris", "Europe", 67_000_000L, 643_801.0);
    public static final Country GERMANY = new Country("Germany", "DE", "DEU", 276,
            "Berlin", "Europe", 83_000_000L, 357_022.0);
    public static final Country UK = new Country("United Kingdom", "GB", "GBR", 826,
            "London", "Europe", 67_000_000L, 242_495.0);
    public static final Country JAPAN = new Country("Japan", "JP", "JPN", 392,
            "Tokyo", "Asia", 126_000_000L, 377_975.0);
    public static final Country BRAZIL = new Country("Brazil", "BR", "BRA", 76,
            "BrasÃƒÂ­lia", "South America", 212_000_000L, 8_515_767.0);
    // Expanded fields from Factbook data
    private double coastlineKm;
    private String governmentType;
    private int independenceYear;
    private double populationGrowthRate;
    private double lifeExpectancy;
    private double birthRate;
    private double deathRate;
    private String currencyCode;
    private java.util.List<String> majorIndustries = new java.util.ArrayList<>();
    private java.util.List<String> naturalResources = new java.util.ArrayList<>();
    private java.util.List<String> borderCountries = new java.util.ArrayList<>();
    
    // Merged from jscience-natural
    private double stability; // 0.0 - 1.0
    private double militarySpending; // Billions USD

    public double getStability() {
        return stability;
    }

    public void setStability(double stability) {
        this.stability = stability;
    }

    public double getMilitarySpending() {
        return militarySpending;
    }

    public void setMilitarySpending(double militarySpending) {
        this.militarySpending = militarySpending;
    }

    public double getCoastlineKm() {
        return coastlineKm;
    }

    public void setCoastlineKm(double coastlineKm) {
        this.coastlineKm = coastlineKm;
    }

    public String getGovernmentType() {
        return governmentType;
    }

    public void setGovernmentType(String governmentType) {
        this.governmentType = governmentType;
    }

    public int getIndependenceYear() {
        return independenceYear;
    }

    public void setIndependenceYear(int independenceYear) {
        this.independenceYear = independenceYear;
    }

    public double getPopulationGrowthRate() {
        return populationGrowthRate;
    }

    public void setPopulationGrowthRate(double populationGrowthRate) {
        this.populationGrowthRate = populationGrowthRate;
    }

    public double getLifeExpectancy() {
        return lifeExpectancy;
    }

    public void setLifeExpectancy(double lifeExpectancy) {
        this.lifeExpectancy = lifeExpectancy;
    }

    public double getBirthRate() {
        return birthRate;
    }

    public void setBirthRate(double birthRate) {
        this.birthRate = birthRate;
    }

    public double getDeathRate() {
        return deathRate;
    }

    public void setDeathRate(double deathRate) {
        this.deathRate = deathRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public java.util.List<String> getMajorIndustries() {
        return majorIndustries;
    }

    public java.util.List<String> getNaturalResources() {
        return naturalResources;
    }

    public java.util.List<String> getBorderCountries() {
        return borderCountries;
    }
}
