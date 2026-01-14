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

/* IndexAllocator.java */
package org.jscience.computing.ai.blackboard.util;

import java.util.BitSet;


/**
 * IndexAllocator class.  Instances of this class are used to generate
 * unique indexes.
 *
 * @author:   Paul Brown
 * @version:  1.5, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public final class IndexAllocator {
    /** The value of the next valid index. */
    private int next_index;

    /** A record of used indexes. */
    private BitSet index_record;

/**
         * Constructs a new index allocator.
         */
    public IndexAllocator() {
        next_index = 0;
        index_record = new BitSet();
    }

    /**
     * Returns a unique index.
     *
     * @return a new index
     */
    public Integer get() {
        Integer index = new Integer(next_index);
        index_record.set(next_index);

        do
            next_index++;
        while (index_record.get(next_index) == true);

        return (index);
    }

    /**
     * This method is used to inform the allocator that an index is no
     * longer in use, this will allow the allocator to reuse the surrendered
     * index.
     *
     * @param index the index to recycle
     */
    public void recycle(Integer index) {
        index_record.clear(index.intValue());

        if (index.intValue() < next_index) {
            next_index = index.intValue();
        }
    }

    /**
     * Accessor method to return the indexes in use.
     *
     * @return a bit pattern of the indexes in use
     */
    public BitSet indexes() {
        return (index_record);
    }

    /**
     * Resets the index allocator.
     */
    public void reset() {
        next_index = 0;
        index_record.xor(index_record);
    }

    /**
     * Returns a String representation of an instance of this class.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{next index=");
        buffer.append(Integer.toString(next_index));
        buffer.append(", indexes in use=");
        buffer.append(index_record.toString());
        buffer.append("}");

        return (buffer.toString());
    }
}
