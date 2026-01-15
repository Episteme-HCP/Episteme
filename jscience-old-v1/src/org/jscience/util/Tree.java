package org.jscience.util;

import java.util.Set;


/**
 * A class representing a tree data structure.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//should implement Cloneable
//should be updated to Java 1.5 generics
public interface Tree {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getContents();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildren();
}
