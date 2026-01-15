package org.jscience.sociology;

/**
 * A class representing a traditional behavior.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//rituals normally occur as part of (official) ceremonies but not only (for example hand shaking, let the women enter first, etc).
//although there is formally no difference with Celebrations, this class should rather model short term activities which are rather automatic. Specific events, involving many persons and a complex underlying organization should be considered as Celebrations
public class Ritual extends Situation {
/**
     * Creates a new Ritual object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Ritual(String name, String comments) {
        super(name, comments);
    }
}
