package org.jscience.util;

/**
 * A class representing a CircularReferenceException, to be used with
 * trees.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we should extend Exception
public class CircularReferenceException extends RuntimeException {
/**
     * Creates a new CircularReferenceException object.
     */
    public CircularReferenceException() {
        super();
    }

/**
     * Creates a new CircularReferenceException object.
     *
     * @param cause DOCUMENT ME!
     */
    public CircularReferenceException(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new CircularReferenceException object.
     *
     * @param message DOCUMENT ME!
     */
    public CircularReferenceException(String message) {
        super(message);
    }

/**
     * Creates a new CircularReferenceException object.
     *
     * @param message DOCUMENT ME!
     * @param cause   DOCUMENT ME!
     */
    public CircularReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
