package org.jscience.politics;

import org.jscience.economics.Organization;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Identification;

import java.util.Set;


/**
 * An organization targetted towards managing a human group living.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//just a tagging class
public class PoliticalParty extends Organization {
/**
     * Creates a new PoliticalParty object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param owners         DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param accounts       DOCUMENT ME!
     */
    public PoliticalParty(String name, Identification identification,
        Set owners, BusinessPlace place, Set accounts) {
        super(name, identification, owners, place, accounts);
    }
}
