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

/* ValuePair.java */
package org.jscience.computing.ai.blackboard.util;

/**
 * ValuePair class.  This is a simple aggregation utility class.
 * Semantically related objects may be meaningfully treated as a pair of
 * values.  One object is referred to as the key value and the other is
 * referred to as the data value.
 *
 * @author:   Paul Brown
 * @version:  1.5, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public final class ValuePair {
    /** The key value of this pair. */
    private Object key;

    /** The data value of this pair. */
    private Object data;

/**
         * Constructs a new value pair initialised with the specified key and
         * data values.
         * @param key the key value of the pair
         * @param data the data value of the pair
         */
    public ValuePair(Object key, Object data) {
        this.key = key;
        this.data = data;
    }

    /**
     * Accessor method for setting a new key value.
     *
     * @param key the new key value
     *
     * @return the old key value
     */
    public Object key(Object key) {
        Object old_key = this.key;
        this.key = key;

        return (old_key);
    }

    /**
     * Accessor method to return the key value.
     *
     * @return the key value
     */
    public Object key() {
        return (key);
    }

    /**
     * Accessor method for setting a new data value.
     *
     * @param data the new data value
     *
     * @return the old data value
     */
    public Object data(Object data) {
        Object old_data = this.data;
        this.data = data;

        return (old_data);
    }

    /**
     * Accessor method to return the data value.
     *
     * @return the data value
     */
    public Object data() {
        return (data);
    }

    /**
     * Returns a String representation of an instance of this class.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{key=");
        buffer.append(key.toString());
        buffer.append(", data=");
        buffer.append(data.toString());
        buffer.append("}");

        return (buffer.toString());
    }
}
