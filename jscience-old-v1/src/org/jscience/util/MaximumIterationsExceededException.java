package org.jscience.util;

/**
 * This exception occurs when a numerical algorithm exceeds it maximum
 * number of allowable iterations.
 *
 * @author Mark Hale
 * @version 1.0
 */

//may be we should extend Exception
public final class MaximumIterationsExceededException extends RuntimeException {
/**
     * Constructs a MaximumIterationsExceededException with no detail message.
     */
    public MaximumIterationsExceededException() {
    }

/**
     * Constructs a MaximumIterationsExceededException with the specified
     * detail message.
     *
     * @param s DOCUMENT ME!
     */
    public MaximumIterationsExceededException(String s) {
        super(s);
    }
}
