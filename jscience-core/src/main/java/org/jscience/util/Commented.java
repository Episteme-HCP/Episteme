package org.jscience.util;

/**
 * An interface to mark up something that has some comments. Usually you also
 * implement Named at the same time.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Commented {
    /**
     * Returns the comments for this object.
     *
     * @return the comments
     */
    public String getComments();
}
