/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
