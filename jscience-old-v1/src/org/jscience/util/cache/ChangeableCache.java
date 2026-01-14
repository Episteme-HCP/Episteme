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

package org.jscience.util.cache;

import org.jscience.util.*;

import java.util.HashMap;
import java.util.Map;


/**
 * A cache that clears values as the keys fire ChangeEvents of a given
 * type.
 *
 * @author Matthew Pocock
 *
 * @since 1.1
 */
public class ChangeableCache {
    /** DOCUMENT ME! */
    private ChangeType changeType;

    /** DOCUMENT ME! */
    private Map cache = new HashMap();

    /** DOCUMENT ME! */
    private ChangeListener listener = new ChangeAdapter() {
            public void postChangeEvent(ChangeEvent ce) {
                Changeable source = (Changeable) ce.getSource();
                cache.remove(source);
                source.removeChangeListener(listener);
            }
        };

/**
     * Creates a new ChangeableCache object.
     *
     * @param ct DOCUMENT ME!
     */
    public ChangeableCache(ChangeType ct) {
        this.changeType = ct;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void put(Object key, Object value) {
        cache.put(key, value);

        if (key instanceof Changeable) {
            ((Changeable) key).addChangeListener(listener, changeType);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object key) {
        return cache.get(key);
    }
}
