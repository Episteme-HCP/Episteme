/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org
 *
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
