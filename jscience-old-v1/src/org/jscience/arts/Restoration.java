package org.jscience.arts;

import org.jscience.economics.Organization;

import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import org.jscience.util.Commented;
import org.jscience.util.Named;

import java.util.Date;


/**
 * A class representing a restoration for a piece of art.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//although this class is very similar to an analysis the use and meaning is very different and should not be mistaken.
public class Restoration extends Object implements Named, Identified, Commented {
    /** DOCUMENT ME! */
    private String name; //name of test

    /** DOCUMENT ME! */
    private Identification identification; //unique identification value

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Artwork sample; //the thing that is restored

    /** DOCUMENT ME! */
    private Organization authority; //the museum who provides the restoration

    /** DOCUMENT ME! */
    private String result;

    /** DOCUMENT ME! */
    private String comments; //the process of the restoration

    //there is no set method because you don't change your restoration, you perform a new one (eventually stating the previous one was false)
    /**
     * Creates a new Restoration object.
     *
     * @param name DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param sample DOCUMENT ME!
     * @param authority DOCUMENT ME!
     */
    public Restoration(String name, Identification identification, Date date,
        Artwork sample, Organization authority) {
        if ((name != null) && (name.length() > 0) && (identification != null) &&
                (sample != null) && (authority != null)) {
            this.name = name;
            this.identification = identification;
            this.date = date;
            this.sample = sample;
            this.authority = authority;
            this.result = null;
            this.comments = null;
        } else {
            throw new IllegalArgumentException(
                "The Restoration constructor can't have null arguments (and name can't be empty).");
        }
    }

/**
     * Creates a new Restoration object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param date           DOCUMENT ME!
     * @param sample         DOCUMENT ME!
     * @param authority      DOCUMENT ME!
     * @param result         DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Restoration(String name, Identification identification, Date date,
        Artwork sample, Organization authority, String result) {
        if ((name != null) && (name.length() > 0) && (identification != null) &&
                (sample != null) && (authority != null) && (result != null) &&
                (result.length() > 0)) {
            this.name = name;
            this.identification = identification;
            this.date = date;
            this.sample = sample;
            this.authority = authority;
            this.result = result;
            this.comments = null;
        } else {
            throw new IllegalArgumentException(
                "The Restoration constructor can't have null arguments (and name and result can't be empty).");
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
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Artwork getSample() {
        return sample;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Organization getAuthority() {
        return authority;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getResult() {
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    //you should set comments only once (unchecked)
    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    //checks fields correspond
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Restoration restoration;

        if ((o != null) && (o instanceof Restoration)) {
            restoration = (Restoration) o;

            return this.getName().equals(restoration.getName()) &&
            this.getIdentification().equals(restoration.getIdentification()) &&
            this.getDate().equals(restoration.getDate()) &&
            this.getSample().equals(restoration.getSample()) &&
            this.getAuthority().equals(restoration.getAuthority()) &&
            this.getResult().equals(restoration.getResult()) &&
            this.getComments().equals(restoration.getComments());
        } else {
            return false;
        }
    }
}
