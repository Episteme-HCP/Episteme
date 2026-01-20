package org.jscience.politics;

import org.jscience.economics.Organization;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Identification;
import org.jscience.measure.StringIdentificationFactory;

import java.util.Set;


/**
 * A class representing a group of people organized in a hierarchy and
 * loyal subjects to a state, they represent the active force. Armies, Police,
 * may be politicians, and prosecutors
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//stateforce require no identification as they are part of the state and are all unique: there is one army, one police, etc.
//for the same reason there no owners as the government or normally the whole country owns the stateforce
//also called bureaucracy
public class Administration extends Organization {
    /** DOCUMENT ME! */
    private Country country;

/**
     * Creates a new Administration object.
     *
     * @param name     DOCUMENT ME!
     * @param country  DOCUMENT ME!
     * @param place    DOCUMENT ME!
     * @param accounts DOCUMENT ME!
     */
    public Administration(String name, Country country, BusinessPlace place,
        Set accounts) {
        super(name,
            new StringIdentificationFactory().generateIdentification(name),
            country.getNation().getIndividuals(), place, accounts);
        this.country = country;
    }

/**
     * Creates a new Administration object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param country        DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param accounts       DOCUMENT ME!
     */
    public Administration(String name, Identification identification,
        Country country, BusinessPlace place, Set accounts) {
        super(name, identification, country.getNation().getIndividuals(),
            place, accounts);
        this.country = country;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Country getCountry() {
        return country;
    }
}
