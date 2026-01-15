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
