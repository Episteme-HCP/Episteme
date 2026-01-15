package org.jscience.mathematics.analysis;

/**
 * This class represents exceptions thrown by sample iterators.
 *
 * @author L. Maisonobe
 * @version $Id: ExhaustedSampleException.java,v 1.2 2007-10-23 18:18:53 virtualcall Exp $
 */

//may be to be replaced by org.jscience.util.UnavailableDataException
public class ExhaustedSampleException extends Exception {
/**
     * Simple constructor. Build an exception with a default message
     */
    public ExhaustedSampleException() {
        super("Exhausted samples.");
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message exception message
     */
    public ExhaustedSampleException(String message) {
        super(message);
    }
}
