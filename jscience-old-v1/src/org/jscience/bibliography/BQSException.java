// BQSException.java
//
//    Just a renamed GException - to get more specific error messages.
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

import embl.ebi.utils.GException;


/**
 * Just a renamed GException - to get more specific error messages.
 * <p/>
 * <P></p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: BQSException.java,v 1.2 2007-10-21 17:37:41 virtualcall Exp $
 */
public class BQSException extends GException {
    /**
     * Creates a new BQSException object.
     */
    public BQSException() {
        super();
    }

    /**
     * Creates a new BQSException object.
     *
     * @param s DOCUMENT ME!
     */
    public BQSException(String s) {
        super(s);
    }

    /**
     * Creates a new BQSException object.
     *
     * @param s                    DOCUMENT ME!
     * @param theOriginalException DOCUMENT ME!
     */
    public BQSException(String s, Throwable theOriginalException) {
        super(s, theOriginalException);
    }
}
