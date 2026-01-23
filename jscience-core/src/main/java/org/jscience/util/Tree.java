package org.jscience.util;

import java.util.Set;

/**
 * A class representing a tree data structure.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */
public interface Tree<T> {
    /**
     * Returns the contents of this tree node.
     *
     * @return the contents
     */
    public T getContents();

    /**
     * Returns the children of this tree node.
     *
     * @return the set of children
     */
    public Set<? extends Tree<T>> getChildren();
}
