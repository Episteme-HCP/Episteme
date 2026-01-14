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

package org.jscience.util;

import java.util.EventObject;


/**
 * Event which encapsulates a change in any mutable BioJava object.
 *
 * @author Thomas Down
 * @author Matthew Pocock
 * @author Greg Cox
 *
 * @since 1.1
 */
public class ChangeEvent extends EventObject {
    /** DOCUMENT ME! */
    private final ChangeType type;

    /** DOCUMENT ME! */
    private final Object change;

    /** DOCUMENT ME! */
    private final Object previous;

    /** DOCUMENT ME! */
    private final ChangeEvent chain;

/**
     * Construct a ChangeEvent with no change details.
     *
     * @param source The object being changed.
     * @param type   The type of change being made.
     */
    public ChangeEvent(Object source, ChangeType type) {
        this(source, type, null, null, null);
    }

/**
     * Construct a ChangeEvent specifying a new value for a property, or an
     * object to be added to a collection.
     *
     * @param source The object being changed.
     * @param type   The type of change being made.
     * @param change The new value of the property being changed.
     */
    public ChangeEvent(Object source, ChangeType type, Object change) {
        this(source, type, change, null, null);
    }

/**
     * Construct a ChangeEvent specifying a new value for a property, and
     * giving the previous value.
     *
     * @param source   The object being changed.
     * @param type     The type of change being made.
     * @param change   The new value of the property being changed.
     * @param previous The old value of the property being changed.
     */
    public ChangeEvent(Object source, ChangeType type, Object change,
        Object previous) {
        this(source, type, change, previous, null);
    }

/**
     * Construct a ChangeEvent to be fired because another ChangeEvent has been
     * received from a property object.
     *
     * @param source   The object being changed.
     * @param type     The type of change being made.
     * @param change   The new value of the property being changed.
     * @param previous The old value of the property being changed.
     * @param chain    The event which caused this event to be fired.
     */
    public ChangeEvent(Object source, ChangeType type, Object change,
        Object previous, ChangeEvent chain) {
        super(source);
        this.type = type;
        this.change = change;
        this.previous = previous;
        this.chain = chain;
    }

    /**
     * Find the type of this event.
     *
     * @return The Type value
     */
    public ChangeType getType() {
        return type;
    }

    /**
     * Return an object which is to be the new value of some property,
     * or is to be added to a collection.  May return <code>null</code> is
     * this is not meaningful.
     *
     * @return The Change value
     */
    public Object getChange() {
        return change;
    }

    /**
     * Return the old value of a property being changed.  May return
     * <code>null</code> is this is not meaningful.
     *
     * @return The Previous value
     */
    public Object getPrevious() {
        return previous;
    }

    /**
     * Return the event which caused this to be fired, or
     * <code>null</code> if this change was not caused by another event.
     *
     * @return The ChainedEvent value
     */
    public ChangeEvent getChainedEvent() {
        return chain;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return super.toString() + "[" + "type:" + getType() + ", change: " +
        getChange() + ", previous: " + getPrevious() + ", chainedEvent: " +
        getChainedEvent() + "]";
    }
}
