package org.jscience.politics;

import org.jscience.biology.Individual;

import org.jscience.measure.Identification;
import org.jscience.measure.StringIdentificationFactory;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a citizen basic facts.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Citizen extends Role {
    /** DOCUMENT ME! */
    private Identification identification; //the number that is written on you ID, passport or Social Security depending on what fits best and on the country

    /** DOCUMENT ME! */
    private Set nationalities;

/**
     * Creates a new Citizen object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public Citizen(Individual individual, CivilSituation situation) {
        super(individual, "Citizen", situation, Role.CLIENT);
        this.nationalities = Collections.EMPTY_SET;
        this.identification = new StringIdentificationFactory().generateIdentification(new String(
                    "Unset"));
    }

    //may return an empty set
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getNationalities() {
        return nationalities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param country DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addNationality(Country country) {
        if (country != null) {
            nationalities.add(country);
        } else {
            throw new IllegalArgumentException("You can't add a null country.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param country DOCUMENT ME!
     */
    public void removeNationality(Country country) {
        nationalities.remove(country);
    }

    //there is no setNationality as there are really few
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @param identification DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setIdentification(Identification identification) {
        if (identification != null) {
            this.identification = identification;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null identification.");
        }
    }
}
