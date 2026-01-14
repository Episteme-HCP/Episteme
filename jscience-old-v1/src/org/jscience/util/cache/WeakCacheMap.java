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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import java.util.HashMap;
import java.util.Map;


/**
 * A cache which retains weak references to objects
 *
 * @author Thomas Down
 *
 * @since 1.3
 */
public class WeakCacheMap implements CacheMap {
    /** DOCUMENT ME! */
    private Map map = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void put(Object key, Object value) {
        map.put(key, new WeakReference(value));
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object key) {
        Reference ref = (Reference) map.get(key);

        if (ref != null) {
            return ref.get();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void remove(Object key) {
        map.remove(key);
    }
}
