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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.geography.Boundary;
import org.jscience.geography.Place;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a major subnational administrative division (State, Province, Oblast, etc.).
 * Regions aggregate local leadership and territorial boundaries within a Country.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Region extends Place implements Serializable {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Country country;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Individual> leaders = new HashSet<>();

    /**
     * Initializes a new Region.
     *
     * @param name     the name of the region
     * @param boundary the geographic boundary
     * @param country  the parent country
     * @throws NullPointerException if any argument is null
     */
    public Region(String name, Boundary boundary, Country country) {
        super(Objects.requireNonNull(name, "Name cannot be null"), 
              Objects.requireNonNull(boundary, "Boundary cannot be null"));
        this.country = Objects.requireNonNull(country, "Country cannot be null");
        this.country.addRegion(this);
    }

    /**
     * Returns an unmodifiable set of the region's current leaders.
     * @return leaders set
     */
    public Set<Individual> getLeaders() {
        return Collections.unmodifiableSet(leaders);
    }

    /**
     * Adds an individual to the region's leadership team.
     * @param leader the individual to add
     */
    public void addLeader(Individual leader) {
        if (leader != null) {
            leaders.add(leader);
        }
    }

    /**
     * Removes an individual from the region's leadership.
     * @param leader the individual to remove
     */
    public void removeLeader(Individual leader) {
        leaders.remove(leader);
    }

    /**
     * Returns the parent country associated with this region.
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Updates the parent country association.
     * @param country the new country
     */
    protected void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return getName() + " [" + (country != null ? country.getName() : "Independent") + "]";
    }
}
