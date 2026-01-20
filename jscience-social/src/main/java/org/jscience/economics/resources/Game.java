package org.jscience.economics.resources;

import java.util.Set;
import java.util.Vector;


/**
 * A class representing something you can play with.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//many things are not games by themselves but you can nevertheless play with
//they should not implement this interface
//on the contrary many games need objects to play with such as cards, dices
public interface Game {
    //who may be teams or opponents, as Individuals
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getPlayers();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getRules();

    //one of the rules
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAim();

    //there is usually a random element
}
