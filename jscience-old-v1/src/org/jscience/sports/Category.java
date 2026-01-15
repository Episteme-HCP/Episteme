package org.jscience.sports;

import org.jscience.util.Named;


/**
 * A class representing a category of competitors within a sport.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Category extends Object implements Named {
    /** DOCUMENT ME! */
    private String name; //the given name

    /** DOCUMENT ME! */
    private String description; //a lenghty description of the category

/**
     * Creates a new Category object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Category(String name, String description) {
        if ((name != null) && (description != null)) {
            this.name = name;
            this.description = description;
        } else {
            throw new IllegalArgumentException(
                "The Category constructor can't have null arguments.");
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
    public String getDescription() {
        return description;
    }
}
