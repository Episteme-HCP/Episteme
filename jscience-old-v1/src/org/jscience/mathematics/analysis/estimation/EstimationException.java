package org.jscience.mathematics.analysis.estimation;

/**
 * This class represents exceptions thrown by the estimation solvers.
 *
 * @author L. Maisonobe
 * @version $Id: EstimationException.java,v 1.2 2007-10-23 18:19:01 virtualcall Exp $
 */
public class EstimationException extends Exception {
/**
     * Simple constructor. build an exception with a default message.
     */
    public EstimationException() {
        super("Estimation problem.");
    }

/**
     * Simple constructor. build an exception with the specified message.
     *
     * @param message message to use to build the exception
     */
    public EstimationException(String message) {
        super(message);
    }
}
