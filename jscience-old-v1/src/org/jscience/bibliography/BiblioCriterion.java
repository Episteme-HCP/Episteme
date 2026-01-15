// BiblioCriterion.java
//
//    senger@ebi.ac.uk
//    April 2001
//
package org.jscience.bibliography;

/**
 * Represents an ordering or searching Criterion.
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
 * @version $Id: BiblioCriterion.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioCriterion {
    //
    // Criteria types
    //

    /**
     * DOCUMENT ME!
     */
    public static final int N_A_CRITERION = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int QUERY_CRITERION = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int SORT_CRITERION = 2;

    /**
     * DOCUMENT ME!
     */
    public String name;

    /**
     * DOCUMENT ME!
     */
    public int type = QUERY_CRITERION;

    /**
     * DOCUMENT ME!
     */
    public String[] mutuallyExclusiveWith;

    /**
     * DOCUMENT ME!
     */
    public String forSubset;
}
