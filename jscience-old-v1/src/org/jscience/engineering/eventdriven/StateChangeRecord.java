package org.jscience.engineering.eventdriven;

import java.util.Date;


/**
 * <p/>
 * This contains all of the details about a state change that a state change
 * handler might want to know when reporting it.
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

public final class StateChangeRecord {
    /**
     * <p/>
     * The time of the state change.
     * </p>
     */
    public Date timeStamp;

    /**
     * <p/>
     * The name of the entity that changed state.
     * </p>
     */
    public String entityId;

    /**
     * <p/>
     * The state of the entity before the state change.
     * </p>
     */
    public String startStateId;

    /**
     * <p/>
     * The event type.
     * </p>
     */
    public String eventSpecId;

    /**
     * <p/>
     * The event arguments.
     * </p>
     */
    public Object[] eventArgs;

    /**
     * <p/>
     * The transition type code.
     * </p>
     */
    public int transitionType;

    /**
     * <p/>
     * The state of the entity after the state change.
     * </p>
     */
    public String endStateId;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param timeStamp      The time of the state change.
     * @param entityId       The name of the entity that changed state.
     * @param startStateId   The state of the entity before the state change.
     * @param eventSpecId    The event type.
     * @param eventArgs      The event arguments.
     * @param transitionType The transition type code.
     * @param endStateId     The state of the entity after the state change.
     */
    StateChangeRecord(Date timeStamp, String entityId, String startStateId,
                      String eventSpecId, Object[] eventArgs, int transitionType,
                      String endStateId) {
        this.timeStamp = timeStamp;
        this.entityId = entityId;
        this.startStateId = startStateId;
        this.eventSpecId = eventSpecId;
        this.eventArgs = eventArgs;
        this.transitionType = transitionType;
        this.endStateId = endStateId;
    }
}
