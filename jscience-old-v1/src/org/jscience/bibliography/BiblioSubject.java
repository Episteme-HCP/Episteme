// BiblioSubject.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

import java.util.Hashtable;


/**
 * Represents a bibliographic subject (e.i.meywords, subheadings, etc.).
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
 * @version $Id: BiblioSubject.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioSubject {
    /**
     * DOCUMENT ME!
     */
    public Hashtable keywords = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    public Hashtable subjectHeadings = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    public String subjectHeadingsSource;

    /**
     * DOCUMENT ME!
     */
    public Hashtable codes = new Hashtable();
}
