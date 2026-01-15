package org.jscience.measure;

import org.jscience.biology.human.Human;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a description of something.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also accounts for a scientific observation
//"hypothesis", "model", "theory" and, "law" are all based on descriptions
//and don't forget to respect Occam, you damn *!?!
//see http://en.wikipedia.org/wiki/Scientific_method
//could be renamed Describable 
// we could also define an Experiment class as a collection of Measures but Analysis, Description, Sample and Report are here for this purpose
public class Description extends Object implements java.io.Serializable {
    /** DOCUMENT ME! */
    private Set authors;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private String contents;

    //there is no set method because you don't change your description, you perform a new one (eventually stating the previous one was false)
    /**
     * Creates a new Description object.
     *
     * @param authors DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param contents DOCUMENT ME!
     */
    public Description(Set authors, Date date, String contents) {
        Iterator iterator;
        boolean valid;

        if ((authors != null) && (authors.size() > 0) && (date != null) &&
                (contents != null) && (contents.length() > 0)) {
            iterator = authors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.authors = authors;
                this.date = date;
                this.contents = contents;
            } else {
                throw new IllegalArgumentException(
                    "The Set of authors should contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Description constructor can't have null arguments (and authors and contents can't be empty).");
        }
    }

    //a Set of Humans
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAuthors() {
        return authors;
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
    public String getContents() {
        return contents;
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
        Description description;

        if ((o != null) && (o instanceof Description)) {
            description = (Description) o;

            return this.getAuthors().equals(description.getAuthors()) &&
            this.getDate().equals(description.getDate()) &&
            this.getContents().equals(description.getContents());
        } else {
            return false;
        }
    }
}
