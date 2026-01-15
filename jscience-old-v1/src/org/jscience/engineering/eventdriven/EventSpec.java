package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Defines a type of event. The event type has a name and a list of argument
 * types.
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

final class EventSpec {
    /**
     * <p/>
     * The event's name.
     * </p>
     */
    String id;

    /**
     * <p/>
     * Array of argument classes.
     * </p>
     */
    Class[] argClasses;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param id         The event id.
     * @param argClasses The argument class list.
     */
    EventSpec(String id, Class[] argClasses) {
        this.id = id;
        this.argClasses = argClasses;
    }
}
