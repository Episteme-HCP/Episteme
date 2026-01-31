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

package org.jscience.social.politics;

import org.jscience.natural.biology.Individual;

import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;

import org.jscience.social.sociology.Role;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a person's legal status as a citizen, including nationalities and identification.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Citizen extends Role {
    /** The official identification (e.g., ID card, passport, or social security number). */
    @Attribute
    private Identification identification;

    /** The set of countries where this individual holds citizenship. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Country> nationalities;

    /**
     * Initializes a new Citizen role for an individual within a civil situation.
     *
     * @param individual the individual taking on the citizen role
     * @param situation  the civil context
     * @throws NullPointerException if any argument is null
     */
    public Citizen(Individual individual, CivilSituation situation) {
        super(Objects.requireNonNull(individual, "Individual cannot be null"), 
              "Citizen", 
              Objects.requireNonNull(situation, "Situation cannot be null"), 
              Role.CLIENT);
        this.nationalities = new HashSet<>();
        this.identification = new SimpleIdentification("Unset");
    }

    /**
     * Returns an unmodifiable set of nationalities.
     *
     * @return an unmodifiable view of the nationalities
     */
    public Set<Country> getNationalities() {
        return Collections.unmodifiableSet(nationalities);
    }

    /**
     * Adds a nationality to the citizen.
     *
     * @param country the country of citizenship
     * @throws NullPointerException if country is null
     */
    public void addNationality(Country country) {
        nationalities.add(Objects.requireNonNull(country, "Country cannot be null"));
    }

    /**
     * Removes a nationality from the citizen.
     *
     * @param country the country to remove
     */
    public void removeNationality(Country country) {
        nationalities.remove(country);
    }

    /**
     * Returns the identification for this citizen.
     *
     * @return the identification
     */
    public Identification getIdentification() {
        return identification;
    }

    /**
     * Updates the identification for this citizen.
     *
     * @param identification the new identification
     * @throws NullPointerException if identification is null
     */
    public void setIdentification(Identification identification) {
        this.identification = Objects.requireNonNull(identification, "Identification cannot be null");
    }
}

