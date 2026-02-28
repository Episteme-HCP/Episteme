package org.episteme.psychology.social;

import org.episteme.biology.human.HumanSpecies;

import org.episteme.earth.Place;


/**
 * A class representing a human group.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//due to Java single inheritance scheme you have to subclass this class or the Family class to define a HumanFamily class.
public class HumanGroup extends Group {
/**
     * Creates a new HumanGroup object.
     */
    public HumanGroup() {
        super(new HumanSpecies());
    }

/**
     * Creates a new HumanGroup object.
     *
     * @param formalTerritory DOCUMENT ME!
     */
    public HumanGroup(Place formalTerritory) {
        super(new HumanSpecies(), formalTerritory);
    }
}
