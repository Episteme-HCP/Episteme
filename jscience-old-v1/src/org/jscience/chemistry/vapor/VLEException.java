/*
 * Generic exception class for VLE.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor;

/**
 * Generic exception class for VLE.
 */
public class VLEException extends Exception {
    /**
     * Creates a new VLEException object.
     */
    public VLEException() {
        super();
    }

    /**
     * Creates a new VLEException object.
     *
     * @param message DOCUMENT ME!
     */
    public VLEException(String message) {
        super(message);
    }
}
