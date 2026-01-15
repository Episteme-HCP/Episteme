package org.jscience.mathematics.analysis.optimization;

/**
 * This class represents exceptions thrown by optimization algorithms.
 *
 * @author L. Maisonobe
 * @version $Id: NoConvergenceException.java,v 1.3 2007-10-23 18:19:20 virtualcall Exp $
 */
public class NoConvergenceException extends Exception {
/**
     * Simple constructor. Build an exception with a default message
     */
    public NoConvergenceException() {
        super("No convergence.");
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message exception message
     */
    public NoConvergenceException(String message) {
        super(message);
    }

/**
     * Simple constructor. Build an exception from a cause
     *
     * @param cause cause of this exception
     */
    public NoConvergenceException(Throwable cause) {
        super(cause);
    }

/**
     * Simple constructor. Build an exception from a message and a cause
     *
     * @param message exception message
     * @param cause   cause of this exception
     */
    public NoConvergenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
