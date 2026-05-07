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

package org.episteme.social.politics;

import java.awt.Image;

import java.util.*;
import org.episteme.social.economics.money.Currency;
import org.episteme.social.geography.Region;

import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Time;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.social.economics.money.Money;

/**
 * Represents a sovereign state, kingdom, empire, or modern nation-state.
 * <p>
 * A country aggregates territorial information, cultural identity (Nation), 
 * and administrative structures (Army, Police, Justice).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Country extends Region {

    private static final long serialVersionUID = 1L;

    // --- Common Static Constants ---
    public static final Country FRANCE = new Country("France", "FR", "FRA", 250, "Paris", "Europe", 67000000L, 643801.0);
    public static final Country USA = new Country("United States", "US", "USA", 840, "Washington, D.C.", "North America", 331000000L, 9833517.0);
    public static final Country CHINA = new Country("China", "CN", "CHN", 156, "Beijing", "Asia", 1411000000L, 9596960.0);
    public static final Country GERMANY = new Country("Germany", "DE", "DEU", 276, "Berlin", "Europe", 83000000L, 357022.0);
    public static final Country JAPAN = new Country("Japan", "JP", "JPN", 392, "Tokyo", "Asia", 125000000L, 377975.0);

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Nation nation;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Administration army;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Administration police;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Administration justice;

    @Attribute
    private Currency currency;
    
    @Attribute
    private String currencyCode;

    @Attribute
    private transient Image flag;

    @Attribute
    private GovernmentType governmentType;

    @Attribute
    private Integer independenceYear;

    @Attribute
    private Quantity<Time> lifeExpectancy;


    @Attribute
    private Quantity<Dimensionless> populationGrowthRate;

    @Attribute
    private Quantity<Dimensionless> stability; // Index from 0.0 to 1.0 (Unit.ONE)

    @Attribute
    private Quantity<Money> militarySpending; // Usually in billions of USD or % of GDP

    @Attribute
    private String continent;
    
    @Attribute
    private String alpha3;

    @Attribute
    private Quantity<Dimensionless> hdi; // Human Development Index (0.0 to 1.0)

    private final Set<String> majorIndustries = new HashSet<>();
    private final Set<String> naturalResources = new HashSet<>();
    private final Set<String> borderCountries = new HashSet<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<City> cities = new HashSet<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Region> subRegions = new HashSet<>();

    /**
     * Modern constructor.
     */
    public Country(String name, Nation nation, City capital) {
        super(name, Region.SubType.COUNTRY);
        this.nation = nation;
        if (capital != null) setCapital(capital.getName());
    }

    /**
     * Legacy/Factbook constructor.
     */
    public Country(String name, String code) {
        super(name, Region.SubType.COUNTRY);
        setIsoCode(code);
    }

    /**
     * Rich Factbook constructor.
     */
    public Country(String name, String iso2, String iso3, int numeric, String capital, String continent, long pop, double area) {
        super(name, Region.SubType.COUNTRY);
        setIsoCode(iso2);
        setAlpha3(iso3);
        setCapital(capital);
        setContinent(continent);
        setPopulation(pop);
        setAreaSqKm(area);
    }

    @SuppressWarnings("unchecked")
    public void setAreaSqKm(double areaSqKm) {
        org.episteme.core.measure.Unit<org.episteme.core.measure.quantity.Area> sqMeter = 
            (org.episteme.core.measure.Unit<org.episteme.core.measure.quantity.Area>) (Object) org.episteme.core.measure.Units.METER.pow(2);
        setArea(org.episteme.core.measure.Quantities.create(areaSqKm * 1_000_000.0, sqMeter));
    }

    @SuppressWarnings("unchecked")
    public double getAreaSqKm() {
        if (getArea() == null) return 0.0;
        org.episteme.core.measure.Unit<org.episteme.core.measure.quantity.Area> sqMeter = 
            (org.episteme.core.measure.Unit<org.episteme.core.measure.quantity.Area>) (Object) org.episteme.core.measure.Units.METER.pow(2);
        return getArea().to(sqMeter).getValue().doubleValue() / 1_000_000.0;
    }

    public long getPopulationLong() {
        return getPopulation();
    }

    public Nation getNation() { return nation; }
    public void setNation(Nation nation) { this.nation = nation; }

    public Administration getArmy() { return army; }
    public void setArmy(Administration army) { this.army = army; }

    public Administration getPolice() { return police; }
    public void setPolice(Administration police) { this.police = police; }

    public Administration getJustice() { return justice; }
    public void setJustice(Administration justice) { this.justice = justice; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String code) { this.currencyCode = code; }

    public Image getFlag() { return flag; }
    public void setFlag(Image flag) { this.flag = flag; }

    public GovernmentType getGovernmentType() { return governmentType; }
    public void setGovernmentType(GovernmentType type) { this.governmentType = type; }

    public Integer getIndependenceYear() { return independenceYear; }
    public void setIndependenceYear(Integer year) { this.independenceYear = year; }

    public Quantity<Time> getLifeExpectancy() { return lifeExpectancy; }
    public void setLifeExpectancy(Quantity<Time> life) { this.lifeExpectancy = life; }

    public Quantity<Dimensionless> getPopulationGrowthRate() { return populationGrowthRate; }
    public void setPopulationGrowthRate(Quantity<Dimensionless> rate) { this.populationGrowthRate = rate; }

    public Quantity<Dimensionless> getStability() { return stability; }
    public void setStability(Quantity<Dimensionless> stability) { this.stability = stability; }

    public Quantity<Money> getMilitarySpending() { return militarySpending; }
    public void setMilitarySpending(Quantity<Money> spending) { this.militarySpending = spending; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getAlpha2() { return getIsoCode(); }
    public void setAlpha2(String code) { setIsoCode(code); }
    
    public String getAlpha3() { return alpha3; }
    public void setAlpha3(String code) { this.alpha3 = code; }

    public Quantity<Dimensionless> getHdi() { return hdi; }
    public void setHdi(Quantity<Dimensionless> hdi) { this.hdi = hdi; }

    public Set<String> getMajorIndustries() { return majorIndustries; }
    public Set<String> getNaturalResources() { return naturalResources; }
    public Set<String> getBorderCountries() { return borderCountries; }

    public void addCity(City city) { if (city != null) cities.add(city); }
    public Set<City> getCities() { return Collections.unmodifiableSet(cities); }

    public void addSubRegion(Region region) { if (region != null) subRegions.add(region); }
    public Set<Region> getSubRegions() { return Collections.unmodifiableSet(subRegions); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getIsoCode(), country.getIsoCode()) || Objects.equals(alpha3, country.alpha3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIsoCode(), alpha3);
    }

    @Override
    public String toString() {
        return getName() + " [" + getIsoCode() + "]";
    }
}

