package org.jscience.io.fits;

/**
 * Thrown when the requested HDU is not present in the FITS file.
 */
public class NoSuchFitsHDUException extends FitsException {
/**
     * Create a new exception
     *
     * @param message - a human-readable description of the problem.
     */
    public NoSuchFitsHDUException(String message) {
        super(message);
    }
}
