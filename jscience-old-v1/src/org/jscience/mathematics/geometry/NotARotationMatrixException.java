package org.jscience.mathematics.geometry;

/**
 * This class represents exceptions thrown while building rotations from
 * matrices.
 *
 * @author L. Maisonobe
 * @version $Id: NotARotationMatrixException.java,v 1.2 2007-10-23 18:19:43 virtualcall Exp $
 */
public class NotARotationMatrixException extends Exception {
/**
     * Simple constructor. build an exception with a default message.
     */
    public NotARotationMatrixException() {
        super("Matrix is not a rotation matrix.");
    }

/**
     * Simple constructor. build an exception with the specified message.
     *
     * @param message message to use to build the exception
     */
    public NotARotationMatrixException(String message) {
        super(message);
    }
}
