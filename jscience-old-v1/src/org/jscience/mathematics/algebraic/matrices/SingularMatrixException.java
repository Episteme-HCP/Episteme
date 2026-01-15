package org.jscience.mathematics.algebraic.matrices;

/**
 * This class represent exceptions thrown by some matrix operations.
 *
 * @author L. Maisonobe
 * @version $Id: SingularMatrixException.java,v 1.3 2007-10-23 18:18:44 virtualcall Exp $
 */

//this class is currently unused by our implementations although this would be a good practice
public class SingularMatrixException extends Exception {
/**
     * Simple constructor. build an exception with a default message.
     */
    SingularMatrixException() {
        super("singular matrix");
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message DOCUMENT ME!
     */
    public SingularMatrixException(String message) {
        super(message);
    }
}
