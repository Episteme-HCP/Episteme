// BiblioWebResource.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

/**
 * Represents a WWW resource.
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
 * @version $Id: BiblioWebResource.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioWebResource extends BibRef {
    /**
     * DOCUMENT ME!
     */
    public String url;

    /**
     * DOCUMENT ME!
     */
    public int estimatedSize = 0;

    /**
     * DOCUMENT ME!
     */
    public String cost;
}
