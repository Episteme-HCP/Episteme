package org.jscience.util;

/**
 * The IllegalDimensionException class is thrown when a conflicting number
 * of dimensions is found in a system with dimensions, it is therefore a kind
 * of generalization of IndexOutOfBoundsException.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be this should extend IndexOutOfBoundsException or IllegalArgumentException
//may be we should extend Exception
public final class IllegalDimensionException extends RuntimeException {
/**
     * Creates a new IllegalDimensionException object.
     */
    public IllegalDimensionException() {
        super();
    }

/**
     * Creates a new IllegalDimensionException object.
     *
     * @param cause DOCUMENT ME!
     */
    public IllegalDimensionException(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new IllegalDimensionException object.
     *
     * @param message DOCUMENT ME!
     */
    public IllegalDimensionException(String message) {
        super(message);
    }

/**
     * Creates a new IllegalDimensionException object.
     *
     * @param message DOCUMENT ME!
     * @param cause   DOCUMENT ME!
     */
    public IllegalDimensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
