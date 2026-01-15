package org.jscience.util;

/**
 * The UnavailableDataException is thrown when there are problems fetching
 * some data from un unreliable source (this can be the network availability
 * or because the data is not yet ready).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class UnavailableDataException extends Exception {
/**
     * Creates a new UnavailableDataException object.
     */
    public UnavailableDataException() {
        super();
    }

/**
     * Creates a new UnavailableDataException object.
     *
     * @param msg DOCUMENT ME!
     */
    public UnavailableDataException(String msg) {
        super(msg);
    }
}
