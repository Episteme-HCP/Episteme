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

import java.lang.ref.WeakReference;

import java.util.LinkedList;
import java.util.List;


/**
 * Cache which stores up to <code>limit</code> Objects.
 *
 * @author Thomas Down
 *
 * @since 1.1
 */
public class FixedSizeCache implements Cache {
    /** DOCUMENT ME! */
    private List objects;

    /** DOCUMENT ME! */
    private int sizeLimit;

    {
        objects = new LinkedList();
    }

/**
     * Creates a new FixedSizeCache object.
     *
     * @param limit DOCUMENT ME!
     */
    public FixedSizeCache(int limit) {
        sizeLimit = limit;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CacheReference makeReference(Object o) {
        CacheReference cr = new FixedSizeCacheReference(o);
        objects.add(new WeakReference(cr));

        while (objects.size() > sizeLimit) {
            CacheReference old = (CacheReference) ((WeakReference) objects.remove(0)).get();

            if (old != null) {
                old.clear();
            }
        }

        return cr;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLimit() {
        return sizeLimit;
    }

    /**
     * DOCUMENT ME!
     *
     * @param limit DOCUMENT ME!
     */
    public void setLimit(int limit) {
        this.sizeLimit = limit;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class FixedSizeCacheReference implements CacheReference {
        /** DOCUMENT ME! */
        private Object o;

/**
         * Creates a new FixedSizeCacheReference object.
         *
         * @param o DOCUMENT ME!
         */
        private FixedSizeCacheReference(Object o) {
            this.o = o;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object get() {
            return o;
        }

        /**
         * DOCUMENT ME!
         */
        public void clear() {
            o = null;
        }
    }
}
