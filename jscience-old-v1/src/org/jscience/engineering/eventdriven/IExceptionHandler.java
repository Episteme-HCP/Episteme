package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Interface to be implemented when developing an Exception Handler. Exception
 * handlers are used for logging exceptions thrown during the running of the
 * Engine. Exception handlers must have public no-argument constructors.
 * </p>
 *
 * @author Pete Ford, May 31, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
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

public interface IExceptionHandler {
    /**
     * <p/>
     * Called by the Engine when an exception occurs during normal running.
     * </p>
     *
     * @param ex The Exception to report.
     */
    public abstract void handleException(Exception ex);
}
