package org.jscience.mathematics.analysis.ode;

/**
 * This exception is made available to users to report the error conditions
 * that are trigegred while computing the differential equations.
 *
 * @author Luc Maisonobe
 * @version $Id: DerivativeException.java,v 1.2 2007-10-23 18:19:18 virtualcall Exp $
 */
public class DerivativeException extends Exception {
/**
     * Simple constructor. Build an exception with a default message
     */
    public DerivativeException() {
        super();
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message DOCUMENT ME!
     */
    public DerivativeException(String message) {
        super(message);
    }
}
