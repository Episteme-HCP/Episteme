package org.jscience.io.fits;

/**
 * Thrown when the requested header card is not present.
 */
public class NoSuchFitsCardException extends FitsCardException {
/**
     * Create a new exception
     *
     * @param message - a human-readable description of the problem.
     */
    public NoSuchFitsCardException(String message) {
        super(message);
    }
}
