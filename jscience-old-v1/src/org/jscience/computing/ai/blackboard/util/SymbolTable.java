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

/* SymbolTable.java */
package org.jscience.computing.ai.blackboard.util;

import java.util.Enumeration;


/**
 * SymbolTable class. Implements a simple symbol table; string values are
 * deposited in return for integer values.  The string symbols may be
 * retrieved again using the returned integer value.
 *
 * @author:   Paul Brown
 * @version:  1.2, 04/26/96
 *
 * @see java.util.Hashtable#toString
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class SymbolTable extends java.util.Hashtable {
    /**
     * This member variable is used to generate unique identifiers for
     * string values.
     */
    private IndexAllocator indexer;

/**
         * Constructs a new empty symbol table.
         */
    public SymbolTable() {
        indexer = new IndexAllocator();
    }

    /**
     * Inserts a specified string value into the symbol table and
     * returns a unique integer identifier.
     *
     * @param string the string value to be inserted
     *
     * @return an integer identifier corresponding to the inserted string
     */
    public Integer put(String string) {
        Integer value = find(string);

        if (value == null) {
            value = indexer.get();
            put(value, string);
        }

        return (value);
    }

    /**
     * Returns the integer value associated with the specified string.
     *
     * @param string the string to search for
     *
     * @return the integer associated with the string, or null is the string is
     *         not contained in the symbol table
     */
    private Integer find(String string) {
        Enumeration keys = keys();
        Enumeration elements = elements();
        Integer value = null;

        while ((elements.hasMoreElements()) && (value == null))

            if (elements.nextElement().equals(string)) {
                value = (Integer) keys.nextElement();
            } else {
                keys.nextElement();
            }

        return (value);
    }

    /**
     * Returns the string associated with the specified integer
     * identifier.
     *
     * @param value an integer identifier
     *
     * @return the associated string, or null if the integer is invalid
     */
    public String get(Integer value) {
        return ((String) super.get(value));
    }
}
