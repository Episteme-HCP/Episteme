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

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.*;

/**
 * Map implementation which keeps weak references to values.
 * Entries are removed from the map when their value is
 * no longer reachable using normal (hard) references.  This is
 * useful for maintaining canonical copies of objects without forcing
 * these objects to remain in memory forever.
 * <p/>
 * <p/>
 * Note that this is distinct from the standard library class,
 * <code>WeakHashMap</code> which has weak <em>keys</em>.
 * </p>
 *
 * @author Thomas Down
 * @since 1.3
 */
public class WeakValueHashMap extends AbstractMap {
    private final Map keyToRefMap;
    private final ReferenceQueue queue;
    private final Set iteratorRefs;
    private final ReferenceQueue iteratorRefQueue;

    /**
     * Creates a new WeakValueHashMap object.
     */
    public WeakValueHashMap() {
        keyToRefMap = new HashMap();
        queue = new ReferenceQueue();
        iteratorRefs = new HashSet();
        iteratorRefQueue = new ReferenceQueue();
    }

    private void diddleReferenceQueue() {
        // Avoid making behind-the-scenes modifications while iterators exist.
        if (iteratorRefs.size() > 0) {
            Reference ref;

            while ((ref = iteratorRefQueue.poll()) != null) {
                iteratorRefs.remove(ref);
            }

            if (iteratorRefs.size() > 0) {
                return;
            }
        }

        KeyedWeakReference ref;

        while ((ref = (KeyedWeakReference) queue.poll()) != null) {
            keyToRefMap.remove(ref.getKey());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key   DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Object put(Object key, Object value) {
        diddleReferenceQueue();

        Reference oldRef = (Reference) keyToRefMap.put(key,
                new KeyedWeakReference(key, value, queue));

        if (oldRef != null) {
            Object oldRefVal = oldRef.get();
            oldRef.clear();

            return oldRefVal;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Object get(Object key) {
        Reference ref = (Reference) keyToRefMap.get(key);

        if (ref != null) {
            return ref.get();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean containsKey(Object o) {
        diddleReferenceQueue();

        return keyToRefMap.containsKey(o);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set entrySet() {
        diddleReferenceQueue();

        return new WVEntrySet();
    }

    private class WVEntrySet extends AbstractSet {
        private Set keyRefEntrySet;

        public WVEntrySet() {
            super();
            keyRefEntrySet = keyToRefMap.entrySet();
        }

        public int size() {
            return keyRefEntrySet.size();
        }

        public Iterator iterator() {
            Iterator i = new WVEntryIterator(keyRefEntrySet.iterator());
            iteratorRefs.add(new PhantomReference(i, iteratorRefQueue));

            return i;
        }
    }

    private class WVEntryIterator implements Iterator {
        private Object cache;
        private Iterator keyRefIterator;

        public WVEntryIterator(Iterator keyRefIterator) {
            this.keyRefIterator = keyRefIterator;
        }

        public boolean hasNext() {
            if (cache == null) {
                primeCache();
            }

            return cache != null;
        }

        public Object next() {
            if (cache == null) {
                primeCache();
            }

            if (cache == null) {
                throw new NoSuchElementException();
            } else {
                Object o = cache;
                cache = null;

                return o;
            }
        }

        public void remove() {
            if (cache != null) {
                throw new IllegalStateException("next() not called");
            } else {
                keyRefIterator.remove();
            }
        }

        private void primeCache() {
            while (keyRefIterator.hasNext()) {
                Map.Entry krme = (Map.Entry) keyRefIterator.next();
                Object ref = ((Reference) krme.getValue()).get();

                if (ref != null) {
                    cache = new WVMapEntry(krme.getKey(), ref);

                    return;
                }
            }
        }
    }

    private static class WVMapEntry implements Map.Entry {
        private Object key;
        private Object value;

        private WVMapEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object v) {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry mo = (Map.Entry) o;

            return (((key == null) ? (mo.getKey() == null)
                    : key.equals(mo.getKey())) &&
                    ((value == null) ? (mo.getValue() == null)
                            : value.equals(mo.getValue())));
        }

        public int hashCode() {
            return ((key == null) ? 0 : key.hashCode()) ^
                    ((value == null) ? 0 : value.hashCode());
        }
    }
}
