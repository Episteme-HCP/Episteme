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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.episteme.core.util.Named;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a PsychologicalGroup of countries cooperating within a formal union or federation.
 * <p>
 * Countries in a federation may or may not share common geography.
 * Examples include the European Union, United Nations, or a Federal State.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Federation implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String name;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Country> countries = new HashSet<>();

    /**
     * Initializes a new Federation.
     *
     * @param name      the name of the federation
     * @param countries set of member countries
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public Federation(String name, Set<Country> countries) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        
        if (countries != null) {
            this.countries.addAll(countries);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the federation.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    /**
     * Returns an unmodifiable set of member countries.
     * @return members
     */
    public Set<Country> getCountries() {
        return Collections.unmodifiableSet(countries);
    }

    /**
     * Adds a country to the federation.
     * @param country the country to add
     */
    public void addCountry(Country country) {
        if (country != null) {
            countries.add(country);
        }
    }

    /**
     * Removes a country from the federation.
     * @param country the country to remove
     */
    public void removeCountry(Country country) {
        countries.remove(country);
    }

    @Override
    public String toString() {
        return name + " [" + countries.size() + " member countries]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Federation)) return false;
        Federation other = (Federation) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

