package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Represents a state transition.
 * </p>
 *
 * @author Pete Ford, May 29, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
 * @see org.jscience.engineering.eventdriven.ITransitionType
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

final class Transition {
    /**
     * <p/>
     * The type of transition.
     * </p>
     *
     * @see ITransitionType
     */
    int transitionType;

    /**
     * <p/>
     * The target state.
     * </p>
     */
    State endState;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param transitionType The type.
     * @param endState       The target state.
     */
    Transition(int transitionType, State endState) {
        this.transitionType = transitionType;
        this.endState = endState;
    }
}
