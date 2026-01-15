// BiblioJournalArticle.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

/**
 * Represents a journal article.
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
 * @version $Id: BiblioJournalArticle.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioJournalArticle extends BiblioArticle {
    /**
     * DOCUMENT ME!
     */
    public String volume;

    /**
     * DOCUMENT ME!
     */
    public String issue;

    /**
     * DOCUMENT ME!
     */
    public String issueSupplement;

    /**
     * DOCUMENT ME!
     */
    public BiblioJournal fromJournal;
}
