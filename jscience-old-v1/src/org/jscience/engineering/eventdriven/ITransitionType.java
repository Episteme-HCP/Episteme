package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Contains a set of constants used to specify transition types.
 * </p>
 *
 * @author Pete Ford, May 29, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
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

public interface ITransitionType {
    /**
     * <p/>
     * NORMAL transition. The entity state changes to the new state and the
     * state code is executed.
     * </p>
     */
    public final static int NORMAL = 0;

    /**
     * <p/>
     * DO NOT EXECUTE transition. The entity state changes to the new state but
     * the state code is not executed.
     * </p>
     */
    public final static int DO_NOT_EXECUTE = 1;

    /**
     * <p/>
     * EXCURSION transition. The entity state changes to the new state and the
     * state code is executed, then the entity state is restored to its
     * starting value.
     * </p>
     */
    public final static int EXCURSION = 2;

    /**
     * <p/>
     * IGNORE transition. The entity state is not changed and no state code is
     * executed.
     * </p>
     */
    public final static int IGNORE = 3;

    /**
     * <p/>
     * Used for bounds checking.
     * </p>
     */
    public final static int MIN_VALUE = 0;

    /**
     * <p/>
     * Used for bounds checking.
     * </p>
     */
    public final static int MAX_VALUE = 3;
}
