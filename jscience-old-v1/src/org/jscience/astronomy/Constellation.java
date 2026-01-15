package org.jscience.astronomy;

import org.jscience.util.Commented;
import org.jscience.util.Named;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * The Constellation class provides support for constellations.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a group of stars that are visually spotted together from a star system
//there are 88 defined constellation visible from the Solar system
//see http://www.seds.org/Maps/Stars_en/Fig/const.html
public class Constellation extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name; //common name

    /** DOCUMENT ME! */
    private Set stars;

    /** DOCUMENT ME! */
    private String comments;

    //although we can imagine that you want to define specific constellations for eachg of your favorite star system
    /**
     * Creates a new Constellation object.
     *
     * @param name DOCUMENT ME!
     */
    public Constellation(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.stars = Collections.EMPTY_SET;
            this.comments = new String("");
        } else {
            throw new IllegalArgumentException(
                "The Constellation constructor doesn't accept null arguments.");
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
     * @param name DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getStars() {
        return stars;
    }

    //all elements of the set have to be stars
    /**
     * DOCUMENT ME!
     *
     * @param stars DOCUMENT ME!
     */
    public void setStars(Set stars) {
        Iterator i;
        boolean valid;
        Object currentElement;

        if (stars != null) {
            i = stars.iterator();
            valid = true;

            while (i.hasNext() && valid) {
                currentElement = i.next();
                valid = i.next() instanceof Star;
            }

            if (valid) {
                this.stars = stars;
            } else {
                throw new IllegalArgumentException(
                    "A set of stars should contain only stars.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set null stars for a constellation.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    //extra description or comments
    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     */
    public void setComments(String comments) {
        if ((comments != null) && (comments.length() > 0)) {
            this.comments = comments;
        } else {
            throw new IllegalArgumentException(
                "Comments can't be null or empty.");
        }
    }
}
