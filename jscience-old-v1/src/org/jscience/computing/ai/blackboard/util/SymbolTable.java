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
