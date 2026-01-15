package org.jscience.economics.resources;

import java.util.Set;


/**
 * A class representing a container.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//describes the fact that some things may be contained within this thing
//for example a fridge stores food, a vehicle stores passengers
//buildings (houses, cages) also store creatures
//this class is useful to provide some knowledge about the things that are in, how they get in and out
//stored things, once in, of course move with the thing they are into
public interface Store {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getContents();

    /**
     * DOCUMENT ME!
     */
    public void getIn();

    /**
     * DOCUMENT ME!
     */
    public void getOut();
}
