// BiblioEntryStatus.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

import java.util.Hashtable;


/**
 * Represents a status of a record in a bibliographic repository.
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
 * @version $Id: BiblioEntryStatus.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioEntryStatus {
    /**
     * DOCUMENT ME!
     */
    public Hashtable properties = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    public String lastModifiedDate;

    /**
     * DOCUMENT ME!
     */
    public String repositorySubset;
}
