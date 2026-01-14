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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * A cache that only remembers a given number of keys.
 *
 * @author Matthew Pocock
 *
 * @since 1.2
 */
public class FixedSizeMap implements CacheMap {
    /** DOCUMENT ME! */
    private Map map = new HashMap();

    /** DOCUMENT ME! */
    private LinkedList keys = new LinkedList();

    /** DOCUMENT ME! */
    private int maxSize;

/**
     * Creates a new FixedSizeMap object.
     *
     * @param maxSize DOCUMENT ME!
     */
    public FixedSizeMap(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void put(Object key, Object value) {
        if (map.containsKey(key)) {
            keys.remove(key);
        }

        keys.addLast(key);

        if (keys.size() > maxSize) {
            Object k = keys.removeFirst();
            map.remove(k);
        }

        map.put(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object key) {
        return map.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void remove(Object key) {
        map.remove(key);
        keys.remove(key);
    }
}
