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


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.earth.Place;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a city.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class City extends Place {

    private static final long serialVersionUID = 1L;

    @Attribute
    private String zipCode;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Country country;

    @Attribute
    private long population;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Individual> leaders = new HashSet<>();

    /**
     * Legacy constructor.
     */
    public City(String name, Country country, String zipCode, int population) {
        this(name, country);
        this.zipCode = zipCode;
        this.population = population;
    }

    /**
     * Creates a new City.
     *
     * @param name     the name of the city
     * @param country  the country to which the city belongs
     * @throws NullPointerException if any argument is null
     */
    public City(String name, Country country) {
        super(Objects.requireNonNull(name, "City name cannot be null"), Place.Type.CITY);
        this.country = Objects.requireNonNull(country, "Country cannot be null");
        this.country.addCity(this);
    }

    /**
     * Returns an unmodifiable view of the city's current leaders.
     * @return unmodifiable set of leaders
     */
    public Set<Individual> getLeaders() {
        return Collections.unmodifiableSet(leaders);
    }

    /**
     * Adds an individual to the city's leadership team.
     * @param leader the individual to add
     */
    public void addLeader(Individual leader) {
        if (leader != null) {
            leaders.add(leader);
        }
    }

    /**
     * Removes an individual from the city's leadership.
     * @param leader the individual to remove
     */
    public void removeLeader(Individual leader) {
        leaders.remove(leader);
    }

    /**
     * Returns the country associated with this city.
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    public Country getExactCountry() {
        return country;
    }

    /**
     * Internal update of country affiliation.
     * @param country the new country
     */
    protected void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Returns the postal or zip code of the city.
     * @return the zip code, or null if unset
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip code for the city.
     * @param zipCode the code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        if (!super.equals(o)) return false;
        City city = (City) o;
        return Objects.equals(country, city.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), country);
    }
}
