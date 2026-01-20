package org.jscience.law;

import org.jscience.util.Commented;
import org.jscience.util.Named;
import org.jscience.util.Numbering;

import java.util.Date;


/**
 * A class representing an element of a law, code, constitution.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Article extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name; //rarely used

    /** DOCUMENT ME! */
    private Numbering numbering;

    /** DOCUMENT ME! */
    private String contents;

    /** DOCUMENT ME! */
    private String comments; //they are part of the article and should be considered as important as the contents themselves. They are used to make an idea clearer.

    /** DOCUMENT ME! */
    private Date date; //the date at which this article was voted, or printed or applied depending on the constitution

/**
     * Creates a new Article object.
     *
     * @param numbering DOCUMENT ME!
     * @param contents  DOCUMENT ME!
     */
    public Article(Numbering numbering, String contents) {
        this(new String(""), numbering, contents, new String(""), null);
    }

/**
     * Creates a new Article object.
     *
     * @param numbering DOCUMENT ME!
     * @param contents  DOCUMENT ME!
     * @param date      DOCUMENT ME!
     */
    public Article(Numbering numbering, String contents, Date date) {
        this(new String(""), numbering, contents, new String(""), date);
    }

/**
     * Creates a new Article object.
     *
     * @param name      DOCUMENT ME!
     * @param numbering DOCUMENT ME!
     * @param contents  DOCUMENT ME!
     * @param comments  DOCUMENT ME!
     * @param date      DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Article(String name, Numbering numbering, String contents,
        String comments, Date date) {
        if ((name != null) && (name.length() > 0) && (numbering != null) &&
                (contents != null) && (contents.length() > 0) &&
                (comments != null) && (date != null)) {
            this.name = name;
            this.numbering = numbering;
            this.contents = contents;
            this.comments = comments;
            this.date = date;
        } else {
            throw new IllegalArgumentException(
                "The Article constructor can't have null arguments.");
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
    public Numbering getNumbering() {
        return numbering;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContents() {
        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    //may be null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }
}
