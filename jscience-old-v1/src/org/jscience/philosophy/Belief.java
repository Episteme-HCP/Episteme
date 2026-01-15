package org.jscience.philosophy;

import org.jscience.util.Commented;
import org.jscience.util.Named;


/**
 * A class representing the basic and common principles of all
 * philosophies.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
 
 //there should be classes like desire, fear and hope along with belief
 //all these should have ancestor class Concept
public class Belief extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String comments;

/**
     * Creates a new Belief object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Belief(String name, String comments) {
        if ((name != null) && (name.length() > 0) && (comments != null) &&
                (comments.length() > 0)) {
            this.name = name;
            this.comments = comments;
        } else {
            throw new IllegalArgumentException(
                "The Belief constructor doesn't accept null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }
}
