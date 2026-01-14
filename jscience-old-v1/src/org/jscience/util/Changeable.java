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

/**
 * This is a flag interface that defines the common add/remove listener methods
 * for classes and interfaces that wish to indicate that they are sources of
 * ChangeEvents.
 *
 * @author Matthew Pocock
 */
public interface Changeable {
    /**
     * Add a listener that will be informed of all changes.
     *
     * @param cl the ChangeListener to add
     * @deprecated use addChangeListener(cl, ChangeType.UNKNOWN)
     */
    public void addChangeListener(ChangeListener cl);

    /**
     * Add a listener that will be informed of changes of a given type.
     *
     * @param cl the ChangeListener
     * @param ct the ChangeType it is to be informed of
     */
    public void addChangeListener(ChangeListener cl, ChangeType ct);

    /**
     * Remove a listener that was interested in all types of changes.
     *
     * @param cl a ChangeListener to remove
     * @deprecated use removeChangeListener(cl, ChangeType.UNKNOWN)
     */
    public void removeChangeListener(ChangeListener cl);

    /**
     * Remove a listener that was interested in a specific types of changes.
     *
     * @param cl a ChangeListener to remove
     * @param ct the ChangeType that it was interested in
     */
    public void removeChangeListener(ChangeListener cl, ChangeType ct);

    /**
     * <p/>
     * A particular ChangeType can never be raised by this Changeable.
     * </p>
     * <p/>
     * <p/>
     * If this returns true, then it is guaranteed that change events of this type
     * (and all child types) can never under any circumstances be fired by this
     * Changeable instance. If it returns false, that does not mean that this type
     * of event will or even can be raised, but that it is worth registering
     * listeners incase.
     * </p>
     *
     * @param ct the ChangeType to check
     * @return true if ChangeEvents of this type are guaranteed to never be fired
     */
    public boolean isUnchanging(ChangeType ct);
}
