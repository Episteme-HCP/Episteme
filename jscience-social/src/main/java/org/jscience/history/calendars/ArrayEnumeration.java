//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;

import java.util.Enumeration;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class ArrayEnumeration implements Enumeration {
    /** DOCUMENT ME! */
    private int index;

    /** DOCUMENT ME! */
    private Object[] array;

/**
     * Creates a new ArrayEnumeration object.
     *
     * @param aobj DOCUMENT ME!
     */
    public ArrayEnumeration(Object[] aobj) {
        array = aobj;
        index = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object nextElement() {
        try {
            return array[index++];
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasMoreElements() {
        return index < array.length;
    }
}
