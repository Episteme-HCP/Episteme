package org.jscience.law;

import org.jscience.biology.human.Human;

import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import org.jscience.util.Commented;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing the paper (contract)  which defines ownership,
 * citizenship...
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//no relation with an act in a play, see Jsci.arts.theater.Act.
public class Act extends Object implements Identified, Commented {
    /** DOCUMENT ME! */
    public final static String BIRTH = "Birth";

    /** DOCUMENT ME! */
    public final static String RESIDENCE = "Residence";

    /** DOCUMENT ME! */
    public final static String NATIONALITY = "Nationality";

    /** DOCUMENT ME! */
    public final static String MARRIAGE = "Marriage";

    /** DOCUMENT ME! */
    public final static String DIVORCE = "Divorce";

    /** DOCUMENT ME! */
    public final static String PROPERTY = "Property";

    /** DOCUMENT ME! */
    public final static String APTITUDE = "Aptitude";

    /** DOCUMENT ME! */
    public final static String DEATH = "Death";

    /** DOCUMENT ME! */
    private Identification identification;

    /** DOCUMENT ME! */
    private String matter;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private String object; //"you ... have the right to ... under these circumstances..."

    /** DOCUMENT ME! */
    private Set subjects; //the people in the act, a Set of humans

    /** DOCUMENT ME! */
    private String comments;

    //subjects is a Set of String or Humans (contents is unchecked)
    /**
     * Creates a new Act object.
     *
     * @param identification DOCUMENT ME!
     * @param matter DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param object DOCUMENT ME!
     * @param subjects DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Act(Identification identification, String matter, Date date,
        String object, Set subjects, String comments) {
        Iterator iterator;
        boolean valid;

        if ((identification != null) && (matter != null) &&
                (matter.length() > 0) && (date != null) && (object != null) &&
                (object.length() > 0) && (subjects != null) &&
                (comments != null) && (comments.length() > 0)) {
            iterator = subjects.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.identification = identification;
                this.matter = matter;
                this.date = date;
                this.object = object;
                this.subjects = subjects;
                this.comments = comments;
            } else {
                throw new IllegalArgumentException(
                    "The subjects Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Act constructor can't have null arguments.");
        }
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
    public String getMatter() {
        return matter;
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
    public String getObject() {
        return object;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSubjects() {
        return subjects;
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
