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
