package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Exception class thrown for errors detected when creating a Model.
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

public class StateModelConfigurationException extends Exception {
    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param message Descriptive message.
     */
    public StateModelConfigurationException(String message) {
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
    public StateModelConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
