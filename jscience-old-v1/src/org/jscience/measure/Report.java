package org.jscience.measure;

import org.jscience.biology.human.Human;

import org.jscience.economics.Organization;

import org.jscience.geography.Place;

import org.jscience.sociology.Person;

import org.jscience.util.Positioned;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;


/**
 * A class representing an information concerning someone or something.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//there is no set method because this is meant to be a paper that you can't fill or modify
//reports can be used to build out descriptions
//can also be used to represent the contents of news inside a newspaper
// we could also define an Experiment class as a collection of Measures but Analysis, Description, Sample and Report are here for this purpose
public class Report extends Object implements java.io.Serializable, Identified,
    Positioned {
    /** DOCUMENT ME! */
    private Organization authority;

    /** DOCUMENT ME! */
    private Identification identification;

    /** DOCUMENT ME! */
    private Human author;

    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Vector contents;

/**
     * Creates a new Report object.
     *
     * @param authority      DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param author         DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param date           DOCUMENT ME!
     * @param contents       DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Report(Organization authority, Identification identification,
        Human author, Place place, Date date, String contents) {
        Vector strings;

        if ((authority != null) && (identification != null) &&
                (author != null) && (place != null) && (date != null) &&
                (contents != null) && (contents.length() > 0)) {
            this.authority = authority;
            this.identification = identification;
            this.author = author;
            this.place = place;
            this.date = date;
            strings = new Vector();

            //may be we should use clone to have a full copy
            strings.add(contents);
            this.contents = strings;
        } else {
            throw new IllegalArgumentException(
                "The Report constructor can't have null or empty arguments.");
        }
    }

/**
     * Creates a new Report object.
     *
     * @param authority      DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param author         DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param date           DOCUMENT ME!
     * @param contents       DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Report(Organization authority, Identification identification,
        Person author, Place place, Date date, Vector contents) {
        Iterator iterator;
        boolean valid;

        if ((authority != null) && (identification != null) &&
                (author != null) && (place != null) && (date != null) &&
                (contents != null) && (contents.size() > 0)) {
            iterator = contents.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.authority = authority;
                this.identification = identification;
                this.author = author;
                this.place = place;
                this.date = date;
                this.contents = contents;
            } else {
                throw new IllegalArgumentException(
                    "The rights Vector must contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Report constructor can't have null or empty arguments.");
        }
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
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Human getAuthor() {
        return author;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
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
    public Vector getContents() {
        return contents;
    }
}
