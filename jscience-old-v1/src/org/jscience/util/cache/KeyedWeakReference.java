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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;


/**
 * Subclass of WeakReference which includes and extra field (the key) which
 * can be used to help cleanup once this reference has been enqueued.
 *
 * @author Thomas Down
 *
 * @since 1.3
 */
public class KeyedWeakReference extends WeakReference {
    /** DOCUMENT ME! */
    private Object key;

/**
     * Creates a new KeyedWeakReference object.
     *
     * @param key DOCUMENT ME!
     * @param ref DOCUMENT ME!
     */
    public KeyedWeakReference(Object key, Object ref) {
        super(ref);
        this.key = key;
    }

/**
     * Creates a new KeyedWeakReference object.
     *
     * @param key   DOCUMENT ME!
     * @param ref   DOCUMENT ME!
     * @param queue DOCUMENT ME!
     */
    public KeyedWeakReference(Object key, Object ref, ReferenceQueue queue) {
        super(ref, queue);
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getKey() {
        return key;
    }
}
