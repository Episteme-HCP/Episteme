/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.politics;

/**
 * Represents a country/nation with standard codes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Country {

    private final String name;
    private final String alpha2; // ISO 3166-1 alpha-2
    private final String alpha3; // ISO 3166-1 alpha-3
    private final int numericCode; // ISO 3166-1 numeric
    private final String capital;
    private final String continent;
    private final long population;
    private final double areaSqKm;

    public Country(String name, String alpha2, String alpha3, int numericCode,
            String capital, String continent, long population, double areaSqKm) {
        this.name = name;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.numericCode = numericCode;
        this.capital = capital;
        this.continent = continent;
        this.population = population;
        this.areaSqKm = areaSqKm;
    }

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

    public long getPopulation() {
        return population;
    }

    public double getAreaSqKm() {
        return areaSqKm;
    }

    public double getPopulationDensity() {
        return areaSqKm > 0 ? population / areaSqKm : 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, alpha2);
    }

    // Major countries
    public static final Country USA = new Country("United States", "US", "USA", 840,
            "Washington D.C.", "North America", 331_000_000L, 9_833_520);
    public static final Country CHINA = new Country("China", "CN", "CHN", 156,
            "Beijing", "Asia", 1_412_000_000L, 9_596_960);
    public static final Country INDIA = new Country("India", "IN", "IND", 356,
            "New Delhi", "Asia", 1_380_000_000L, 3_287_263);
    public static final Country FRANCE = new Country("France", "FR", "FRA", 250,
            "Paris", "Europe", 67_000_000L, 643_801);
    public static final Country GERMANY = new Country("Germany", "DE", "DEU", 276,
            "Berlin", "Europe", 83_000_000L, 357_022);
    public static final Country UK = new Country("United Kingdom", "GB", "GBR", 826,
            "London", "Europe", 67_000_000L, 242_495);
    public static final Country JAPAN = new Country("Japan", "JP", "JPN", 392,
            "Tokyo", "Asia", 126_000_000L, 377_975);
    public static final Country BRAZIL = new Country("Brazil", "BR", "BRA", 76,
            "Brasília", "South America", 212_000_000L, 8_515_767);
}
