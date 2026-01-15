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

import org.jscience.util.*;

import java.util.HashMap;
import java.util.Map;


/**
 * A cache that clears values as the keys fire ChangeEvents of a given
 * type.
 *
 * @author Matthew Pocock
 *
 * @since 1.1
 */
public class ChangeableCache {
    /** DOCUMENT ME! */
    private ChangeType changeType;

    /** DOCUMENT ME! */
    private Map cache = new HashMap();

    /** DOCUMENT ME! */
    private ChangeListener listener = new ChangeAdapter() {
            public void postChangeEvent(ChangeEvent ce) {
                Changeable source = (Changeable) ce.getSource();
                cache.remove(source);
                source.removeChangeListener(listener);
            }
        };

/**
     * Creates a new ChangeableCache object.
     *
     * @param ct DOCUMENT ME!
     */
    public ChangeableCache(ChangeType ct) {
        this.changeType = ct;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void put(Object key, Object value) {
        cache.put(key, value);

        if (key instanceof Changeable) {
            ((Changeable) key).addChangeListener(listener, changeType);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object key) {
        return cache.get(key);
    }
}
