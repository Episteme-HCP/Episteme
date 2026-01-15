package org.jscience.mathematics.analysis.optimization;

/**
 * This class represents exceptions thrown by cost functions.
 *
 * @author L. Maisonobe
 * @version $Id: CostException.java,v 1.3 2007-10-23 18:19:19 virtualcall Exp $
 */
public class CostException extends Exception {
/**
     * Simple constructor. Build an exception with a default message
     */
    public CostException() {
        super("Cost exception.");
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message exception message
     */
    public CostException(String message) {
        super(message);
    }

/**
     * Simple constructor. Build an exception from a cause
     *
     * @param cause cause of this exception
     */
    public CostException(Throwable cause) {
        super(cause);
    }

/**
     * Simple constructor. Build an exception from a message and a cause
     *
     * @param message exception message
     * @param cause   cause of this exception
     */
    public CostException(String message, Throwable cause) {
        super(message, cause);
    }
}
