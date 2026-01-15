package org.jscience.mathematics.analysis.ode;

/**
 * This exception is made available to users to report the error conditions
 * that are triggered during integration
 *
 * @author Luc Maisonobe
 * @version $Id: IntegrationException.java,v 1.2 2007-10-23 18:19:19 virtualcall Exp $
 */
public class IntegrationException extends Exception {
/**
     * Simple constructor. Build an exception with a default message
     */
    public IntegrationException() {
        super();
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message DOCUMENT ME!
     */
    public IntegrationException(String message) {
        super(message);
    }
}
