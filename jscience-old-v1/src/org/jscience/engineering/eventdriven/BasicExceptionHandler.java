package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Provides the default reporting/handling of run-time exceptions. This handler
 * dumps the exception's stack trace to the standard error stream.
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

public class BasicExceptionHandler implements IExceptionHandler {
    /*
     * (non-Javadoc)
     *
     * @see org.jscience.engineering.eventdriven.IExceptionHandler#handleException(java.lang.Exception)
     */
    public void handleException(Exception ex) {
        ex.printStackTrace();
    }
}
