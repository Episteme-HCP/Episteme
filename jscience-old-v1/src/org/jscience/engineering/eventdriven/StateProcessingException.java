package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Exception class thrown by the Engine during normal running.
 * </p>
 *
 * @author Pete Ford, May 30, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
 */

//**********************************************
//
//This package is rebundled after the code from JSpasm
//
// Project Homepage : http://jspasm.sourceforge.net/
// Original Developer : Pete Ford
// Official Domain : CodeXombie.com
//
//**********************************************

public class StateProcessingException extends Exception {
    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param message Descriptive message.
     */
    public StateProcessingException(String message) {
        super(message);
    }

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param message Descriptive message.
     * @param cause   Nested exception.
     */
    public StateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
