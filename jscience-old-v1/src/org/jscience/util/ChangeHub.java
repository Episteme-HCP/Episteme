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
 * Interface implemented by ChangeHubs, i.e.
 * classes that handle behaviour for
 * multiple instances of Changeable classes.
 * <p/>
 * Listeners are indexed with a key and when
 * an event is fired, only listeners with the same
 * key are invoked.  The class manages the mapping
 * between key and listener.  It is the users responsibility
 * to compute the key.
 *
 * @author Thomas Down (original implementation)
 * @author David Huen (refactoring)
 * @since 1.3
 */
public interface ChangeHub {
    /**
     * add a ChangeListener associated with given key.
     *
     * @param key DOCUMENT ME!
     * @param listener DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public void addListener(Object key, ChangeListener listener, ChangeType ct);

    /**
     * remove a ChangeListener associated with given key.
     *
     * @param key DOCUMENT ME!
     * @param listener DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public void removeListener(Object key, ChangeListener listener,
        ChangeType ct);

    /**
     * invoke the firePreChangeEvent on all ChangeListeners associated
     * with a specific key.
     *
     * @param key DOCUMENT ME!
     * @param cev DOCUMENT ME!
     *
     * @throws ChangeVetoException DOCUMENT ME!
     */
    public void firePreChange(Object key, ChangeEvent cev)
        throws ChangeVetoException;

    /**
     * invoke the firePostChangeEvent on all ChangeListeners associated
     * with a specific key.
     *
     * @param key DOCUMENT ME!
     * @param cev DOCUMENT ME!
     */
    public void firePostChange(Object key, ChangeEvent cev);
}
