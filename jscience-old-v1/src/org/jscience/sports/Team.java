package org.jscience.sports;

import org.jscience.biology.Individual;

import org.jscience.util.Named;

import java.util.Date;


/**
 * A class representing a group of competitors going together.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Team extends Object implements Named {
    /** DOCUMENT ME! */
    private String name; //the given name

    /** DOCUMENT ME! */
    private String description; //a lenghty description of the category

    /** DOCUMENT ME! */
    private Date season;

    /** DOCUMENT ME! */
    private Individual[] members;

/**
     * Creates a new Team object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param season      DOCUMENT ME!
     * @param members     DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Team(String name, String description, Date season,
        Individual[] members) {
        if ((name != null) && (description != null) && (season != null) &&
                (members != null)) {
            this.name = name;
            this.description = description;
            this.season = season;
            this.members = members;
        } else {
            throw new IllegalArgumentException(
                "The Team constructor can't have null arguments.");
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return season;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Individual[] getMembers() {
        return members;
    }
}
