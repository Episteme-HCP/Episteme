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

package org.jscience.politics;

import java.awt.Image;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.economics.money.Currency;
import org.jscience.geography.Place;
import org.jscience.util.Named;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a sovereign state, kingdom, empire, or modern nation-state.
 * A country aggregates territorial information, cultural identity (Nation), 
 * and administrative structures (Army, Police, Justice).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Country extends Place implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final String name;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Nation nation;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Administration army;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Administration police;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Administration justice;

    @Attribute
    private Currency currency;

    @Attribute
    private double gdp = -1.0;

    @Attribute
    private double gnp = -1.0;

    @Attribute
    private transient Image flag;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private City capital;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<City> cities = new HashSet<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Region> regions = new HashSet<>();

    /**
     * Initializes a new Country instance.
     *
     * @param name    the name of the country
     * @param nation  the cultural nation or group identity
     * @param capital the capital city
     * @throws NullPointerException if any argument is null
     */
    public Country(String name, Nation nation, City capital) {
        super(Objects.requireNonNull(name, "Name cannot be null"), 
              Objects.requireNonNull(nation, "Nation cannot be null").getFormalTerritory().getBoundary());
        this.name = name;
        this.nation = nation;
        this.capital = Objects.requireNonNull(capital, "Capital cannot be null");
    }

    @Override
    public String getName() {
        return name;
    }

    public Nation getNation() {
        return nation;
    }

    public Administration getArmy() {
        return army;
    }

    /**
     * Sets the national army.
     * @param army the administration
     * @throws IllegalArgumentException if the administration belongs to another country
     */
    public void setArmy(Administration army) {
        if (army != null && !Objects.equals(army.getPosition(), this)) {
            throw new IllegalArgumentException("Army must be positioned within this country.");
        }
        this.army = army;
    }

    public Administration getPolice() {
        return police;
    }

    /**
     * Sets the national police.
     * @param police the administration
     * @throws IllegalArgumentException if the administration belongs to another country
     */
    public void setPolice(Administration police) {
        if (police != null && !Objects.equals(police.getPosition(), this)) {
            throw new IllegalArgumentException("Police must be positioned within this country.");
        }
        this.police = police;
    }

    public Administration getJustice() {
        return justice;
    }

    /**
     * Sets the national justice system.
     * @param justice the administration
     * @throws IllegalArgumentException if the administration belongs to another country
     */
    public void setJustice(Administration justice) {
        if (justice != null && !Objects.equals(justice.getPosition(), this)) {
            throw new IllegalArgumentException("Justice must be positioned within this country.");
        }
        this.justice = justice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getGDP() {
        return gdp;
    }

    public void setGDP(double gdp) {
        this.gdp = gdp;
    }

    public double getGNP() {
        return gnp;
    }

    public void setGNP(double gnp) {
        this.gnp = gnp;
    }

    public Image getFlag() {
        return flag;
    }

    public void setFlag(Image flag) {
        this.flag = flag;
    }

    public City getCapital() {
        return capital;
    }

    /**
     * Sets the capital city.
     * @param capital the city, must be within this country's cities
     * @throws IllegalArgumentException if capital is null or from another country
     */
    public void setCapital(City capital) {
        if (capital == null) {
            throw new IllegalArgumentException("Capital cannot be null.");
        }
        if (!cities.contains(capital)) {
            throw new IllegalArgumentException("The capital city must belong to this country.");
        }
        this.capital = capital;
    }

    public void addCity(City city) {
        if (city != null) {
            cities.add(city);
        }
    }

    public void removeCity(City city) {
        cities.remove(city);
    }

    public Set<City> getCities() {
        return Collections.unmodifiableSet(cities);
    }

    public void addRegion(Region region) {
        if (region != null) {
            regions.add(region);
        }
    }

    public void removeRegion(Region region) {
        regions.remove(region);
    }

    public Set<Region> getRegions() {
        return Collections.unmodifiableSet(regions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        if (!super.equals(o)) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return name + " (Nation: " + nation.getName() + ")";
    }
}
