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

/**
 * <p/>
 * Interface for managing caches of objects fetchable by key.
 * </p>
 * <p/>
 * <p/>
 * The map may chose to remove a mapping, for example to free memory, or if the
 * data has become too old to be useful.
 * </p>
 *
 * @author Matthew Pocock
 * @since 1.1
 */
public interface CacheMap {
    /**
     * Associate a value with a key. The association may be broken at
     * any time.
     *
     * @param key the key Object
     * @param value the Object to associate with the key
     */
    public void put(Object key, Object value);

    /**
     * Retrieve the Object associated with the key, or null if either
     * no value has been associated or if the key's value has been cleared by
     * the cache.
     *
     * @param key the key Object
     *
     * @return the Object currently associated with the key
     */
    public Object get(Object key);

    /**
     * Explicitly remove an object.
     *
     * @param value the item to remove
     */
    public void remove(Object value);
}
