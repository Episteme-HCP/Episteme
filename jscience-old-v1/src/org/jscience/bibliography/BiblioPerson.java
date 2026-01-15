// BiblioPerson.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

/**
 * Represents a person dealing with the bibliographic resources.
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
 * @version $Id: BiblioPerson.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioPerson extends BiblioProvider {
    /**
     * DOCUMENT ME!
     */
    public String surname;

    /**
     * DOCUMENT ME!
     */
    public String firstName;

    /**
     * DOCUMENT ME!
     */
    public String midInitials;

    /**
     * DOCUMENT ME!
     */
    public String email;

    /**
     * DOCUMENT ME!
     */
    public String postalAddress;

    /**
     * DOCUMENT ME!
     */
    public String affiliation;
}
