package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Interface to be implemented when developing a State Change Handler. State
 * change handlers are used for logging state changes for debugging purposes.
 * State change handlers must have public no-argument constructors.
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

public interface IStateChangeHandler {
    /**
     * <p/>
     * Called by the engine whenever an entity changes state. Concrete
     * implementations can report the details using any suitable mechanism.
     * </p>
     *
     * @param stateChangeRecord The record containing details about the state
     *                          change.
     */
    public abstract void handleStateChange(StateChangeRecord stateChangeRecord);
}
