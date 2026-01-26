package org.jscience.politics;

import org.jscience.biology.Individual;

import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a person's legal status as a citizen, including nationalities and identification.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
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
