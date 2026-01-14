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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * <p/>
 * Represents a State.
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

final class State {
    /**
     * <p/>
     * The state's name.
     * </p>
     */
    String id;

    /**
     * <p/>
     * The state's action method.
     * </p>
     */
    Method actionMethod;

    /**
     * <p/>
     * The argument classes expected by the action method.
     * </p>
     */
    Class[] argClasses;

    /**
     * <p/>
     * Maps event types to transitions.
     * </p>
     */
    Map transitionMap;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param id           The state's name.
     * @param actionMethod The state's action method.
     * @param argClasses   The action method's argument classes.
     */
    State(String id, Method actionMethod, Class[] argClasses) {
        this.id = id;
        this.actionMethod = actionMethod;
        this.argClasses = argClasses;
        this.transitionMap = new HashMap();
    }

    /**
     * <p/>
     * Register a transition for a given event type.
     * </p>
     *
     * @param eventSpec  The event type.
     * @param transition The transition.
     * @throws StateModelConfigurationException
     *          if a transition has already
     *          been registered for the event type.
     */
    void addTransition(EventSpec eventSpec, Transition transition)
            throws StateModelConfigurationException {
        if (transitionMap.containsKey(eventSpec)) {
            throw new StateModelConfigurationException(
                    "Transition already defined for event <" + eventSpec.id +
                            "> in state <" + id + ">");
        }

        transitionMap.put(eventSpec, transition);
    }
}
