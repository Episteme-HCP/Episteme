// CandyEntry.java
//
//    senger@ebi.ac.uk
//    February 2001
//
package org.jscience.bibliography.candy;

import java.util.Hashtable;

/**
 * <p/>
 * This is a basic container for a vocabulary entry. It consists only
 * of the basic attributes which is sufficient for the vocabularies
 * providing string-type contents.
 * </p>
 * <p/>
 * <p/>
 * However, it may still accomodate more complex data types using
 * the {@link #extras} member.
 * </p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: CandyEntry.java,v 1.2 2007-10-21 17:37:41 virtualcall Exp $
 */
public class CandyEntry {
    /**
     * A unique identifier of this entry.
     */
    public String entry = "";

    /**
     * A value of this entry.
     */
    public String description = "";

    /**
     * A container for the additional properties represented by this entry.
     */
    public Hashtable extras = new Hashtable();

    /**
     * An empty constructor.
     */
    public CandyEntry() {
    }

    /**
     * It creates an entry instance with given name and empty value.
     */
    public CandyEntry(String entry) {
        this(entry, "", null);
    }

    /**
     * It creates an entry instance with given name and value.
     */
    public CandyEntry(String entry, String description) {
        this(entry, description, null);
    }

    /**
     * It creates an entry instance with given name, value and
     * additional properties.
     */
    public CandyEntry(String entry, String description, Hashtable extras) {
        this.entry = entry;
        this.description = description;

        if (extras != null) {
            this.extras = extras;
        }
    }

    /**
     * It prints the entry contents.
     */
    public String toString() {
        return entry + "\t" + description +
                (((extras != null) && (extras.size() > 0)) ? ("\n\t" +
                        extras.toString()) : "");
    }
}
