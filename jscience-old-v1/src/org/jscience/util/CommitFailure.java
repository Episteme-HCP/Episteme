package org.jscience.util;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CommitFailure extends Exception {
/**
     * Creates a new CommitFailure object.
     *
     * @param message DOCUMENT ME!
     */
    public CommitFailure(String message) {
        super(message);
    }

/**
     * Creates a new CommitFailure object.
     *
     * @param cause DOCUMENT ME!
     */
    public CommitFailure(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new CommitFailure object.
     *
     * @param message DOCUMENT ME!
     * @param cause   DOCUMENT ME!
     */
    public CommitFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
