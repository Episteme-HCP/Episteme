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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;


/**
 * Cache which is cleared according to memory pressure.  This is simply a
 * wrapper around java.lang.ref.SoftReference, and the performance will depend
 * on the behaviour of SoftReference on your platform.
 *
 * @author Thomas Down
 *
 * @since 1.1
 */
public class SoftReferenceCache implements Cache {
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CacheReference makeReference(Object o) {
        return new ReferenceReference(new SoftReference(o));
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class ReferenceReference implements CacheReference {
        /** DOCUMENT ME! */
        private Reference ref;

/**
         * Creates a new ReferenceReference object.
         *
         * @param r DOCUMENT ME!
         */
        ReferenceReference(Reference r) {
            ref = r;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object get() {
            if (ref != null) {
                return ref.get();
            }

            return null;
        }

        /**
         * DOCUMENT ME!
         */
        public void clear() {
            ref = null;
        }
    }
}
