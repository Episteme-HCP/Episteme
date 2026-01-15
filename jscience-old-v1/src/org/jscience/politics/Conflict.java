package org.jscience.politics;

import org.jscience.biology.Population;

import org.jscience.geography.Place;

import org.jscience.util.Commented;
import org.jscience.util.Named;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing the basic information about a conflict.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Conflict extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Set groups;

    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private Date startingDate;

    /** DOCUMENT ME! */
    private Date endDate;

    /** DOCUMENT ME! */
    private String comments;

/**
     * Creates a new Conflict object.
     *
     * @param name         DOCUMENT ME!
     * @param groups       DOCUMENT ME!
     * @param place        DOCUMENT ME!
     * @param startingDate DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Conflict(String name, Set groups, Place place, Date startingDate) {
        if ((name != null) && (name.length() > 0) && (groups != null) &&
                (groups.size() > 0) && (place != null) &&
                (startingDate != null)) {
            Iterator iterator;
            boolean valid;

            iterator = groups.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Population;
            }

            if (valid) {
                this.name = name;
                this.place = place;
                this.groups = groups;
                this.startingDate = startingDate;
                this.endDate = null;
                this.comments = null;
            } else {
                throw new IllegalArgumentException(
                    "The groups Set must contain only Populations.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Conflict constructor can't have null or empty arguments.");
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
            throw new IllegalArgumentException(
                "The name can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getGroups() {
        return groups;
    }

    /**
     * DOCUMENT ME!
     *
     * @param groups DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setGroups(Set groups) {
        Iterator iterator;
        boolean valid;

        if ((groups != null) && (groups.size() > 0)) {
            iterator = groups.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Population;
            }

            if (valid) {
                this.groups = groups;
            } else {
                throw new IllegalArgumentException(
                    "The groups Set must contain only Populations.");
            }
        } else {
            throw new IllegalArgumentException(
                "The groups Set can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPlace() {
        return place;
    }

    /**
     * DOCUMENT ME!
     *
     * @param place DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPlace(Place place) {
        if ((place != null)) {
            this.place = place;
        } else {
            throw new IllegalArgumentException("The place must be non null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getStartingDate() {
        return startingDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setStartingDate(Date date) {
        if ((date != null)) {
            this.startingDate = date;
        } else {
            throw new IllegalArgumentException("The date must be non null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //may return null
    public Date getEndDate() {
        return endDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     */
    public void setEndDate(Date date) {
        this.endDate = date;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //may return null
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
