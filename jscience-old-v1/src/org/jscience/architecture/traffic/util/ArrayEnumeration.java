/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
package org.jscience.architecture.traffic.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;


/**
 * This enumeration can walk 1 or 2 dimensional arrays
 *
 * @author Joep Moritz
 * @version 1.0
 */
public class ArrayEnumeration implements Enumeration {
    /** DOCUMENT ME! */
    Object[][] ar;

    /** DOCUMENT ME! */
    int i;

    /** DOCUMENT ME! */
    int j;

/**
     * Creates a new ArrayEnumeration object.
     *
     * @param _ar DOCUMENT ME!
     */
    public ArrayEnumeration(Object[] _ar) {
        ar = new Object[1][];
        ar[0] = _ar;
        i = 0;
        j = 0;
    }

/**
     * Creates a new ArrayEnumeration object.
     *
     * @param _ar DOCUMENT ME!
     */
    public ArrayEnumeration(Object[][] _ar) {
        ar = _ar;
        i = 0;
        j = 0;
    }

/**
     * Creates a new ArrayEnumeration object.
     *
     * @param _ar1 DOCUMENT ME!
     * @param _ar2 DOCUMENT ME!
     */
    public ArrayEnumeration(Object[] _ar1, Object[] _ar2) {
        ar = new Object[2][];
        ar[0] = _ar1;
        ar[1] = _ar2;
        i = 0;
        j = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasMoreElements() {
        return (i < ar.length) && (j < ar[i].length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public Object nextElement() throws NoSuchElementException {
        if (!hasMoreElements()) {
            throw new NoSuchElementException();
        }

        Object o = ar[i][j++];

        if (j >= ar[i].length) {
            i++;
            j = 0;
        }

        return o;
    }
}
