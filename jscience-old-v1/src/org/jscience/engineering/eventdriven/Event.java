package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Events are delivered to entities by the Engine, precipitating state changes.
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

final class Event {
    /**
     * <p/>
     * The target entity.
     * </p>
     */
    AbsEntity entity;

    /**
     * <p/>
     * The type of event.
     * </p>
     */
    EventSpec eventSpec;

    /**
     * <p/>
     * The event arguments.
     * </p>
     */
    Object[] args;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param entity    The target entity.
     * @param eventSpec The event type.
     * @param args      The event arguments.
     */
    Event(AbsEntity entity, EventSpec eventSpec, Object[] args) {
        this.entity = entity;
        this.eventSpec = eventSpec;
        this.args = args;
    }
}
