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

/**
 * <p/>
 * The abstract superclass of all Entity classes. Entity objects are what the
 * state engine tracks through the state models in response to events. The
 * action methods associated with states are also defined in the Entity - this
 * may seem strange but in practice the state code almost always manipulates
 * Entity data, so making the methods members of the Entity class removes the
 * need to cast the entity references in every state action.
 * </p>
 * <p/>
 * <p/>
 * Entities are created by calling <code>Engine.createEntity()</code>. An
 * entity can delete itself from the system at the end of its lifecycle by
 * calling <code>delete()</code> in the termination state method.
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

public abstract class AbsEntity {
    /**
     * <p/>
     * Identifier of the entity. Each entity associated with a state engine
     * must have an id unique to that entity (the id is used to direct events
     * to the correct entity).
     * </p>
     */
    protected String id;

    /**
     * <p/>
     * Used by the Engine to detect when an event is delivered to an entity
     * that has been deleted from the system.
     * </p>
     */
    boolean active = true;

    /**
     * <p/>
     * The current state of the entity (references a State object in the state
     * model).
     * </p>
     */
    protected State currentState;

    /**
     * <p/>
     * This entity's dispatcher, in thread-per-entity mode.
     * </p>
     */
    Dispatcher dispatcher;

    /**
     * <p/>
     * Entity's event queue.
     * </p>
     */
    EventQueue eventQueue;

    /**
     * <p/>
     * The state engine within which this entity exists.
     * </p>
     */
    protected Engine ownerEngine;

    /**
     * <p/>
     * The state model that the entity is associated with.
     * </p>
     */
    protected Model ownerModel;

    /**
     * <p/>
     * Deletes this entity from the state engine (usually called from the
     * termination state code in the state model).
     * </p>
     */
    protected final void delete() {
        active = false;
        ownerEngine.deleteEntity(this);
    }

    /**
     * <p/>
     * Generate a normal event.
     * </p>
     *
     * @param entity    The target entity.
     * @param eventSpec The event type specifier.
     * @param args      The arguments to the target state code.
     * @throws StateProcessingException if any arguments are <code>null</code>.
     */
    protected final void generateEvent(AbsEntity entity, EventSpec eventSpec,
                                       Object[] args) throws StateProcessingException {
        if ((entity == null) || (eventSpec == null) || (args == null)) {
            throw new StateProcessingException("Null Argument");
        }

        ownerEngine.generateEvent(entity, eventSpec, args, true);
    }

    /**
     * <p/>
     * Generate a normal event.
     * </p>
     *
     * @param entity      The target entity.
     * @param eventSpecId The name of the event type specifier.
     * @param args        The arguments to the target state code.
     * @throws StateProcessingException if any arguments are <code>null</code>
     *                                  or the event spec name is not recognized.
     */
    protected final void generateEvent(AbsEntity entity, String eventSpecId,
                                       Object[] args) throws StateProcessingException {
        if ((entity == null) || (eventSpecId == null) || (args == null)) {
            throw new StateProcessingException("Null Argument");
        }

        if (!entity.ownerModel.eventSpecMap.containsKey(eventSpecId)) {
            throw new StateProcessingException("Event type <" + eventSpecId +
                    "> not known in model <" + entity.ownerModel.id + ">");
        }

        EventSpec eventSpec = (EventSpec) entity.ownerModel.eventSpecMap.get(eventSpecId);
        ownerEngine.generateEvent(entity, eventSpec, args, true);
    }

    /**
     * <p/>
     * Generate a normal event.
     * </p>
     *
     * @param entityId    The target entity id.
     * @param eventSpecId The name of the event type specifier.
     * @param args        The arguments to the target state code.
     * @throws StateProcessingException if any arguments are <code>null</code>
     *                                  or the entity or event spec name is not recognized..
     */
    protected final void generateEvent(String entityId, String eventSpecId,
                                       Object[] args) throws StateProcessingException {
        if ((entityId == null) || (eventSpecId == null) || (args == null)) {
            throw new StateProcessingException("Null Argument");
        }

        if (!ownerEngine.entityMap.containsKey(entityId)) {
            throw new StateProcessingException("Entity <" + entityId +
                    "> not known");
        }

        AbsEntity entity = (AbsEntity) ownerEngine.entityMap.get(entityId);

        if (!entity.ownerModel.eventSpecMap.containsKey(eventSpecId)) {
            throw new StateProcessingException("Event type <" + eventSpecId +
                    "> not known in model <" + entity.ownerModel.id + ">");
        }

        EventSpec eventSpec = (EventSpec) entity.ownerModel.eventSpecMap.get(eventSpecId);
        ownerEngine.generateEvent(entity, eventSpec, args, true);
    }

    /**
     * <p/>
     * Generate an internal event.
     * </p>
     *
     * @param eventSpecId The name of the event type specifier.
     * @param args        The arguments to the target state code.
     * @throws StateProcessingException if any arguments are <code>null</code>
     *                                  or the event spec name is not recognized.
     */
    protected final void generateInternalEvent(String eventSpecId, Object[] args)
            throws StateProcessingException {
        if ((eventSpecId == null) || (args == null)) {
            throw new StateProcessingException("Null Argument");
        }

        if (!ownerModel.eventSpecMap.containsKey(eventSpecId)) {
            throw new StateProcessingException("Event type <" + eventSpecId +
                    "> not known in model <" + ownerModel.id + ">");
        }

        EventSpec eventSpec = (EventSpec) ownerModel.eventSpecMap.get(eventSpecId);
        ownerEngine.generateEvent(this, eventSpec, args, false);
    }

    /**
     * <p/>
     * Generate an internal event with no arguments.
     * </p>
     *
     * @param eventSpecId The name of the event type specifier.
     * @throws StateProcessingException if any arguments are <code>null</code>
     *                                  or the event spec name is not recognized.
     */
    protected final void generateInternalEvent(String eventSpecId)
            throws StateProcessingException {
        if (eventSpecId == null) {
            throw new StateProcessingException("Null Argument");
        }

        if (!ownerModel.eventSpecMap.containsKey(eventSpecId)) {
            throw new StateProcessingException("Event type <" + eventSpecId +
                    "> not known in model <" + ownerModel.id + ">");
        }

        EventSpec eventSpec = (EventSpec) ownerModel.eventSpecMap.get(eventSpecId);
        ownerEngine.generateEvent(this, eventSpec, new Object[0], false);
    }

    /**
     * <p/>
     * Generate an internal event.
     * </p>
     *
     * @param eventSpec The event type specifier.
     * @param args      The arguments to the target state code.
     * @throws StateProcessingException if any arguments are <code>null</code>
     *                                  or the event spec name is not recognized.
     */
    protected final void generateInternalEvent(EventSpec eventSpec,
                                               Object[] args) throws StateProcessingException {
        if ((eventSpec == null) || (args == null)) {
            throw new StateProcessingException("Null Argument");
        }

        ownerEngine.generateEvent(this, eventSpec, args, false);
    }
}
