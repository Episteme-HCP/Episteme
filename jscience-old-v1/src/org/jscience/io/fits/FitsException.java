package org.jscience.io.fits;

import java.io.IOException;


/**
 * Thrown when there is a problem with the FITS format.
 */
public class FitsException extends IOException {
/**
     * Create a new exception
     *
     * @param message - a human-readable description of the problem.
     */
    public FitsException(String message) {
        super(message);
    }
}
