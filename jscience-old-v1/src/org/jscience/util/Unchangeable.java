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

import java.util.Collections;
import java.util.Set;


/**
 * This is a utility implementation of Changeable that doesn't fire any
 * events or keep references to any listeners. Use this when you have a final
 * immutable class and can't be bothered to fill in all those method stubs.
 *
 * @author Matthew Pocock
 *
 * @since 1.3
 */
public class Unchangeable implements Changeable {
    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     */
    public final void addChangeListener(ChangeListener cl) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public final void addChangeListener(ChangeListener cl, ChangeType ct) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param ct DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Set getListeners(ChangeType ct) {
        return Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     */
    public final void removeChangeListener(ChangeListener cl) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public final void removeChangeListener(ChangeListener cl, ChangeType ct) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param cf DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public final void addForwarder(ChangeForwarder cf, ChangeType ct) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param cf DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public final void removeForwarder(ChangeForwarder cf, ChangeType ct) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param ct DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Set getForwarders(ChangeType ct) {
        return Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ct DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isUnchanging(ChangeType ct) {
        return true;
    }
}
