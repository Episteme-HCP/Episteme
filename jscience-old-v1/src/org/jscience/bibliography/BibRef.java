// BibRef.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

import embl.ebi.utils.ReflectUtils;

import java.util.Hashtable;


/**
 * Represents a bibliographic reference.
 * <p/>
 * <p/>
 * It is a CORBA-independent container of members, without any methods. Some
 * attributes can be <tt>null</tt> if they are not supported by the
 * repository.
 * </p>
 * <p/>
 * <P></p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: BibRef.java,v 1.2 2007-10-21 17:37:41 virtualcall Exp $
 */
public class BibRef {
    /**
     * DOCUMENT ME!
     */
    public Hashtable properties = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    public String identifier;

    /**
     * DOCUMENT ME!
     */
    public String type;

    /**
     * DOCUMENT ME!
     */
    public String[] crossReferences;

    /**
     * DOCUMENT ME!
     */
    public String title;

    /**
     * DOCUMENT ME!
     */
    public BiblioSubject subject;

    /**
     * DOCUMENT ME!
     */
    public BiblioDescription description;

    /**
     * DOCUMENT ME!
     */
    public BiblioScope coverage;

    /**
     * DOCUMENT ME!
     */
    public BiblioProvider[] authors;

    /**
     * DOCUMENT ME!
     */
    public BiblioProvider[] contributors;

    /**
     * DOCUMENT ME!
     */
    public BiblioProvider publisher;

    /**
     * DOCUMENT ME!
     */
    public String rights;

    /**
     * DOCUMENT ME!
     */
    public String date;

    /**
     * DOCUMENT ME!
     */
    public String language;

    /**
     * DOCUMENT ME!
     */
    public String format;

    /**
     * DOCUMENT ME!
     */
    public BiblioEntryStatus entryStatus;

    /**
     * A bit of "pretty" formatting. It even tries to make some indentations
     * :-)
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ReflectUtils.formatPublicFields(this);
    }
}
