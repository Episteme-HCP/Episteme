package org.jscience.mathematics.geometry;

/**
 * This class represents exceptions thrown while extractiong Cardan or
 * Euler angles from a rotation.
 *
 * @author L. Maisonobe
 * @version $Id: CardanEulerSingularityException.java,v 1.2 2007-10-23 18:19:38 virtualcall Exp $
 */
public class CardanEulerSingularityException extends Exception {
/**
     * Simple constructor. build an exception with a default message.
     */
    public CardanEulerSingularityException() {
        super("Cardan/Euler angles singularity.");
    }

/**
     * Simple constructor. build an exception with the specified message.
     *
     * @param message message to use to build the exception
     */
    public CardanEulerSingularityException(String message) {
        super(message);
    }
}
