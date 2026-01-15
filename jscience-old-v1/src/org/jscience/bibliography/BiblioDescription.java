// BiblioDescription.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

/**
 * Represents a description of a bibliographic resource.
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
 * @version $Id: BiblioDescription.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioDescription {
    /**
     * DOCUMENT ME!
     */
    public String theAbstract;

    /**
     * DOCUMENT ME!
     */
    public String abstractType;

    /**
     * DOCUMENT ME!
     */
    public String tableOfContents;

    /**
     * DOCUMENT ME!
     */
    public String tableOfContentsType;

    /**
     * DOCUMENT ME!
     */
    public String language;
}
