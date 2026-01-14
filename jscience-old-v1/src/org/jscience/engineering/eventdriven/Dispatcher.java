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
 * This thread is responsible for taking events from the event queue and
 * delivering them to the entities. Delivery entails locating the target
 * entity, identifying the required transition, updating the state of the
 * entity and executing the state code against that entity. The exact actions
 * may be modified according to the transition type.
 * </p>
 *
 * @author Pete Ford, Jun 1, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
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

class Dispatcher extends Thread {
    /**
     * <p/>
     * The owning Engine.
     * </p>
     */
    Engine engine;

    /**
     * <p/>
     * The event queue.
     * </p>
     */
    EventQueue eventQueue;

    /**
     * <p/>
     * Used for <code>controlledStart()</code>.
     * </p>
     */
    private boolean ready;

    /**
     * <p/>
     * Reflects and controls the run state of the dispatcher.
     * </p>
     */
    private boolean running;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param engine
     * @param eventQueue
     */
    Dispatcher(Engine engine, EventQueue eventQueue) {
        this.engine = engine;
        this.eventQueue = eventQueue;
        running = false;
        ready = false;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        // Flag can be cleared to stop the Dispatcher.
        running = true;

        synchronized (this) {
            ready = true;
            notify();
        }

        while (running) {
            // Get the next event off the queue. Ignore null events and
            // interrupts.
            Event event;

            try {
                event = eventQueue.dequeue();
            } catch (InterruptedException ex) {
                continue;
            }

            if (event == null) {
                continue;
            }

            // Identify the target entity.
            AbsEntity entity = event.entity;

            // If the entity is not active, that means we're trying to deliver
            // an event to an entity that has already been terminated.
            if (!entity.active) {
                try {
                    throw new StateProcessingException(
                            "Event delivered to deleted entity <" + entity.id +
                                    ">");
                } catch (StateProcessingException ex) {
                    if (engine.exceptionHandler != null) {
                        engine.exceptionHandler.handleException(ex);
                    }
                }

                continue;
            }

            // Save the original state in case this event means an Excursion
            // transition.
            org.jscience.engineering.eventdriven.State originalState = entity.currentState;

            // Null event spec means this is an initialization event.
            Transition transition = null;

            if (event.eventSpec != null) {
                // Get the transition. No transition means a "can't happen"
                // error - report it and continue.
                transition = (Transition) entity.currentState.transitionMap.get(event.eventSpec);

                if (transition == null) {
                    if (engine.exceptionHandler != null) {
                        engine.exceptionHandler.handleException(new StateProcessingException(
                                "No transition for event <" +
                                        event.eventSpec.id + "> in state <" +
                                        originalState.id + ">"));
                    }

                    continue;
                }
            }

            // If we have a state change handler, report the state change.
            if (engine.stateChangeHandler != null) {
                String endStateStr;

                if (transition != null) {
                    endStateStr = ((transition.transitionType != ITransitionType.IGNORE)
                            ? transition.endState.id : null);
                } else {
                    endStateStr = entity.ownerModel.initialState.id;
                }

                StateChangeRecord stateChangeRecord = new StateChangeRecord(new Date(),
                        entity.id,
                        ((transition != null) ? originalState.id : "<new>"),
                        ((transition != null) ? event.eventSpec.id : "<init>"),
                        event.args,
                        ((transition != null) ? transition.transitionType
                                : ITransitionType.NORMAL),
                        endStateStr);
                engine.stateChangeHandler.handleStateChange(stateChangeRecord);
            }

            // If this is an IGNORE transition, ermm.. ignore it.
            if ((transition != null) &&
                    (transition.transitionType == ITransitionType.IGNORE)) {
                continue;
            }

            // Put the entity into the target state.
            if (transition != null) {
                entity.currentState = transition.endState;
            }

            // Execute the state code unless this is a DO NOT EXECUTE
            // transition.
            if ((transition == null) ||
                    (transition.transitionType != ITransitionType.DO_NOT_EXECUTE)) {
                try {
                    entity.currentState.actionMethod.invoke(entity, event.args);
                } catch (Exception ex) {
                    if (engine.exceptionHandler != null) {
                        engine.exceptionHandler.handleException(ex);
                    }
                }
            }

            // If this is an EXCURSION transition, reset the entity to the
            // stored state.
            if ((transition != null) &&
                    (transition.transitionType == ITransitionType.EXCURSION)) {
                entity.currentState = originalState;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Thread#start()
     */
    public synchronized void start() {
        super.start();

        while (!ready) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * <p/>
     * Order the Dispatcher thread to terminate.
     * </p>
     */
    void shutdown() {
        running = false;
        interrupt();
    }
}
