package org.jscience.sports;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the past competitions and matches results.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we should provide a way to read and write an XML file to store that
public class Almanac extends Object {
    /** DOCUMENT ME! */
    private Set results;

/**
     * Creates a new Almanac object.
     */
    public Almanac() {
        results = Collections.EMPTY_SET;
    }

    //don't add matches as competitions or the opposite if you don't want to have them twice
    /**
     * DOCUMENT ME!
     *
     * @param match DOCUMENT ME!
     */
    public void addResult(Match match) {
        results.add(match);
    }

    /**
     * DOCUMENT ME!
     *
     * @param match DOCUMENT ME!
     */
    public void removeResult(Match match) {
        results.remove(match);
    }

    //don't add matches as competitions or the opposite if you don't want to have them twice
    /**
     * DOCUMENT ME!
     *
     * @param competition DOCUMENT ME!
     */
    public void addResult(Competition competition) {
        results.add(competition);
    }

    /**
     * DOCUMENT ME!
     *
     * @param competition DOCUMENT ME!
     */
    public void removeResult(Competition competition) {
        results.remove(competition);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getResults() {
        return results;
    }
}
