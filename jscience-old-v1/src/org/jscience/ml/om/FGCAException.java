/* ====================================================================
 * /FCGAException.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

/**
 * The root class for all exceptions used in the API.
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public class FGCAException extends Exception {
    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of a FGCAException.
     *
     * @param message The exceptions message
     */
    public FGCAException(String message) {
        super(addMessageFlavour(message));
    }

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of a FGCAException.
     *
     * @param message The exceptions message
     * @param cause   The exceptions cause
     */
    public FGCAException(String message, Throwable cause) {
        super(addMessageFlavour(message), cause);
    }

    // ---------------
    // Private methods ---------------------------------------------------
    // ---------------

    // -------------------------------------------------------------------
    /**
     * Adds a special flavour around the exceptions message that sould
     * make it easier to point out FGCAExceptions.
     *
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String addMessageFlavour(String message) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\n*********");
        buffer.append(message);
        buffer.append("*********\n");

        return buffer.toString();
    }
}
