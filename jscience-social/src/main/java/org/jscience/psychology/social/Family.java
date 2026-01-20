package org.jscience.psychology.social;

import org.jscience.biology.Species;

import org.jscience.geography.Place;


/**
 * A class representing a family which is a group whose individual share
 * some specific bond (usually genetic).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//due to Java single inheritance scheme you have to subclass this class or the HumanGroup class to define a HumanFamily class.
//TIP: you can use this class with HistoricalIndividuals to define a FamilyGroup
//though we perfom here no check if all members are parents of one another.
//family can also be thought as a situation, with roles such has parent, child, etc 
public class Family extends Group {
/**
     * Creates a new Family object.
     *
     * @param species DOCUMENT ME!
     */
    public Family(Species species) {
        super(species);
    }

/**
     * Creates a new Family object.
     *
     * @param species         DOCUMENT ME!
     * @param formalTerritory DOCUMENT ME!
     */
    public Family(Species species, Place formalTerritory) {
        super(species, formalTerritory);
    }
}
